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
 * A utility class for working with URLs. 
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import org.springframework.web.util.UriComponentsBuilder;

public class UrlUtils {

    /*
     * Private constructor to prevent instantiation.
     */
    private UrlUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Extracts the value of the "code" parameter from a URL.
     * 
     * @param url the URL to extract the code parameter from
     * @return the value of the "code" parameter or null if the URL is null or does
     *         not contain a "code" parameter
     */
    public static String extractCodeParamValue(String url) {

        if (url == null)
            return null;

        return UriComponentsBuilder.fromUriString(url).build().getQueryParams().getFirst("code");
    }
}
