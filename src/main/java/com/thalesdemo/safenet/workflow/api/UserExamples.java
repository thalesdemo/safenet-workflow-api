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
 * Utility class for providing examples of user accounts for documentation 
 * purposes.
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

public class UserExamples {

    /**
     * Private constructor to prevent instantiation of the utility class.
     * Throws an IllegalStateException if called.
     */
    private UserExamples() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Example JSON payload for creating a user account.
     * Contains default values for all user fields.
     */
    public static final String CREATE = "{\n  \"firstname\": \"\",\n  \"lastname\": \"\",\n  \"email\": \"\",\n  \"mobile\": \"\",\n  \"telephone\": \"\",\n  \"extension\": \"\",\n  \"address\": \"\",\n  \"city\": \"\",\n  \"state\": \"\",\n  \"country\": \"\",\n  \"custom_attributes\": [\n    \"\",\n    \"\",\n    \"\"\n  ],\n  \"container_name\": \"Default\"\n}";
}
