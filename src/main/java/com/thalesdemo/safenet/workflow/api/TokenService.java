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
 * A service class that provides methods for interacting with tokens through
 * the BSIDCA API. This class contains methods for retrieving token information,
 * revoking tokens, and filtering tokens based on various criteria.
 * 
 * This class is designed to be used in conjunction with the WorkflowService 
 * class, which provides methods for interacting with the BSIDCA API
 * more broadly.
 *
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cryptocard.www.blackshield.GetTokensByOwnerDocument;
import com.cryptocard.www.blackshield.GetTokensByOwnerResponseDocument;
import com.cryptocard.www.blackshield.GetTokensDocument;
import com.cryptocard.www.blackshield.GetTokensResponseDocument;
import com.cryptocard.www.blackshield.RevokeResult;
import com.cryptocard.www.blackshield.RevokeSelection;
import com.cryptocard.www.blackshield.RevokeTokenDocument;
import com.cryptocard.www.blackshield.RevokeTokenResponseDocument;

@Service
public class TokenService {

    /**
     * The WorkflowService is a dependency of the TokenService that provides access
     * to the methods and functionality of the BSIDCA API system.
     */
    @Autowired
    WorkflowService workflowService;

    /**
     * This is the logger instance for the WorkflowService class. The logger is
     * initialized with the name of the class
     * for which it is used.
     */
    private static final Logger Log = Logger.getLogger(TokenService.class.getName());

    /**
     * This method returns a token schema based on the provided serial number and
     * organization.
     * 
     * @param serial       the serial number of the token to retrieve
     * @param organization the organization where the token resides
     * @return a TokenSchema object representing the retrieved token, or null if the
     *         token could not be retrieved
     */
    public TokenSchema getTokenBySerialNumber(String serial, String organization) {

        // Logging a fine level message to indicate that a token is being checked
        Log.log(Level.FINE, "Checking token: {0}", serial);

        // Creating a new instance of GetTokensDocument and setting the necessary
        // properties
        GetTokensDocument getTokenDocument = GetTokensDocument.Factory.newInstance();
        GetTokensDocument.GetTokens getTokenData = getTokenDocument.addNewGetTokens();
        getTokenData.setSerial(serial);
        getTokenData.setOrganization(organization);
        getTokenData.setStartRecord(0);
        getTokenData.setPageSize(1);

        try {
            // Sending the GetTokensDocument to the BSIDCA to retrieve a token response
            // document
            GetTokensResponseDocument getTokenResponseDocument = this.workflowService.getBsidca()
                    .getTokens(getTokenDocument);
            // Extracting the token response from the document
            GetTokensResponseDocument.GetTokensResponse getTokenResponse = getTokenResponseDocument
                    .getGetTokensResponse();
            // Logging the XML server response at the fine level
            Log.log(Level.FINE, "getTokenBySerialNumber() XML server response: {0}", getTokenResponse);
            // Parsing the token response and returning the token schema
            return TokenSchemaParser.parse(getTokenResponse.toString());
        } catch (RemoteException e) {
            // Logging a severe level message and the exception stack trace if a remote
            // exception occurs
            Log.log(Level.SEVERE, "BSIDCA remote server exception encountered during getTokenBySerialNumber:", e);
        }
        // Returning null if the token schema cannot be retrieved
        return null;
    }

    /**
     * This method returns a list of token serial numbers for a given username and
     * organization.
     * 
     * @param username     the username to retrieve token serial numbers for
     * @param organization the organization where the tokens reside
     * @return a list of token serial numbers owned by the specified username
     */
    public List<String> getSerialsByUsername(String username, String organization) {
        // create a new instance of the GetTokensByOwnerDocument
        GetTokensByOwnerDocument getTokensDocument = GetTokensByOwnerDocument.Factory.newInstance();

        // create a new instance of the GetTokensByOwner object and set the required
        // fields
        GetTokensByOwnerDocument.GetTokensByOwner getTokensData = getTokensDocument.addNewGetTokensByOwner();
        getTokensData.setUserName(username);
        getTokensData.setOrganization(organization);

        try {
            // get the response from the server by calling the getTokensByOwner method on
            // the workflow service
            GetTokensByOwnerResponseDocument getTokensResponseDocument = this.workflowService.getBsidca()
                    .getTokensByOwner(getTokensDocument);

            // convert the response to a string and log it
            String getTokensResponse = getTokensResponseDocument.getGetTokensByOwnerResponse().toString();
            Log.log(Level.FINE, "BSIDCA get tokens response document: {0}", getTokensResponseDocument);

            // extract the token serials from the response XML using the XmlUtils helper
            // class
            return XmlUtils.extractTokenSerials(getTokensResponse);
        } catch (Exception e) {
            // if an exception occurs while getting the tokens by owner, log the error and
            // return an empty list
            Log.log(Level.SEVERE, "BSIDCA exception encountered while retrieivng token serials by username:", e);
            return new ArrayList<>();
        }

    }

    /**
     * This method returns a list of tokens owned by the specified username and
     * matching the provided token type and state filters.
     * 
     * @param username     the username to retrieve tokens for
     * @param organization the organization where the tokens reside
     * @param tokenType    the type of token to filter on (null to retrieve all
     *                     token types)
     * @param tokenState   the state of token to filter on (null to retrieve tokens
     *                     in all states)
     * @return a list of TokenSchema objects matching the specified filters
     */
    public List<TokenSchema> getTokensByUsername(String username, String organization, TokenType tokenType,
            TokenState tokenState) {
        // Get a list of token serial numbers associated with the given username and
        // organization
        List<String> tokenSerials = this.getSerialsByUsername(username, organization);

        // Create a new list to hold the TokenSchema objects
        List<TokenSchema> tokenList = new ArrayList<>();

        // Log the list of token serial numbers that will be checked
        Log.log(Level.FINE, "Checking each of the serials to see the type: {0}", tokenSerials);

        try {
            // Check each serial to see if it is a grid token and matches the filter
            // criteria
            for (String serial : tokenSerials) {
                // Get the token schema object for the current serial number
                TokenSchema tokenResponse = this.getTokenBySerialNumber(serial, organization);

                // Determine if the token schema object matches the filter criteria
                boolean isFilterMatchType = tokenType == null
                        || tokenResponse.getType().equalsIgnoreCase(tokenType.getValue());
                boolean isFilterMatchState = tokenState == null
                        || tokenResponse.getState().equalsIgnoreCase(tokenState.toString());

                // Log if the TokenState is null
                if (tokenState == null) {
                    Log.fine("TokenState is null in getTokensbyUsername");
                } else {
                    Log.log(Level.FINE, "TokenState is not null in getTokensbyUsername: {0}", tokenState);
                }

                // If the token schema object matches the filter criteria, add it to the list
                if (isFilterMatchType && isFilterMatchState) {
                    tokenList.add(tokenResponse);
                }
            }
            // Return the list of TokenSchema objects that match the filter criteria
            return tokenList;

        } catch (Exception e) {
            // Log and return an empty list if there is an exception retrieving the tokens
            Log.log(Level.SEVERE, "BSIDCA exception while retrieving tokens by username:", e);
            return new ArrayList<>();
        }
    }

    /**
     * This method revokes the token with the specified serial number and
     * organization.
     * 
     * @param serial       the serial number of the token to revoke
     * @param organization the organization where the token resides
     * @return true if the token was successfully revoked, false otherwise
     */
    public boolean revokeTokenBySerial(String serial, String organization) {
        // Get the token object associated with the provided serial and organization
        TokenSchema token = this.getTokenBySerialNumber(serial, organization);

        // Revoke the token for the user associated with the token and return the result
        return this.revokeToken(token.getUserId(), serial, organization);
    }

    /**
     * This method revokes all tokens owned by the specified username and matching
     * the provided token type and state filters.
     * 
     * @param username     the username to revoke tokens for
     * @param organization the organization where the tokens reside
     * @param tokenType    the type of token to filter on (null to revoke all token
     *                     types)
     * @param tokenState   the state of token to filter on (null to revoke tokens in
     *                     all states)
     * @return a map of token serial numbers and their corresponding revoke success
     *         status
     */
    public Map<String, Boolean> revokeTokenByUsername(String username, String organization, TokenType tokenType,
            TokenState tokenState) {

        // Retrieve a list of tokens using the given username, organization, token type,
        // and token state
        List<TokenSchema> tokenSerials = this.getTokensByUsername(username, organization, tokenType, tokenState);

        // Create a HashMap to store the results of the token revocation operation
        Map<String, Boolean> tokenResults = new HashMap<>();

        // Iterate through the list of tokens and revoke each one, storing the result in
        // the tokenResults HashMap
        for (TokenSchema token : tokenSerials) {
            boolean response = this.revokeToken(username, token.getSerialNumber(), organization);
            tokenResults.put(token.getSerialNumber(), response);
        }

        // Return the tokenResults HashMap containing the results of the token
        // revocation operation
        return tokenResults;
    }

    /**
     * This method revokes the token owned by the specified username and matching
     * the provided serial number and organization.
     * 
     * @param username     the username to revoke the token for
     * @param serial       the serial number of the token to revoke
     * @param organization the organization where the token resides
     * @return true if the token was successfully revoked, false otherwise
     */
    public boolean revokeToken(String username, String serial, String organization) {
        // Build a message to log
        String infoMessage = "Revoking token# " + serial + " from user " + username + " in organization "
                + organization;
        // Log the message
        Log.info(infoMessage);
        // Create a new RevokeTokenDocument instance
        RevokeTokenDocument revokeTokenDocument = RevokeTokenDocument.Factory.newInstance();
        // Get the RevokeToken instance from the RevokeTokenDocument
        RevokeTokenDocument.RevokeToken revokeTokenData = revokeTokenDocument.addNewRevokeToken();
        // Set the values for the RevokeToken fields
        revokeTokenData.setUserName(username);
        revokeTokenData.setSerial(serial);
        revokeTokenData.setOrganization(organization);
        revokeTokenData.setRevokeMode(RevokeSelection.RETURNTO_INVENTORY_INITIALIZED);
        revokeTokenData.setRevokeStaticPassword(false);

        RevokeTokenResponseDocument revokeTokenResponseDocument;
        try {
            // Call the remote revokeToken method and get the response
            revokeTokenResponseDocument = this.workflowService.getBsidca().revokeToken(revokeTokenDocument);
            // Return true if the revokeTokenResult equals SUCCESS
            return revokeTokenResponseDocument.getRevokeTokenResponse().getRevokeTokenResult()
                    .equals(RevokeResult.SUCCESS);

        } catch (RemoteException e) {
            // Log a severe error message if there is a remote exception
            Log.log(Level.SEVERE, "BSIDCA remote server exception while revoking token:", e);
        }

        // Return false if there is any error
        return false;
    }
}
