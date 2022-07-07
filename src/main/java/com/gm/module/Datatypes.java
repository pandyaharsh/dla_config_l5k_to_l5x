package com.gm.module;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.regex.Matcher;

public class Datatypes {

    updateData update_data = new updateData();

    public void update_single_dataype(String data, Element Datatype, Document doc) {

        String regex = "DATATYPE\\s*(\\S*)\\s*\\((\\S*\\n*[^)]*)\\)";
        Matcher datatype_name_attributes = update_data.matchRegex(regex, data);
        String name = "", attributes = "";
        if (datatype_name_attributes.find()) {
            name = datatype_name_attributes.group(1);
            attributes = datatype_name_attributes.group(2);
        }

        update_data.create_attribute("Name", name, Datatype, doc);

        String[] arrOfattributes = attributes.split(",", -1);
        for (String attr : arrOfattributes) {
            String[] attribute_key_value = attr.split(":=", -1);
            if (attribute_key_value[0].strip().equals("Description")) {
                update_data.update_description(doc, attribute_key_value[1].strip(), Datatype);
            }

            if (attribute_key_value[0].strip().equals("FamilyType")) {
                update_data.create_attribute("Family", attribute_key_value[1].strip(), Datatype, doc);
            }
            update_data.create_attribute("Class", "User", Datatype, doc);

        }

        Element Members = doc.createElement("Members");
        Datatype.appendChild(Members);

        update_datatype_members(data, doc, Members);

    }

    public void handleDatatype(String data, Document doc, String regex, Element datatypes) {
        Matcher matcher = update_data.matchRegex(regex, data);

        while (matcher.find()) {
            Element Datatype = doc.createElement("Datatype");
            update_single_dataype(matcher.group(), Datatype, doc);
            datatypes.appendChild(Datatype);
        }
    }

    public void update_datatype_members(String data, Document doc, Element members) {

        String members_regex = "\\t{2}(\\S*)(.*);";
        Matcher members_data = update_data.matchRegex(members_regex, data);
        while (members_data.find()) {
            if (members_data.group(1).equals("SINT"))
                update_sint_member(members_data.group(0), doc, members);

            if (members_data.group(1).equals("BIT"))
                update_bit_member(members_data.group(0), doc, members);

            if (members_data.group(1).equals("DINT"))
                update_dint_member(data, doc, members);
        }
    }

    public void update_bit_member(String data, Document doc, Element members) {
        String bit_regex = "(BIT)\\s(\\w*)\\s(\\w*)\\s:\\s(\\d*)\\s\\((.*)\\)";
        Matcher bit_match = update_data.matchRegex(bit_regex, data);

        if (bit_match.find()){
            Element Member = doc.createElement("Member");
            members.appendChild(Member);

            update_data.create_attribute("DataType", bit_match.group(1), Member, doc);
            update_data.create_attribute("Name", bit_match.group(2), Member, doc);
            update_data.create_attribute("BitMember", bit_match.group(3), Member, doc);
            update_data.create_attribute("BitNumber", bit_match.group(4), Member, doc);
            update_data.create_attribute("Dimension", "0", Member, doc);
//                update_data.update_description(doc, );

            String[] elements = {"Description"};
            update_data.update_attributes(bit_match.group(5), elements, doc, Member);

        }
    }

    private void update_counter_member(String data, Document doc, Element members) {
        Element Member = doc.createElement("Member");
        members.appendChild(Member);

    }

    private void update_dint_member(String data, Document doc, Element members) {
        String regex = "(DINT)\\s(\\w*)((\\[(\\d*)\\])?\\s\\((.*)\\))?";
        Matcher dint_match = update_data.matchRegex(regex, data);

        while  (dint_match.find()) {
            Element Member = doc.createElement("Member");
            members.appendChild(Member);

            update_data.create_attribute("DataType", dint_match.group(1), Member, doc);
            update_data.create_attribute("Name", dint_match.group(2), Member, doc);

            Attr DataType_dimension = doc.createAttribute("Dimension");
            if (dint_match.group(5) != null) {
                DataType_dimension.setValue(dint_match.group(5));
            } else {
                DataType_dimension.setValue("0");
            }
            Member.setAttributeNode(DataType_dimension);
            String[] elements = {"Description"};
        }

    }

    public void update_sint_member(String data, Document doc, Element members) {

        String sint_regex = "(SINT)\\s(\\w*)(\\[(\\d*)\\])?\\s\\((.*)\\);";

        Matcher sint_match = update_data.matchRegex(sint_regex, data);

        while (sint_match.find()) {
            Element Member = doc.createElement("Member");
            members.appendChild(Member);

            update_data.create_attribute("DataType", sint_match.group(1), Member, doc);
            update_data.create_attribute("Name", sint_match.group(2), Member, doc);

            Attr DataType_dimension = doc.createAttribute("Dimension");
            if (sint_match.group(4) != null) {
                DataType_dimension.setValue(sint_match.group(4));
            } else {
                DataType_dimension.setValue("0");
            }
            Member.setAttributeNode(DataType_dimension);

            String[] elements = {"Description"};
            update_data.update_attributes(sint_match.group(5), elements, doc, Member);
        }

    }
    private void update_int_member(String data, Document doc, Element members) {
        Element Member = doc.createElement("Member");
        members.appendChild(Member);
    }
}