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
 * Represents a group schema in the BlackShield ID provisioning system.
 * A group schema defines a group of users and their associated attributes. 
 * The schema contains information such as the group's name and a description.
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Group", namespace = "http://www.cryptocard.com/blackshield/")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("GroupSchema")
public class GroupSchema {

    /**
     * The group name.
     */
    @JacksonXmlProperty(localName = "GroupName")
    @JsonProperty("name")
    private String groupName;

    /**
     * The group description.
     */
    @JacksonXmlProperty(localName = "Description")
    @JsonProperty("description")
    private String description;

    /**
     * Returns the group name.
     * 
     * @return the group name
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Sets the group name.
     * 
     * @param groupName the group name to set
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Returns the group description.
     * 
     * @return the group description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the group description.
     * 
     * @param description the group description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}