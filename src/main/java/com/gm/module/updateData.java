package com.gm.module;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class updateData {

    public String boolean_value(String attribute_name, String value){
        String[] bool_attributes = {"KeepTestEditsOnSwitchOver", "Enabled", "SafetyLocked", "ConfiguredSafetyIOAlways", "SignatureRunModeProtect"};
        boolean present_in_array = Arrays.asList(bool_attributes).contains(attribute_name);
        if (present_in_array){
            if (value.equals("0") || value.equals("No"))
                return "false";
            else
                return "true";
        }
        else
            return value;
    }

    public Matcher matchRegex(String regex, String data){
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        return pattern.matcher(data);
    }
    public void update_description(Document doc, String value, Element parent_element){
        Element Description = doc.createElement("Description");
        Description.appendChild(doc.createCDATASection(value));
        parent_element.appendChild(Description);
    }

    public Element create_element(String element_name, Element parent_element, Document doc){
        Element element = doc.createElement(element_name);
        parent_element.appendChild(element);

        return element;
    }

    public void create_attribute(String attribute_name, String attribute_value, Element parent_element, Document doc){
        Attr attribute = doc.createAttribute(attribute_name);
        attribute.setValue(attribute_value);
        parent_element.setAttributeNode(attribute);
    }

    public void update_attributes(String data, String[] elements, Document doc, Element parent_node){
        String[] ar_of_attributes = data.split(", \n", -1);

        for (String attr: ar_of_attributes){
            String[] attribute_key_value = attr.split(":=", -1);

            if (Arrays.asList(elements).contains(attribute_key_value[0].strip())) {
                if (attribute_key_value[0].strip().equals("Description")){
                    update_description(doc, attribute_key_value[1].strip(), parent_node);
                }
                else{
                    create_element(attribute_key_value[0].strip(), parent_node, doc);
                }
            }
            else {
                create_attribute(attribute_key_value[0].strip(), attribute_key_value[1].strip(), parent_node, doc);
            }
        }
    }
}