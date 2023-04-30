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
 * Configuration class for the SpringDoc OpenAPI library to generate 
 * documentation for the RESTful API. The OpenAPI specification is used to
 * describe the endpoints, operations, and models used in the API. 
 *
 * This class configures and returns an instance of the OpenAPI class 
 * for the Spring application.
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class SwaggerConfig {

  /**
   * 
   * Configures and returns the OpenAPI instance for the Spring application using
   * the SpringDoc OpenAPI library. This method creates and configures the
   * necessary components of the OpenAPI specification, such as security
   * schemes, tags, and the application information. The method receives a
   * {@link BuildProperties} object to inject the build properties, such as
   * project name, version, and description, for the application.
   * 
   * @param buildProperties the build properties of the Spring application
   * @return the OpenAPI instance for the Spring application
   */
  @Bean
  OpenAPI springOpenApiConfig(BuildProperties buildProperties) {
    // Configure security scheme to include the API key in the request headers
    SecurityScheme apiKeyScheme = new SecurityScheme()
        .type(SecurityScheme.Type.APIKEY)
        .description(
            "##### To access the endpoints listed on this page, the client key **must always be included** in the request headers using the `X-API-Key` field.")
        .in(SecurityScheme.In.HEADER)
        .name("X-API-Key");

    // Add the security scheme to the OpenAPI components
    Components components = new Components();
    components.addSecuritySchemes("X-API-Key", apiKeyScheme);

    // Configure the security requirements to include the API key
    SecurityRequirement apiKeyRequirement = new SecurityRequirement().addList("X-API-Key");

    // Configure the tags to group the APIs by their purpose
    Tag workflowTag = new Tag().name("Workflow").description("APIs for workflows");
    Tag userTag = new Tag().name("User").description("APIs for users");
    Tag tokenTag = new Tag().name("Token").description("API for tokens");

    // Add the tags to the OpenAPI specification
    List<Tag> tags = Arrays.asList(workflowTag, userTag, tokenTag);

    // Configure the server information
    Server server = serverInfo();

    // Create and configure the OpenAPI specification
    return new OpenAPI()
        .components(components)
        .security(Arrays.asList(apiKeyRequirement))
        .tags(tags)
        .addServersItem(server)
        .info(new Info().title(buildProperties.getName())
            .description(buildProperties.get("project.description"))
            .version(buildProperties.getVersion())
            .contact(new Contact().email(buildProperties.get("contact.email")))
            .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0.html")))
        .externalDocs(new ExternalDocumentation()
            .description(buildProperties.get("contact.git.name"))
            .url(buildProperties.get("contact.git.url")));
  }

  /**
   * Creates a new instance of the Server class with an empty URL and returns it.
   * This is used to provide server information
   * to the OpenAPI documentation generated by the SpringDoc library.
   * 
   * @return an instance of the Server class with an empty URL
   */
  private Server serverInfo() {
    return new Server().url("");
  }
}
