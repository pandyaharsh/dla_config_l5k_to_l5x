package com.gm.module;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Datatypes {

    public void update_description(Document doc, String value, Element parent_element){
        Element Description = doc.createElement("Description");
        Description.appendChild(doc.createCDATASection(value));
        parent_element.appendChild(Description);
    }


    public Matcher matchRegex(String regex, String data){

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(data);

        return matcher;
    }

    public void update_single_dataype(String data, Element Datatype, Document doc){

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

                update_description(doc, attribute_key_value[1].strip(), Datatype);

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
        Element Members = doc.createElement("Members");
        Datatype.appendChild(Members);

        update_datatype_members(data, doc, Members);

    }

    public void handleDatatype(String data, Document doc, String regex, Element datatypes){

        Matcher matcher = matchRegex(regex, data);

        while (matcher.find()){

            Element Datatype = doc.createElement("Datatype");
//            Datatype.setTextContent(matcher.group());


            update_single_dataype(matcher.group(), Datatype, doc);
            datatypes.appendChild(Datatype);

        }

    }

    public void update_datatype_members(String data, Document doc, Element members) {

//        update_int_member(data, doc, members);
        update_sint_member(data, doc, members);
//        update_dint_member(data, doc, members);
//        update_counter_member(data, doc, members);
//        update_bit_member(data, doc, members);

    }

    public void update_bit_member(String data, Document doc, Element members) {
        Element Member = doc.createElement("Member");
        members.appendChild(Member);
    }

    private void update_counter_member(String data, Document doc, Element members) {
        Element Member = doc.createElement("Member");
        members.appendChild(Member);

    }

    private void update_dint_member(String data, Document doc, Element members) {
        Element Member = doc.createElement("Member");
        members.appendChild(Member);

    }

    public void update_sint_member(String data, Document doc, Element members) {

        String regex = "(SINT)\\s(\\w*)(\\[(\\d*)\\])?\\s\\((.*)\\)";

        Matcher sint_match = matchRegex(regex, data);

        while (sint_match.find()) {
            Element Member = doc.createElement("Member");
            members.appendChild(Member);

            Attr DataType_type = doc.createAttribute("DataType");
            DataType_type.setValue(sint_match.group(1));
            Member.setAttributeNode(DataType_type);

            Attr DataType_name = doc.createAttribute("Name");
            DataType_name.setValue(sint_match.group(2));
            Member.setAttributeNode(DataType_name);

            Attr DataType_dimension = doc.createAttribute("Dimension");
            if (sint_match.group(4) != null){
                DataType_dimension.setValue(sint_match.group(4));
            }
            else{
                DataType_dimension.setValue("0");
            }
            Member.setAttributeNode(DataType_dimension);

            String[] arrOfattributes = sint_match.group(5).split(",", -1);
            for (String attr: arrOfattributes){
                String[] attribute_key_value = attr.split(":=", -1);

//                 supercars element
                if (attribute_key_value[0].strip().equals("Description")){
                    update_description(doc, attribute_key_value[1].strip(), Member);
                }
                else {
                    Attr attribute = doc.createAttribute(attribute_key_value[0].strip());
                    attribute.setValue(attribute_key_value[1].strip());
                    Member.setAttributeNode(attribute);
                }
            }
        }
    }

    private void update_int_member(String data, Document doc, Element members) {
        Element Member = doc.createElement("Member");
        members.appendChild(Member);

    }
}
