package ch.fhnw;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Serkan & Berat
 */
public class CollectData {
    
    // ArrayList von Daten der Webservice
    static ArrayList<BankJDSpar> kundenSparkonto = new ArrayList<BankJDSpar>();
    static ArrayList<BankJDkKorrent> kundenFirmakonto = new ArrayList<BankJDkKorrent>();
    
    // ArrayList von Enddaten
    static ArrayList<Zielkunde> zielKunden = new ArrayList<Zielkunde>(); 
    static ArrayList<Zielkonto> zielKonten = new ArrayList<Zielkonto>(); 
    
    static BankVCT vctData = null;
    static int kid = 1;
    
    public static void main (String[] args){
        
        String[] jdNachnamen = null;
        
        // Holt die Daten vom VCT
        System.out.println("BankVCT:");
        vctData = new BankVCT();
        
        // Holt die Daten vom Webserver
        System.out.println("BankJD - Sparkonto:");
        jdSparKundenLaden();
        jdFirmaKundenLaden();
        
        zielvctDatenSpeichern(vctData);
        vctData = null;
        
        zielJDDatenSpeichern();
        
        // Status setzen
        setRank();
        
        AllesAusgeben();
        
    } 
    
    //Methode welche alle Kunden mit einem Sparkonto ladet
    private static void jdSparKundenLaden(){
        Object[] jdNname = listeSparkontoNachname().toArray();
        
        // Speichert alle Kunden
        for(Object sname : jdNname){
            BankJDSpar kunde = new BankJDSpar(sname);
            kundenSparkonto.add(kunde);
            System.out.println(kunde.Vorname.value);
        }
        System.out.println("---------- Liste von den Kunden die einen Sparkonto haben ----------");
    }
    
    //Methode welche alle Kunden mit einem Kontokorrentkonto ladet
    private static void jdFirmaKundenLaden(){
        Object[] jdNname = listeKontokorrentNachname().toArray();
        for(Object sname : jdNname){
            BankJDkKorrent kunde = new BankJDkKorrent(sname);
            kundenFirmakonto.add(kunde);
            System.out.println(kunde.Vorname.value);
        }
        System.out.println("---------- Liste von den Kunden die ein Firmenkonto haben ----------");
    }
    // VCT Daten richtig mirgieren
    private static void zielvctDatenSpeichern(BankVCT vctData){
   
        String vorname = null;
        String nachname= null;
        String adresse = null;
        String laendercode = null;
        String status = null;
        
        String IBAN = null;
        double kontostand = 0;
        String kontoart = null;
        
        
        try {
            while(vctData.rs.next()){
               
            vorname = "";
            nachname= "";
            adresse = "";
            laendercode = "";
            status = ""; 

            IBAN = "";
            kontostand = 0;
            kontoart = "";
            
            if(!vctData.rs.getString("Kundenart").equalsIgnoreCase("Firma")){
            
            
               // Vor und Nachname
               String[] resultat = vctData.rs.getString("Kundenname").split(" ");
               
               // Umlaute umwandeln
               for(int i = 0; i < resultat.length; i++){
                   resultat[i] = ersetzeUmlaut(resultat[i]);
               }
               
               if(resultat.length==1){
                   vorname = resultat[0];
               }
               
                else if(resultat.length==2 ){
                       // Standard Name
                       vorname = resultat[0];
                       nachname = resultat[1];
                       if(resultat[0].equalsIgnoreCase("Knut")){
                        vorname = "Knut";
                        nachname = "Hinkelmann";
                       }
                }else{
                    //Name 3 teile
                    if(resultat[0].equalsIgnoreCase("Dr.")){
                        vorname = resultat[1];
                        nachname = resultat[2];
                    } else if(resultat[0].equalsIgnoreCase("M.")){
                        vorname = "Michael M.";
                        nachname = "Richter";
                    } else{
                        vorname = resultat[0];                     
                        nachname = resultat[1] + " " + resultat[2];
                    }
                }
               
               
               //Adresse
               String strassenname = vctData.rs.getString("Strassenname");
               String PLZ = vctData.rs.getString("PLZ");
               String stadt = vctData.rs.getString("Stadt");
               
               adresse = strassenname + ", " + PLZ + ", " + stadt;
               
               //Ländercode
               String land = vctData.rs.getString("Land");
               
               if(land.equalsIgnoreCase("Schweiz") || land.equalsIgnoreCase("Switzerland")){
                   laendercode = "CH";
               }
               else if(land.equalsIgnoreCase("Deutschland") || land.equalsIgnoreCase("Germany")){
                   laendercode = "DE";
               }
               else if(land.equalsIgnoreCase("The Netherlands")){
                   laendercode = "NL";
               }
                    
               Zielkunde zKunde = new Zielkunde(kid,vorname,nachname,adresse,laendercode,"");
               zielKunden.add(zKunde);
               
               //IBAN berechnen (gemäss Online Recherche)
               String ktnr = vctData.rs.getString("Kontonummer");
               String clr = vctData.rs.getString("Clearing");
               
               String IbanOhnePP =  clr + "000" + ktnr +"121700";
               double ibanPPBerechner = Double.parseDouble(IbanOhnePP);
               double ppDbl = (98 - ibanPPBerechner%97);
               int pp = (int) ppDbl;
               
               IBAN = "CH" + pp + clr + "000" + ktnr;
               
               //Kontostand
               kontostand = Double.parseDouble(vctData.rs.getString("Saldo"));
               
               
               //Kontoart
               String kundenart = vctData.rs.getString("Kundenart");
               if(kundenart.equalsIgnoreCase("Firma")){
                   kontoart = "Kontokorrent";
               }else{
                   kontoart = "Privatkonto";
               }
               
               // Zielkonto erstellen
               Zielkonto zKonto = new Zielkonto(kid, IBAN, kontostand, kontoart);
               zielKonten.add(zKonto);
               kid++;
            }
          } 
        } catch (SQLException ex) {
            Logger.getLogger(CollectData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
 
    }
    
    // JD Daten richtig migrieren
    private static void zielJDDatenSpeichern(){
        String vorname = null;
        String nachname= null;
        String adresse = null;
        String laendercode = null;
        String status = null;
        
        String IBAN = null;
        double kontostand = 0;
        String kontoart = null;
        
        int kundenIDKonto = 0;
    
        //Schlaufe welche für jeden Kunden mit einem SparKonto ein zielkunde erstellt und die Daten richtig migriert
        for(int i = 0; i < kundenSparkonto.size(); i++){
            
            vorname = "";
            nachname= "";
            adresse = "";
            laendercode = "";
            status = ""; 

            IBAN = "";
            kontostand = 0;
            kontoart = "";
            kundenIDKonto = 0;         
            boolean vorhanden = false;
            
            // Vorname und Nachname
            if(kundenSparkonto.get(i).Vorname.value.toString().equalsIgnoreCase("Bendel")){
                vorname = "Oliver";
                nachname = "Bendel";
            } else if(kundenSparkonto.get(i).Vorname.value.toString().equalsIgnoreCase("Knut")){
                vorname = "Knut";
                nachname = "Hinkelmann";
            } 
            
            else {
                vorname = kundenSparkonto.get(i).Vorname.value.toString();
                nachname = kundenSparkonto.get(i).Nachname.value.toString();
            }
            
            //Schleife welche schaut ob dieser Kunde mit einem Sparkonto schon mit diesem nachnamen existiert wenn ja setzt er vorhanden auf true
            for(int j = 0; j < zielKunden.size();j++){
                if(zielKunden.get(j).getNachname().equalsIgnoreCase(nachname)){
                    vorhanden = true;
                    System.out.println("VORHANDEN");
                }
            }
            //Kunde erstellen wenn er nicht vorhanden ist
            if(!vorhanden){
                //Adresse
                 String[] plzOrt = kundenSparkonto.get(i).PLZORT.value.toString().split(" ");         
                 String plz = plzOrt[0];
                 String ort = plzOrt[1];
                 adresse = kundenSparkonto.get(i).Strasse.value.toString() + ", " +plz + ", " + ort;
                 
                 //Laendercode wenn 4 stellig = CH wenn 5 stellig DE
                 if(plz.length()<5){
                     laendercode = "CH";
                 }else{
                     laendercode = "DE";
                 }

                 
                 //Kunden speichern
                 Zielkunde zKunde = new Zielkunde(kid,vorname,nachname,adresse,laendercode,"");
                 zielKunden.add(zKunde);
                 kid++;
            }
            
            /*Konto wird erstellt
            *Sparkonten haben nur eine Kontonummer, da es sich aber um die gleiche bank handelt
            *ist CH und pp (27) immer gleich. Nur die BLZ ändert sich bei Roger der bei einer anderen Filiale
            *seine Konten hat.
            */
            String kontonummer = kundenSparkonto.get(i).KontoNr.value.toString();

            
            //Also wenn Herr Mueller an der reihe ist wird eine andere BLZ genommen = 0901 (09010) da 5 stellig
            if(nachname.equalsIgnoreCase("Mueller")){
                IBAN = "CH27" + "09010" + "0000" + kontonummer;
            //Sonst die normale blz der filliale 2201 (22010) da 5 stellig
            }else{
                IBAN = "CH27" + "22010" + "0000" + kontonummer;
            }
            
            kontostand = Double.parseDouble(kundenSparkonto.get(i).Kontostand.value.toString());
            
            kontoart = "Sparkonto";
            
            //Schleife welche die ID des spezifischen Kunden nimmt nach nachnamen gesucht
            for(int j = 0; j < zielKunden.size();j++){
                if(zielKunden.get(j).Nachname.equalsIgnoreCase(nachname)){
                    kundenIDKonto = zielKunden.get(j).KID;
                    System.out.println(kundenIDKonto);
                }
                
            }
            
            Zielkonto zKonto = new Zielkonto(kundenIDKonto,IBAN,kontostand,kontoart);
            zielKonten.add(zKonto);
        }
        
        //Schleife welche schaut ob dieser Kunde mit einem Kontokorrentkonto schon mit diesem nachnamen existiert wenn ja setzt er vorhanden auf true
        for(int i = 0; i < kundenFirmakonto.size(); i++){
            
            vorname = "";
            nachname= "";
            adresse = "";
            laendercode = "";
            status = ""; 

            IBAN = "";
            kontostand = 0;
            kontoart = "";
            kundenIDKonto = 0;           
            boolean vorhanden = false;
            
            // Vorname und Nachname
            if(kundenFirmakonto.get(i).Vorname.value.toString().equalsIgnoreCase("Bendel")){
                vorname = "Oliver";
                nachname = "Bendel";
            } else {
                vorname = kundenFirmakonto.get(i).Vorname.value.toString();
                nachname = kundenFirmakonto.get(i).Nachname.value.toString();
            }
            
            //Schleife welche schaut ob dieser Kunde schon mit diesem vornamen existiert wenn ja setzt er vorhanden auf true
            for(int j = 0; j < zielKunden.size();j++){
                if(zielKunden.get(j).Nachname.equalsIgnoreCase(nachname)){
                    vorhanden = true;
                }
            }
            //Wenn der Kunde noch nicht vorhanden ist erstelle den Kunden
            if(!vorhanden){
                //Adresse 
                 String[] strasseUndPlzOrt = kundenFirmakonto.get(i).Adresse.value.toString().split(",");         
                 String[] strasseUndPlzUndOrt = strasseUndPlzOrt[1].split(" ");
                 
                 String strasse = strasseUndPlzOrt[0];
                 String plz = strasseUndPlzUndOrt[1];
                 String ort = strasseUndPlzUndOrt[2];
                 
                 adresse = strasse + ", " +plz + ", " + ort;

                 //Laendercode
                 laendercode = kundenFirmakonto.get(i).Land.value.toString();
                 
                 //Status
                 status = "";

                 Zielkunde zKunde = new Zielkunde(kid,vorname,nachname,adresse,laendercode,"");
                 zielKunden.add(zKunde);
                 kid++;
            }
            
            //Konto wird erstellt
            
            //IBAN
            IBAN = kundenFirmakonto.get(i).IBAN.value.toString();
            
            //Kontostand
            kontostand = Double.parseDouble(kundenFirmakonto.get(i).Kontostand.value.toString());
            
            
            //Kontoart
            kontoart = "Kontokorrent";
            
            //Schleife welche die ID des spezifischen Kunden nimmt nach nachnamen gesucht
            for(int j = 0; j < zielKunden.size();j++){
                if(zielKunden.get(j).Nachname.equalsIgnoreCase(nachname)){
                    kundenIDKonto = zielKunden.get(j).KID;
                    System.out.println(kundenIDKonto);
                
                }
                
            }
            
            Zielkonto zKonto = new Zielkonto(kundenIDKonto,IBAN,kontostand,kontoart);
            zielKonten.add(zKonto);
            
        }
        
    }
    
    //Methode welche Umlate umwandelt
    private static String ersetzeUmlaut(String input){
    String result =
        input
        .replaceAll("ü", "ue")
        .replaceAll("ö", "oe")
        .replaceAll("ä", "ae");
    
    return result;
    }
    
    // Setzt Kundenrank
    private static void setRank(){
        
        final int gold= 500000;
        final int silber= 100000;
        final int bronze= 0;
        
        double stand;
        
        for (int i=0; i<zielKunden.size(); i++){
            stand = 0;
         for(int j=0; j<zielKonten.size(); j++){
             if(zielKunden.get(i).KID==zielKonten.get(j).KID){
                 stand = stand + zielKonten.get(j).kontostand;
             }
             
         }
            
         if(stand >= gold){
             zielKunden.get(i).Status = "Gold";
         }else if(stand < gold && stand >= silber){
             zielKunden.get(i).Status = "Silber";
         }else if(stand < silber && stand >= bronze){
             zielKunden.get(i).Status = "Bronze";
         }
         
        }
        
    }
   
    
    //Methode welche die Kunden und die dazugehörigen Kontos ausgiebt
    private static void AllesAusgeben(){
        System.out.println("###############################################");
        System.out.println("Kundenkonten:");
        System.out.println("###############################################");
        //Forschleife für alle Kunden in der ArrayList zielKunden
        for(int i = 0; i < zielKunden.size(); i++){
            System.out.println("    ");
            System.out.println("Kunde:" +" "+ zielKunden.get(i).Vorname+" " + zielKunden.get(i).Nachname);
            System.out.println("    ");
            System.out.println("KID:"+"\t"+"\t"+ zielKunden.get(i).KID);
            System.out.println("Vorname:"+"\t"+ zielKunden.get(i).Vorname);
            System.out.println("Nachname:"+"\t"+ zielKunden.get(i).Nachname);
            System.out.println("Adresse:"+"\t"+ zielKunden.get(i).Adresse);
            System.out.println("Ländercode:"+"\t"+ zielKunden.get(i).Land);
            System.out.println("Status"+"\t"+"\t"+ zielKunden.get(i).Status);
            System.out.println("+++++++++++++++++++++++++++");
            System.out.println("Konten");
            System.out.println("+++++++++++++++++++++++++++");
            //Forschleife für alle Konten in der ArrayList zielKonten
            for(int j = 0; j <zielKonten.size(); j++){
                //Wenn das Konto die gleiche ID hat wie der Kunde dann gebe ihn auf der Konsole aus
                if(zielKonten.get(j).KID == zielKunden.get(i).KID){
                    System.out.println("KID:"+"\t"+"\t"+zielKonten.get(j).KID);
                    System.out.println("IBAN:"+"\t"+"\t"+ zielKonten.get(j).IBAN); 
                    System.out.println("Kontostand:"+"\t"+ macheCHF("" + zielKonten.get(j).kontostand));
                    System.out.println("Kontoart:"+"\t"+ zielKonten.get(j).kontoart);
                    System.out.println("+++++++++++++++++++++++++++");
                }
            }
        }
    
    }
    
    // Intigrationsmethode welche den Kontostandin Schweizerfranken umwandelt, Methode aus internetrecherche
    private static String macheCHF(String number){
        // Setze das Local objekt auf CH
        Locale locale = new Locale("de", "CH");
        NumberFormat nformat = DecimalFormat.getCurrencyInstance(locale);
        
        // Setze Währung
        Currency currency = Currency.getInstance("CHF");
        nformat.setCurrency(currency);
        
        // Formatiere die return Variable
        BigDecimal amount = new BigDecimal(number);
        String formatted = nformat.format(amount.doubleValue());

       return formatted; 
    }
    
    private static java.util.List<java.lang.String> listeKontokorrentNachname() {
        ch.fhnw.wi.eai.bankjd.BankJDService service = new ch.fhnw.wi.eai.bankjd.BankJDService();
        ch.fhnw.wi.eai.bankjd.BankJD port = service.getBankJDPort();
        return port.listeKontokorrentNachname();
    }

    private static java.util.List<java.lang.String> listeSparkontoNachname() {
        ch.fhnw.wi.eai.bankjd.BankJDService service = new ch.fhnw.wi.eai.bankjd.BankJDService();
        ch.fhnw.wi.eai.bankjd.BankJD port = service.getBankJDPort();
        return port.listeSparkontoNachname();
    }  

   
}
