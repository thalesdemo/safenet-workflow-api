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
 * The TokenController is a RESTful web service controller that handles
 * requests related to user tokens. It provides endpoints for retrieving token
 * information, revoking tokens, and retrieving a list of tokens associated 
 * with a user's account. The controller is responsible for mapping the 
 * requests to their respective service methods and returning the appropriate
 * HTTP response to the client. The TokenController class is annotated with
 * Spring annotations to specify the base path for the API and to inject the
 * TokenService dependency. The endpoints are also annotated with Swagger
 * annotations to provide documentation for the API. 
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("${api.basePath}")
@Tag(name = "Token")
public class TokenController {

    /**
     * The TokenService dependency is injected using the @Autowired annotation.
     */
    @Autowired
    TokenService tokenService;

    /**
     * The value of api.user.delimiter is injected using the @Value annotation.
     */
    @Value("${api.user.delimiter}")
    private String delimiter;

    /**
     * Retrieves information about the token(s) associated with a user's account.
     *
     * @param username  The username for which to retrieve token information.
     *                  This is a required path variable.
     * @param tokenType The type of token for which to retrieve information.
     *                  This is an optional query parameter.
     * @return A string containing the requested token information.
     */

    @Operation(summary = "Get token(s) information for a user", description = "Retrieve information about the token(s) associated with a user's account.")

    @ApiResponse(responseCode = "200", description = "Token information retrieved successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")

    @GetMapping("/tokens/{realm_id}/{username}")
    public List<TokenSchema> getTokensByUsername(
            @Parameter(description = "The realm ID of the user for which to retrieve token(s) information.") @PathVariable("realm_id") String realmId,
            @Parameter(description = "The username for which to retrieve token(s) information.") @PathVariable("username") String username,
            @Parameter(description = "The name of the organization.") @RequestParam(value = "organization") String organization,
            @Parameter(description = "The type of token(s) for which to retrieve information.", schema = @Schema(type = "string", allowableValues = {
                    "GrIDsure", "RADIUS", "OATH", "SMS", "Email", "Password", "KT", "RB", "GOLD", "eToken",
                    "MobilePASS", "GoogleAuthenticator"
            })) @RequestParam(value = "token_type", required = false) TokenType tokenType,
            @Parameter(description = "The state of token(s) for which to retrieve information.", schema = @Schema(type = "string", allowableValues = {
                    "Active", "Suspended", "Locked"
            })) @RequestParam(value = "token_state", required = false) TokenState tokenState) {

        try {
            // Concatenate realmId and username to create a uniqueUsername
            String uniqueUsername = UserUtils.getUniqueUsername(realmId, delimiter, username);

            // Retrieve the requested token information
            return this.tokenService.getTokensByUsername(uniqueUsername, organization, tokenType, tokenState);
        } catch (Exception e) {
            // If there is an exception, return an empty list
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves information about a token based on its serial number.
     * 
     * @param serialnumber The serial number of the token for which to retrieve
     *                     information.
     *                     This is a required path variable.
     * @param organization The name of the organization.
     *                     This is a required query parameter.
     * @return The token schema containing the requested token information.
     */
    @Operation(summary = "Get token information from the serial number", description = "Retrieve information about the token based on a token serial number.")
    @GetMapping("/token/{serial_number}")
    public TokenSchema getTokenBySerial(
            @Parameter(description = "The serial number of the token for which to retrieve information.") @PathVariable(value = "serial_number") String serialnumber,
            @Parameter(description = "The name of the organization.") @RequestParam(value = "organization") String organization) {

        // Try to retrieve the token information by its serial number and return it
        // If there is an exception, return null
        try {
            return this.tokenService.getTokenBySerialNumber(serialnumber, organization);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Revoke a token based on the provided serial number.
     * 
     * @param serial       The serial number of the token to revoke.
     *                     This is a required path variable.
     * @param organization The name of the organization.
     *                     This is a required query parameter.
     * @return A boolean indicating whether the token was successfully revoked.
     */
    @Operation(summary = "Revoke a token from the serial number", description = "Revoke a token based on a token serial number.")
    @DeleteMapping("/token/{serial_number}")
    public boolean revokeTokenBySerial(
            @Parameter(description = "The serial number of the token for which to revoke from a user.") @PathVariable("serial_number") String serial,
            @Parameter(description = "The name of the organization.") @RequestParam String organization) {

        // call the revokeTokenBySerial method in TokenService and return the result
        return this.tokenService.revokeTokenBySerial(serial, organization);
    }

    /**
     * Revokes token(s) associated with a user's account.
     * 
     * @param realmId      The realm ID of the user for which to revoke token(s).
     *                     This is a required path variable.
     * @param username     The username for which to revoke token(s).
     *                     This is a required path variable.
     * @param organization The name of the organization.
     *                     This is a required query parameter.
     * @param tokenType    The type of token(s) for which to revoke.
     *                     This is an optional query parameter.
     * @param tokenState   The state of token(s) for which to revoke.
     *                     This is an optional query parameter.
     */
    @Operation(summary = "Revoke token(s) from a user", description = "Revoke token(s) associated with a user's account.")
    @DeleteMapping("/tokens/{realm_id}/{username}")
    public Map<String, Boolean> revokeTokensByUsername(
            @Parameter(description = "The realm ID of the user for which to revoke token(s).") @PathVariable("realm_id") String realmId,
            @Parameter(description = "The username for which to revoke token(s).") @PathVariable("username") String username,
            @Parameter(description = "The name of the organization.") @RequestParam(value = "organization") String organization,
            @Parameter(description = "The type of token(s) for which to revoke.", schema = @Schema(type = "string", allowableValues = {
                    "GrIDsure", "RADIUS", "OATH", "SMS", "Email", "Password", "KT", "RB", "GOLD", "eToken",
                    "MobilePASS", "GoogleAuthenticator"
            })) @RequestParam(value = "token_type", required = false) TokenType tokenType,
            @Parameter(description = "The state of token(s) for which to revoke.", schema = @Schema(type = "string", allowableValues = {
                    "Active", "Suspended", "Locked"
            })) @RequestParam(value = "token_state", required = false) TokenState tokenState) {

        // Combine the realm ID and username to get the unique username
        String uniqueUsername = UserUtils.getUniqueUsername(realmId, delimiter, username);

        // Call the revokeTokenByUsername() method of the tokenService object
        // to revoke the tokens and get the status of each revocation request.
        return this.tokenService.revokeTokenByUsername(uniqueUsername, organization, tokenType, tokenState);
    }

}
