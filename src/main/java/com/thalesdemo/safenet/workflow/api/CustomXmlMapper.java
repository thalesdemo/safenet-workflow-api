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
 * This utility class creates a customized instance of the {@code XmlMapper} 
 * class with a {@code JavaTimeModule}.
 * 
 * The {@code JavaTimeModule} is responsible for serializing and deserializing 
 * Java 8 date/time objects such as {@code LocalDateTime} using ISO-8601 format. 
 *  
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class CustomXmlMapper {

    /**
     * Private constructor to prevent instantiation of the class.
     */
    private CustomXmlMapper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Creates a customized instance of the {@code XmlMapper} class with a
     * {@code JavaTimeModule} that serializes and
     * deserializes Java 8 date/time objects using ISO-8601 format.
     *
     * @return A customized instance of the {@code XmlMapper} class with a
     *         {@code JavaTimeModule}.
     */
    public static XmlMapper create() {
        // Create a new instance of the XmlMapper class
        XmlMapper xmlMapper = new XmlMapper();

        // Create a new JavaTimeModule and add serializers and deserializers for
        // LocalDateTime objects
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        module.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        // Register the JavaTimeModule with the XmlMapper object
        xmlMapper.registerModule(module);

        // Return the customized XmlMapper object
        return xmlMapper;
    }
}
