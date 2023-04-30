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
 * This class configures the web security for the application.
 * 
 * It extends the WebSecurityConfigurerAdapter and overrides the 
 * configure(HttpSecurity http) method to specify the authentication and 
 * authorization rules.
 * 
 * It disables CSRF protection for API requests and sets up API 
 * authentication using an API key.
 * 
 * It also disables all other forms of authentication such as basic
 * authentication, form-based authentication, and logout.
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * An instance of {@link ApiKeyAuthService} used for API key authentication.
     */
    private final ApiKeyAuthService apiKeyAuthService;

    /**
     * Constructs a new {@code WebSecurityConfig} instance with the specified
     * {@code ApiKeyAuthService} instance to be used for API key authentication.
     * 
     * @param apiKeyAuthService the {@code ApiKeyAuthService} instance to be used
     *                          for API key authentication
     */
    public WebSecurityConfig(ApiKeyAuthService apiKeyAuthService) {
        this.apiKeyAuthService = apiKeyAuthService;
    }

    /**
     * This method is used to configure the HTTP security of the application using
     * the HttpSecurity object provided by Spring Security. It disables CSRF
     * protection for API requests, configures API authentication, and disables all
     * other forms of authentication.
     * 
     * Note: this implementation is deprecated and should be migrated -
     * https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
     * 
     * @param http an HttpSecurity object that is used to configure HTTP security
     *             for the application
     * @throws Exception if an error occurs while configuring the HttpSecurity
     *                   object
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Disable CSRF protection for API requests
        http.csrf().ignoringAntMatchers("/api/**");

        // Configure API authentication
        http.authorizeRequests()
                .antMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(new ApiKeyAuthenticationFilter(apiKeyAuthService),
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

        // Disable all other forms of authentication
        http.httpBasic().disable()
                .formLogin().disable()
                .logout().disable();
    }
}