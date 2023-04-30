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
 * The TokenSchema class represents the schema for a token object returned 
 * by the API.
 * It contains fields for the different attributes of a token such as its
 * serial number, type, state, state ID, state last set date, user ID, 
 * organization name, container name, and other related information.
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.Hidden;

public class TokenSchema {
    /**
     * The serial number of the token.
     */
    @JsonProperty("serial_number")
    private String serialNumber;

    /**
     * The type of the token.
     */
    @JsonProperty("token_type")
    private String type;

    /**
     * The state of the token.
     */
    @JsonProperty("state")
    private String state;

    /**
     * The state ID of the token.
     */
    @JsonProperty("state_id")
    private Integer stateInt;

    /**
     * The number of tokens allowed.
     */
    @Hidden
    @JsonIgnore
    private Integer tokenAllowed;

    /**
     * The state last set date of the token.
     */
    @JsonProperty("state_last_set_date")
    private String stateSetDate;

    /**
     * The user ID associated with the token.
     */
    @JsonProperty("username")
    private String userId;

    /**
     * The organization name associated with the token.
     */
    @JsonProperty("organization")
    private String orgName;

    /**
     * The container name associated with the token.
     */
    @JsonProperty("container_name")
    private String container;

    /**
     * Indicates whether the hardware of the token has been initialized or not.
     */
    @JsonProperty("hardware_initialized")
    private Boolean hardwareInit;

    /**
     * Indicates whether the token can be assigned or not.
     */
    @JsonProperty("can_be_assigned")
    private Boolean assignable;

    /**
     * The ICE of the token.
     */
    @Hidden
    @JsonIgnore
    private String ice;

    /**
     * The rented value of the token.
     */
    @Hidden
    @JsonIgnore
    private String rented;

    /**
     * Returns the serial number of the token.
     * 
     * @return The serial number of the token.
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * Sets the serial number of the token.
     * 
     * @param serialNumber The serial number of the token to be set.
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * Returns the state of the token.
     * 
     * @return The state of the token.
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the state of the token.
     * 
     * @param state The state of the token to be set.
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Returns the state last set date of the token.
     * 
     * @return The state last set date of the token.
     */
    public String getStateSetDate() {
        return stateSetDate;
    }

    /**
     * Sets the state last set date of the token.
     * 
     * @param stateSetDate The state last set date of the token to be set.
     */
    public void setStateSetDate(String stateSetDate) {
        this.stateSetDate = stateSetDate;
    }

    /**
     * Gets the user ID associated with the token.
     *
     * @return The user ID associated with the token.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user ID associated with the token.
     *
     * @param userId The user ID associated with the token to set.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets the name of the organization associated with the token.
     *
     * @return The name of the organization associated with the token.
     */
    public String getOrgName() {
        return orgName;
    }

    /**
     * Sets the name of the organization associated with the token.
     *
     * @param orgName The name of the organization associated with the token to set.
     */
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     * Gets the token type.
     *
     * @return The token type.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the token type.
     *
     * @param type The token type to set.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the container name associated with the token.
     *
     * @return The container name associated with the token.
     */
    public String getContainer() {
        return container;
    }

    /**
     * Sets the container name associated with the token.
     *
     * @param container The container name associated with the token to set.
     */
    public void setContainer(String container) {
        this.container = container;
    }

    /**
     * Gets the rented property of the token.
     *
     * @return The rented property of the token.
     */
    public String getRented() {
        return rented;
    }

    /**
     * Sets the rented property of the token.
     *
     * @param rented The rented property of the token to set.
     */
    public void setRented(String rented) {
        this.rented = rented;
    }

    /**
     * Gets the hardware initialization status of the token.
     *
     * @return The hardware initialization status of the token.
     */
    public Boolean getHardwareInit() {
        return hardwareInit;
    }

    /**
     * Sets the value of the hardware initialization flag.
     *
     * @param hardwareInit the new value of the hardware initialization flag.
     */
    public void setHardwareInit(Boolean hardwareInit) {
        this.hardwareInit = hardwareInit;
    }

    /**
     * Returns the value of the assignable flag.
     *
     * @return the value of the assignable flag.
     */
    public Boolean getAssignable() {
        return assignable;
    }

    /**
     * Sets the value of the assignable flag.
     *
     * @param assignable the new value of the assignable flag.
     */
    public void setAssignable(Boolean assignable) {
        this.assignable = assignable;
    }

    /**
     * Returns the value of the ICE property.
     *
     * @return the value of the ICE property.
     */
    public String getIce() {
        return ice;
    }

    /**
     * Sets the value of the ICE property.
     *
     * @param ice the new value of the ICE property.
     */
    public void setIce(String ice) {
        this.ice = ice;
    }

    /**
     * Returns the integer value of the state property.
     *
     * @return the integer value of the state property.
     */
    public Integer getStateInt() {
        return stateInt;
    }

    /**
     * Sets the integer value of the state property.
     *
     * @param stateInt the new integer value of the state property.
     */
    public void setStateInt(Integer stateInt) {
        this.stateInt = stateInt;
    }

    /**
     * Returns the value of the token allowed property.
     *
     * @return the value of the token allowed property.
     */
    public Integer getTokenAllowed() {
        return tokenAllowed;
    }

    /**
     * Sets the value of the token allowed property.
     *
     * @param tokenAllowed the new value of the token allowed property.
     */
    public void setTokenAllowed(Integer tokenAllowed) {
        this.tokenAllowed = tokenAllowed;
    }

}
