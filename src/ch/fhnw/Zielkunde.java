/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.fhnw;

/**
 *
 * @author Berat
 */
public class Zielkunde {

    int KID;
    
     //Kunde
    String Vorname;
    String Nachname;
    String Adresse;
    String Land;
    String Status;
    
    public Zielkunde(int kid,String vorname, String nachname, String adresse, String lCode, String status){
        
        this.KID = kid;
        this.Vorname = vorname;
        this.Nachname = nachname;
        this.Adresse = adresse;
        this.Land = lCode;
        this.Status = status;
  
    }

    public int getKID() {
        return KID;
    }

    public String getVorname() {
        return Vorname;
    }

    public String getNachname() {
        return Nachname;
    }

    public void setStatus(String inStatus){
        this.Status = inStatus;
    }
    
    
    
}
