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
 * This class represents an authentication filter that checks for the presence 
 * and validity of an API key in incoming requests to the API. If a valid API 
 * key is found, the authentication is set in the Security Context and the 
 * request is passed on to the next filter in the chain. If no API key is found 
 * or an invalid API key is provided, an HTTP error response is sent back to the 
 * client.
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * The {@code ApiKeyAuthenticationFilter} class is annotated with
 * {@code @Order(Ordered.HIGHEST_PRECEDENCE)} to ensure that it is executed
 * before all other filters in the filter chain. This is necessary because API
 * key authentication must be performed before any other authentication
 * mechanisms.
 * The class extends {@code OncePerRequestFilter}, which ensures that the filter
 * is only executed once per request, even if the request matches multiple
 * filter patterns.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    /*
     * The header that contains the API key.
     */
    private static final String API_KEY_HEADER = "X-API-Key";

    /**
     * 
     * The {@code ApiKeyAuthService} object that is responsible for validating API
     * keys and providing authentication
     * objects for authenticated requests.
     */
    private final ApiKeyAuthService apiKeyAuthService;

    /**
     * Constructor for the ApiKeyAuthenticationFilter class.
     *
     * @param apiKeyAuthService The service responsible for validating API keys.
     */
    public ApiKeyAuthenticationFilter(ApiKeyAuthService apiKeyAuthService) {
        this.apiKeyAuthService = apiKeyAuthService;
    }

    /**
     * Method that checks for the presence and validity of an API key in incoming
     * requests to the API. If a valid
     * API key is found, the authentication is set in the Security Context and the
     * request is passed on to the
     * next filter in the chain. If no API key is found or an invalid API key is
     * provided, an HTTP error response is
     * sent back to the client.
     *
     * @param request     The HTTP request to be processed.
     * @param response    The HTTP response to be sent.
     * @param filterChain The filter chain to be executed.
     * @throws ServletException if the request cannot be handled.
     * @throws IOException      if an input or output error occurs while the request
     *                          is being handled.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Get the request URI
        String requestUri = request.getRequestURI();

        // Create a path matcher to check if the request URI matches the /api/** or
        // api/** pattern
        AntPathMatcher pathMatcher = new AntPathMatcher();

        if (pathMatcher.match("/api/**", requestUri) || pathMatcher.match("api/**", requestUri)) {

            // Get the API key from the header of the HTTP request
            String apiKey = request.getHeader(API_KEY_HEADER);

            // If no API key is found, send an HTTP error response and return
            if (apiKey == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                        "No API key found in header " + API_KEY_HEADER + " of the HTTP request!");
                return;
            }

            // Validate the API key and get the authentication object
            Authentication authentication = apiKeyAuthService.getAuthentication(apiKey);

            // If the API key is not valid, send an HTTP error response and return
            if (authentication == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API key!");
                return;
            }
            // Set the authentication object in the Security Context
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Pass the request on to the next filter in the chain
        filterChain.doFilter(request, response);
    }
}
