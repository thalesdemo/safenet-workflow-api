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
 * A utility class containing methods for working with user-related data,
 * such as the username generation based on the realm identifier.
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

public class UserUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private UserUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Returns a unique username by combining the given realm ID and username with
     * the specified delimiter.
     * If the realm ID is "default" (case-insensitive), only the username will be
     * returned without any additional concatenation.
     * 
     * @param realmId   the realm ID to use when forming the unique username
     * @param delimiter the delimiter to use when combining the realm ID and
     *                  username
     * @param username  the username to use when forming the unique username
     * @return a unique username formed by combining the realm ID and username with
     *         the specified delimiter, or only the
     *         username if the realm ID is "default" (case-insensitive).
     */
    public static String getUniqueUsername(String realmId, String delimiter, String username) {
        if ("default".equalsIgnoreCase(realmId)) {
            return username;
        }
        return realmId + delimiter + username;
    }
}