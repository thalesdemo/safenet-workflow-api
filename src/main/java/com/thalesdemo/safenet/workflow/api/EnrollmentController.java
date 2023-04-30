
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
 * The {@code EnrollmentController} class is a REST controller that handles 
 * requests for enrolling tokens for users.
 *
 * The controller is mapped to the "/token/enroll" endpoint of the API using 
 * the {@code @RequestMapping} annotation.
 *
 * The class has a single method, "enrollToken", which handles POST requests to 
 * the "/token/enroll/{partner_id}/{username}" endpoint of the API. The method
 * takes in a number of parameters and a {@code EnrollmentRequest} object in 
 * the request body, and returns an {@code EnrollmentResponse} object.
 *
 * @author Cina Shaykhian
 * @contact hello@onewelco.me 
 */

package com.thalesdemo.safenet.workflow.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("${api.basePath}/token/enroll")
@Tag(name = "Token")
public class EnrollmentController {

        /**
         * The {@code EnrollmentService} object responsible for handling enrollment
         * requests.
         */
        @Autowired
        EnrollmentService enrollmentService;

        /**
         * The delimiter used to separate the partner ID and username in the unique
         * username.
         */
        @Value("${api.user.delimiter}")
        private String delimiter;

        /**
         * Handles requests for enrolling tokens for users.
         * 
         * @param partnerId         The partner ID of the user for which to enroll a
         *                          token.
         * @param username          The username for which to enroll a token.
         * @param organization      The name of the organization.
         * @param tokenType         The type of token for which to enroll.
         * @param enrollmentMethod  The enrollment method to use.
         * @param enrollmentRequest The enrollment request object in the request body.
         * @return An {@code EnrollmentResponse} object with details of the enrollment
         *         process.
         */
        @Operation(summary = "Enroll a token to a given user", description = "Enroll a token to a user's account.")
        @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnrollmentResponse.class), examples = @ExampleObject(description = "API enrollment", value = "To do")), description = "The enrollment was successful via API.")
        @PostMapping("/{partner_id}/{username}")
        public EnrollmentResponse enrollToken(
                        @Parameter(description = "The partner ID of the user for which to enroll a token.") @PathVariable("partner_id") String partnerId,
                        @Parameter(description = "The username for which to enroll a token.") @PathVariable("username") String username,
                        @Parameter(description = "The name of the organization.") @RequestParam(value = "organization") String organization,
                        @Parameter(description = "The type of token for which to enroll.", schema = @Schema(type = "string", allowableValues = {
                                        "GrIDsure", "RADIUS", "OATH", "SMS", "Email", "Password", "KT", "RB", "GOLD",
                                        "eToken",
                                        "MobilePASS", "GoogleAuthenticator"
                        })) @RequestParam(value = "token_type") TokenType tokenType,
                        @Parameter(description = "The enrollment method to use.", schema = @Schema(type = "string", allowableValues = {
                                        "Email", "URL", "API"
                        })) @RequestParam(value = "method") EnrollmentMethod enrollmentMethod,
                        @RequestBody @Schema(example = EnrollmentExamples.REQUEST) EnrollmentRequest enrollmentRequest) {

                // Combine partner ID and username to form a unique username
                String uniqueUsername = partnerId + delimiter + username;

                // Set the necessary fields in the enrollment request
                enrollmentRequest.setUsername(uniqueUsername);
                enrollmentRequest.setOrganization(organization);
                enrollmentRequest.setTokenType(tokenType);
                enrollmentRequest.setEnrollmentMethod(enrollmentMethod);

                // Delegate the enrollment process to the EnrollmentService and return the
                // result
                return this.enrollmentService.enrollToken(enrollmentRequest);
        }

}