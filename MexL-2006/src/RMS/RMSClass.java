package RMS;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStoreNotOpenException;
import javax.microedition.rms.InvalidRecordIDException;
import MainPackage.Settings;
import javax.microedition.lcdui.Form;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStore;
import MainPackage.DTMFRequestSony;
import DatePackage.DateClass;
import java.util.Date;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class RMSClass {


    public RecordStore recStore = null;
    static final String REC_STORE = "Data_Store_attendant_145";
    private DateClass date;
    private DTMFRequestSony call;
    public String accessNumber, switchBoardNumber, smsServerNumber, setMounth,
    setDate, setYear, DBdate, DBmounth, DByear, DBdateBack, DBmounthBack,
    DByearBack, getTWO, dateString, setViewMounth, ViewDateString,
    setdayBack, setmounthBack, setyearBack;


    public RMSClass() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {



        try {
            this.accessNumber = getAccessNumber();
        } catch (RecordStoreNotOpenException ex1) {
        } catch (InvalidRecordIDException ex1) {
        } catch (RecordStoreException ex1) {
        }
        try {
            this.switchBoardNumber = getSwitchBoardNumber();
        } catch (RecordStoreNotOpenException ex2) {
        } catch (InvalidRecordIDException ex2) {
        } catch (RecordStoreException ex2) {
        }
        try {
            this.smsServerNumber = getSMSServerNumber();
        } catch (RecordStoreNotOpenException ex2) {
        } catch (InvalidRecordIDException ex2) {
        } catch (RecordStoreException ex2) {
        }
        try {
            date.setDBDate(); // OBS.. Det här metodanropet ska ligga här efter month och day.
        } catch (InvalidRecordIDException ex) {
        } catch (RecordStoreNotOpenException ex) {
        } catch (RecordStoreException ex) {
        }
        try {
            date.setDBDateBack();
        } catch (Exception ex3) {
        }


    }
    public void startRMS(){

        try {
            setDataStore();
        } catch (InvalidRecordIDException ex) {
        } catch (RecordStoreNotOpenException ex) {
        } catch (RecordStoreException ex) {
        }
        try {
            upDateDataStore();
        } catch (RecordStoreNotOpenException ex1) {
        } catch (InvalidRecordIDException ex1) {
        } catch (RecordStoreException ex1) {
        }

    }

    //------------ D A T A - B A S - R M S -----------------------------------------

    public Form getEditSettingForm() { // METODEN RETURNERAR FORMEN FÖR EDITSETTINGS I EGENSKAPER

        call.editSettingForm.deleteAll();
        openRecStore();
        call.accessNumbers.setString(accessNumber);
        call.editSettingForm.append(call.accessNumbers);
        call.editSwitchBoardNumber.setString(switchBoardNumber);
        call.editSettingForm.append(call.editSwitchBoardNumber);
        call.editSMSServerNumber.setString(smsServerNumber);
        call.editSettingForm.append(call.editSMSServerNumber);
        closeRecStore();

        return call.editSettingForm;
    }

    // --- SET-metoder ------



    public void setDateNumber() {

        try {
            recStore.setRecord(3, call.dateNumber.getString().getBytes(), 0,
                               call.dateNumber.getString().length());
        } catch (Exception e) {
            // ALERT
        }
    }

    public void setAccessNumber() {

        try {
            recStore.setRecord(4, call.accessNumbers.getString().getBytes(), 0,
                               call.accessNumbers.getString().length());
        } catch (Exception e) {
            // ALERT
        }
    }

    public void setSwitchBoardNumber() {
        try {
            recStore.setRecord(5,
                               call.editSwitchBoardNumber.getString().getBytes(),
                               0,
                               call.editSwitchBoardNumber.getString().length());
        } catch (Exception e) {
            // ALERT
        }
    }

    public void setSMSServerNumber() {
        try {
            recStore.setRecord(9, call.editSMSServerNumber.getString().getBytes(),
                               0,
                               call.editSMSServerNumber.getString().length());
        } catch (Exception e) {
            // ALERT
        }
    }

    public void setTWO() { // skiver in i första lediga plats i databasen.. tex. om db 1 - 9 är upptagna skriver metoden in på plats 10...
        try {

            openRecStore();
            String appt = "2";
            byte bytes[] = appt.getBytes();
            recStore.addRecord(bytes, 0, bytes.length);

            closeRecStore();
            upDateDataStore();
            call.startApp();

        } catch (Exception e) {
            // ALERT
        }
    }


    // ---- GET-metoder ---------

    public String getYear() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();

        byte a[] = recStore.getRecord(1);
        setYear = new String(a, 0, a.length);

        closeRecStore();

        return setYear;

    }

    public String getMounth() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();

        byte b[] = recStore.getRecord(2);
        setMounth = new String(b, 0, b.length);

        closeRecStore();

        return call.setMounth;

    }

    public String getDate() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();

        byte c[] = recStore.getRecord(3);
        setDate = new String(c, 0, c.length);

        closeRecStore();

        return call.setDate;

    }

    public String getAccessNumber() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();

        byte d[] = recStore.getRecord(4);
        accessNumber = new String(d, 0, d.length);

        closeRecStore();

        return accessNumber;

    }


    public String getSwitchBoardNumber() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();

        byte e[] = recStore.getRecord(5);
        switchBoardNumber = new String(e, 0, e.length);

        closeRecStore();

        return switchBoardNumber;

    }

    public String getThisYearBack() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();

        byte f[] = recStore.getRecord(6);
        setyearBack = new String(f, 0, f.length);

        closeRecStore();

        return setyearBack;

    }

    public String getThisMounthBack() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();

        byte g[] = recStore.getRecord(7);
        setmounthBack = new String(g, 0, g.length);

        closeRecStore();

        return setmounthBack;

    }

    public String getThisDayBack() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();

        byte h[] = recStore.getRecord(8);
        setdayBack = new String(h, 0, h.length);

        closeRecStore();

        return setdayBack;

    }


    public void getTWO() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();
        readRecords();
        readRecordsUpdate();

        try {
            byte i[] = recStore.getRecord(10);
            getTWO = new String(i, 0, i.length);
        } catch (InvalidRecordIDException ex) {
        } catch (RecordStoreNotOpenException ex) {
        } catch (RecordStoreException ex) {
        }

        try {
            this.dateString = getTWO;
        } catch (Exception ex1) {
        }

        System.out.println("häääääääääärrrrrrrr >>> getTWO >> " + call.getTWO);
        closeRecStore();

    }

    public String getSMSServerNumber() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();

        byte j[] = recStore.getRecord(9);
        smsServerNumber = new String(j, 0, j.length);

        closeRecStore();

        return smsServerNumber;

    }

    public void readRecordsUpdate() {
        try {
            System.out.println("Number of records: " + recStore.getNumRecords());

            if (recStore.getNumRecords() > 0) {
                RecordEnumeration re = recStore.enumerateRecords(null, null, false);
                while (re.hasNextElement()) {
                    String str = new String(re.nextRecord());
                    System.out.println("Record: " + str);
                }
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void readRecords() {
        try {
            // Intentionally small to test code below
            byte[] recData = new byte[5];
            int len;

            for (int i = 1; i <= recStore.getNumRecords(); i++) {
                // Allocate more storage if necessary
                if (recStore.getRecordSize(i) > recData.length) {
                    recData = new byte[recStore.getRecordSize(i)];
                }

                len = recStore.getRecord(i, recData, 0);
                if (Settings.debug) {
                    System.out.println("Record ID#" + i + ": " +
                                       new String(recData, 0, len));
                }
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void writeRecord(String str) {
        byte[] rec = str.getBytes();

        try {
            System.out.println("sparar ");
            recStore.addRecord(rec, 0, rec.length);
            System.out.println("Writing record: " + str);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }


    public void openRecStore() {
        try {
            System.out.println("Öppnar databasen");
            // The second parameter indicates that the record store
            // should be created if it does not exist
            recStore = RecordStore.openRecordStore(REC_STORE, true);

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void closeRecStore() {
        try {
            recStore.closeRecordStore();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void setDataStore() throws RecordStoreNotOpenException,
            InvalidRecordIDException, RecordStoreNotOpenException,
            RecordStoreException {

        openRecStore();
        readRecords();
        readRecordsUpdate();

        if (recStore.getNumRecords() == 0) { // om innehållet i databasen är '0' så spara de tre första elementen i databasen.

            writeRecord(setYear);
            writeRecord(setMounth);
            writeRecord(setDate);
            writeRecord("0");
            writeRecord("+46");
            writeRecord(setyearBack);
            writeRecord(setmounthBack);
            writeRecord(setdayBack);
            writeRecord("+45");

        }

        // sätter nummer i fönstret under inställningar...

        byte d[] = recStore.getRecord(4);
        accessNumber = new String(d, 0, d.length);

        byte e[] = recStore.getRecord(5);
        switchBoardNumber = new String(e, 0, e.length);

        byte j[] = recStore.getRecord(9);
        smsServerNumber = new String(j, 0, j.length);

        closeRecStore();
    }

    // Om något inputfönster(post) i databasen är tom sätt tillbaka värdet...
    public void upDateDataStore() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        openRecStore();
        String setBackUserDateRecord = setDate;
        String setBackAccessNumberRecord = accessNumber;
        String setBackSwitchBoardNumberRecord = switchBoardNumber;
        String setBackSmsServerNumberRecord = smsServerNumber;

        if (recStore.getRecord(1) == null && recStore.getRecord(4) == null &&
            recStore.getRecord(5) == null) {

            recStore.setRecord(1, setBackUserDateRecord.getBytes(), 0,
                               setBackUserDateRecord.length());
            recStore.setRecord(4, setBackAccessNumberRecord.getBytes(), 0,
                               setBackAccessNumberRecord.length());
            recStore.setRecord(5, setBackSwitchBoardNumberRecord.getBytes(), 0,
                               setBackSwitchBoardNumberRecord.length());
        } else if (recStore.getRecord(1) == null && recStore.getRecord(4) == null) {

            recStore.setRecord(1, setBackUserDateRecord.getBytes(), 0,
                               setBackUserDateRecord.length());
            recStore.setRecord(4, setBackAccessNumberRecord.getBytes(), 0,
                               setBackAccessNumberRecord.length());

        } else if (recStore.getRecord(4) == null && recStore.getRecord(5) == null) {

            recStore.setRecord(4, setBackAccessNumberRecord.getBytes(), 0,
                               setBackAccessNumberRecord.length());
            recStore.setRecord(5, setBackSwitchBoardNumberRecord.getBytes(), 0,
                               setBackSwitchBoardNumberRecord.length());
        } else if (recStore.getRecord(1) == null && recStore.getRecord(5) == null) {

            recStore.setRecord(1, setBackUserDateRecord.getBytes(), 0,
                               setBackUserDateRecord.length());
            recStore.setRecord(5, setBackSwitchBoardNumberRecord.getBytes(), 0,
                               setBackSwitchBoardNumberRecord.length());
        } else if (recStore.getRecord(1) == null) {

            recStore.setRecord(1, setBackUserDateRecord.getBytes(), 0,
                               setBackUserDateRecord.length());
        } else if (recStore.getRecord(4) == null) {

            recStore.setRecord(4, setBackAccessNumberRecord.getBytes(), 0,
                               setBackAccessNumberRecord.length());
        } else if (recStore.getRecord(5) == null) {

            recStore.setRecord(5, setBackSwitchBoardNumberRecord.getBytes(), 0,
                               setBackSwitchBoardNumberRecord.length());

        } else if (recStore.getRecord(9) == null) {

            recStore.setRecord(9, setBackSmsServerNumberRecord.getBytes(), 0,
                               setBackSmsServerNumberRecord.length());
        }

        closeRecStore();
    }

}
