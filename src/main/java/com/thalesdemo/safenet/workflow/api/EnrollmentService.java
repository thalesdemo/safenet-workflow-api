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
 * Service class responsible for handling token enrollment operations.
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cryptocard.www.blackshield.ArrayOfProvisioningResult;
import com.cryptocard.www.blackshield.ArrayOfString;
import com.cryptocard.www.blackshield.CustomTokenInformation;
import com.cryptocard.www.blackshield.EnrollmentResult;
import com.cryptocard.www.blackshield.GetEnrollmentURLDocument;
import com.cryptocard.www.blackshield.GetEnrollmentURLResponseDocument;
import com.cryptocard.www.blackshield.GetMobilePASSProvisioningActivationCodeDocument;
import com.cryptocard.www.blackshield.GetMobilePASSProvisioningActivationCodeResponseDocument;
import com.cryptocard.www.blackshield.GetProvisioningTasksForUserDocument;
import com.cryptocard.www.blackshield.GetProvisioningTasksForUserResponseDocument;
import com.cryptocard.www.blackshield.ProcessEnrollmentDocument;
import com.cryptocard.www.blackshield.ProcessEnrollmentResponseDocument;
import com.cryptocard.www.blackshield.ProvisionUsersDocument;
import com.cryptocard.www.blackshield.ProvisionUsersGrIDsureTokensDocument;
import com.cryptocard.www.blackshield.ProvisionUsersResponseDocument;
import com.cryptocard.www.blackshield.ProvisioningResult;
import com.cryptocard.www.blackshield.TokenOption;

@Service
public class EnrollmentService {

    /**
     * The maximum number of tokens per user.
     */
    private static final int MAX_TOKENS_PER_USER = 1000;

    /**
     * The service class for interacting with the workflow service.
     */
    @Autowired
    WorkflowService workflowService;

    /**
     * The logger instance for the {@code EnrollmentService} class.
     */
    private static final Logger Log = Logger.getLogger(EnrollmentService.class.getName());

    /**
     * 
     * Enrolls a new token for the user based on the given enrollment request.
     * 
     * @param enrollmentRequest The enrollment request containing the necessary
     *                          information for the enrollment.
     * @return An EnrollmentResponse object containing the result of the enrollment
     *         process.
     */
    public EnrollmentResponse enrollToken(EnrollmentRequest enrollmentRequest) {

        EnrollmentResponse response = new EnrollmentResponse();
        EnrollmentTokenData tokenData = new EnrollmentTokenData();

        switch (enrollmentRequest.getEnrollmentMethod()) {
            // Enroll token by email
            case EMAIL:
                tokenData = enrollTokenByEmail(enrollmentRequest);

                // Check if the token enrollment was successful
                if (tokenData.getProvId() != 0) {
                    response.setMessage(
                            "An enrollment email has been sent containing the steps to activate your authenticator.");
                    response.setStatus(EnrollmentStatus.COMPLETE);
                } else {
                    response.setMessage(
                            "We encountered an error while trying to email your token enrollment steps. Please try again in a few minutes.");
                    response.setStatus(EnrollmentStatus.ERROR);
                }
                break;

            // Enroll token by URL
            case URL:
                tokenData = enrollTokenByUrl(enrollmentRequest);

                // Check if the token enrollment was successful
                if (tokenData != null && tokenData.getUrl() != null && !(tokenData.getUrl().isEmpty())) {
                    response.setMessage("Follow the instructions to activate your authenticator at the link below.");
                    response.setStatus(EnrollmentStatus.COMPLETE);
                } else {
                    response.setMessage(
                            "We encountered an error while trying to generate your enrollment link. Please try again in a few minutes.");
                    response.setStatus(EnrollmentStatus.ERROR);
                }
                break;

            case API:

                // Enroll token by API
                tokenData = enrollTokenByApi(enrollmentRequest);

                // Check if the token enrollment was successful
                if (tokenData == null || tokenData.getState() == null
                        || tokenData.getState().equals(EnrollmentStatus.ERROR.getValue())) {
                    response.setMessage(
                            "There has been an issue during the enrollment process of your authenticator. Please try again in a few minutes.");
                    response.setStatus(EnrollmentStatus.ERROR);
                    return response;
                } else if (tokenData.getState().equals(EnrollmentStatus.COMPLETE.getValue())) {
                    // If enrollment was successful, update response message accordingly
                    if (enrollmentRequest.getTokenType() == TokenType.GRIDSURE) {
                        response.setMessage("Your authenticator has been successfully activated!");
                    } else {
                        response.setMessage("Enroll your authenticator with the following activation link.");
                    }
                    response.setStatus(EnrollmentStatus.COMPLETE);
                    tokenData.setState(null);
                    tokenData.setProvId(null);
                }

                // If GRIDSURE token and enrollment not yet complete, generate challenge message
                if (enrollmentRequest.getTokenType() == TokenType.GRIDSURE && response.getStatus() == null) {
                    if (tokenData.getImage() == null || tokenData.getImage().isEmpty()) {
                        response.setMessage(
                                "We encountered an error while generating the GRID enrollment image. Please try again in a few minutes.");
                        response.setStatus(EnrollmentStatus.ERROR);
                    } else {
                        response.setMessage(
                                "Please select your personal identification pattern (PIP) to complete the activation process.");
                        response.setStatus(EnrollmentStatus.CHALLENGE);
                        tokenData.setUrl(null); // remove URL from response
                    }
                }
                break;

        }

        // Check if tokenData exists and the enrollment process did not result in an
        // error
        if (tokenData != null && response.getStatus() != EnrollmentStatus.ERROR) {

            // Set the EnrollmentMethod and TokenType properties of the tokenData object to
            // the values in enrollmentRequest
            tokenData.setEnrollmentMethod(enrollmentRequest.getEnrollmentMethod());
            tokenData.setTokenType(enrollmentRequest.getTokenType());

            // Set the tokenData object as a property of the response object
            response.setTokenData(tokenData);
        }

        return response;
    }

    /**
     * 
     * Enrolls a token using the 'email' method.
     * 
     * @param enrollmentRequest the enrollment request containing the necessary
     *                          information to enroll the token
     * @return the enrollment token data containing the provision ID
     */
    public EnrollmentTokenData enrollTokenByEmail(EnrollmentRequest enrollmentRequest) {
        // log the enrollment method
        Log.fine("Enrolling token to user using the 'email' method.");
        // create an enrollment token data object
        EnrollmentTokenData tokenData = new EnrollmentTokenData();
        // provision the token with the given information
        int provId = provisionToken(enrollmentRequest.getUsername(), enrollmentRequest.getOrganization(),
                enrollmentRequest.getTokenType());
        // set the provision ID in the enrollment token data object
        tokenData.setProvId(provId);
        // return the enrollment token data
        return tokenData;
    }

    /**
     * 
     * Enrolls a token to a user using the 'url' method by first invoking the
     * enrollTokenByEmail method to provision the token,
     * then obtaining the enrollment URL for the provisioned token using a web
     * service call.
     * 
     * @param enrollmentRequest an object representing the enrollment request
     *                          containing the user's information, organization,
     *                          enrollment method and token type
     * @return an object containing the provisioned token's data, including the
     *         token's provision ID and enrollment URL
     */
    public EnrollmentTokenData enrollTokenByUrl(EnrollmentRequest enrollmentRequest) {
        // Log that the token is being enrolled using the 'url' method.
        Log.fine("Enrolling token to user using the 'url' method.");

        // Enroll the token using the 'email' method since it's a prerequisite for
        // getting the enrollment URL.
        EnrollmentTokenData tokenData = enrollTokenByEmail(enrollmentRequest);

        // Create a new document for getting the enrollment URL.
        GetEnrollmentURLDocument enrollmentUrlDocument = GetEnrollmentURLDocument.Factory.newInstance();

        // Get the GetEnrollmentURL element from the document and set its properties.
        GetEnrollmentURLDocument.GetEnrollmentURL enrollmentUrlData = enrollmentUrlDocument.addNewGetEnrollmentURL();
        enrollmentUrlData.setUserName(enrollmentRequest.getUsername());
        enrollmentUrlData.setTaskID(tokenData.getProvId());
        enrollmentUrlData.setOrganization(enrollmentRequest.getOrganization());

        // Declare a variable to hold the response from the getEnrollmentURL web service
        // call.
        GetEnrollmentURLResponseDocument getEnrollmentUrlResponseDocument;
        try {
            // Invoke the web service to obtain the enrollment URL for the provisioned token
            getEnrollmentUrlResponseDocument = this.workflowService.getBsidca().getEnrollmentURL(enrollmentUrlDocument);
            GetEnrollmentURLResponseDocument.GetEnrollmentURLResponse getEnrollmentUrlResponse = getEnrollmentUrlResponseDocument
                    .getGetEnrollmentURLResponse();
            String enrollmentUrlResult = getEnrollmentUrlResponse.getGetEnrollmentURLResult();
            Log.log(Level.FINE, "Enrollment URL: {0}", enrollmentUrlResult);
            tokenData.setUrl(enrollmentUrlResult);
        } catch (RemoteException e) {
            // Handle any exceptions that might occur when invoking the web service
            Log.log(Level.SEVERE,
                    String.format(
                            "BSIDCA remote server exception while retrieving the URL of the provisioning task ID: %d",
                            tokenData.getProvId()),
                    e);
        }
        return tokenData;
    }

    /**
     * 
     * Retrieves the base64 mobilepass activation string for a given user and task
     * ID.
     * 
     * @param username     the username of the user
     * @param organization the organization of the user
     * @param taskId       the ID of the task for which the activation string is
     *                     required
     * @return the base64 encoded mobilepass activation string
     */
    public String getMobilePassBase64ActivationString(String username, String organization, int taskId) {
        // Create a new GetMobilePASSProvisioningActivationCodeDocument
        GetMobilePASSProvisioningActivationCodeDocument getMobilePassActivationCodeDocument = GetMobilePASSProvisioningActivationCodeDocument.Factory
                .newInstance();

        // Add a new GetMobilePASSProvisioningActivationCode to the
        // GetMobilePASSProvisioningActivationCodeDocument
        GetMobilePASSProvisioningActivationCodeDocument.GetMobilePASSProvisioningActivationCode getMobilePASSProvisioningActivationCodeData = getMobilePassActivationCodeDocument
                .addNewGetMobilePASSProvisioningActivationCode();

        // Set the user name, organization, and task ID
        getMobilePASSProvisioningActivationCodeData.setUserName(username);
        getMobilePASSProvisioningActivationCodeData.setOrganization(organization);
        getMobilePASSProvisioningActivationCodeData.setTaskID(taskId);

        try {
            // Call the getMobilePASSProvisioningActivationCode method on the BSIDCA Web
            // Service
            GetMobilePASSProvisioningActivationCodeResponseDocument responseDocument = this.workflowService.getBsidca()
                    .getMobilePASSProvisioningActivationCode(getMobilePassActivationCodeDocument);
            // Return the base64 mobilepass activation code from the response document
            return responseDocument.getGetMobilePASSProvisioningActivationCodeResponse()
                    .getGetMobilePASSProvisioningActivationCodeResult();
        } catch (RemoteException e) {
            // Log a SEVERE level message if a RemoteException occurs
            Log.log(Level.SEVERE,
                    "Remote exception encountered during retrieval of the base64 mobilepass activation code",
                    e);
        }

        return null;
    }

    /**
     * 
     * Enrolls a token using the 'api' method.
     * 
     * @param enrollmentRequest The enrollment request data.
     * @return The enrollment token data.
     */
    public EnrollmentTokenData enrollTokenByApi(EnrollmentRequest enrollmentRequest) {

        Log.fine("Enrolling token to user using the 'api' method.");

        EnrollmentTokenData tokenData = new EnrollmentTokenData();

        // Enroll mobile pass by URL method and generate activation string
        if (enrollmentRequest.getTokenType() == TokenType.MOBILEPASS) {
            tokenData = enrollTokenByUrl(enrollmentRequest);
            Log.log(Level.FINE, "Provisioning task ID: {0}", tokenData.getProvId());
            String activationString = this.getMobilePassBase64ActivationString(enrollmentRequest.getUsername(),
                    enrollmentRequest.getOrganization(), tokenData.getProvId());

            Log.log(Level.FINEST, "MobilePASS activation string received from SafeNet server: {0}", activationString);
            // Set tokenData state based on activation string and set URL
            if (activationString == null || activationString.isEmpty()) {
                tokenData.setState(EnrollmentStatus.ERROR.getValue());
            } else {
                tokenData.setState(EnrollmentStatus.COMPLETE.getValue()); // TODO: add checks
                tokenData.setUrl("mobilepassplus://autoenrollment?str=" + activationString);
            }
            return tokenData;
        }

        try {
            // Process first request
            if (enrollmentRequest.getState() == null || enrollmentRequest.getState().isEmpty()) {
                Log.fine("Processing first request in enrollTokenByApi()");

                // Enroll token by URL method and extract enrollment code from URL
                tokenData = enrollTokenByUrl(enrollmentRequest);
                String enrollmentCode = UrlUtils.extractCodeParamValue(tokenData.getUrl());
                Log.log(Level.FINE, "Enrollment code: {0}", enrollmentCode);

                // STEP: start processEnrollment
                ProcessEnrollmentDocument enrollmentDocument = ProcessEnrollmentDocument.Factory.newInstance();
                ProcessEnrollmentDocument.ProcessEnrollment enrollmentData = enrollmentDocument
                        .addNewProcessEnrollment();
                enrollmentData.setCode(enrollmentCode);

                // Call processEnrollment method to initiate enrollment process
                ProcessEnrollmentResponseDocument enrollmentResponseDocument = this.workflowService.getBsidca()
                        .processEnrollment(enrollmentDocument);

                // Retrieve enrollment information from response
                ProcessEnrollmentResponseDocument.ProcessEnrollmentResponse enrollmentResponseData = enrollmentResponseDocument
                        .getProcessEnrollmentResponse();
                CustomTokenInformation enrollmentTokenInfo = enrollmentResponseData.getCustomInfo();

                // Set tokenData state and image based on enrollment information
                if (enrollmentTokenInfo == null) {
                    tokenData.setState(EnrollmentStatus.ERROR.getValue());
                    Log.warning(
                            "Could not process token enrollment info at ProcessEnrollment(). Enable debug for hints on the cause of this error.");
                } else {
                    tokenData.setImage(XmlUtils.returnEnrollmentImage(enrollmentTokenInfo.toString()));
                    tokenData.setState(enrollmentCode);
                }
            } else {
                // Process second request with user response to challenge

                Log.fine("Processing second request (includes state) in enrollTokenByApi()");

                String userResponseToChallenge = enrollmentRequest.getUserResponseChallenge();
                Log.log(Level.FINE, "User chose pattern: {0}", userResponseToChallenge);

                // STEP: finish processEnrollment
                ProcessEnrollmentDocument finishEnrollmentDocument = ProcessEnrollmentDocument.Factory.newInstance();
                ProcessEnrollmentDocument.ProcessEnrollment finishEnrollmentData = finishEnrollmentDocument
                        .addNewProcessEnrollment();
                finishEnrollmentData.setCode(enrollmentRequest.getState());
                finishEnrollmentData.setOTP(userResponseToChallenge);

                // Call processEnrollment method to complete enrollment process
                ProcessEnrollmentResponseDocument finishEnrollmentResponseDocument = this.workflowService.getBsidca()
                        .processEnrollment(finishEnrollmentDocument);

                // Get the ProcessEnrollmentResponse from the response document.
                ProcessEnrollmentResponseDocument.ProcessEnrollmentResponse finishEnrollmentResponse = finishEnrollmentResponseDocument
                        .getProcessEnrollmentResponse();

                Log.fine(
                        "Second phase of enrollment via API: " + finishEnrollmentResponse.getProcessEnrollmentResult());

                // TODO: add check enrollment completed or not.

                // Check if the enrollment was successful, if yes then set the status to
                // "COMPLETE",
                // otherwise set it to "ERROR"
                if (finishEnrollmentResponse.getProcessEnrollmentResult().equals(EnrollmentResult.SUCCESS)) {
                    tokenData.setState(EnrollmentStatus.COMPLETE.getValue());
                } else {
                    tokenData.setState(EnrollmentStatus.ERROR.getValue());
                }

            }
        } catch (RemoteException e) {
            Log.log(Level.SEVERE, "BSIDCA remote server exception while enrolling token via API:", e);
        } catch (Exception e) {
            Log.log(Level.SEVERE, "BSIDCA general exception while enrolling token via API:", e);
        }
        return tokenData;
    }

    /**
     * 
     * This method provisions a token based on the given token type and returns the
     * ID of the provisioning task.
     * 
     * @param username     the username of the user who will be associated with the
     *                     token
     * @param organization the organization for which the token will be provisioned
     * @param tokenType    the type of the token to be provisioned
     * @return an integer representing the ID of the provisioning task
     */
    public int provisionToken(String username, String organization, TokenType tokenType) {
        int provTaskId = 0;
        // Provision a Grid token if tokenType is GRIDSURE, otherwise provision a
        // standard token
        switch (tokenType) {
            case GRIDSURE:
                provTaskId = provisionGridToken(username, organization);
                break;
            case MOBILEPASS:
            default:
                provTaskId = provisionStandardToken(username, organization, tokenType);
                break;
        }
        // Returns the ID of the provisioning task for either type of token.
        return provTaskId;
    }

    /**
     * 
     * Provision a GRIDSURE token for a user using the BSIDCA API.
     * 
     * @param username     the username of the user to provision the token for
     * @param organization the organization name
     * @return the ID of the provisioning task
     */
    private int provisionGridToken(String username, String organization) {
        String description = "Token provisioned by safenet-workflow-api";
        TokenType tokenType = TokenType.GRIDSURE;
        String provState = "Active";
        // Create prov task
        ProvisionUsersGrIDsureTokensDocument provisioningRequest = ProvisionUsersGrIDsureTokensDocument.Factory
                .newInstance();
        ProvisionUsersGrIDsureTokensDocument.ProvisionUsersGrIDsureTokens provisioningRequestData = provisioningRequest
                .addNewProvisionUsersGrIDsureTokens();
        // Declare and initialize the array with one element
        ArrayOfString usernames = ArrayOfString.Factory.newInstance();
        usernames.addString(username);
        provisioningRequestData.setUserNames(usernames);
        provisioningRequestData.setOrganization(organization);
        provisioningRequestData.setDescription(description);
        ArrayOfProvisioningResult provisioningResults;
        try {
            provisioningResults = this.workflowService.getBsidca()
                    .provisionUsersGrIDsureTokens(provisioningRequest).getProvisionUsersGrIDsureTokensResponse()
                    .getProvisionUsersGrIDsureTokensResult();
        } catch (RemoteException e) {
            Log.log(Level.SEVERE, "BSIDCA remote server exception while provisioning GRID token:", e);
            return 0;
        }
        // Logging provisioning results
        Log.log(Level.FINE, "Provisioning results: {0}", provisioningResults);

        // TODO: check for different provisioning results by token type, currently
        // getFirstTokenProvTaskId() only supports the first provisioning task ID (of
        // that token type) found in the response.
        String provisioningTasksResults = getProvisioningTasks(username, organization);
        int taskId = 0;

        // Find pattern match: <ProvisioningResult .*>EmailSent</ProvisioningResult>
        try {
            taskId = Integer.parseInt(XmlUtils.getFirstTokenProvTaskId(provisioningTasksResults, provState, tokenType));
            Log.log(Level.FINE, "Provisioning task ID found: {0}", taskId);
        } catch (NumberFormatException e) {
            Log.log(Level.SEVERE, "Exception caught: Could not locate provisioning task ID in BSIDCA response.", e);
        } catch (Exception e) {
            Log.log(Level.SEVERE, "General exception while retrieving provisioning task ID from BSIDCA:", e);
        }
        return taskId;
    }

    /**
     * 
     * Provision a standard token.
     * 
     * @param username     the username of the user who will be provisioned with the
     *                     token.
     * @param organization the name of the organization the user belongs to.
     * @param tokenType    the type of token to be provisioned (e.g. MobilePASS,
     *                     GridSure, etc).
     * @return the task ID of the provisioned token.
     */
    private int provisionStandardToken(String username, String organization, TokenType tokenType) {
        String description = "Token provisioned by safenet-workflow-api";
        String provState = "Active";
        // Create prov task
        ProvisionUsersDocument provisionUsersDocument = ProvisionUsersDocument.Factory.newInstance();
        ProvisionUsersDocument.ProvisionUsers provisionUsersData = provisionUsersDocument.addNewProvisionUsers();
        provisionUsersData.setOrganization(organization);
        provisionUsersData.setDescription(description);
        ArrayOfString usernames = ArrayOfString.Factory.newInstance();
        usernames.addString(username);
        provisionUsersData.setUserNames(usernames);
        // Convert tokenType to TokenOption
        TokenOption.Enum convertedTokenType = TokenConverter.tokenTypeToTokenOption(tokenType);
        provisionUsersData.setTokenClass(convertedTokenType);
        Log.log(Level.FINE, "Token type requested: {0}", convertedTokenType);
        try {
            ProvisionUsersResponseDocument provisionUsersResponseDocument = this.workflowService.getBsidca()
                    .provisionUsers(provisionUsersDocument);
            boolean result = ProvisioningResult.EMAIL_SENT == provisionUsersResponseDocument.getProvisionUsersResponse()
                    .getProvisionUsersResult().getProvisioningResultArray(0);
            Log.log(Level.FINE, "Provisioning result? {0}", result);
        } catch (RemoteException e) {
            Log.log(Level.SEVERE, "BSIDCA remote server exception while provisioning standard token", e);
            return 0;
        }
        // TODO: check for different provisioning results by token type, currently
        // getFirstTokenProvTaskId() only
        // supports the first provisioning task ID found in the response.
        String provisioningTasksResults = getProvisioningTasks(username, organization);
        int taskId = 0;
        try {
            taskId = Integer.parseInt(XmlUtils.getFirstTokenProvTaskId(provisioningTasksResults, provState, tokenType));
            Log.log(Level.FINE, "Provisioning task ID found: {0}", taskId);
        } catch (NumberFormatException e) {
            Log.log(Level.SEVERE, "Caught exception: could not locate provisioning task ID in BSIDCA response.", e);
        } catch (Exception e) {
            Log.log(Level.SEVERE, "General exception while retrieving provisioning task ID from BSIDCA:", e);
        }
        return taskId;
    }

    /**
     * Retrieves the provisioning tasks for a given user and organization, returning
     * the result as a string.
     * 
     * @param username     the user name
     * @param organization the organization
     * @return a string representation of the result of retrieving the provisioning
     *         tasks or null if an exception occurred during the retrieval
     */
    public String getProvisioningTasks(String username, String organization) {

        // create a new GetProvisioningTasksForUserDocument instance
        GetProvisioningTasksForUserDocument getProvisioningTasksDocument = GetProvisioningTasksForUserDocument.Factory
                .newInstance();

        // get the GetProvisioningTasksForUser object and set the user name,
        // organization, start record, and number of records
        GetProvisioningTasksForUserDocument.GetProvisioningTasksForUser getProvisioningTasksData = getProvisioningTasksDocument
                .addNewGetProvisioningTasksForUser();
        getProvisioningTasksData.setUser(username);
        getProvisioningTasksData.setOrganization(organization);
        getProvisioningTasksData.setStartRecord(0);
        getProvisioningTasksData.setNumberOfRecords(MAX_TOKENS_PER_USER);

        try {
            // get the response from the BSIDCA service for the provisioning tasks
            GetProvisioningTasksForUserResponseDocument provisioningTasksResponseDocument = this.workflowService
                    .getBsidca()
                    .getProvisioningTasksForUser(getProvisioningTasksDocument);
            GetProvisioningTasksForUserResponseDocument.GetProvisioningTasksForUserResponse provisioningTasksResponse = provisioningTasksResponseDocument
                    .getGetProvisioningTasksForUserResponse();

            // get the string representation of the result of retrieving the provisioning
            // tasks
            String provisioningTasksResult = provisioningTasksResponse.getGetProvisioningTasksForUserResult()
                    .toString();

            // log the result of retrieving the provisioning tasks
            Log.log(Level.FINE, "BSIDCA response for getProvisioningTasks: {0}", provisioningTasksResult);

            // return the string representation of the result of retrieving the provisioning
            // tasks
            return provisioningTasksResult;
        } catch (RemoteException e) {
            // log the exception encountered during the retrieval and return null
            Log.log(Level.SEVERE, "BSIDCA remote server exception seen while retrieving provisioning tasks for user:",
                    e);
        }

        // return null if an exception occurred during the retrieval
        return null;
    }

}
