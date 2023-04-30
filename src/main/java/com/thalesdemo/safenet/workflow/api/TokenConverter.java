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
 * The TokenConverter class provides methods for converting between
 * TokenType and TokenOption types.
 *
 * TokenOption and TokenType are used in different contexts within the
 * application.
 *
 * TokenOption is used by the SafeNet BlackShield SDK to represent the
 * different types of tokens, whereas TokenType is used internally to
 * represent the different token types used by the application.
 *
 * This class provides a mapping between the two types to allow for seamless
 * integration between the different components of the application.
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import com.cryptocard.www.blackshield.TokenOption;

public class TokenConverter {

    /**
     * Private constructor to prevent instantiation.
     */
    private TokenConverter() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Converts a TokenType value to its corresponding TokenOption value.
     * 
     * @param tokenType The TokenType value to convert.
     * @return The corresponding TokenOption value.
     * @throws IllegalArgumentException if the tokenType value is invalid.
     */
    public static TokenOption.Enum tokenTypeToTokenOption(TokenType tokenType) {
        // Create a mapping between TokenType and TokenOption
        switch (tokenType) {
            case GRIDSURE:
                return TokenOption.CUSTOM;
            case MOBILEPASS:
                return TokenOption.MOBILE_PASS;
            case RADIUS:
                return TokenOption.CUSTOM;
            case OATH:
                return TokenOption.OATH;
            case SMS:
                return TokenOption.SMS;
            case EMAIL:
                return TokenOption.SMS;
            case KT:
                return TokenOption.KT;
            case RB:
                return TokenOption.RB;
            case PASSWORD:
                return TokenOption.PASSWORD;
            case GOLD:
                return TokenOption.GOLD;
            case ETOKEN:
                return TokenOption.E_TOKEN;
            case GOOGLEAUTHENTICATOR:
                return TokenOption.GOOGLE_AUTHENTICATOR;

            default:
                throw new IllegalArgumentException("Invalid TokenType value: " + tokenType);
        }
    }

    /**
     * Converts a TokenOption value to its corresponding TokenType value.
     * 
     * @param tokenOptionEnum The TokenOption value to convert.
     * @return The corresponding TokenType value.
     * @throws IllegalArgumentException if the tokenOptionEnum value is invalid.
     */
    public static TokenType tokenOptionToTokenType(TokenOption.Enum tokenOptionEnum) {
        // Create a mapping between TokenOption and TokenType
        switch (tokenOptionEnum.intValue()) {
            case TokenOption.INT_CUSTOM:
                return TokenType.GRIDSURE;

            case TokenOption.INT_MOBILE_PASS:
                return TokenType.MOBILEPASS;

            case TokenOption.INT_OATH:
                return TokenType.OATH;

            case TokenOption.INT_SMS:
                return TokenType.EMAIL;

            case TokenOption.INT_KT:
                return TokenType.KT;

            case TokenOption.INT_RB:
                return TokenType.RB;

            case TokenOption.INT_PASSWORD:
                return TokenType.PASSWORD;

            case TokenOption.INT_GOLD:
                return TokenType.GOLD;

            case TokenOption.INT_E_TOKEN:
                return TokenType.ETOKEN;

            case TokenOption.INT_GOOGLE_AUTHENTICATOR:
                return TokenType.GOOGLEAUTHENTICATOR;

            // TODO: Handle SMS and RADIUS tokens as needed

            default:
                throw new IllegalArgumentException("Invalid TokenOption value: " + tokenOptionEnum);
        }
    }
}