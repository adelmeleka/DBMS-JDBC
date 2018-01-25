/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eg.edu.alexu.csd.oop.db;

//import static eg.edu.alexu.csd.oop.db.DBMS.workingDirectory;
import java.io.FileWriter;
import java.util.ArrayList;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventFactory;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author adelm
 */
public class DBMS {

    ArrayList<String> tablesNames = new ArrayList();
    static String workingDirectory = "C:\\Users\\adelm\\Downloads\\DBMS\\src\\eg\\edu\\alexu\\csd\\oop\\db";
    File folder = new File(workingDirectory);
    File[] listOfFiles = folder.listFiles();
    static int rowsCounter = 0;

    public DBMS() {

        for (File f : listOfFiles) {
            if (f.getName().endsWith(".xml")) {
                tablesNames.add(f.getName().substring(0, f.getName().length() - 4));
            }
        }
    }

    public boolean deleteTable(String fileName) {

        for (String name : tablesNames) {
            if (fileName.equals(name)) {

                tablesNames.remove(name);

                for (File f : listOfFiles) {
                    if (f.getName().endsWith(".xml")) {
                        if (f.getName().substring(0, f.getName().length() - 4).equals(name)) {
                            f.delete();
                        }
                    }
                }

                return true;
            }

        }
        System.out.println("File Not Found");
        return false;
    }

    public boolean createTable(String fileName, String[] columns, String[] types) throws IOException, XMLStreamException, ParserConfigurationException, TransformerConfigurationException, TransformerException {

        for (String name : tablesNames) {
            if (fileName.equals(name)) {
                System.out.println("File name already exists!");
                return false;
            }
        }
        

        XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
        try (FileWriter fWriter = new FileWriter(new File(workingDirectory + "\\" + fileName + ".xml"))) {
            XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(fWriter);
            xMLStreamWriter.writeStartDocument();
            xMLStreamWriter.writeStartElement(fileName);

            xMLStreamWriter.writeEndElement();
            xMLStreamWriter.writeEndDocument();

            xMLStreamWriter.flush();
            xMLStreamWriter.close();
        }

        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        FileWriter fileWriter = new FileWriter(workingDirectory + "\\" + fileName + ".xsd");
        XMLEventWriter writer = factory.createXMLEventWriter(fileWriter);
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();

        writer.add(eventFactory.createStartDocument());
        writer.add(eventFactory.createStartElement("", null, "xs:schema"));
        writer.add(eventFactory.createAttribute("xmlns:xs", "http://www.w3.org/2001/XMLSchema"));

        writer.add(eventFactory.createStartElement("", null, "xs:element"));
        writer.add(eventFactory.createAttribute("name", fileName));
        writer.add(eventFactory.createStartElement("", null, "xs:complexType"));
        writer.add(eventFactory.createStartElement("", null, "xs:sequence"));

        writer.add(eventFactory.createStartElement("", null, "xs:element"));
        writer.add(eventFactory.createAttribute("name", "row"));
        writer.add(eventFactory.createAttribute("minOccurs", "0"));
        writer.add(eventFactory.createAttribute("maxOccurs", "unbounded"));
        writer.add(eventFactory.createStartElement("", null, "xs:complexType"));
        writer.add(eventFactory.createStartElement("", null, "xs:sequence"));

        for (int i = 0; i < columns.length; i++) {
            if(columns[i]!=null){

            writer.add(eventFactory.createStartElement("", null, "xs:element"));
            writer.add(eventFactory.createAttribute("name", columns[i]));
            writer.add(eventFactory.createAttribute("type", "xs:" + types[i]));
            writer.add(eventFactory.createEndElement("", null, ""));
            }
        }

        writer.add(eventFactory.createEndElement("", null, "xs:sequence"));
        writer.add(eventFactory.createEndElement("", null, "xs:complexType"));
        writer.add(eventFactory.createEndElement("", null, "xs:element"));

        writer.add(eventFactory.createEndElement("", null, "xs:sequence"));
        writer.add(eventFactory.createEndElement("", null, "xs:complexType"));
        writer.add(eventFactory.createEndElement("", null, "xs:element"));

        writer.add(eventFactory.createEndElement("", null, "xs:schema"));
        writer.add(eventFactory.createEndDocument());

        writer.flush();
        writer.close();
        fileWriter.close();

        tablesNames.add(fileName);
        return true;

    }
    
    public static boolean validateXMLSchema(String xsdPath, String xmlPath) throws JAXBException, IOException {

        try {
            SchemaFactory factory
                    = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            javax.xml.validation.Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
            System.out.println("XML is Valid");
        } catch (SAXException e) {
            System.out.println("Exception: " + e.getMessage());
            return false;
        }
        return true;
    }

    public int insertInto(String tableName, String[] columns, String[] values) throws XMLStreamException, FileNotFoundException, IOException, JAXBException {

        for (String name : tablesNames) {
            if (tableName.equals(name)) {
                XMLInputFactory inFactory = XMLInputFactory.newInstance();
                XMLEventReader eventReader = inFactory.createXMLEventReader(new FileInputStream(workingDirectory + "\\" + tableName + ".xml"));
                XMLOutputFactory factory = XMLOutputFactory.newInstance();
                FileWriter fileWriter = new FileWriter(workingDirectory + "\\temp.xml");
                XMLEventWriter writer = factory.createXMLEventWriter(fileWriter);
                XMLEventFactory eventFactory = XMLEventFactory.newInstance();
                while (eventReader.hasNext()) {
                    XMLEvent event = eventReader.nextEvent();
                    writer.add(event);
                    if (event.getEventType() == XMLEvent.START_ELEMENT) {
                        if (event.asStartElement().getName().toString().equalsIgnoreCase(tableName)) {

                            writer.add(eventFactory.createStartElement("", null, "row"));

                            int i = 0;
                            for (String column : columns) {
                                if (columns[i]!=null){
                                writer.add(eventFactory.createStartElement("", null, column));
                                writer.add(eventFactory.createCharacters(values[i++]));
                                writer.add(eventFactory.createEndElement("", null, column));
                                }
                            }
                            writer.add(eventFactory.createEndElement("", null, "row"));

                            rowsCounter++;
                        }
                        if (event.asStartElement().getName().toString().equalsIgnoreCase("row")) {
                            rowsCounter++;
                        }

                    }
                }

                writer.flush();
                writer.close();
                fileWriter.close();

                File inputFile = new File(workingDirectory + "\\" + tableName + ".xml");
                File outFile = new File(workingDirectory + "\\temp.xml");
                if (validateXMLSchema(workingDirectory + "\\" + tableName + ".xsd",workingDirectory + "\\temp.xml")) {
                    System.out.println("Valid Input");
                    if (inputFile.delete()) {
                        outFile.renameTo(inputFile);
                    }
                } else {
                    System.out.println("Invalid Input");
                    outFile.delete();
                }

                return rowsCounter;
            }
        }
        System.out.println("Table Name Not Found!");
     
        return 0;
    }

    public Object[][] selectFrom(String tableName, String column, char condition, Object Value) throws FileNotFoundException, XMLStreamException, IOException {

        int i, j;

        int count = 1;
        Object[][] result = null;
        String lastColumn = null;
        boolean found = false;
        List<List<String>> listOfLists = new ArrayList<List<String>>();

        int conditionValue = 0;
        switch (condition) {
            case '>':
                conditionValue = 1;
                break;
            case '=':
                conditionValue = 0;
                break;
            case '<':
                conditionValue = -1;
                break;
        }

        for (String name : tablesNames) {
            if (tableName.equals(name)) {
                XMLInputFactory inFactory = XMLInputFactory.newInstance();
                FileInputStream file = new FileInputStream(workingDirectory + "\\" + tableName + ".xml");
                XMLEventReader eventReader = inFactory.createXMLEventReader(file);

                ArrayList<String> row = new ArrayList<>();
                while (eventReader.hasNext()) {

                    XMLEvent event = eventReader.nextEvent();
                   // System.out.println(event);

                    if (event.getEventType() == XMLEvent.START_ELEMENT) {

                        lastColumn = event.asStartElement().getName().toString();
                        System.out.println(lastColumn);

                    } else if (event.getEventType() == XMLEvent.CHARACTERS) {

                        row.add(event.asCharacters().getData());

                        int comparable = event.asCharacters().getData().compareToIgnoreCase((String) Value);
                        if (comparable != 0) {
                            comparable /= Math.abs(comparable);
                        }

                        if (comparable == conditionValue && lastColumn.equalsIgnoreCase(column)) {
                            found = true;
                        }
                    } else if (event.getEventType() == XMLEvent.END_ELEMENT) {
                        if (event.asEndElement().getName().toString().equalsIgnoreCase("row")) {
                            if (found) {

                                listOfLists.add(new ArrayList<>());
                                for (String element : row) {
                                    listOfLists.get(listOfLists.size() - 1).add(element);
                                }

                            }
                            count = row.size();
                            row = new ArrayList<>();
                            found = false;

                        }
                    }

                }

                eventReader.close();
                file.close();
                
                result = new String[listOfLists.size()][count];

                for (i = 0; i < listOfLists.size(); i++) {
                    for (j = 0; j < count; j++) {
                        result[i][j] = listOfLists.get(i).get(j);
                    }
                }
                
                for (i = 0; i < listOfLists.size(); i++) {
                    for (j = 0; j < count; j++) {
                        System.out.println(result[i][j]);
                    }
                }

                return result;
            }

        }

        System.out.println("Filename not found!");
        return null;
    }

    public int deleteFrom(String tableName, String coloumn, char condition, Object Value) throws FileNotFoundException, XMLStreamException, IOException {
        int count = 0;
        for (String name : tablesNames) {
            if (tableName.equals(name)) {

                String lastColumn = null;
                ArrayList<String> columns = new ArrayList<>();
                ArrayList<String> row = new ArrayList<>();
                boolean found = false;
                int comparable = 0;
                int conditionValue = 0;

                switch (condition) {
                    case '>':
                        conditionValue = 1;
                        break;
                    case '=':
                        conditionValue = 0;
                        break;
                    case '<':
                        conditionValue = -1;
                        break;
                }

                XMLInputFactory inFactory = XMLInputFactory.newInstance();
                XMLEventReader eventReader = inFactory.createXMLEventReader(new FileInputStream(workingDirectory + "\\" + tableName + ".xml"));
                XMLOutputFactory factory = XMLOutputFactory.newInstance();
                FileWriter fileWriter = new FileWriter(workingDirectory + "\\temp.xml");
                XMLEventWriter writer = factory.createXMLEventWriter(fileWriter);
                XMLEventFactory eventFactory = XMLEventFactory.newInstance();
                XMLEvent event = eventReader.nextEvent();

                writer.add(event);

                event = eventReader.nextEvent();

                writer.add(event);
                event = eventReader.nextEvent();

                while (eventReader.hasNext()) {
                    event = eventReader.nextEvent();

                    if (event.getEventType() == XMLEvent.START_ELEMENT) {

                        lastColumn = event.asStartElement().getName().toString();
                        if (lastColumn != "row") {
                            columns.add(lastColumn);

                        }

                    } else if (event.getEventType() == XMLEvent.CHARACTERS) {

                        row.add(event.asCharacters().getData());

                        if (lastColumn.equalsIgnoreCase(coloumn)) {

                            comparable = event.asCharacters().getData().compareToIgnoreCase((String) Value);

                            if (comparable != 0) {
                                comparable /= Math.abs(comparable);
                            }
                            if (comparable != conditionValue && lastColumn.equalsIgnoreCase(coloumn)) {

                                found = true;
                                count++;

                            }
                        }

                    } else if (event.getEventType() == XMLEvent.END_ELEMENT) {

                        if (event.asEndElement().getName().toString().equalsIgnoreCase("row")) {
                            if (found) {

                                writer.add(eventFactory.createStartElement("", null, "row"));
                                int i;

                                for (i = 0; i < columns.size(); i++) {

                                    writer.add(eventFactory.createStartElement("", null, columns.get(i)));
                                    writer.add(eventFactory.createCharacters(row.get(i)));
                                    writer.add(eventFactory.createEndElement("", null, columns.get(i)));

                                }
                                writer.add(eventFactory.createEndElement("", null, "row"));

                            }

                            row = new ArrayList<>();
                            columns = new ArrayList<>();
                            found = false;
                        }

                        if (event.asEndElement().getName().toString().equalsIgnoreCase(tableName)) {
                            writer.add(event);
                        }
                    }

                }

                eventReader.close();
                writer.flush();
                writer.close();
                fileWriter.close();

                File inputFile = new File(workingDirectory + "\\" + tableName + ".xml");
                File outFile = new File(workingDirectory + "\\temp.xml");
                if (inputFile.delete()) {
                    outFile.renameTo(inputFile);
                }

                rowsCounter = count;
                return rowsCounter;

            }

        }

        System.out.println("Filename not found!");
        return 0;

    }

}
