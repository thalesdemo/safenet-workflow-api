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
 * The UserService class is responsible for handling all operations related 
 * to user accounts, such as retrieving, creating and deleting user accounts.
 * It communicates with the SafeNet (BSIDCA) API to perform these operations.
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cryptocard.www.blackshield.AddUserDocument;
import com.cryptocard.www.blackshield.AddUserResponseDocument;
import com.cryptocard.www.blackshield.GetUserDocument;
import com.cryptocard.www.blackshield.GetUserResponseDocument;
import com.cryptocard.www.blackshield.RemoveUserDocument;
import com.cryptocard.www.blackshield.RemoveUserResponseDocument;
import com.cryptocard.www.blackshield.RevokeSelection;
import com.cryptocard.www.blackshield.User;
import com.cryptocard.www.blackshield.UserDeleteResult;
import com.cryptocard.www.blackshield.RemoveUserResponseDocument.RemoveUserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserService {

    /**
     * 
     * Autowires the WorkflowService instance used for interacting with the
     * BlackShield API.
     */
    @Autowired
    WorkflowService workflowService;

    /**
     * 
     * Autowires the ObjectMapper instance used for serializing and deserializing
     * JSON objects.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * This is the logger instance for the UserService class. The logger is
     * initialized with the name of the class for which it is used.
     */
    private static final Logger Log = Logger.getLogger(UserService.class.getName());

    /**
     * Retrieves a user account from the BSIDCA server based on the specified
     * username and organization.
     * 
     * @param username     The username of the user account to retrieve.
     * @param organization The name of the organization that the user belongs to.
     * @return A UserSchema object representing the user account.
     */
    public UserSchema getUser(String username, String organization) {

        // Create a GetUserDocument and GetUser object to retrieve the user
        GetUserDocument getUser = GetUserDocument.Factory.newInstance();
        GetUserDocument.GetUser getUserData = getUser.addNewGetUser();
        getUserData.setUserName(username);
        getUserData.setOrganization(organization);

        // Make a call to retrieve the user
        GetUserResponseDocument getUserResponse = null;
        try {
            getUserResponse = workflowService.getBsidca().getUser(getUser);
        } catch (RemoteException e) {
            String errorMsg = "Could not retrieve user information due to a server remote exception with user: "
                    + username;
            Log.log(Level.SEVERE, errorMsg, e);
        }

        // TODO: bool getUserResponse.getGetUserResponse().isSetGetUserResult()

        // Check if the response is null (TODO: throw exception instead?)
        if (getUserResponse == null) {
            return new UserSchema();
        }

        // Get the GetUserResponse and User objects from the response
        GetUserResponseDocument.GetUserResponse getResponse = getUserResponse.getGetUserResponse();
        User result = getResponse.getGetUserResult();

        // TODO: getRestrictedDays() is not set

        // Log the GetUser response (debugging)
        Log.log(Level.FINE, "GetUser returns: {0}", result);

        // Check if the User object is null
        if (result == null) {
            return new UserSchema();
        }

        try {
            // Convert the User object to a JSON object using the custom serializer
            return objectMapper.convertValue(result, UserSchema.class);
        } catch (IllegalArgumentException e) {
            Log.log(Level.SEVERE, "Could not convert User object to JSON", e);
        }

        return new UserSchema();

    }

    /**
     * Deletes a user account from the BSIDCA server based on the specified username
     * and organization.
     * 
     * @param username     The username of the user account to delete.
     * @param organization The name of the organization that the user belongs to.
     * @return A boolean indicating whether the deletion was successful or not.
     */
    public boolean deleteUser(String username, String organization) {
        // Create message string for log info
        String message = String.format("Deleting %s from %s", username, organization);
        Log.info(message);

        // Create new RemoveUserDocument and its RemoveUser object
        RemoveUserDocument removeUserDocument = RemoveUserDocument.Factory.newInstance();
        RemoveUserDocument.RemoveUser removeUserData = removeUserDocument.addNewRemoveUser();

        // Set the properties of the RemoveUser object
        removeUserData.setUserName(username);
        removeUserData.setOrganization(organization);
        removeUserData.setTokenOption(RevokeSelection.RETURNTO_INVENTORY_INITIALIZED);

        try {
            // Call the BSIDCA removeUser method with the RemoveUserDocument
            RemoveUserResponseDocument removeUserResponseDocument = this.workflowService.getBsidca()
                    .removeUser(removeUserDocument);

            // Get the RemoveUserResponse from the RemoveUserResponseDocument
            RemoveUserResponse removeUserResponse = removeUserResponseDocument.getRemoveUserResponse();

            // Log the BSIDCA response at FINE level
            Log.log(Level.FINE, "BSIDCA response: {0}", removeUserResponse);

            // Return true if the user was successfully deleted, false otherwise
            return removeUserResponse.getRemoveUserResult() == UserDeleteResult.DELETED;

        } catch (RemoteException e) {
            // Log a SEVERE error if an exception occurred while calling BSIDCA removeUser
            // method
            Log.log(Level.SEVERE, "Could not delete user due to a remote BSIDCA exception. ", e);
        }

        // Return false if the user was not deleted
        return false;

    }

    /**
     * Creates a user with the given UserSchema and organization in the BSIDCA
     * system.
     * Converts the UserSchema object to JSON string and deserializes it to User
     * object.
     * Creates an AddUserDocument instance and sets the User object and
     * organization.
     * Sends the AddUserDocument to the BSIDCA system and returns a boolean
     * indicating success or failure.
     * 
     * @param userJson     The UserSchema object to be created as a user.
     * @param organization The organization in which to create the user.
     * @return A boolean indicating success or failure of user creation.
     */
    public boolean createUser(UserSchema userJson, String organization) {

        try {
            // Convert the UserSchema object to JSON string
            String userJsonString = objectMapper.writeValueAsString(userJson);

            // Create a new AddUserDocument and AddUser object
            AddUserDocument userDocument = AddUserDocument.Factory.newInstance();
            AddUserDocument.AddUser userData = userDocument.addNewAddUser();

            // Deserialize the JSON string to a User object
            User user = objectMapper.readValue(userJsonString, User.class);

            // Set the User object and organization on the AddUser object
            userData.setUser(user);
            userData.setOrganization(organization);

            // Log the request document for debugging purposes
            String debugMessage = "Creating user with the below request document:\n" + userDocument;
            Log.fine(debugMessage);

            // Call the BSIDCA addUser method with the AddUserDocument and return the result
            AddUserResponseDocument addUserResponseDocument = this.workflowService.getBsidca().addUser(userDocument);
            return addUserResponseDocument.getAddUserResponse().getAddUserResult();
        } catch (Exception e) {
            // Log an error message if an exception is encountered
            Log.log(Level.SEVERE, "An exception was encountered while creating the user.", e);
        }
        return false;
    }
}
