/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.fhnw;

/**
 *
 * @author Serkan
 */
public class Zielkonto {
    int KID;
    
    //Konto
    String IBAN;
    double kontostand;
    String kontoart;
    
    public Zielkonto(int KID, String IBAN, double kontostand, String kontoart){
        this.KID = KID;
        this.IBAN = IBAN;
        this.kontostand = kontostand;
        this.kontoart = kontoart;
       
    }
}
