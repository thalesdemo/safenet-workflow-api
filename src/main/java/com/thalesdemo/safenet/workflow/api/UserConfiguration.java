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
 * Configuration class for the User class serialization/deserialization.
 * It defines a bean for the ObjectMapper and registers the custom serializer
 * and deserializer for the User class.
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.cryptocard.www.blackshield.User;

@Configuration
public class UserConfiguration {

    /**
     * Creates a bean for the ObjectMapper and registers the custom serializer and
     * deserializer for the User class.
     * 
     * @return the configured ObjectMapper instance
     */
    @Bean
    public ObjectMapper objectMapper() {
        // Creates a new instance of the ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        // Registers the custom serializer and deserializer for the User class
        SimpleModule module = new SimpleModule();
        module.addSerializer(User.class, new UserSerializer());
        module.addDeserializer(User.class, new UserDeserializer());
        objectMapper.registerModule(module);
        // Returns the configured ObjectMapper instance
        return objectMapper;
    }
}
