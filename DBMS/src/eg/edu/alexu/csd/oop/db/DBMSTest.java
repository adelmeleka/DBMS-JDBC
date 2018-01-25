/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eg.edu.alexu.csd.oop.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;
import static javafx.application.Platform.exit;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;


/**
 *
 * @author adelm
 */
public class DBMSTest{


    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws javax.xml.stream.XMLStreamException
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws javax.xml.transform.TransformerException
     * @throws java.io.FileNotFoundException
     * @throws org.xml.sax.SAXException
     * @throws javax.xml.bind.JAXBException
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws IOException, XMLStreamException, ParserConfigurationException, TransformerException, FileNotFoundException, SAXException, JAXBException, SQLException {
        // TODO code application logic here
    
        Parser p = new Parser();
        String Entry = new String();
        System.out.println("PLEASE ENTER A COMMAND:");
        Scanner in = new Scanner(System.in);
        Entry = in.nextLine();
        while (!Entry.equalsIgnoreCase("EXIT;")) {
            String[] f = p.Tooken(Entry);
            if (f[0].equalsIgnoreCase("CREATE") || f[0].equalsIgnoreCase("DROP") || f[0].equalsIgnoreCase("INSERT") || f[0].equalsIgnoreCase("DELETE") || f[0].equalsIgnoreCase("SELECT")) {

                while (Entry.charAt(Entry.length() - 1) != ';') {
                    Entry = Entry + "\n";
                    Entry = Entry + in.nextLine();
                }

                String[] r = p.Tooken(Entry);
                r[0] = r[0].toUpperCase();
                System.out.println(r[0]);
                switch (r[0]) {
                    case "CREATE":
                        if (p.regexChecker("create+\\s+table+\\s+[A-Za-z0-9_. ]+\\s+\\(+\\s*+[[A-Za-z0-9_.]+\\s+[A-Za-z0-9_.]+(,*)+\\s]+\\)+;", Entry) == null) {
                            System.out.println("WRONG SYNTAX");
                            break;
                        } else {
                            p.executeStructureQuery(Entry);
                            break;
                        }
                    case "DROP":
                        if (p.regexChecker("drop+\\s+table+\\s+[A-Za-z0-9_]+;", Entry) == null) {
                            System.out.println("WRONG SYNTAX");
                            break;
                        } else {
                            p.executeStructureQuery(Entry);
                            break;
                        }
                    case "INSERT":
                        if (p.regexChecker("insert+\\s+into+\\s+[A-Za-z0-9_]+\\s+\\(+[A-Za-z0-9_, ]+\\)+\\s+values+\\s+\\(+[A-Za-z0-9_, ]+\\)+;", Entry) == null) {
                            System.out.println("WRONG SYNTAX");
                            break;
                        } else {
                            p.executeUpdateQuery(Entry);
                            break;
                        }
                    case "SELECT":
                        if (p.regexChecker("select+\\s+[A-Za-z0-9_ ]+(,*)+\\s+from+\\s+[A-Za-z0-9_]+\\s+where+\\s+[<>=]+\\s+[A-Za-z0-9]+;", Entry) == null) {
                            System.out.println("WRONG SYNTAX");
                            break;
                        } else {
                            p.executeRetrievalQuery(Entry);
                            break;
                        }
                    case "DELETE":
                        if (p.regexChecker("delete+\\s+from+\\s+[A-Za-z0-9_]+\\s+where+\\s+[A-Za-z0-9_]+\\s+[<>=]+\\s+[A-Za-z0-9_]+;", Entry) == null) {
                            System.out.println("WRONG SYNTAX");
                            break;
                        } else {
                            p.executeUpdateQuery(Entry);
                            break;
                        }
                    default:
                        System.out.println("NOT SUPPORTED ENTRY!!");

                }
                
              

            } else {
                System.out.println("NOT SUPPORTED ENTRY!!");

            }

            System.out.println("***PRESS Enter KEY TO CONTINUE***");
            in.nextLine();
            System.out.print("\f");

            System.out.println("PLEASE ENTER A COMMAND:");
            Entry = in.nextLine();
        }
        exit(); 
    }

}
