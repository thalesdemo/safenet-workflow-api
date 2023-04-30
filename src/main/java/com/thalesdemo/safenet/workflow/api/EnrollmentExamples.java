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
 * The {@code EnrollmentExamples} class provides constants representing sample
 * enrollment requests for use in testing or as examples in documentation. 
 * The class has a single field, "REQUEST", which is a JSON string representing 
 * a sample enrollment request.
 *
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

public class EnrollmentExamples {

    /**
     * Private constructor to prevent instantiation of this utility class.
     * Throws an {@code IllegalStateException} if called, since the class is not
     * intended to be instantiated.
     */
    private EnrollmentExamples() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * A JSON string representing a sample enrollment request. The string contains
     * a JSON object with two fields, "response_challenge" and "state", both of
     * which are empty strings.
     */
    public static final String REQUEST = "{"
            + "\"response_challenge\": \"\","
            + "\"state\": \"\""
            + "}";

}
