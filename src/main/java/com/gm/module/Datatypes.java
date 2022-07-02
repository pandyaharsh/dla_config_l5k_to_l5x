package com.gm.module;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Datatypes {

    String[] attributes = {"FamilyType", "Class"};

    public Matcher matchRegex(String regex, String data){

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(data);

        return matcher;
    }

    public void update_dataypes_attributes(String data, Element Datatype, Document doc){

        String regex = "DATATYPE\\s*(\\S*)\\s*\\((\\S*\\n*[^)]*)\\)";
        Matcher datatype_name_attributes = matchRegex(regex, data);
        String name = "", attributes = "";
        if (datatype_name_attributes.find()) {
            name = datatype_name_attributes.group(1);
            attributes = datatype_name_attributes.group(2);
        }

        Attr datatypeName = doc.createAttribute("Name");
        datatypeName.setValue(name);
        Datatype.setAttributeNode(datatypeName);

        String[] arrOfattributes = attributes.split(",", -1);
        for (String attr: arrOfattributes){
            String[] attribute_key_value = attr.split(":=", -1);
//                System.out.println(attribute_key_value[0].strip()+"="+attribute_key_value[1].strip());
            if (attribute_key_value[0].strip().equals("Description")){

                Element Description = doc.createElement("Description");
                Description.appendChild(doc.createCDATASection(attribute_key_value[1].strip()));
                Datatype.appendChild(Description);

            }

            if (attribute_key_value[0].strip().equals("FamilyType")){
                Attr attribute = doc.createAttribute("Family");
                attribute.setValue(attribute_key_value[1].strip());
                Datatype.setAttributeNode(attribute);
            }

            Attr attribute = doc.createAttribute("Class");
            attribute.setValue("User");
            Datatype.setAttributeNode(attribute);

        }

    }

    public void handleDatatype(String data, Document doc, String regex, Element datatypes){

        Matcher matcher = matchRegex(regex, data);

        while (matcher.find()){

            Element Datatype = doc.createElement("Datatype");
//            Datatype.setTextContent(matcher.group());

            update_dataypes_attributes(matcher.group(), Datatype, doc);
            datatypes.appendChild(Datatype);

        }

    }
}
