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
 * Represents an enrollment request, which specifies the details of a request
 * to enroll a token to a user's account.
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * Represents a request to enroll a token to a user's account.
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class EnrollmentRequest {
    /**
     * The enrollment method to be used.
     */
    @JsonProperty("method")
    private EnrollmentMethod enrollmentMethod;
    /**
     * The type of token to be enrolled.
     */
    @JsonProperty("token_type")
    private TokenType tokenType;
    /**
     * The enrollment state.
     */
    @JsonProperty("state")
    private String enrollmentState;
    /**
     * The user response challenge.
     */
    @JsonProperty("response_challenge")
    private String userResponseChallenge;
    /**
     * The username of the user for which the token is being enrolled.
     */
    private String username;
    /**
     * The name of the organization for which the token is being enrolled.
     */
    private String organization;

    /**
     * Returns the username of the user for which the token is being enrolled.
     * 
     * @return The username of the user for which the token is being enrolled.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user for which the token is being enrolled.
     * 
     * @param username The username of the user for which the token is being
     *                 enrolled.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the name of the organization for which the token is being enrolled.
     * 
     * @return The name of the organization for which the token is being enrolled.
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * Sets the name of the organization for which the token is being enrolled.
     * 
     * @param organization The name of the organization for which the token is being
     *                     enrolled.
     */
    public void setOrganization(String organization) {
        this.organization = organization;
    }

    /**
     * Returns the type of token to be enrolled.
     * 
     * @return The type of token to be enrolled.
     */
    public TokenType getTokenType() {
        return tokenType;
    }

    /**
     * Sets the type of token to be enrolled.
     * 
     * @param tokenType The type of token to be enrolled.
     */
    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    /**
     * Returns the enrollment method to be used.
     * 
     * @return The enrollment method to be used.
     */
    public EnrollmentMethod getEnrollmentMethod() {
        return enrollmentMethod;
    }

    /**
     * Returns the enrollment state.
     * 
     * @return The enrollment state.
     */
    public String getState() {
        return enrollmentState;
    }

    /**
     * Sets the enrollment method to be used.
     * 
     * @param enrollmentMethod The enrollment method to be used.
     */
    public void setEnrollmentMethod(EnrollmentMethod enrollmentMethod) {
        this.enrollmentMethod = enrollmentMethod;
    }

    /**
     * Sets the enrollment state.
     * 
     * @param enrollmentState The enrollment state.
     */
    public void setState(String enrollmentState) {
        this.enrollmentState = enrollmentState;
    }

    /**
     * Returns the user response challenge.
     * 
     * @return The user response challenge.
     */
    public String getUserResponseChallenge() {
        return userResponseChallenge;
    }

    /**
     * Sets the user response challenge.
     * 
     * @param userResponseChallenge The user response challenge.
     */
    public void setUserResponseChallenge(String userResponseChallenge) {
        this.userResponseChallenge = userResponseChallenge;
    }
}
