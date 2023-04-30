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
 * The TokenConstants class is a utility class that provides constants and 
 * methods related to the supported tokens in the application.
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import java.util.Arrays;

public class TokenConstants {

    /**
     * This private constructor is used to prevent the class from being
     * instantiated.
     */
    private TokenConstants() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * An array of supported tokens, represented as strings.
     */
    public static final String[] SUPPORTED_TOKENS = {
            "GrIDsure", "RADIUS", "OATH", "SMS", "Email", "Password", "KT", "RB", "GOLD", "eToken", "MobilePASS",
            "Google Authenticator"
    };

    /**
     * A method that returns an array of supported tokens, represented as strings.
     * The method uses a stream to map each token type value to a string and then
     * converts the resulting
     * stream of strings to an array of strings.
     * 
     * @return an array of supported tokens, represented as strings
     */
    public static String[] getSupportedTokens() {
        return Arrays.stream(TokenType.values())
                .map(TokenType::getValue)
                .toArray(String[]::new);
    }

}
