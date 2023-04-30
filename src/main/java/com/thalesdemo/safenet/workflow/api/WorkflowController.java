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
 * The WorkflowController class is a Spring REST controller that handles 
 * HTTP requests related to the WorkflowService. It exposes an endpoint to
 * verify the connection status of the remote SafeNet authentication server.
 *
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("${api.basePath}")
@Tag(name = "Workflow")
public class WorkflowController {

    /**
     * The WorkflowService instance is auto-wired to the controller.
     */
    @Autowired
    WorkflowService workflow;

    /**
     * Returns the connection status of the remote SafeNet authentication server.
     * 
     * @return true if the connection is active and healthy, false otherwise.
     */
    @Operation(summary = "Ping connection status of the remote SafeNet authentication server", description = "Verify if the API session against the SafeNet server is active and healthy.")
    @GetMapping("/ping")
    public boolean getConnectDetails() {

        return workflow.pingConnection();
    }
}