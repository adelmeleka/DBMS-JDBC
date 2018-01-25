package eg.edu.alexu.csd.oop.db;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author pc
 */

public class Parser implements Database {

    String filename = new String();
    String valueName[] = new String[50];
    String columnName[] = new String[50];
    String datatypeName[] = new String[50];
    DBMS d = new DBMS();
    int k=0;
    

    @Override
    public boolean executeStructureQuery(String query) throws SQLException {
        DBMS d = new DBMS();
        String[] r = Tooken(query);
        
       
        if (r[0].equalsIgnoreCase("DROP")) {

            filename = r[2].replace(";", "");
            d.deleteTable(filename);

        }
        if (r[0].equalsIgnoreCase("CREATE")) {
            
            filename = r[2];
            columnName = col(r);
            try {
                datatypeName = data(r);
            } catch (IOException ex) {
                Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (XMLStreamException ex) {
                Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(datatypeName!=null){
            try {
                d.createTable(filename, columnName, datatypeName);
            } catch (IOException ex) {
                Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (XMLStreamException ex) {
                Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            }   catch (ParserConfigurationException ex) {
                    Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
                } catch (TransformerException ex) {
                    Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
                }}
        }
        return true;
    }

    @Override
    public Object[][] executeRetrievalQuery(String query) throws SQLException {
        String[] r = Tooken(query);
        String[] f = selectcondition(r);
        Object[][] g = new Object[50][50];
        try {
           
            g = d.selectFrom(r[3], r[1], f[0].charAt(0), f[1]);
        } catch (XMLStreamException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
     
        return g;
    }

    @Override
    public int executeUpdateQuery(String query) throws SQLException {
        DBMS d = new DBMS();
        
        String[] r = Tooken(query);
        if (r[0].equalsIgnoreCase("INSERT")) {
            
          
            try {
              k=  d.insertInto(r[2], insertcol(r), insertval(r));
              
            } catch (XMLStreamException | IOException ex) {
                Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JAXBException ex) {
                Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        if (r[0].equalsIgnoreCase("DELETE")) {
            String[] f = deletecondition(r);
            try {
              k=  d.deleteFrom(fileNameselect(r), f[0], f[1].charAt(0), f[2]);
              
            } catch (XMLStreamException ex) {
                Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return k;
    }

    public static String regexChecker(String theRegex, String str2Check) {
        String t = null;
        Pattern checkRegex = Pattern.compile(theRegex, Pattern.CASE_INSENSITIVE);
        Matcher regexMatcher = checkRegex.matcher(str2Check);

        while (regexMatcher.find()) {
            if (regexMatcher.group().length() != 0) {
                t = regexMatcher.group().trim();
               

            }
        }
       
        return t;
    }

    public static String regexReplace(String temp) {
        Pattern replace = Pattern.compile("\\s+");
        Matcher regexMatcher = replace.matcher(temp.trim());
      
        return regexMatcher.replaceAll(",");
    }

    public String[] Tooken(String f) {
        //String f;
        StringTokenizer st = new StringTokenizer(f);
        int i = 0;
        String[] regex;
        String temp;
        regex = new String[20];
        while (st.hasMoreTokens()) {

            regex[i] = st.nextToken();
            if (regex[i].charAt(0) == '(') {
                temp=  regex[i].replace("(", "");
                regex[i] = "(";
                regex[i+1]=temp;
                i++;
            }
           // System.out.println(regex[i]);
            i++;
        }
        if (regex[i - 1]!= ");") {
            regex[i - 1] = regex[i - 1].replace(");", "");
            regex[i] = ");";
            regex[i + 1] = null;

        }
        return regex;
    }

    public String[] col(String[] f) {
        String s[] = new String[25];
        int j = 0;
        int i = 4;
        while (f[i] != ");") {
            s[j] = f[i];
          
            i = i + 2;
            j++;
        }

        return s;
    }

public String[] data(String[] f) throws SQLException, IOException, FileNotFoundException, XMLStreamException {
        String s[] = new String[25];
        int j = 0;
        int i = 5;
        while (f[i] != null) {
            if(f[i].equalsIgnoreCase("int,")||f[i].equalsIgnoreCase("varchar,")||f[i].equalsIgnoreCase("int")||f[i].equalsIgnoreCase("varchar")){
           if(f[i].equalsIgnoreCase("int,")||f[i].equalsIgnoreCase("int"))
                s[j] = "integer";
           else s[j]="string";
          
            i = i + 2;
            j++;
        }
            else {s=null;
                System.out.println("wrong syntax");
                break;
               
            }
        }

        return s;
    }

    public String[] insertcol(String[] f) {
        int i = 4;
        int j = 0;
        String temp = new String();
        String []r =new String[25];
    
        while (f[i] != null) {
            if (f[i].equalsIgnoreCase("VALUES")||f[i].equals(")")) {
                break;
            }
            temp = f[i].replace(",", "");
            temp = temp.replace("(", "");
            temp = temp.replace(")", "");
            r[j] = temp;
          
            j++;
            i++;
        }
       
        return r;
    }

    public String[] insertval(String[] f) {
        int i = 0;
        int j = 0;
        String temp = new String();
        String r[] = new String[25];
        while (f[i]!=null){
        if (!f[i].equalsIgnoreCase("VALUES"))
            i++;
            else  {
                i+=2;
                while (!f[i].equals(");")&&f[i]!=null) {
                    
                    temp = f[i].replace(",", "");
                    temp = temp.replace("(", "");
                    temp = temp.replace(")", "");
                    temp = temp.replace(";", "");
                    r[j] = temp;
                   
                    j++;
                    i++;
                }
                
              }
        }
     
        return r;
    }


    public String fileNameselect(String[] f) {
        int i = 0;
        String s = new String();
        while (f[i] != null) {
            if (f[i].equalsIgnoreCase("FROM")) {
                s = f[++i];
                break;
            }
            i++;
        }
        return s;
    }

    public String[] deletecondition(String[] f) {
        int i = 0;
        int j = 0;
        String s[] = new String[4];
        while (f[i++] != null) {
            if (f[i].equalsIgnoreCase("WHERE")) {
                i++;
                while (f[i] != null) {
                    s[j] = f[i].replace(";", "");
                    j++;
                    i++;

                }
                break;
            }
        }
        s[3] = null;
        return s;
    }

    public String[] selectcondition(String[] f) {
        int i = 0;
        int j = 0;
        String s[] = new String[3];
        while (f[i++] != null) {
            if (f[i].equalsIgnoreCase("WHERE")) {
                i++;
                while (f[i] != null) {
                    s[j] = f[i].replace(";", "");
                    j++;
                    i++;

                }
                break;
            }
        }
        s[2] = null;
        return s;
    }
}
