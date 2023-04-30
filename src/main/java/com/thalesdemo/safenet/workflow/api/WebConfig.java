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
 * The WebConfig class is a Spring configuration class that configures web
 * MVC features. It implements the WebMvcConfigurer interface, which provides
 * callback methods for configuring Spring MVC.
 *
 * This class overrides the addFormatters method to register custom converters
 * for various enum types used in the application, such as TokenType, 
 * TokenState, and EnrollmentMethod. The converters are registered with the
 * FormatterRegistry, which is responsible for converting between objects and 
 * strings.
 * 
 * In this application, the converters are used to convert the string values
 * shown in spring-openapi-ui into the corresponding objects used in the
 * application.
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Overrides the default implementation of adding formatters to the
     * FormatterRegistry by adding custom converters to it.
     * The TokenType, TokenState, and EnrollmentMethod converters are added to
     * the registry.
     * 
     * @param registry the FormatterRegistry to add the converters to
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new TokenType.TokenTypeConverter());
        registry.addConverter(new TokenState.TokenStateConverter());
        registry.addConverter(new EnrollmentMethod.EnrollmentMethodConverter());
    }
}
