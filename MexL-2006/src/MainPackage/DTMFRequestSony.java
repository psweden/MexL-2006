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

        this.antalDagar = 30; // anger hur m�nga dagar programmet ska vara �ppet innan det st�ngs....

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

        editSwitchBoardNumber = new TextField("V�xelnummer: ", "", 32,
                                              TextField.PHONENUMBER);

        editSMSServerNumber = new TextField("SMS-Server: ", "", 32,
                                            TextField.PHONENUMBER);

        AboutCommand = new Command("Om MexB", Command.HELP, 5);
        helpCommand = new Command("Hj�lp", Command.HELP, 4);

        editSettingBackCommand = new Command("Bak�t", Command.BACK, 1);
        editSettingCancelCommand = new Command("Avbryt", Command.BACK, 1);
        editSettingSaveCommand = new Command("Spara", Command.OK, 2);

        editSettingForm.addCommand(editSettingBackCommand);
        editSettingForm.addCommand(editSettingCancelCommand);
        editSettingForm.addCommand(editSettingSaveCommand);
        editSettingForm.setCommandListener(this);

        //--------------- Alert-Screen -----------------------------------------

        alertEditSettings = new Alert("Sparar �ndringar",
                                      "\n\n\n...�ndringar sparas... ",
                                      null, AlertType.CONFIRMATION);

        alertEditSettings.setTimeout(2000);

        dialTextBox = new TextBox("MexD Ver 1.0", "", 30, TextField.PHONENUMBER);

        DialCommand = new Command("Dial", Command.OK, 0);
        AboutCommand = new Command("Om MexD", Command.HELP, 1);

        goToBackInfoCommand = new Command("Bak�t", Command.BACK, 0);

        ExitCommand = new Command("Avsluta", Command.EXIT, 2);
        minimazeCommand = new Command("Minimera", Command.SCREEN, 3);
        settingsCommand = new Command("Inst�llningar", Command.SCREEN, 6);

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

        this.sortString = identy; // sortString inneh�ller samma som f�r IMEI-str�ngen f�r att kunna kontrollera � sortera bort tecken....

        StringBuffer bTecken = new StringBuffer(sortString); // L�gg str�ngen sortString i ett stringbuffer objekt...

        for (int i = 0; i < bTecken.length(); i++) { // r�kna upp hela bTecken-str�ngens inneh�ll hela dess l�ngd

            char tecken = bTecken.charAt(i); // char tecken �r inneh�llet i hela l�ngden

            if ('A' <= tecken && tecken <= 'Z' ||
                'a' <= tecken && tecken <= 'z' // Sorterar ur tecken ur IMEI-str�ngen
                || tecken == '-' || tecken == '/' || tecken == '\\' ||
                tecken == ':' || tecken == ';'
                || tecken == '.' || tecken == ',' || tecken == '_' ||
                tecken == '|' || tecken == '<'
                || tecken == '>' || tecken == '+' || tecken == '(' ||
                tecken == ')') {

                bTecken.setCharAt(i, ' '); // l�gg in blanksteg i IMEI-str�ngen d�r n�got av ovanst�ende tecken finns....
            }

        }

        bTecken.append(' '); // l�gger till blanksteg sist i raden s� att sista kommer med f�r att do-satsen ska kunna hitta och sortera...

        String setString = new String(bTecken); // G�r om char-str�ngen till en string-str�ng

        int antal = 0;
        char separator = ' '; // f�r att kunna sortera i do-satsen

        int index = 0;

        do { // do satsen sorterar ut blankstegen och g�r en ny str�ng f�r att j�mf�ra IMEI med...
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

            setNumber += subStr[k]; // L�gg in v�rdena fr�n subStr[k] i str�ngen setNumber....
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

    public void checkCountryNumber() { // Justerar landsiffra som �r inmatad! Tar bort '+' och l�gger in '00' f�re landssiffran

        String larmNummer = "112";
        String Number = "+";
        String setNumber = "00";
        String validate = dialTextBox.getString();
        String validate46 = "46";
        String setNumberNoll = "0";

        if (Number.equals(validate.substring(0, 1)) &&
            validate46.equals(validate.substring(1, 3))) { // Om numret startar med '+' OCH '46' �r sann s� g�r om till '0'

            accessCode = accessNumber;

            System.out.println("+46 �r SANN g�r om till 0 ");

            String setString = dialTextBox.getString();

            String deletePartOfString = setString.substring(3); // ta bort plast 0 - 1 ur str�ngen....

            String setStringTotal = setNumberNoll + deletePartOfString; // s�tt ihop str�ngen setStringTotal

            stringTotal = setStringTotal;

            this.stringTotal = stringTotal;

            System.out.println("Landsnummer �r : " + stringTotal);

        }
        if (Number.equals(validate.substring(0, 1)) &&
            !validate46.equals(validate.substring(1, 3))) { // Om numret startar med '+' OCH 46 �r falsk s� g�r om till '00'

            accessCode = accessNumber;

            System.out.println("Andra landsnummer tex +47 blir 00 SANN");

            String setString = dialTextBox.getString();

            String deletePartOfString = setString.substring(1); // ta bort plast 0 - 1 ur str�ngen....

            String setStringTotal = setNumber + deletePartOfString; // s�tt ihop str�ngen setStringTotal

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

    public void commandAction(Command c, Displayable d) { // S�TTER COMMAND-ACTION STARTAR TR�DETS KOMMANDON (tr�dar)
        Thread th = new Thread(this);
        thCmd = c;
        th.start();
    }

    public void run() {
        try {
            if (thCmd.getCommandType() == Command.EXIT) {
                notifyDestroyed();
            } else if (thCmd == AboutCommand) { // Kommandot 'Om Tv-Moble' h�r till huvudf�nstret listan

                backCommand = new Command("Bak�t", Command.OK, 2);

                Displayable k = new AboutUs();
                Display.getDisplay(this).setCurrent(k);
                k.addCommand(backCommand);
                k.setCommandListener(this);

            } else if (thCmd == helpCommand) { // Kommandot 'Om Tv-Moble' h�r till huvudf�nstret listan

                backCommand = new Command("Bak�t", Command.OK, 2);

                Displayable k = new HelpInfo();
                Display.getDisplay(this).setCurrent(k);
                k.addCommand(backCommand);
                k.setCommandListener(this);

            } else if (thCmd == propertiesCommand) { // Kommandot 'Redigera' h�r till setting-Form

                Display.getDisplay(this).setCurrent(rms.getEditSettingForm());

            } else if (thCmd == settingsCommand) { // Kommandot 'Om Tv-Moble' h�r till huvudf�nstret listan

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

            } else if (thCmd == editSettingBackCommand) { // Kommandot 'Tillbaka' h�r till editSetting-Form

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

            } else if (thCmd == backCommand) { // Kommandot 'Tillbaka' h�r till about-formen

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

            } else if (thCmd == editSettingCancelCommand) { // Kommandot 'Logga Ut' h�r till setting-Form

                Display.getDisplay(this).setCurrent(dialTextBox);

            } else if (thCmd == editSettingSaveCommand) { // Kommandot 'Spara' h�r till editSetting-Form

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

            } else if (thCmd.getCommandType() == Command.BACK) { // Kommandot 'Tillbaka' h�r till about-formen

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


    private String regFromTextFile() { // L�ser textfilen tmp.txt
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
