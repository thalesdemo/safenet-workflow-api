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
 * The {@code TokenSchemaParser} class is a utility class that is used to
 * parse XML strings representing token schema data into {@code TokenSchema} 
 * objects. The XML data is expected to have a format that is specified by
 * SafeNet.
 * 
 * The class uses the W3C DOM API to parse the XML data, and then extracts
 * the relevant data fields from the DOM tree.
 * 
 * The extracted data is used to create a new {@code TokenSchema} object, 
 * which is then returned to the calling method.
 * 
 * This class provides the following public method: 
 * {@code parse(String xml)}: Parses the specified XML string and returns
 * a new {@code TokenSchema} object that contains the relevant data fields
 * extracted from the XML.
 * 
 * This class should not be instantiated, as it is a utility class that 
 * only provides static methods. Therefore, its constructor has been marked 
 * as private to prevent instantiation.
 * 
 * @author Cina Shaykhian
 * @contact hello@onewelco.me
 */
package com.thalesdemo.safenet.workflow.api;

import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.StringReader;

public class TokenSchemaParser {

    private TokenSchemaParser() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Parses an XML representation of a token schema and returns a TokenSchema
     * object.
     *
     * @param xml the XML representation of the token schema
     * @return a TokenSchema object parsed from the XML
     */
    public static TokenSchema parse(String xml) {
        try {
            // create a DocumentBuilderFactory instance
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // create a DocumentBuilder instance from the factory
            DocumentBuilder builder = factory.newDocumentBuilder();
            // parse the given XML string using a StringReader wrapped in an InputSource
            // object
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            // get a list of elements with the tag "Named_Tokens_Table" from the document
            NodeList nodeList = document.getElementsByTagName("Named_Tokens_Table");
            // create a new TokenSchema object
            TokenSchema token = new TokenSchema();
            // get the first element in the NodeList
            Element element = (Element) nodeList.item(0);

            // set the values of the TokenSchema object's properties based on the values of
            // the corresponding elements in the XML document
            token.setSerialNumber(getValue("serialnumber", element));
            token.setState(getValue("state", element));
            token.setStateSetDate(getDateValue("stateSetDate", element));
            token.setUserId(getValue("userid", element));
            token.setOrgName(getValue("orgName", element));
            token.setType(getValue("type", element));
            token.setContainer(getValue("container", element));
            token.setRented(getValue("rented", element));
            token.setHardwareInit(getBooleanValue("hardwareInit", element));
            token.setAssignable(getBooleanValue("assignable", element));
            token.setIce(getValue("ice", element));
            token.setStateInt(getIntValue("stateInt", element));
            token.setTokenAllowed(getIntValue("tokenAllowed", element));

            // return the TokenSchema object
            return token;
        } catch (Exception e) {
            // if an exception is thrown, print the stack trace and return null
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves the value of a tag in an XML element.
     *
     * @param tag     the name of the tag to retrieve
     * @param element the XML element containing the tag
     * @return the value of the tag in the element
     */
    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node == null ? "" : node.getNodeValue();
    }

    /**
     * Retrieves the boolean value of a tag in an XML element.
     *
     * @param tag     the name of the tag to retrieve
     * @param element the XML element containing the tag
     * @return the boolean value of the tag in the element
     */
    private static boolean getBooleanValue(String tag, Element element) {
        return Boolean.parseBoolean(getValue(tag, element));
    }

    /**
     * Retrieves the integer value of a tag in an XML element.
     *
     * @param tag     the name of the tag to retrieve
     * @param element the XML element containing the tag
     * @return the integer value of the tag in the element
     */
    private static int getIntValue(String tag, Element element) {
        return Integer.parseInt(getValue(tag, element));
    }

    /**
     * Retrieves the date value of a tag in an XML element.
     *
     * @param tag     the name of the tag to retrieve
     * @param element the XML element containing the tag
     * @return the date value of the tag in the element
     * @throws ParseException if the date value cannot be parsed
     */
    private static String getDateValue(String tag, Element element) throws ParseException {
        String dateString = getValue(tag, element);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        return format.parse(dateString).toString();
    }
}
