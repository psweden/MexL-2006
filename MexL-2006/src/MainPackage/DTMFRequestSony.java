package MainPackage;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.RecordStoreNotOpenException;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.InvalidRecordIDException;
import java.util.Date;
import java.util.TimeZone;
import java.io.PrintStream;
import java.io.IOException;
import java.io.InputStream;
import Canvas.AboutUs;
import Canvas.HelpInfo;
import Canvas.ServerNumber;
import RMS.*;
import DatePackage.DateClass;

public class DTMFRequestSony extends MIDlet implements CommandListener,
        Runnable {

    private DateClass date;
    private RMSClass rms;
    private TextBox dialTextBox;
    public Form editSettingForm;

    private Command DialCommand, ExitCommand, AboutCommand, goToBackInfoCommand,
    minimazeCommand, helpCommand, editSettingBackCommand,
    editSettingCancelCommand,
    editSettingSaveCommand, propertiesCommand, backCommand, settingsCommand;

    private Command thCmd;
    private String stringTotal;
    private int type = 0;
    private String SOS;
    private String setP = ";postd="; //;postd= // /p
    private String accessCode, internNumber;
    private String identy, checkIdenty;
    private String sortString;
    private String[] subStr;
    public String accessNumber, switchBoardNumber, smsServerNumber, setMounth,
            setDate, setYear,
    DBdate, DBmounth, DByear, DBdateBack, DBmounthBack, DByearBack, getTWO,
    dateString, setViewMounth, ViewDateString, setdayBack,
    setmounthBack, setyearBack;
    private Alert alertEditSettings;

    public TextField dateNumber, accessNumbers, editSwitchBoardNumber,
            editSMSServerNumber;

    public int antalDagar;
    public int dayBack;
    public int mounthBack;
    public int yearBack;
    public int dayAfter;
    public int monthAfter;
    public int yearAfter;
    public int day;
    public int month;
    public int checkYear;

    public Date today;
    private TimeZone tz = TimeZone.getTimeZone("GMT+1");


    public DTMFRequestSony() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException, IOException {

        this.tz = tz;

        today = new Date();
        today.getTime();
        today.toString();

        System.out.println(today);

        this.antalDagar = 30; // anger hur många dagar programmet ska vara öppet innan det stängs....

        /*this.accessNumber = rms.getAccessNumber();
                 this.switchBoardNumber = rms.getSwitchBoardNumber();
                 this.smsServerNumber = rms.getSMSServerNumber();*/


        this.accessCode = accessCode;
        this.internNumber = internNumber;
        this.accessNumber = accessNumber;

        this.setP = setP;
        this.SOS = "112";
        this.identy = ""; // System.getProperty("com.sonyericsson.imei");
        this.checkIdenty = checkIdenty;

        this.day = day;
        this.month = month;

        //---------------- EDITSETTINGFORM ---------------------------------------------

        editSettingForm = new Form("Egenskaper");

        dateNumber = new TextField("dagensdatum: ", "", 32, TextField.ANY);
        accessNumbers = new TextField("Accessnummer: ", "", 32,
                                      TextField.NUMERIC);

        editSwitchBoardNumber = new TextField("Växelnummer: ", "", 32,
                                              TextField.PHONENUMBER);

        editSMSServerNumber = new TextField("SMS-Server: ", "", 32,
                                            TextField.PHONENUMBER);

        AboutCommand = new Command("Om MexB", Command.HELP, 5);
        helpCommand = new Command("Hjälp", Command.HELP, 4);

        editSettingBackCommand = new Command("Bakåt", Command.BACK, 1);
        editSettingCancelCommand = new Command("Avbryt", Command.BACK, 1);
        editSettingSaveCommand = new Command("Spara", Command.OK, 2);

        editSettingForm.addCommand(editSettingBackCommand);
        editSettingForm.addCommand(editSettingCancelCommand);
        editSettingForm.addCommand(editSettingSaveCommand);
        editSettingForm.setCommandListener(this);

        //--------------- Alert-Screen -----------------------------------------

        alertEditSettings = new Alert("Sparar Ändringar",
                                      "\n\n\n...Ändringar sparas... ",
                                      null, AlertType.CONFIRMATION);

        alertEditSettings.setTimeout(2000);

        dialTextBox = new TextBox("MexD Ver 1.0", "", 30, TextField.PHONENUMBER);

        DialCommand = new Command("Dial", Command.OK, 0);
        AboutCommand = new Command("Om MexD", Command.HELP, 1);

        goToBackInfoCommand = new Command("Bakåt", Command.BACK, 0);

        ExitCommand = new Command("Avsluta", Command.EXIT, 2);
        minimazeCommand = new Command("Minimera", Command.SCREEN, 3);
        settingsCommand = new Command("Inställningar", Command.SCREEN, 6);

        dialTextBox.addCommand(minimazeCommand);
        dialTextBox.addCommand(settingsCommand);
        dialTextBox.addCommand(ExitCommand);
        dialTextBox.addCommand(DialCommand);
        dialTextBox.setCommandListener(this);

        /* date.controllString();
              date.controllDate();
                 this.ViewDateString = date.setViewDateString();

         if(ViewDateString == null){

             this.ViewDateString = "Enterprise License";

         }*/


    }

    public String sortCharAt(String s) {

        this.sortString = identy; // sortString innehåller samma som för IMEI-strängen för att kunna kontrollera å sortera bort tecken....

        StringBuffer bTecken = new StringBuffer(sortString); // Lägg strängen sortString i ett stringbuffer objekt...

        for (int i = 0; i < bTecken.length(); i++) { // räkna upp hela bTecken-strängens innehåll hela dess längd

            char tecken = bTecken.charAt(i); // char tecken är innehållet i hela längden

            if ('A' <= tecken && tecken <= 'Z' ||
                'a' <= tecken && tecken <= 'z' // Sorterar ur tecken ur IMEI-strängen
                || tecken == '-' || tecken == '/' || tecken == '\\' ||
                tecken == ':' || tecken == ';'
                || tecken == '.' || tecken == ',' || tecken == '_' ||
                tecken == '|' || tecken == '<'
                || tecken == '>' || tecken == '+' || tecken == '(' ||
                tecken == ')') {

                bTecken.setCharAt(i, ' '); // lägg in blanksteg i IMEI-strängen där något av ovanstående tecken finns....
            }

        }

        bTecken.append(' '); // lägger till blanksteg sist i raden så att sista kommer med för att do-satsen ska kunna hitta och sortera...

        String setString = new String(bTecken); // Gör om char-strängen till en string-sträng

        int antal = 0;
        char separator = ' '; // för att kunna sortera i do-satsen

        int index = 0;

        do { // do satsen sorterar ut blankstegen och gör en ny sträng för att jämföra IMEI med...
            ++antal;
            ++index;

            index = setString.indexOf(separator, index);
        } while (index != -1);

        subStr = new String[antal];
        index = 0;
        int slutindex = 0;

        for (int j = 0; j < antal; j++) {

            slutindex = setString.indexOf(separator, index);

            if (slutindex == -1) {
                subStr[j] = setString.substring(index);
            }

            else {
                subStr[j] = setString.substring(index, slutindex);
            }

            index = slutindex + 1;

        }
        String setNumber = "";
        for (int k = 0; k < subStr.length; k++) {

            setNumber += subStr[k]; // Lägg in värdena från subStr[k] i strängen setNumber....
        }

        System.out.println("Sorterad: " + setNumber);

        System.out.println("" + identy);

        String sendIMEI = setNumber;

        return sendIMEI;
    }

    public String toString(String b) {

        String s = b;

        return s;
    }

    public void startApp() {



        Display.getDisplay(this).setCurrent(dialTextBox);
    }
    public void startRMs(){

        try {
            this.rms.setDataStore();
        } catch (InvalidRecordIDException ex) {
        } catch (RecordStoreNotOpenException ex) {
        } catch (RecordStoreException ex) {
        }
        try {
            this.rms.upDateDataStore();
        } catch (RecordStoreNotOpenException ex1) {
        } catch (InvalidRecordIDException ex1) {
        } catch (RecordStoreException ex1) {
        }
        try {
            this.date.controllString();
        } catch (InvalidRecordIDException ex2) {
        } catch (RecordStoreNotOpenException ex2) {
        } catch (RecordStoreException ex2) {
        }


    }
    public void pauseApp() {

    }

    public void destroyApp(boolean unconditional) {

    }

    public void checkCountryNumber() { // Justerar landsiffra som är inmatad! Tar bort '+' och lägger in '00' före landssiffran

        String larmNummer = "112";
        String Number = "+";
        String setNumber = "00";
        String validate = dialTextBox.getString();
        String validate46 = "46";
        String setNumberNoll = "0";

        if (Number.equals(validate.substring(0, 1)) &&
            validate46.equals(validate.substring(1, 3))) { // Om numret startar med '+' OCH '46' är sann så gör om till '0'

            accessCode = accessNumber;

            System.out.println("+46 är SANN gör om till 0 ");

            String setString = dialTextBox.getString();

            String deletePartOfString = setString.substring(3); // ta bort plast 0 - 1 ur strängen....

            String setStringTotal = setNumberNoll + deletePartOfString; // sätt ihop strängen setStringTotal

            stringTotal = setStringTotal;

            this.stringTotal = stringTotal;

            System.out.println("Landsnummer är : " + stringTotal);

        }
        if (Number.equals(validate.substring(0, 1)) &&
            !validate46.equals(validate.substring(1, 3))) { // Om numret startar med '+' OCH 46 är falsk så gör om till '00'

            accessCode = accessNumber;

            System.out.println("Andra landsnummer tex +47 blir 00 SANN");

            String setString = dialTextBox.getString();

            String deletePartOfString = setString.substring(1); // ta bort plast 0 - 1 ur strängen....

            String setStringTotal = setNumber + deletePartOfString; // sätt ihop strängen setStringTotal

            stringTotal = setStringTotal;

            this.stringTotal = stringTotal;

            System.out.println("Landsnummer: " + stringTotal);

        }
        if (!Number.equals(validate.substring(0, 1))) { // ring vanligt nummer

            accessCode = accessNumber;

            this.stringTotal = dialTextBox.getString();

            System.out.println("Telefonnummer: " + stringTotal);

        }
        if (validate.equals(validate.substring(0, 1)) ||
            validate.equals(validate.substring(0, 2)) ||
            validate.equals(validate.substring(0, 3)) ||
            validate.equals(validate.substring(0, 4))) {

            accessCode = "";

            this.stringTotal = dialTextBox.getString();

            System.out.println("Internnummer: " + stringTotal);
        }
    }

    public void commandAction(Command c, Displayable d) { // SÄTTER COMMAND-ACTION STARTAR TRÄDETS KOMMANDON (trådar)
        Thread th = new Thread(this);
        thCmd = c;
        th.start();
    }

    public void run() {
        try {
            if (thCmd.getCommandType() == Command.EXIT) {
                notifyDestroyed();
            } else if (thCmd == AboutCommand) { // Kommandot 'Om Tv-Moble' hör till huvudfönstret listan

                backCommand = new Command("Bakåt", Command.OK, 2);

                Displayable k = new AboutUs();
                Display.getDisplay(this).setCurrent(k);
                k.addCommand(backCommand);
                k.setCommandListener(this);

            } else if (thCmd == helpCommand) { // Kommandot 'Om Tv-Moble' hör till huvudfönstret listan

                backCommand = new Command("Bakåt", Command.OK, 2);

                Displayable k = new HelpInfo();
                Display.getDisplay(this).setCurrent(k);
                k.addCommand(backCommand);
                k.setCommandListener(this);

            } else if (thCmd == propertiesCommand) { // Kommandot 'Redigera' hör till setting-Form

                Display.getDisplay(this).setCurrent(rms.getEditSettingForm());

            } else if (thCmd == settingsCommand) { // Kommandot 'Om Tv-Moble' hör till huvudfönstret listan

                propertiesCommand = new Command("Egenskaper", Command.OK, 3);
                rms.setDataStore();
                rms.upDateDataStore();

                Displayable k = new ServerNumber(switchBoardNumber, /*, IMEI,
                                                 star,*/
                                                 accessNumber, ViewDateString);
                Display.getDisplay(this).setCurrent(k);
                k.addCommand(propertiesCommand);
                k.addCommand(goToBackInfoCommand);
                k.addCommand(AboutCommand);
                k.addCommand(helpCommand);
                k.setCommandListener(this);

            } else if (thCmd == editSettingBackCommand) { // Kommandot 'Tillbaka' hör till editSetting-Form

                propertiesCommand = new Command("Egenskaper", Command.OK, 3);
                rms.setDataStore();
                rms.upDateDataStore();

                Displayable k = new ServerNumber(switchBoardNumber, /*, IMEI,
                                                 star,*/
                                                 accessNumber, ViewDateString);
                Display.getDisplay(this).setCurrent(k);
                k.addCommand(propertiesCommand);
                k.addCommand(goToBackInfoCommand);
                k.addCommand(AboutCommand);
                k.addCommand(helpCommand);
                k.setCommandListener(this);

            } else if (thCmd == backCommand) { // Kommandot 'Tillbaka' hör till about-formen

                propertiesCommand = new Command("Egenskaper", Command.OK, 3);
                rms.setDataStore();
                rms.upDateDataStore();

                Displayable k = new ServerNumber(switchBoardNumber, /*, IMEI,
                                                 star,*/
                                                 accessNumber, ViewDateString);
                Display.getDisplay(this).setCurrent(k);
                k.addCommand(propertiesCommand);
                k.addCommand(goToBackInfoCommand);
                k.addCommand(AboutCommand);
                k.addCommand(helpCommand);
                k.setCommandListener(this);

            } else if (thCmd == editSettingCancelCommand) { // Kommandot 'Logga Ut' hör till setting-Form

                Display.getDisplay(this).setCurrent(dialTextBox);

            } else if (thCmd == editSettingSaveCommand) { // Kommandot 'Spara' hör till editSetting-Form

                rms.openRecStore();
                rms.setAccessNumber();
                rms.setSwitchBoardNumber();
                rms.setSMSServerNumber();
                rms.closeRecStore();
                rms.upDateDataStore();
                startApp();

                Display.getDisplay(this).setCurrent(alertEditSettings,
                        dialTextBox);

            } else if (thCmd == minimazeCommand) {

                Display.getDisplay(this).setCurrent(null);

            } else if (thCmd.getCommandType() == Command.BACK) { // Kommandot 'Tillbaka' hör till about-formen

                Display.getDisplay(this).setCurrent(dialTextBox);
            } // "tel:+46735708606/p9" >> p910/p990/m600i/N70/N80 // tel:+46851492163;postd=9 >> k700,k750,v600,s700,w810

            else if (thCmd == DialCommand) {
                if (type == 0) {
                    try {
                        checkCountryNumber();
                        if (SOS.equals(stringTotal)) {
                            platformRequest("tel:" + stringTotal.trim());
                        } else {
                            platformRequest("tel:" + switchBoardNumber + setP +
                                            accessCode + stringTotal.trim()); // dial the number > DTMF-signals.
                        }

                        dialTextBox.setString("");
                    } catch (Exception e) {
                    }
                } else {
                    try {
                        platformRequest(dialTextBox.getString()); // open the wap browser.
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception ex) {
        }
    }


    private String regFromTextFile() { // Läser textfilen tmp.txt
        InputStream is = getClass().getResourceAsStream("tmp.txt");
        try {
            StringBuffer sb = new StringBuffer();
            int chr, i = 0;
            // Read until the end of the stream
            while ((chr = is.read()) != -1) {
                sb.append((char) chr);
            }

            return sb.toString();
        } catch (Exception e) {
            System.out.println("Unable to create stream");
        }
        return null;
    }

    /**
     * DTMFRequestSony
     */


}
