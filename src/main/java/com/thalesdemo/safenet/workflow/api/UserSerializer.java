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
 * The UserSerializer class is a custom serializer for the User class.
 * It is responsible for serializing a User object into a UserSchema object,
 * which can be used to generate JSON output. 
 * 
 * The User object is a representation of a user in the BSIDCA system, and this
 * class handles the conversion of the user object to a UserSchema object.
 * 
 * This class sets the properties of the UserSchema object using the properties 
 * of the User object, and parses the custom attributes XML to set the custom
 * attributes property of the UserSchema object. The resulting UserSchema
 * object is then written to the JSON output.
 * 
 * The User object is a map of user attributes returned by the BlackShield ID
 * (BSIDCA) API, whereas the UserSchema object is a representation of the user
 * object in a JSON format.
 *
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.cryptocard.www.blackshield.ArrayOfGroup;
import com.cryptocard.www.blackshield.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.io.StringReader;
import org.xml.sax.InputSource;

// Define a custom serializer for the User class
public class UserSerializer extends JsonSerializer<User> {

    /**
     * This is the logger instance for the UserController class. The logger is
     * initialized with the name of the class for which it is used.
     */

    private static final Logger Log = Logger.getLogger(UserSerializer.class.getName());

    /**
     * Serializes a User object into a UserSchema object.
     *
     * @param user               The User object to be serialized.
     * @param jsonGen            The JSON generator used to write the JSON output.
     * @param serializerProvider The serializer provider.
     * @throws IOException             if an I/O error occurs.
     * @throws JsonProcessingException if an error occurs during JSON processing.
     */
    @Override
    public void serialize(User user, JsonGenerator jsonGen, SerializerProvider serializerProvider)
            throws IOException, JsonProcessingException {
        // Create a new instance of the schema class
        UserSchema schema = new UserSchema();

        // Set the properties of the schema using the User object
        schema.setStartDate(user.getStartDate());
        schema.setEndDate(user.getEndDate());
        schema.setStartTime(user.getStartTime());
        schema.setEndTime(user.getEndTime());
        schema.setPasswordSetDate(user.getPasswordSetDate());
        schema.setPasswordAttemptCount(user.getPasswordAttemptCount());
        schema.setUserName(user.getUserName());
        schema.setFirstName(user.getFirstName());
        schema.setLastName(user.getLastname());
        schema.setAddress(user.getAddress1());
        schema.setCity(user.getCity());
        schema.setState(user.getState());
        schema.setCountry(user.getCountry());
        schema.setZip(user.getZip());
        schema.setEmail(user.getEmail());
        schema.setTelephone(user.getTelephone());
        schema.setExtension(user.getExtension());
        schema.setMobile(user.getMobile());
        schema.setLocked(user.getLocked());
        schema.setUnlockAt(user.getUnlockAt());
        schema.setMessage(user.getMessage());
        schema.setTempPasswordEnabled(user.getTempPasswordEnabled());
        schema.setTempPasswordChangeReq(user.getTempPasswordChangeReq());
        schema.setContainerName(user.getContainerName());
        schema.setUseExternalCredentials(user.getUseExternalCredentials());
        schema.setAccountDormant(user.getIsAccountDormant());

        schema.setExtension(user.getExtension());

        // Convert the user groups to a list and set it on the schema
        ArrayOfGroup arrayOfGroup = user.getGroups();
        List<GroupSchema> groupList;
        if (arrayOfGroup != null && arrayOfGroup.getGroupArray().length > 0) {
            groupList = new ArrayList<>();
            for (com.cryptocard.www.blackshield.Group group : arrayOfGroup.getGroupArray()) {
                GroupSchema customGroup = new GroupSchema();
                customGroup.setGroupName(group.getGroupName());
                customGroup.setDescription(group.getDescription());
                groupList.add(customGroup);
            }
        } else {
            groupList = null;
        }
        schema.setGroups(groupList);

        try {
            // Parse the custom attributes XML and convert to a ArrayList
            String customAttributesXml = user.getCustomAttributes().toString();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new StringReader(customAttributesXml)));
            NodeList nodeList = doc.getElementsByTagName("blac:string");
            List<String> customAttributes = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                customAttributes.add(nodeList.item(i).getTextContent());
            }
            // Set the user's custom attributes
            schema.setCustomAttributes(customAttributes);
        } catch (Exception e) {
            Log.log(Level.WARNING, "Could not serialize custom attributes to user.", e);

        }

        // Write the schema to the JSON output
        jsonGen.writeObject(schema);
    }
}
