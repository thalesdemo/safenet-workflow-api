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
 * This class provides utility methods for working with XML data related to 
 * the SafeNet (BSIDCA) API.
 * It contains methods to extract specific elements from XML strings using
 * DOM parsing and return their values.
 * The class is designed as a utility class, and its constructor is private 
 * to prevent instantiation.
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import java.io.StringReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.NodeList;

public class XmlUtils {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private XmlUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Returns the base64-encoded JPG data from the "EnrollmentImage" element in the
     * given XML string.
     *
     * @param xmlString the XML string to extract the image data from
     * @return the base64-encoded image data
     * @throws Exception if there is an error parsing the XML string
     */
    public static String returnEnrollmentImage(String xmlString) throws Exception {
        // parse the XML string into a Document object
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xmlString)));

        // get the EnrollmentImage element and extract the base64-encoded JPG data
        Element enrollmentImage = (Element) doc.getElementsByTagName("blac:EnrollmentImage").item(0);

        // return the base64-encoded image data
        return enrollmentImage.getTextContent();

    }

    /**
     * Extracts a list of token serial numbers from the given XML string.
     *
     * @param xmlString the XML string to extract the serial numbers from
     * @return a list of token serial numbers
     * @throws Exception if there is an error parsing the XML string
     */
    public static List<String> extractTokenSerials(String xmlString) throws Exception {
        List<String> serials = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xmlString)));
        NodeList nodeList = document.getElementsByTagName("blac:string");
        for (int i = 0; i < nodeList.getLength(); i++) {
            String serial = nodeList.item(i).getTextContent();
            serials.add(serial);
        }
        return serials;
    }

    /**
     * Extracts the value of the "type" element from the given XML string.
     *
     * @param xmlString the XML string to extract the value from
     * @return the value of the "type" element
     * @throws Exception if there is an error parsing the XML string
     */
    public static String extractTypeElementValue(String xmlString) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xmlString)));
        return document.getElementsByTagName("type").item(0).getTextContent();
    }

    /**
     * Extracts the task ID of the active GrIDsure token from the given XML response
     * string.
     *
     * @param xmlResponse the XML response string to extract the task ID from
     * @return the task ID of the active GrIDsure token
     * @throws Exception if there is an error parsing the XML response string
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
     * This method is used to retrieve the task ID of the first token provisioning
     * task with a matching status and token type.
     * It takes in an XML response as a string, the desired token state as a string,
     * and the desired token type as a TokenType enum.
     * The method parses the XML response using DOM, finds the relevant
     * "Provisioning_x0020_Tasks" element, and checks each element for a matching
     * token state and token type. If a match is found, the task ID of the matching
     * token is returned.
     * If no matching token is found, the method returns null.
     * 
     * @param xmlResponse the XML response as a string
     * @param tokenState  the desired token state as a string
     * @param tokenType   the desired token type as a TokenType enum
     * @return the task ID of the matching token or null if no matching token is
     *         found
     * @throws Exception if there is an error parsing the XML or accessing the task
     *                   ID element
     */
    public static String getFirstTokenProvTaskId(String xmlResponse, String tokenState, TokenType tokenType)
            throws Exception {

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
            if (status.equals(tokenState) && tokenOption.equals(tokenType.getValue())) {
                String taskId = node.getElementsByTagName("taskid").item(0).getTextContent();
                return taskId; // return the taskid of the matching token
            }
        }

        return null; // return null if no matching token is found
    }
}
