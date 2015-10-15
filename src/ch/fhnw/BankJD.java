package ch.fhnw;

import javax.xml.ws.Holder;

/**
 *
 * @author Berat
 */
public class BankJD {

    Holder Vorname = new Holder();
    Holder Nachname = new Holder();
    Holder Strasse = new Holder();
    Holder PLZORT = new Holder();
    Holder Zins = new Holder();
    Holder KontoNr = new Holder();
    Holder Kontostand = new Holder();
    
    public BankJD(Object nname){
     
        holeSparkonto("", nname.toString(), Vorname, Nachname, Strasse, PLZORT, Zins, KontoNr, Kontostand);
        
    }

    private static void holeSparkonto(java.lang.String queryVorname, java.lang.String queryNachname, javax.xml.ws.Holder<java.lang.String> vorname, javax.xml.ws.Holder<java.lang.String> nachname, javax.xml.ws.Holder<java.lang.String> strasse, javax.xml.ws.Holder<java.lang.String> plzOrt, javax.xml.ws.Holder<java.lang.Float> zinsen, javax.xml.ws.Holder<java.lang.Long> kontonummer, javax.xml.ws.Holder<java.lang.Long> kontostand) {
        ch.fhnw.wi.eai.bankjd.BankJDService service = new ch.fhnw.wi.eai.bankjd.BankJDService();
        ch.fhnw.wi.eai.bankjd.BankJD port = service.getBankJDPort();
        port.holeSparkonto(queryVorname, queryNachname, vorname, nachname, strasse, plzOrt, zinsen, kontonummer, kontostand);
    }
    
}
