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
 * Represents a response to an enrollment request.
 * 
 * The {@code @JsonInclude} annotation is used to specify that null or empty 
 * values should not be included in the serialized JSON output. The
 * {@code @JsonProperty} annotation is used to map the {@code token_data} 
 * field in the JSON input to the {@code tokenData} field in this class during
 * deserialization.
 * 
 * @author Cina Shaykhian
 * @contact hello @onewelco.me
*/
package com.thalesdemo.safenet.workflow.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_EMPTY)
public class EnrollmentResponse {
    /**
     * The enrollment status of the request.
     */
    private EnrollmentStatus status;

    /**
     * A message indicating the outcome of the request.
     */

    private String message;
    /**
     * The token data associated with the enrollment request.
     */
    @JsonProperty("token_data")
    private EnrollmentTokenData tokenData = new EnrollmentTokenData();

    /**
     * Gets the token data associated with the enrollment request.
     * 
     * @return The token data.
     */
    public EnrollmentTokenData getTokenData() {
        return tokenData;
    }

    /**
     * Sets the token data associated with the enrollment request.
     * 
     * @param tokenData The token data.
     */
    public void setTokenData(EnrollmentTokenData tokenData) {
        this.tokenData = tokenData;
    }

    /**
     * Gets the enrollment status of the request.
     * 
     * @return The enrollment status.
     */
    public EnrollmentStatus getStatus() {
        return status;
    }

    /**
     * Sets the enrollment status of the request.
     * 
     * @param status The enrollment status.
     */
    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    /**
     * Gets the message indicating the outcome of the request.
     * 
     * @return The message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message indicating the outcome of the request.
     * 
     * @param message The message.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}