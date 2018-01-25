package eg.edu.alexu.csd.oop.db;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Result;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pc
 */
public class Test {
         public static void main(String[] args) throws SQLException, IOException, FileNotFoundException, XMLStreamException {
             
             Parser p = new Parser();
             jdbcdriver d = new jdbcdriver();
             jdbcconnection c= new jdbcconnection();
             Properties ygy = null;
             
             d.connect("C:\\Users\\adelm\\Desktop",ygy);
             
            p.executeStructureQuery("Create table college (name varchar, adress varchar, age int);");
             
            jdbcstatement st= (jdbcstatement) c.createStatement();
//            st.executeUpdate("insert into college (name, adress, age) values (adel, smouha, 21);");
//            st.executeUpdate("insert into college (name, adress, age) values (sandra, camp, 21);");
//            st.executeUpdate("insert into college (name, adress, age) values (esraa, ibrahimia, 19);");
//            st.executeUpdate("insert into college (name, adress, age) values (gii, bokla, 20);");
            
//            
//            st.executeQuery("select age from college where = 21; ");
//        
//          jdbcresultset result = new jdbcresultset();
          
          
          
          
           //  c.close();
    
           
         }
}
