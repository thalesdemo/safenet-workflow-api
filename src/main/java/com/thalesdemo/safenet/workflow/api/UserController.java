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
 * The UserController class handles all incoming requests related to user
 * accounts.
 * It is responsible for creating, updating, retrieving and deleting user 
 * accounts.
 * 
 * The UserController class is responsible for handling requests related to
 * user accounts. It exposes methods for creating, updating, retrieving and 
 * deleting user accounts. These methods receive input parameters such as 
 * partner ID, username, and organization name to identify the user account 
 * to act on. The class relies on the UserService to perform these operations,
 * and the delimiter string property is used to concatenate the partner ID and
 * username to form a unique username. The class also logs warnings for 
 * unimplemented methods.
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.logging.Logger;

@RestController
@RequestMapping("${api.basePath}/users")
@Tag(name = "User")
public class UserController {

    /**
     * This is the logger instance for the UserController class. The logger is
     * initialized with the name of the class for which it is used.
     */
    private static final Logger Log = Logger.getLogger(UserController.class.getName());

    /**
     * 
     * The UserService instance used by this controller to handle user-related
     * operations.
     */
    @Autowired
    UserService userService;

    /**
     * 
     * The delimiter string used to separate the partner ID and username in unique
     * usernames.
     * Obtained from the "api.user.delimiter" configuration property.
     */
    @Value("${api.user.delimiter}")
    private String delimiter;

    /**
     * Retrieves account details of a given user.
     * 
     * @param partnerId    The partner ID of the user for which to retrieve account
     *                     details.
     * @param username     The username for which to retrieve account details.
     * @param organization The name of the organization.
     * @return UserSchema containing the account details of the user.
     */
    @Operation(summary = "Get account details of a given user", description = "Retrieve information about a given user account.")
    @GetMapping("/{partner_id}/{username}")
    public UserSchema getUser(
            @Parameter(description = "The partner ID of the user for which to retrieve account details.") @PathVariable("partner_id") String partnerId,
            @Parameter(description = "The username for which to retrieve account details.") @PathVariable("username") String username,
            @Parameter(description = "The name of the organization.") @RequestParam("organization") String organization) {
        // Constructs a unique username by concatenating the partner ID, delimiter, and
        // username
        String uniqueUsername = partnerId + delimiter + username;

        // Calls the UserService to retrieve the user information based on the unique
        // username and organization
        return userService.getUser(uniqueUsername, organization);

    }

    /**
     * Creates an account for a given user.
     * 
     * @param partnerId    The partner ID of the user for which to create an
     *                     account.
     * @param username     The username for which to create an account.
     * @param organization The name of the organization.
     * @param user         UserSchema representing the user account to create.
     * @return boolean indicating whether the user account was created successfully
     *         or not.
     */
    @Operation(summary = "Create an account for a given user", description = "Create a user account.")
    @PostMapping("/{partner_id}/{username}")
    public boolean createUser(
            @Parameter(description = "The partner ID of the user for which to create an account.") @PathVariable("partner_id") String partnerId,
            @Parameter(description = "The username for which to create an account.") @PathVariable("username") String username,
            @Parameter(description = "The name of the organization.") @RequestParam("organization") String organization,
            @RequestBody(required = true) @Schema(example = UserExamples.CREATE) UserSchema user) {
        // Concatenate partnerId, delimiter, and username to form uniqueUsername
        String uniqueUsername = partnerId + delimiter + username;

        // Set the userName field of the UserSchema object to uniqueUsername. Path
        // variable takes precedence over body value.
        user.setUserName(uniqueUsername);

        // Call the UserService to create the user account and return the result
        return this.userService.createUser(user, organization);
    }

    /**
     * Removes an account for a given user.
     * 
     * @param partnerId    The partner ID of the user for which to remove the
     *                     account.
     * @param username     The username for which to remove the account.
     * @param organization The name of the organization.
     * @return String containing a JSON object indicating whether the user account
     *         was deleted successfully or not.
     */
    @Operation(summary = "Remove an account for a given user", description = "Remove a user account.")
    @DeleteMapping(value = "/{partner_id}/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String deleteUser(
            @Parameter(description = "The partner ID of the user for which to remove the account.") @PathVariable("partner_id") String partnerId,
            @Parameter(description = "The username for which to remove the account.") @PathVariable("username") String username,
            @Parameter(description = "The name of the organization.") @RequestParam("organization") String organization) {

        // Construct the unique username for the given user by concatenating partnerId,
        // delimiter, and username
        String uniqueUsername = partnerId + delimiter + username;

        // Call the deleteUser method of the userService and return the result as a JSON
        // string
        return "{ \"delete_status\":"
                + userService.deleteUser(uniqueUsername, organization)
                + "}";

    }

    /**
     * 
     * This method updates an account for a given user.
     * 
     * @return A string message indicating that this method is not implemented yet.
     */
    @Operation(summary = "Update an account for a given user", description = "Update a user account.")
    @PatchMapping("/{partner_id}/{username}")
    public String updateUser(

    ) {
        Log.warning("This method is not implemented yet: updateUser");
        return "Not implemented.";
    }

}
