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
 * This enum class represents the available token types supported by the system.
 * It also provides converters and serializers to handle token types when
 * processing requests and responses.
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.core.convert.converter.Converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@JsonSerialize(using = TokenType.TokenTypeSerializer.class)
@JsonDeserialize(using = TokenType.CaseInsensitiveTokenTypeDeserializer.class)
public enum TokenType {
    /**
     * GrIDsure token type.
     */
    GRIDSURE("GrIDsure"),

    /**
     * RADIUS token type.
     */
    RADIUS("RADIUS"),

    /**
     * OATH token type.
     */
    OATH("OATH"),

    /**
     * SMS token type.
     */
    SMS("SMS"),

    /**
     * Email token type.
     */
    EMAIL("Email"),

    /**
     * Password token type.
     */
    PASSWORD("Password"),

    /**
     * KT token type.
     */
    KT("KT"),

    /**
     * RB token type.
     */
    RB("RB"),

    /**
     * GOLD token type.
     */
    GOLD("GOLD"),

    /**
     * eToken token type.
     */
    ETOKEN("eToken"),

    /**
     * MobilePASS token type.
     */
    MOBILEPASS("MobilePASS"),

    /**
     * Google Authenticator token type.
     */
    GOOGLEAUTHENTICATOR("GoogleAuthenticator");

    /**
     * A string representation of the token type value.
     */
    private String value;

    /**
     * Constructs a new TokenType enum constant with the given string value.
     *
     * @param value the string value for the token type
     */
    private TokenType(String value) {
        this.value = value;
    }

    /**
     * Returns the string value of the token type.
     *
     * @return the string value of the token type
     */
    public String getValue() {
        return value;
    }

    /**
     * Converter class to convert a string representation of TokenType to the
     * corresponding enum value. (For RequestParam)
     */
    public static class TokenTypeConverter implements Converter<String, TokenType> {

        /**
         * Converts a string value to the corresponding TokenType enum value.
         * If the input value is invalid, an IllegalArgumentException is thrown.
         * 
         * @param source the string representation of the TokenType enum value to
         *               convert
         * @return the corresponding TokenType enum value
         * @throws IllegalArgumentException if the input value is invalid or null
         */
        @Override
        public TokenType convert(String source) {
            // Check if source string is not null
            if (source != null) {
                // Convert source string to uppercase
                String value = source.toUpperCase();
                try {
                    // Try to get the corresponding TokenType enum value for the given uppercase
                    // value
                    return TokenType.valueOf(value);
                } catch (IllegalArgumentException e) {
                    // If the value is not a valid TokenType enum constant, throw an exception with
                    // a descriptive message
                    throw new IllegalArgumentException("Invalid token type: " + source);
                }
            }
            // Return null if source string is null
            return null;
        }
    }

    /**
     * Returns an array of string values for all available token types.
     *
     * @return an array of string values for all available token types
     */
    public static String[] getDisplayNames() {

        // Get display names of token types by mapping TokenType values to their
        // corresponding values and return as an array
        return Arrays.stream(values())
                .map(TokenType::getValue)
                .toArray(String[]::new);
    }

    /**
     * Returns a comma-separated string with the proper-cased values of all
     * available token types.
     *
     * @return a comma-separated string with the proper-cased values of all
     *         available token types
     */
    public static final String getProperCasedValues() {
        // Create a new StringBuilder to hold the concatenated values
        StringBuilder properCasedValues = new StringBuilder();

        // Get all values of the TokenType enum
        TokenType[] values = TokenType.values();

        // Loop through the values array and append the token value to the StringBuilder
        // If it's not the last value, add a comma and a space
        for (int i = 0; i < values.length; i++) {
            properCasedValues.append(values[i].getValue());
            if (i < values.length - 1) {
                properCasedValues.append(", ");
            }
        }

        // Return the concatenated string representation of all the TokenType enum
        // values
        return properCasedValues.toString();
    }

    /**
     * Deserializer class to convert a string representation of TokenType to the
     * corresponding enum value. (For BodyParams - Not Used)
     */
    public static class CaseInsensitiveTokenTypeDeserializer extends JsonDeserializer<TokenType> {

        /**
         * A custom deserializer for TokenType that converts the input string value to
         * the corresponding TokenType enum value
         * in a case-insensitive manner. If the input value is not a valid TokenType, an
         * InvalidFormatException is thrown.
         */
        @Override
        public TokenType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String value = p.getText();
            if (value != null) {
                value = value.toUpperCase();
                try {
                    return TokenType.valueOf(value);
                } catch (IllegalArgumentException e) {
                    throw new InvalidFormatException(p, "Invalid token type", value, TokenType.class);
                }
            }
            return null;
        }
    }

    /**
     * A custom serializer for TokenType that serializes the TokenType enum value to
     * its corresponding string value.
     */
    public static class TokenTypeSerializer extends JsonSerializer<TokenType> {
        @Override
        public void serialize(TokenType value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.getValue());
        }
    }
}
