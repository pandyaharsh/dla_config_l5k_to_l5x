package com.gm.module;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {


    public Matcher matchRegex(String regex, String data){
        final String controller_regex = regex;
        final Pattern pattern = Pattern.compile(controller_regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(data);

        return matcher;
    }
    public void controllerHandler(String data, Document doc, String regex, Element controllerElement) {


        String[] elements = {"Description", "EngineeringUnit"};


        String name="", attributes = "";

        Matcher matcher = matchRegex(regex,data);
        if (matcher.find()) {
            name = matcher.group(1);
            attributes = matcher.group(2);
        }

        Attr controllerName = doc.createAttribute("Name");
        controllerName.setValue(name);
        controllerElement.setAttributeNode(controllerName);

        String[] arrOfattributes = attributes.split(",", -1);
        for (String attr: arrOfattributes){
            String[] attribute_key_value = attr.split(":=", -1);
//                System.out.println(attribute_key_value[0].strip()+"="+attribute_key_value[1].strip());

            if (Arrays.asList(elements).contains(attribute_key_value[0].strip())) {
//                 supercars element
                if (attribute_key_value[0].strip().equals("Description")){

                    Element Description = doc.createElement("Description");
                    Description.appendChild(doc.createCDATASection(attribute_key_value[1].strip()));
                    controllerElement.appendChild(Description);

                }
                else{

                Element element = doc.createElement(attribute_key_value[0].strip());
                controllerElement.appendChild(element);
                }
            }
            else {
                Attr attribute = doc.createAttribute(attribute_key_value[0].strip());
                attribute.setValue(attribute_key_value[1].strip());
                controllerElement.setAttributeNode(attribute);
            }
        }


    }
}
