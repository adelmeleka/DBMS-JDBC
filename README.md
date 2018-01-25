# DBMS-JDBC
SIMPLE DBMS  PROGRAM OVERVIEW By opening the program, it will appear in command line asking the user to type a suitable command (correct syntax) for what he wants to do. All possible commands are the following: 1. Create Table It allows the user to create a new XML file containing an empty table with chosen types for columns allowed for user. 2. Delete Table It allows the user to delete a selected table by name .The XML file with the selected name will be deleted. 3. Insert Into It allows the user to insert a new row into a selected file by name. It accept all the details of the new row and simply add it into the selected file. 4. Select From It allows the user to search for a particular value in a specific XML file indicated by its name. It return the data of all the rows that has this specific value. 5. Delete From It allows the user to delete from a specific table (XML file) a specific value. All rows having this value will be deleted from the file.JDBC &amp; API  DESCRIPTION &amp; OVERVIEW In this part of the project we implemented 4 of the jdbc built in interfaces in Java .We used a different class for every interface we implemented and we overrode all methods in that interface but as stated in the pdf only a few of the methods are required from us so the not needed methods throw exceptions , nothing is written in them. The used methods are as follows: 1. java.sql.Driver - connect(String url, Properties info) Sets the working directory (path of the project) with the string . 2. java.sql.Connection - close() sets the working directory to NULL ie. The user can not perform any further operations - createStatement() Creates an object of class Statement. 3. java.sql.Statement - close() sets the working directory to NULL ie. The user can not perform any further operations PAGE 9 - execute(String sql) Returns the parser method executeStructureQuery(). - executeQuery(String sql) Returns object of class ResultSet and calls the parser method executeRetrievalQuery - executeUpdate(String sql) Returns the parser method executeUpdateQuery(). 4. java.sql.Resultset - close() Sets the working directory to NULL ie. The user can not perform any further operations - findColumn(String columnLabel) Returns an integer indicating the the index of a given column label. - first() Return the iterator to point on the first row of result set array . - getInt(int columnIndex) Given the column index and the iterator on a selected row , we get the selected value casted to integer. - getInt(String columnLabel) Given the column label .We return the correct index for it . - getMetaData() Returns a metadata object - getObject(int columnIndex) Returns the value of a selectected iterator and index in an object PAGE 10 - getStatement() Returns a new statement - getString(int columnIndex) Using a given column index we return a string of an appropriate - getString(String columnLabel) Here we get the suitable string given a string of the column label and using the iterator. - last() Move the iterator to the last row of the array - next() Shift the iterator by 1 to point to the next row - previous() Return the iterator back by 1 in order to point to the previous row of the array.
