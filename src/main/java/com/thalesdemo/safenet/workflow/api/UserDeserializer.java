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
 * A utility class for working with user objects.
 * 
 * The UserDeserializer class is responsible for deserializing a JSON input 
 * into a User object.
 * It extends the JsonDeserializer class and implements the deserialize method
 * to perform the deserialization process.
 * It reads the JSON input into a UserSchema object, and then creates a new
 * instance of the User class and sets its properties
 * using the values from the schema. If there are any custom attributes in
 * the schema, it sets them in an ArrayOfString object
 * and adds them to the User object. Finally, it returns the User object.
 *
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import java.util.Collections;
import java.util.List;

import com.cryptocard.www.blackshield.ArrayOfString;
import com.cryptocard.www.blackshield.User;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.io.IOException;

public class UserDeserializer extends JsonDeserializer<User> {

    /**
     * Deserialize the JSON input and convert it into a User object. This method
     * first reads the JSON input into a UserSchema object, and then creates a new
     * User object and sets its properties using the values from the schema. If the
     * schema contains any custom attributes, they are added to the User object's
     * customAttributes property as an ArrayOfString object.
     * 
     * @param jsonParser             the JSON parser used to read the input data
     * @param deserializationContext the deserialization context
     * @return a User object containing the deserialized data
     * @throws IOException if an error occurs while reading or parsing the input
     *                     data
     */
    @Override
    public User deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        // Read the JSON input into a UserSchema object
        UserSchema schema = jsonParser.readValueAs(UserSchema.class);

        // Create a new instance of the User class
        User user = User.Factory.newInstance();

        // Set the properties of the User object using the schema
        user.setUserName(schema.getUserName());
        user.setFirstName(schema.getFirstName());
        user.setLastname(schema.getLastName());
        user.setAddress1(schema.getAddress());
        user.setCity(schema.getCity());
        user.setState(schema.getState());
        user.setCountry(schema.getCountry());
        user.setZip(schema.getZip());
        user.setEmail(schema.getEmail());
        user.setTelephone(schema.getTelephone());
        user.setExtension(schema.getExtension());
        user.setMobile(schema.getMobile());
        user.setContainerName(schema.getContainerName());

        // Add any custom attributes to the User object
        List<String> customAttributes = schema.getCustomAttributes();
        if (customAttributes != null && !customAttributes.isEmpty()) {

            if (customAttributes.size() < 3) {
                customAttributes.addAll(Collections.nCopies(3 - customAttributes.size(), ""));
            }

            ArrayOfString arrayOfString = ArrayOfString.Factory.newInstance();
            arrayOfString.setStringArray(customAttributes.toArray(new String[0]));

            user.setCustomAttributes(arrayOfString);
        }

        // Return the User object
        return user;
    }
}
