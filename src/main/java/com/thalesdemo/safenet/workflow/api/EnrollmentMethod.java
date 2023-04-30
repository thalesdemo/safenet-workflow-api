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
 * The {@code EnrollmentMethod} enum represents the possible methods for
 * enrolling a token, including email, URL, and API. The enum uses custom 
 * serializers and converters to ensure that the enum values are correctly
 * serialized and deserialized to and from JSON.
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
@JsonSerialize(using = EnrollmentMethod.EnrollmentMethodSerializer.class)
public enum EnrollmentMethod {
    EMAIL,
    URL,
    API;

    /**
     * Returns the {@code EnrollmentMethod} enum value corresponding to the given
     * string.
     * 
     * @param value The string to convert to an {@code EnrollmentMethod} enum value.
     * @return The corresponding {@code EnrollmentMethod} enum value.
     */
    @JsonCreator
    public static EnrollmentMethod fromString(String value) {
        return valueOf(value.toUpperCase());
    }

    /**
     * The {@code EnrollmentMethodSerializer} class is a custom serializer for the
     * {@code EnrollmentMethod} enum, used to ensure that the enum values are
     * serialized to lowercase strings in JSON.
     */
    public static class EnrollmentMethodSerializer extends JsonSerializer<EnrollmentMethod> {
        @Override
        public void serialize(EnrollmentMethod value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            // Write the lowercase string representation of the EnrollmentMethod enum value
            // to the JSON generator.
            gen.writeString(value.toString().toLowerCase());
        }
    }

    /**
     * The {@code EnrollmentMethodConverter} class is a custom converter for the
     * {@code EnrollmentMethod} enum, used to convert strings to
     * {@code EnrollmentMethod} enum values. The converter ensures that string
     * values are correctly converted to their corresponding enum values, and throws
     * an exception if an invalid value is provided.
     */
    public static class EnrollmentMethodConverter implements Converter<String, EnrollmentMethod> {

        @Override
        public EnrollmentMethod convert(String source) {
            if (source != null) {
                // Convert the source string to uppercase, to ensure that it matches the case of
                // the enum values.
                String value = source.toUpperCase();
                try {
                    // Attempt to convert the string to an EnrollmentMethod enum value.
                    return EnrollmentMethod.valueOf(value);
                } catch (IllegalArgumentException e) {
                    // If the conversion fails, throw an exception indicating that the provided
                    // string is not a valid EnrollmentMethod value.
                    throw new IllegalArgumentException("Invalid enrollment method: " + source);
                }
            }
            // If the provided string is null, return null.
            return null;
        }
    }
}
