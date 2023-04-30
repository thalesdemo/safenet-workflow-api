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
 * This configuration class defines beans for password encoding and API key 
 * authentication service.
 * The {@code BCryptPasswordEncoder} bean is used for encoding and validating 
 * passwords using the BCrypt algorithm.
 * The {@code ApiKeyAuthService} bean is responsible for validating API keys 
 * and providing authentication objects
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ApiKeyConfiguration {

    /**
     * Creates a {@code BCryptPasswordEncoder} bean for encoding and validating
     * passwords using the BCrypt algorithm.
     * 
     * @return A {@code BCryptPasswordEncoder} object for password encoding and
     *         validation.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates an {@code ApiKeyAuthService} bean that is responsible for validating
     * API keys and providing authentication objects for authenticated requests.
     * The bean depends on the "loadSettings" bean being created first.
     * 
     * @param settings The {@code Settings} object containing the API key hash.
     * @return An {@code ApiKeyAuthService} object for API key authentication.
     */
    @Bean
    @DependsOn("loadSettings")
    public ApiKeyAuthService apiKeyAuthService(Settings settings) {
        return new ApiKeyAuthService(passwordEncoder(), settings.getApiKeyHash());
    }
}
