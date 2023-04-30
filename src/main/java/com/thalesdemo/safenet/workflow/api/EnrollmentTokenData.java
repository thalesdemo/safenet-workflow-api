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
 * This class represents the data associated with an enrollment token.
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(value = Include.NON_EMPTY)
public class EnrollmentTokenData {
    /**
     * The method used to enroll the user.
     */
    @JsonProperty("method")
    @Schema(type = "string", allowableValues = { "EMAIL", "URL", "API" })
    private EnrollmentMethod enrollmentMethod;

    /**
     * The type of token associated with the enrollment.
     */
    @JsonProperty("token_type")
    private TokenType tokenType;

    /**
     * The provisioning ID of the token.
     */
    private Integer provId;

    /**
     * The URL associated with the enrollment.
     */
    private String url;

    /**
     * The image associated with the token.
     */
    private String image;

    /**
     * The state of the enrollment token.
     */
    private String state;

    /**
     * Returns the provisioning ID of the token.
     * 
     * @return the provisioning ID of the token
     */
    public Integer getProvId() {
        return provId;
    }

    /**
     * Sets the provisioning ID of the token.
     * 
     * @param provId the provisioning ID of the token
     */
    public void setProvId(Integer provId) {
        this.provId = provId;
    }

    /**
     * Returns the enrollment method used.
     * 
     * @return the enrollment method used
     */
    public EnrollmentMethod getEnrollmentMethod() {
        return enrollmentMethod;
    }

    /**
     * Sets the enrollment method used.
     * 
     * @param enrollmentMethod the enrollment method used
     */
    public void setEnrollmentMethod(EnrollmentMethod enrollmentMethod) {
        this.enrollmentMethod = enrollmentMethod;
    }

    /**
     * Returns the token type associated with the enrollment.
     * 
     * @return the token type associated with the enrollment
     */
    public TokenType getTokenType() {
        return tokenType;
    }

    /**
     * Sets the token type associated with the enrollment.
     * 
     * @param tokenType the token type associated with the enrollment
     */
    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    /**
     * Returns the URL associated with the enrollment.
     * 
     * @return the URL associated with the enrollment
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL associated with the enrollment.
     * 
     * @param url the URL associated with the enrollment
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Returns the image associated with the token.
     * 
     * @return the image associated with the token
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets the image associated with the token.
     * 
     * @param image the image associated with the token
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Returns the state of the enrollment token.
     * 
     * @return the state of the enrollment token
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the state of the enrollment token.
     * 
     * @param state the state of the enrollment token
     */
    public void setState(String state) {
        this.state = state;
    }

}
