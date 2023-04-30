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
 * This class represents the configuration for the WorkflowService. It 
 * provides a Spring configuration object that loads the settings.json file, 
 * which contains the configuration for the WorkflowService.
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class WorkflowConfig {

    /**
     * The file path to the settings.json file, loaded from the
     * safenet.workflow.config.path property in the Spring
     * application context. This field is used to configure the
     * WorkflowService with settings such as connection details and
     * authentication credentials. The path can be specified as a
     * system property, environment variable, or application
     * property, and is injected into this field using the @Value
     * annotation.
     */
    @Value("${safenet.workflow.config.path}")
    private String configPath;

    /**
     * This is the logger instance for the WorkflowService class. The logger is
     * initialized with the name of the class
     * for which it is used.
     */
    private static final Logger Log = Logger.getLogger(WorkflowConfig.class.getName());

    /**
     * This bean method loads the settings.json file and returns the Settings
     * object. It uses the ObjectMapper to deserialize the JSON file into the
     * Settings class.
     * If the SAFENET_WORKFLOW_CONFIG_PATH environment variable or system
     * property is set, the function loads the file from the specified path.
     * Otherwise, it loads the file called 'settings.json' from the current
     * working directory of the Java process.
     *
     * @return the Settings object containing the configuration for the
     *         WorkflowService.
     */
    @Bean
    public Settings loadSettings() throws SettingsLoadingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Settings settings;

        try {
            Log.log(Level.INFO, "Configuration file: {0}",
                    configPath);
            File settingsFile = new File(configPath);
            settings = objectMapper.readValue(settingsFile, Settings.class);
        } catch (IOException e) {
            String message = "Error loading settings.json: " + e.getMessage();
            Log.log(Level.SEVERE, message, e);
            throw new SettingsLoadingException(message, e);
        }
        return settings;
    }

    /**
     * An exception that is thrown when there is an error loading the settings file
     * in the WorkflowConfig class.
     */

    public class SettingsLoadingException extends RuntimeException {

        /**
         * Constructs a new SettingsLoadingException with the specified detail message.
         * 
         * @param message the detail message
         */
        public SettingsLoadingException(String message) {
            super(message);
        }

        /**
         * Constructs a new SettingsLoadingException with the specified detail message
         * and cause.
         * 
         * @param message the detail message
         * @param cause   the cause of the exception
         */
        public SettingsLoadingException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}