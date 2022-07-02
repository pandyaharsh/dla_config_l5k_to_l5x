package com.gm.service;

import com.gm.module.Controller;
import com.gm.module.Datatypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class parserService {


    public void read_file() throws IOException {


        HashMap<String, String> regexMapping = new HashMap<String, String>();
        regexMapping.put("controller","CONTROLLER\\s*(\\S*)\\s*\\((.*\\n*[^)]*)\\)");
        regexMapping.put("datatype", "(?s)(?=DATATYPE)(.*?)(?<=\\sEND_DATATYPE)");


        Path path = Paths.get("C:/Users/meets/Downloads/rt.L5K");

        String data = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);

        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            ProcessingInstruction newPI = doc.createProcessingInstruction("myxml", "version=\"10.0\"");
            doc.insertBefore(newPI, doc.getDocumentElement());

            // controller element
            Element controllerElement = doc.createElement("controller");
            doc.appendChild(controllerElement);

            Controller controller = new Controller();
            controller.controllerHandler(data, doc,regexMapping.get("controller"), controllerElement);

            Element Datatypes = doc.createElement("Datatypes");
            controllerElement.appendChild(Datatypes);


            Datatypes datatypes = new Datatypes();
            datatypes.handleDatatype(data, doc, regexMapping.get("datatype"), Datatypes);


            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("D:\\l5x.xml"));
            transformer.transform(source, result);


        } catch (Exception e) {
            e.printStackTrace();
        }



    }


}
