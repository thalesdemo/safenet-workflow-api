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
 *  Represents a user in the SafeNet authentication server.
 * 
 * This class represents a user object in the application. It contains 
 * various user-related fields such as personal information, account details,
 * and access permissions.
 * The class includes getter and setter methods for each field, as well as
 * an  empty constructor.
 * The UserSchema object is used throughout the application to represent
 * users and their attributes.
 
 * Attributes:
 * - firstName: The first name of the user.
 * - lastName: The last name of the user.
 * - userName: The username of the user.
 * - email: The email address of the user.
 * - telephone: The telephone number of the user.
 * - mobile: The mobile number of the user.
 * - address: The address of the user.
 * - city: The city of the user.
 * - state: The state of the user.
 * - zip: The zip code of the user.
 * - country: The country of the user.
 * - groups: A list of groups that the user belongs to.
 * - customAttributes: A list of custom attributes associated with the user.
 * - containerName: The name of the container in which the user resides.
 * - lastPasswordSetDate: The date when the user's password was last set.
 * - unlockAt: The date and time when the user's account will be automatically
 *   unlocked.
 * - isLocked: Indicates whether the user's account is locked.
 * - isAccountDormant: Indicates whether the user's account is dormant.
 * - isRestrictionsEnabled: Indicates whether restrictions are enabled for the
 *   user's account.
 * - isTempPasswordEnabled: Indicates whether temporary passwords are enabled
 *   for the user's account.
 * - isTempPasswordChangeRequired: Indicates whether the user is required to
 *   change their temporary password.
 * - useExternalCredentials: Indicates whether external credentials are used 
 *   for the user's account.
 * - extension: The extension associated with the user.
 * 
 * Note: Some attributes are ignored during JSON serialization/deserialization.
 * 
 * @see GroupSchema
 * @see WorkflowService
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonPropertyOrder({
        "firstname",
        "lastname",
        "username",
        "email",
        "telephone",
        "mobile",
        "address",
        "city",
        "state",
        "zip",
        "country",
        "groups",
        "custom_attributes",
        "container_name",
        // "preferred_language",
        "last_password_set_date",
        "unlock_at",
        "is_locked",
        "is_account_dormant",
        "is_restrictions_enabled",
        "is_temp_password_enabled",
        "is_temp_password_change_required",
        "use_external_credentials"
})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null values from output
public class UserSchema {

    /**
     * The address of the user.
     */
    @JsonProperty("address")
    private String address;

    /**
     * The last name of the user.
     */
    @JsonProperty("lastname")
    private String lastName;

    /**
     * The telephone number of the user.
     */
    @JsonProperty("telephone")
    private String telephone;

    /**
     * The message associated with the user.
     * 
     * @deprecated This field is no longer used.
     */
    @JsonIgnore
    private String message;

    /**
     * The end date of the user's account.
     * 
     * @deprecated This field is no longer used.
     */
    // @JsonProperty("end_date")
    @JsonIgnore
    private String endDate;

    /**
     * A boolean flag indicating if the user's account is locked.
     */
    @JsonProperty("is_locked")
    private Boolean locked;

    /**
     * The start date of a user's account.
     * 
     * @deprecated This field is no longer used.
     */
    // @JsonProperty("start_date")
    @JsonIgnore
    private String startDate;

    /**
     * The name of the state of the user.
     */
    @JsonProperty("state")
    private String state;

    /**
     * The date when the user's password was last set.
     */
    @JsonProperty("last_password_set_date")
    private String passwordSetDate;

    /**
     * The country of the user.
     */
    @JsonProperty("country")
    private String country;

    /**
     * The date and time when the user's account will be eligible for unlock.
     */
    @JsonProperty("unlock_at")
    private String unlockAt;

    /**
     * The first name of the user.
     */
    @JsonProperty("firstname")
    private String firstName;

    /**
     * The email address of the user.
     */
    @JsonProperty("email")
    private String email;

    /**
     * The zip code of the user.
     */
    @JsonProperty("zip")
    private String zip;

    /**
     * The mobile number of the user.
     */
    @JsonProperty("mobile")
    private String mobile;

    /**
     * The end time for the user.
     * 
     * @deprecated This field is no longer used.
     */
    @JsonIgnore
    private String endTime;

    /**
     * Determines whether the user account uses external credentials.
     */
    @JsonProperty("use_external_credentials")
    private Boolean useExternalCredentials;

    /**
     * The city in which the user resides.
     */
    @JsonProperty("city")
    private String city;

    /**
     * The name of the container that the user belongs to.
     */
    @JsonProperty("container_name")
    private String containerName;

    /**
     * The start time for the user.
     * 
     * @deprecated This field is no longer used.
     */
    @JsonIgnore
    private String startTime;

    /**
     * Determines whether the user is allowed to use temporary passwords.
     */
    @JsonProperty("is_temp_password_enabled")
    private Boolean tempPasswordEnabled;

    /**
     * Determines whether the user must change their temporary password before
     * they can log in.
     */
    @JsonProperty("is_temp_password_change_required")
    private Boolean tempPasswordChangeReq;

    /**
     * Determines whether the user account is dormant.
     */
    @JsonProperty("is_account_dormant")
    private Boolean accountDormant;

    /**
     * The username for the user account.
     */
    @JsonProperty("username")
    private String userName;

    /**
     * The number of times the user has attempted to log in with an incorrect
     * password.
     * 
     * @deprecated This field is no longer used.
     */
    // @JsonProperty("password_attempt_count")
    @JsonIgnore
    private Integer passwordAttemptCount;

    /**
     * A list of custom attributes associated with the user account.
     */
    @JsonProperty("custom_attributes")
    private List<String> customAttributes;

    /**
     * A list of groups that the user belongs to.
     */
    @JsonProperty("groups")
    private List<GroupSchema> groups;

    /**
     * Determines whether account restrictions are enabled for the user.
     */
    @JsonProperty(value = "is_restrictions_enabled")
    private Boolean restrictionsEnabled;

    /**
     * An optional extension for the user account.
     */
    @JsonProperty("extension")
    private String extension;

    /**
     * Empty constructor for the UserSchema class.
     */
    public UserSchema() {
        // Empty constructor
    }

    /**
     * Getter for the last name of the user.
     *
     * @return A string representing the last name of the user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter for the last name of the user.
     *
     * @param lastName A string representing the last name of the user.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Getter for the telephone number of the user.
     *
     * @return A string representing the telephone number of the user.
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Setter for the telephone number of the user.
     *
     * @param telephone A string representing the telephone number of the user.
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * Getter for the message.
     *
     * @return A string representing the message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter for the message.
     *
     * @param message A string representing the message.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Getter for the end date.
     *
     * @return A string representing the end date.
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Setter for the end date.
     *
     * @param endDate A string representing the end date.
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * Returns the locked status of the user.
     *
     * @return the locked status of the user
     */
    public Boolean getLocked() {
        return locked;
    }

    /**
     * Sets the locked status of the user.
     *
     * @param locked the locked status of the user
     */
    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    /**
     * Returns the start date for the user.
     *
     * @return the start date for the user
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date for the user.
     *
     * @param startDate the start date for the user
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * Returns the state for the user.
     *
     * @return the state for the user
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the state for the user.
     *
     * @param state the state for the user
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Returns the password set date for the user.
     *
     * @return the password set date for the user
     */
    public String getPasswordSetDate() {
        return passwordSetDate;
    }

    /**
     * Sets the password set date for the user.
     *
     * @param passwordSetDate the password set date for the user
     */
    public void setPasswordSetDate(String passwordSetDate) {
        this.passwordSetDate = passwordSetDate;
    }

    /**
     * Returns the country for the user.
     *
     * @return the country for the user
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country for the user.
     *
     * @param country the country for the user
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets the unlock time of the user account.
     *
     * @return the unlock time of the user account.
     */
    public String getUnlockAt() {
        return unlockAt;
    }

    /**
     * Sets the unlock time of the user account.
     *
     * @param unlockAt the new unlock time of the user account.
     */
    public void setUnlockAt(String unlockAt) {
        this.unlockAt = unlockAt;
    }

    /**
     * Gets the first name of the user.
     *
     * @return the first name of the user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName the new first name of the user.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the email of the user.
     *
     * @return the email of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     *
     * @param email the new email of the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the zip code of the user.
     *
     * @return the zip code of the user.
     */
    public String getZip() {
        return zip;
    }

    /**
     * Sets the zip code of the user.
     *
     * @param zip the new zip code of the user.
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * Gets the mobile phone number of the user.
     *
     * @return the mobile phone number of the user.
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Sets the mobile phone number of the user.
     *
     * @param mobile the new mobile phone number of the user.
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * Gets the end time of the user.
     *
     * @return the end time of the user.
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the user.
     *
     * @param endTime the new end time of the user.
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * Getter method for retrieving whether the user's external credentials are
     * used.
     * 
     * @return A boolean representing whether the user's external credentials are
     *         used.
     */
    public Boolean getUseExternalCredentials() {
        return useExternalCredentials;
    }

    /**
     * Setter method for setting whether the user's external credentials are used.
     * 
     * @param useExternalCredentials A boolean representing whether the user's
     *                               external credentials are used.
     */
    public void setUseExternalCredentials(Boolean useExternalCredentials) {
        this.useExternalCredentials = useExternalCredentials;
    }

    /**
     * Getter method for retrieving the city where the user is located.
     * 
     * @return A String representing the city where the user is located.
     */
    public String getCity() {
        return city;
    }

    /**
     * Setter method for setting the city where the user is located.
     * 
     * @param city A String representing the city where the user is located.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Getter method for retrieving the container name of the user.
     * 
     * @return A String representing the container name of the user.
     */
    public String getContainerName() {
        return containerName;
    }

    /**
     * Setter method for setting the container name of the user.
     * 
     * @param containerName A String representing the container name of the user.
     */
    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    /**
     * Getter method for retrieving the start time of the user.
     * 
     * @return A String representing the start time of the user.
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Setter method for setting the start time of the user.
     * 
     * @param startTime A String representing the start time of the user.
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * Getter method for retrieving whether the user has enabled temporary
     * passwords.
     * 
     * @return A boolean representing whether the user has enabled temporary
     *         passwords.
     */
    public Boolean getTempPasswordEnabled() {
        return tempPasswordEnabled;
    }

    /**
     * Setter method for setting whether the user has enabled temporary passwords.
     * 
     * @param tempPasswordEnabled A boolean representing whether the user has
     *                            enabled temporary passwords.
     */
    public void setTempPasswordEnabled(Boolean tempPasswordEnabled) {
        this.tempPasswordEnabled = tempPasswordEnabled;
    }

    /**
     * Getter method for retrieving whether the user's account is dormant.
     * 
     * @return A boolean representing whether the user's account is dormant.
     */
    public Boolean getAccountDormant() {
        return accountDormant;
    }

    /**
     * Setter method for setting whether the user's account is dormant.
     * 
     * @param accountDormant A boolean representing whether the user's account is
     *                       dormant.
     */
    public void setAccountDormant(Boolean accountDormant) {
        this.accountDormant = accountDormant;
    }

    /**
     * Getter method for retrieving the username of the user.
     * 
     * @return A String representing the username of the user.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Setter method for setting the username of the user.
     * 
     * @param userName A String representing the username of the user.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the number of password attempts.
     *
     * @return The number of password attempts.
     */
    public Integer getPasswordAttemptCount() {
        return passwordAttemptCount;
    }

    /**
     * Sets the number of password attempts.
     *
     * @param passwordAttemptCount The number of password attempts.
     */
    public void setPasswordAttemptCount(Integer passwordAttemptCount) {
        this.passwordAttemptCount = passwordAttemptCount;
    }

    /**
     * Gets the list of custom attributes.
     *
     * @return The list of custom attributes.
     */
    public List<String> getCustomAttributes() {
        return customAttributes;
    }

    /**
     * Sets the list of custom attributes.
     *
     * @param customAttributes The list of custom attributes.
     */
    public void setCustomAttributes(List<String> customAttributes) {
        this.customAttributes = customAttributes;
    }

    /**
     * Gets the list of groups that the user belongs to.
     *
     * @return The list of groups that the user belongs to.
     */
    public List<GroupSchema> getGroups() {
        return groups;
    }

    /**
     * Sets the list of groups that the user belongs to.
     *
     * @param groups The list of groups that the user belongs to.
     */
    public void setGroups(List<GroupSchema> groups) {
        this.groups = groups;
    }

    /**
     * Gets whether restrictions are enabled for the user.
     *
     * @return Whether restrictions are enabled for the user.
     */
    public Boolean isRestrictionsEnabled() {
        return restrictionsEnabled;
    }

    /**
     * Sets whether restrictions are enabled for the user.
     *
     * @param restrictionsEnabled Whether restrictions are enabled for the user.
     */
    public void setRestrictionsEnabled(Boolean restrictionsEnabled) {
        this.restrictionsEnabled = restrictionsEnabled;
    }

    /**
     * Sets the user's address.
     *
     * @param address The user's address.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the user's address.
     *
     * @return The user's address.
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Sets the user's extension.
     *
     * @param extension The user's extension.
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * Gets the user's extension.
     *
     * @return The user's extension.
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Gets whether temporary password change is required for the user.
     *
     * @return Whether temporary password change is required for the user.
     */
    public Boolean getTempPasswordChangeReq() {
        return this.tempPasswordChangeReq;
    }

    /**
     * Sets whether temporary password change is required for the user.
     *
     * @param tempPasswordChangeReq Whether temporary password change is required
     *                              for the user.
     */
    public void setTempPasswordChangeReq(Boolean tempPasswordChangeReq) {
        this.tempPasswordChangeReq = tempPasswordChangeReq;
    }
}