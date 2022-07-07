package com.gm.module;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;

public class Controller {
    updateData update_data = new updateData();

    public void controllerHandler(String data, Document doc, String regex, Element controllerElement) {

        HashMap<String, String[]> exceptions = new HashMap<>();
        exceptions.put("RedundancyEnabled", new String[]{"RedundancyInfo", "Enabled"});
        exceptions.put("KeepTestEditsOnSwitchOver", new String[]{"RedundancyInfo", "KeepTestEditsOnSwitchOver"});
        exceptions.put("SecurityCode", new String[]{"Security", "Code"});
        exceptions.put("ChangesToDetect", new String[]{"Security", "ChangesToDetect"});
        exceptions.put("SafetyLocked", new String[]{"SafetyInfo", "SafetyLocked"});
        exceptions.put("ConfiguredSafetyIOAlways", new String[]{"SafetyInfo", "ConfiguredSafetyIOAlways"});
        exceptions.put("SignatureRunModeProtect", new String[]{"SafetyInfo", "SignatureRunModeProtect"});
        exceptions.put("SafetyLevel", new String[]{"SafetyInfo", "SafetyLevel"});

        String[] elements = {"Description", "EngineeringUnit"};

        String name = "", attributes = "";

        Matcher matcher = update_data.matchRegex(regex, data);
        if (matcher.find()) {
            name = matcher.group(1);
            attributes = matcher.group(2);
        }

        Attr controllerName = doc.createAttribute("Name");
        controllerName.setValue(name);
        controllerElement.setAttributeNode(controllerName);

        String[] arr_of_attributes = attributes.split(",", -1);

        for (String attr : arr_of_attributes) {
            String[] attribute_key_value = attr.split(":=", -1);

            if (Arrays.asList(elements).contains(attribute_key_value[0].strip())) {
                if (attribute_key_value[0].strip().equals("Description"))
                    update_data.update_description(doc, attribute_key_value[1].strip(), controllerElement);
                else
                    update_data.create_element(attribute_key_value[0].strip(), controllerElement, doc);
            } else {
                if (!exceptions.containsKey(attribute_key_value[0].strip())) {
                    update_data.create_attribute(attribute_key_value[0].strip(), attribute_key_value[1].strip(), controllerElement, doc);
                }
            }
        }

        Element Security = update_data.create_element("Security", controllerElement, doc);
        Element SafetyInfo = update_data.create_element("SafetyInfo", controllerElement, doc);
        Element RedundancyInfo = update_data.create_element("RedundancyInfo", controllerElement, doc);

        for (String a : arr_of_attributes) {
            String[] attributes_key_value = a.split(":=", -1);
            if (exceptions.containsKey(attributes_key_value[0].strip())) {
                if (exceptions.get(attributes_key_value[0].strip())[0].equals("Security"))
                    update_data.create_attribute(exceptions.get(attributes_key_value[0].strip())[1],
                            update_data.boolean_value(exceptions.get(attributes_key_value[0].strip())[1], attributes_key_value[1].strip()),
                            Security,
                            doc);
                if (exceptions.get(attributes_key_value[0].strip())[0].equals("SafetyInfo"))
                    update_data.create_attribute(exceptions.get(attributes_key_value[0].strip())[1],
                            update_data.boolean_value(exceptions.get(attributes_key_value[0].strip())[1], attributes_key_value[1].strip()),
                            SafetyInfo,
                            doc);
                if (exceptions.get(attributes_key_value[0].strip())[0].equals("RedundancyInfo"))
                    update_data.create_attribute(exceptions.get(attributes_key_value[0].strip())[1],
                            update_data.boolean_value(exceptions.get(attributes_key_value[0].strip())[1], attributes_key_value[1].strip()),
                            RedundancyInfo,
                            doc);
            }
        }
    }
}