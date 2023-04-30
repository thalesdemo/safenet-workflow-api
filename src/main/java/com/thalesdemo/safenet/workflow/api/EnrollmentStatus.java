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
 * An enumeration of enrollment statuses. The values of this enumeration are 
 * "complete", "challenge", and "error".
 * This enumeration is used to represent the current status of a token 
 * enrollment process.
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = EnrollmentStatus.EnrollmentStatusSerializer.class)
public enum EnrollmentStatus {
    /**
     * The enrollment status for a successfully completed token enrollment.
     */
    COMPLETE("complete"),

    /**
     * The enrollment status when the enrollment process requires additional user
     * input or verification.
     */
    CHALLENGE("challenge"),

    /**
     * The enrollment status when an error occurs during the enrollment process.
     */
    ERROR("error");

    /**
     * The string value of the enrollment status.
     */
    private String value;

    /**
     * Constructor for EnrollmentStatus.
     *
     * @param value the string value of the enrollment status.
     */
    private EnrollmentStatus(String value) {
        this.value = value;
    }

    /**
     * Getter method for the value of the enrollment status.
     *
     * @return the string value of the enrollment status.
     */
    public String getValue() {
        return value;
    }

    /**
     * 
     * A serializer for EnrollmentStatus enumeration values. This class is used by
     * Jackson to convert enumeration values
     * to their corresponding JSON representation.
     */
    public static class EnrollmentStatusSerializer extends JsonSerializer<EnrollmentStatus> {
        @Override
        public void serialize(EnrollmentStatus value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            gen.writeString(value.getValue());
        }
    }
}
