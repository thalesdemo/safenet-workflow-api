/**
 * Copyright 2023 safenet-workflow-api
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * The WorkflowService class provides methods to connect to the SafeNet (BSIDCA)
 * service using Apache Axis2 and perform authentication and other operations 
 * necessary for the application. The class manages the connection to the web 
 * service and handles connection errors and retries.
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import com.cryptocard.www.blackshield.ConnectDocument;
import com.cryptocard.www.blackshield.ConnectResponseDocument;
import com.cryptocard.www.blackshield.PingConnectionDocument;
import com.thalesdemo.soap.bsidca.BSIDCAStub;
import org.springframework.stereotype.Service;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.client.Stub;
import org.apache.axis2.kernel.http.HTTPConstants;
import org.apache.axis2.transport.http.CommonsTransportHeaders;
import org.apache.axis2.wsdl.WSDLConstants;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class WorkflowService {

    /**
     * This is the logger instance for the WorkflowService class. The logger is
     * initialized with the name of the class
     * for which it is used.
     */

    private static final Logger Log = Logger.getLogger(WorkflowService.class.getName());

    /**
     * An instance of the SafeNet BSIDCA web service stub.
     */
    private BSIDCAStub bsidca;

    /**
     * An instance of the Settings class, which stores the configuration
     * settings for the application.
     */
    private Settings settings;

    /**
     * The maximum number of reconnect attempts allowed per getBsidca() call.
     */
    private static final int MAX_RETRIES_BSIDCA = 3;

    /**
     * The interval between reconnect attempts in milliseconds (1000ms = 1 second).
     */
    private static final int RECONNECT_INTERVAL = 1000;

    /**
     * A ScheduledExecutorService used for scheduling reconnect attempts at
     * a fixed rate defined by RECONNECT_INTERVAL.
     */
    private ScheduledExecutorService reconnectExecutor;

    /**
     * A counter for the number of reconnect attempts made during a getBsidca()
     * call.
     */
    private int reconnectAttempts;

    /**
     * Attempts to reconnect to the third-party web service at a fixed rate
     * defined by RECONNECT_INTERVAL. The reconnect attempts will stop when a
     * connection is successfully established or when the maximum number of
     * retries (MAX_RETRIES_BSIDCA) is reached.
     */
    private void reconnect() {
        reconnectAttempts++;
        if (reconnectAttempts <= MAX_RETRIES_BSIDCA) {
            String infoMessage = String.format("Trying to re-connect (attempt# %d/%d)", reconnectAttempts,
                    MAX_RETRIES_BSIDCA);
            Log.info(infoMessage);
            this.newConnectSession();

            if (pingConnection()) {
                // Stop scheduling reconnect attempts if the connection is successful
                reconnectExecutor.shutdown();
            }
        } else {
            // Stop scheduling reconnect attempts after reaching the maximum retries
            reconnectExecutor.shutdown();
        }
    }

    /**
     * Constructor for the WorkflowService class. Initializes the settings and
     * BSIDCAStub objects, and calls newConnectSession to establish a connection
     * to the SafeNet (BSIDCA) web service. It also initializes the
     * reconnectExecutor and sets the initial reconnectAttempts to 0.
     *
     * @param settings The application settings used to connect to the BSIDCA
     *                 web service.
     */
    public WorkflowService(Settings settings) {

        this.settings = settings;

        // Initialize the reconnectExecutor with a single thread
        reconnectExecutor = Executors.newSingleThreadScheduledExecutor();
        // Reset reconnectAttempts counter
        reconnectAttempts = 0;

        try {
            this.bsidca = new BSIDCAStub(settings.getBsidcaUrl());
            setContextProperties(this.bsidca);
            Log.info("WorkflowService() constructor set BSIDCA and its context properties");
        } catch (AxisFault e) {
            Log.log(Level.SEVERE, "Exception during BSIDCA connect call in constructor: ", e);
            e.printStackTrace();
        }

        // Establish the initial connection to the BSIDCA web service
        this.newConnectSession();
    }

    /**
     * Returns the BSIDCAStub object used to connect to the SafeNet (BSIDCA)
     * web service.
     * If the connection is lost or not yet established, this method will
     * attempt to reconnect up to MAX_RETRIES_BSIDCA times, with a delay of
     * RECONNECT_INTERVAL between each attempt.
     *
     * @return A BSIDCAStub object used to connect to the SafeNet (BSIDCA) web
     *         service.
     */
    public BSIDCAStub getBsidca() {

        if (!pingConnection()) {
            reconnectExecutor.scheduleAtFixedRate(this::reconnect, 0, RECONNECT_INTERVAL, TimeUnit.MILLISECONDS);
        }
        return bsidca;
    }

    /**
     * Sets the context properties for the provided Apache Axis2 stub object. The
     * context properties include a connection pool, connection timeout, and other
     * HTTP parameters used for establishing a connection to the SafeNet (BSIDCA)
     * web service.
     * 
     * @param stub The Apache Axis2 stub object to configure the context properties
     *             for.
     */
    public static void setContextProperties(Stub stub) {

        // Set the max connections to 20 and the timeout to 20 seconds
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(20);
        connectionManager.setDefaultMaxPerRoute(20);
        connectionManager.setValidateAfterInactivity(20000);

        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setSocketTimeout(20000)
                        .setConnectTimeout(20000)
                        .build())
                .build();

        // stub._getServiceClient().getOptions().setProperty(HTTPConstants.REUSE_HTTP_CLIENT,
        // Constants.VALUE_TRUE);
        stub._getServiceClient().getServiceContext().getConfigurationContext()
                .setProperty(HTTPConstants.CACHED_HTTP_CLIENT, httpClient);

        // Disable validation of DOCTYPE declaration
        stub._getServiceClient().getOptions().setProperty("disableDTD", true);
    }

    /**
     * 
     * This method retrieves the task ID of the currently active GrIDsure token
     * provisioning task from the XML response received from the web service.
     * 
     * @param xmlResponse the XML response string received from the web service
     * @return the task ID of the matching token if found, otherwise null
     * @throws Exception if there is an error parsing the XML response
     */
    public static String getActiveGridSureTokenProvTaskId(String xmlResponse) throws Exception {

        // parse the XML response using DOM
        InputSource inputSource = new InputSource(new StringReader(xmlResponse));
        Element root = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputSource)
                .getDocumentElement();

        // find the relevant "Provisioning_x0020_Tasks" element
        NodeList nodes = root.getElementsByTagName("Provisioning_x0020_Tasks");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element node = (Element) nodes.item(i);
            String status = node.getElementsByTagName("status").item(0).getTextContent();
            String tokenOption = node.getElementsByTagName("tokenoption").item(0).getTextContent();
            if (status.equals("Active") && tokenOption.equals("GrIDsure")) {
                String taskId = node.getElementsByTagName("taskid").item(0).getTextContent();
                return taskId; // return the taskid of the matching token
            }
        }

        return null; // return null if no matching token is found
    }

    /**
     * This method creates a new session with the BSIDCA service. It uses the
     * operator email, OTP, and validation code from the settings to connect to the
     * service. If the connection is successful, it resets the reconnectAttempts
     * counter.
     * In case of connection failure, the method logs an error message with the
     * cause of the failure.
     */
    public void newConnectSession() {
        // connect
        ConnectDocument connectDocument = ConnectDocument.Factory.newInstance();

        // Use the ConnectDocument instance to set the properties
        ConnectDocument.Connect data = connectDocument.addNewConnect();
        data.setOperatorEmail(this.settings.getUsername());
        data.setOTP(this.settings.getPassword());
        data.setValidationCode(null);

        try {
            // call the web service
            ConnectResponseDocument crd = this.bsidca.connect(connectDocument);

            MessageContext msgCtx = this.bsidca._getServiceClient().getLastOperationContext()
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            CommonsTransportHeaders cth = (CommonsTransportHeaders) msgCtx
                    .getProperty(MessageContext.TRANSPORT_HEADERS);
            // String cookie = (String) cth.get(HTTPConstants.HEADER_SET_COOKIE);

            // Print the headers - DEBUG PURPOSES ONLY
            for (Map.Entry<String, String> entry : cth.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                String tmpDebugMsg = key + ": " + value;
                Log.finest(tmpDebugMsg);
            }

            Log.info("Authentication result: " + crd.getConnectResponse().getConnectResult());
            reconnectAttempts = 0; // Reset reconnect attempts on successful authentication

        } catch (Exception e) {
            Log.log(Level.SEVERE, "Failed to connect! ", e);
        }
    }

    /**
     * Pings the connection to the BSIDCA service to check if it's still active.
     * 
     * @return A boolean value indicating if the connection is still active or not.
     */
    public boolean pingConnection() {
        try {
            PingConnectionDocument pingDocument = PingConnectionDocument.Factory.newInstance();
            boolean pingResult = this.bsidca.pingConnection(pingDocument).getPingConnectionResponse()
                    .getPingConnectionResult();
            String debugMessage = "BSIDCA connection status: " + pingResult;
            Log.fine(debugMessage);
            return pingResult;
        } catch (RemoteException e) {
            return false;
        }
    }
}
