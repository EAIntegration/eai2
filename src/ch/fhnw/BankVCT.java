package ch.fhnw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Berat Aliu
 */
public class BankVCT {
    
    // Verbindung Datenbank
    String Host = "jdbc:mysql://localhost:3306/bank-vct";
    String User = "root";
    String Passwort = "";
    Connection Verbindung = null;
    
    Statement st = null;
    String sql = null;
    ResultSet rs = null;
     
    
    public BankVCT(){
        
        try {
            
            // Connect to the DB
            Verbindung = DriverManager.getConnection(Host, User, Passwort);
            
            // Prepare Statement and execute it
            st = Verbindung.createStatement( );
            sql = "SELECT * FROM Account";
            rs = st.executeQuery(sql);
            
            
        } catch (Exception e) {
            System.out.println("Problems with the Database.");
        }     
    }
}
