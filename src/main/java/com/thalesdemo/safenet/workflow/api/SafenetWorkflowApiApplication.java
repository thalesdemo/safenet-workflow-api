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
 *
 * The main class of the SafeNet Workflow API application. This class is 
 * responsible for starting up the application. The application is a Spring Boot
 * application that excludes the default security auto-configuration and user 
 * details service auto-configuration to allow for custom security 
 * configurations to be used.
 * Additionally, this class enables Axis2 logging by setting system properties
 * for the log implementation, showing datetime,and the log level for the Apache 
 * Axis2 package.
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {
		SecurityAutoConfiguration.class,
		UserDetailsServiceAutoConfiguration.class
})
public class SafenetWorkflowApiApplication {

	public static void main(String[] args) {

		// Set system properties to configure the logging level for Apache Axis2
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.axis2", "debug");

		// Start the Spring Boot application
		SpringApplication.run(SafenetWorkflowApiApplication.class, args);
	}

}
