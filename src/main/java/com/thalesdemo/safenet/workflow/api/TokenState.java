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
 * Enum class representing the possible states of a token.
 * 
 * The TokenState class is an enumeration that represents the possible states
 * of a token. These states include ACTIVE, LOCKED, and SUSPENDED. It includes
 * a method to convert a string value to the corresponding TokenState enum
 * value, a converter class to convert a string representation of TokenState
 * to the corresponding enum value, and a serializer class to serialize a
 * TokenState enum value to its lowercase string representation. These methods
 * are used to handle the serialization and deserialization of TokenState enum
 * values to and from JSON format.
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;

import org.springframework.core.convert.converter.Converter;

@JsonFormat(shape = JsonFormat.Shape.STRING)
@JsonSerialize(using = TokenState.TokenStateSerializer.class)
public enum TokenState {
    /**
     * The token is active and can be used.
     */
    ACTIVE,
    /**
     * The token is locked and cannot be used until it is unlocked.
     */
    LOCKED,
    /**
     * The token is suspended and cannot be used until it is unsuspended.
     */
    SUSPENDED;

    /**
     * Converts a string value to the corresponding TokenState enum value.
     * If the input value is invalid or null, null is returned.
     * 
     * @param value the string representation of the TokenState enum value to
     *              convert
     * @return the corresponding TokenState enum value or null if the input is
     *         invalid or null
     */
    @JsonCreator
    public static TokenState fromString(String value) {
        // Try to get the corresponding enum value for the given string
        try {
            return TokenState.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            // If the string is not a valid enum value, handle the exception and return null
            return null;
        }
    }

    /**
     * Converter class to convert a string representation of TokenState to the
     * corresponding enum value.
     */
    public static class TokenStateConverter implements Converter<String, TokenState> {

        /**
         * Converts a string value to the corresponding TokenState enum value.
         * If the input value is invalid, an IllegalArgumentException is thrown.
         * 
         * @param source the string representation of the TokenState enum value to
         *               convert
         * @return the corresponding TokenState enum value
         * @throws IllegalArgumentException if the input value is invalid or null
         */
        @Override
        public TokenState convert(String source) {
            // Checks if the source string is not null
            if (source != null) {
                // Converts the source string to uppercase
                String value = source.toUpperCase();
                try {
                    // Tries to get the corresponding enum constant based on the uppercase value
                    return TokenState.valueOf(value);
                } catch (IllegalArgumentException e) {
                    // If the value is not a valid enum constant, throws an IllegalArgumentException
                    // with a descriptive message
                    throw new IllegalArgumentException("Invalid token state: " + source);
                }
            }
            // Returns null if the source string is null
            return null;
        }
    }

    /**
     * Serializer class to serialize a TokenState enum value to its lowercase string
     * representation.
     */

    public static class TokenStateSerializer extends JsonSerializer<TokenState> {

        /**
         * Serializes a TokenState enum value to its lowercase string representation.
         * 
         * @param value       the TokenState enum value to serialize
         * @param gen         the JsonGenerator used for serialization
         * @param serializers the SerializerProvider used for serialization
         * @throws IOException if an I/O error occurs during serialization
         */
        @Override
        public void serialize(TokenState value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.toString().toLowerCase());
        }
    }
}
