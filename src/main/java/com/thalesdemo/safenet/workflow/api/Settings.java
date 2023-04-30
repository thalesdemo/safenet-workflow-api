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
 * The Settings class represents the configuration settings required for
 * communicating with the SafeNet BSIDCA API. It contains the base URL, 
 * username, password, and API key hash for authentication.
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Settings {

    /**
     * The base URI for the SafeNet BSIDCA API.
     */
    private static final String BASE_URI = "/BSIDCA/BSIDCA.asmx?WSDL";

    /**
     * The base URL for the SafeNet BSIDCA API.
     */
    @JsonProperty("base_url")
    private String baseUrl;

    /**
     * The username used for authentication with the SafeNet BSIDCA API.
     */
    @JsonProperty("username")
    private String username;

    /**
     * The password used for authentication with the SafeNet BSIDCA API.
     */
    @JsonProperty("password")
    private String password;

    /**
     * The API key hash used for authentication with the SafeNet BSIDCA API.
     */
    @JsonProperty("key_hash")
    private String apiKeyHash;

    /**
     * Gets the base URL for the SafeNet BSIDCA API.
     * 
     * @return The base URL for the SafeNet BSIDCA API.
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Gets the username used for authentication with the SafeNet BSIDCA API.
     * 
     * @return The username used for authentication with the SafeNet BSIDCA API.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password used for authentication with the SafeNet BSIDCA API.
     * 
     * @return The password used for authentication with the SafeNet BSIDCA API.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the base URL for the SafeNet BSIDCA API.
     * 
     * @param baseUrl The base URL for the SafeNet BSIDCA API.
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Sets the username used for authentication with the SafeNet BSIDCA API.
     * 
     * @param username The username used for authentication with the SafeNet BSIDCA
     *                 API.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the password used for authentication with the SafeNet BSIDCA API.
     * 
     * @param password The password used for authentication with the SafeNet BSIDCA
     *                 API.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the API key hash used for authentication with the SafeNet BSIDCA API.
     * 
     * @param apiKeyHash The API key hash used for authentication with the SafeNet
     *                   BSIDCA API.
     */
    public void setApiKeyHash(String apiKeyHash) {
        this.apiKeyHash = apiKeyHash;
    }

    /**
     * Gets the URL for the SafeNet BSIDCA API.
     * 
     * @return The URL for the SafeNet BSIDCA API.
     */
    public String getBsidcaUrl() {
        return baseUrl + BASE_URI;
    }

    /**
     * Gets the API key hash used for authentication with the SafeNet BSIDCA API.
     * 
     * @return The API key hash used for authentication with the SafeNet BSIDCA API.
     */
    public String getApiKeyHash() {
        return apiKeyHash;
    }

}