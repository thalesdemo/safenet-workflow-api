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
 * Provides API key authentication functionality.
 * 
 * Attributes:
 * passwordEncoder (BCryptPasswordEncoder): Encoder used to hash the API key.
 * apiKeyHash (str): Hashed API key stored in the settings.json config file.
 *
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class ApiKeyAuthService {

    /**
     * The logger for the ApiKeyAuthService class.
     */
    private static final Logger Log = Logger.getLogger(ApiKeyAuthService.class.getName());

    /**
     * The encoder for encoding the API key hash.
     */
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * The hash of the API key that is stored in the environment variable.
     */
    private final String apiKeyHash;

    /**
     * Constructor for the ApiKeyAuthService class.
     * 
     * @param passwordEncoder Encoder for encoding the API key hash
     * @param apiKeyHash      Hash of the API key that is stored in the environment
     *                        variable
     */
    public ApiKeyAuthService(BCryptPasswordEncoder passwordEncoder, String apiKeyHash) {
        this.passwordEncoder = passwordEncoder;
        this.apiKeyHash = apiKeyHash;
    }

    /**
     * Get the Authentication object if the provided API key matches the stored API
     * key hash.
     *
     * @param apiKey API key from the request
     * @return Authentication object if the API key is valid, null otherwise
     */
    public Authentication getAuthentication(String apiKey) {
        // Check if the provided API key matches the stored hashed API key
        boolean apiKeyIsValid = checkApiKey(apiKey);

        // If the API key is valid, create an authentication object with the
        // "ROLE_API_USER" authority
        if (apiKeyIsValid) {
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_API_USER"));
            return new UsernamePasswordAuthenticationToken(apiKey, null, authorities);
        }
        // If the API key is not valid, return null to indicate that the authentication
        // failed
        else {
            return null;
        }
    }

    /**
     * 
     * A private helper method that checks if the provided API key matches the
     * stored hashed API key using the
     * {@code passwordEncoder}. The method returns {@code true} if the API key
     * matches, and {@code false} otherwise.
     * 
     * @param apiKey The API key to be checked.
     * @return {@code true} if the API key matches the stored hashed API key, and
     *         {@code false} otherwise.
     */
    private boolean checkApiKey(String apiKey) {
        return passwordEncoder.matches(apiKey, this.apiKeyHash);
    }

}
