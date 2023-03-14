package DatePackage;

import java.util.Date;
import java.util.Calendar;
import javax.microedition.rms.RecordStoreNotOpenException;
import javax.microedition.rms.InvalidRecordIDException;
import java.io.IOException;
import javax.microedition.rms.RecordStoreException;
import RMS.RMSClass;
import MainPackage.DTMFRequestSony;

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
public class DateClass {


    private DTMFRequestSony call;
    private RMSClass rms;
    public DateClass() {
    }
    // ------------------- D A T U M -----------------------------------------------

    public void controllString() throws RecordStoreNotOpenException,
            InvalidRecordIDException, RecordStoreException {

        String readRecord;

        rms.getTWO();

        readRecord = call.dateString;

        String viewRecord = readRecord;

        try {
            if (viewRecord.equals("2")) {

                call.notifyDestroyed();
            }
        } catch (Exception ex) {
        }
        System.out.println("V�RDET PLATS 10 DB >> " + viewRecord);
    }

    public void controllDate() throws IOException, RecordStoreNotOpenException,
            InvalidRecordIDException, RecordStoreException {

        try {
            this.call.DBdate = rms.getDate();
        } catch (RecordStoreNotOpenException ex) {
        } catch (InvalidRecordIDException ex) {
        } catch (RecordStoreException ex) {
        }
        try {
            this.call.DBmounth = rms.getMounth();
        } catch (RecordStoreNotOpenException ex1) {
        } catch (InvalidRecordIDException ex1) {
        } catch (RecordStoreException ex1) {
        }
        try {
            this.call.DByear = rms.getYear();
        } catch (RecordStoreNotOpenException ex2) {
        } catch (InvalidRecordIDException ex2) {
        } catch (RecordStoreException ex2) {
        }
        try {
            this.call.DBdateBack = rms.getThisDayBack();
        } catch (RecordStoreNotOpenException ex3) {
        } catch (InvalidRecordIDException ex3) {
        } catch (RecordStoreException ex3) {
        }
        try {
            this.call.DBmounthBack = rms.getThisMounthBack();
        } catch (RecordStoreNotOpenException ex4) {
        } catch (InvalidRecordIDException ex4) {
        } catch (RecordStoreException ex4) {
        }
        try {
            this.call.DByearBack = rms.getThisYearBack();
        } catch (RecordStoreNotOpenException ex5) {
        } catch (InvalidRecordIDException ex5) {
        } catch (RecordStoreException ex5) {
        }

        String useDBdate = call.DBdate.trim();
        String useDBmounth = call.DBmounth.trim();
        String useDByear = call.DByear.trim();

        String useDBdateBack = call.DBdateBack.trim();
        String useDBmounthBack = call.DBmounthBack.trim();
        String useDByearBack = call.DByearBack.trim();

        System.out.println("Skriver ut datum om 30 dagar >>> " + useDBdate);
        System.out.println("Skriver ut m�nad om 30 dagar >>> " + useDBmounth);
        System.out.println("Skriver ut �ret om 30 dagar >>> " + useDByear);

        System.out.println("Skriver ut Kontroll datum >>> " + useDBdateBack);
        System.out.println("Skriver ut Kontroll m�nad >>> " + useDBmounthBack);
        System.out.println("Skriver ut Kontroll �r >>> " + useDByearBack);

        String toDayDate = checkDay().trim();
        String toDayMounth = checkMounth().trim();

        System.out.println("Skriver ut DAGENS DATUM >>> " + toDayDate);
        System.out.println("Skriver ut �RETS M�NAD >>> " + toDayMounth);

        Integer controllDBdateBack = new Integer(0); // G�r om str�ngar till integer
        Integer controllDBmonthBack = new Integer(0); // G�r om str�ngar till integer
        Integer controllDByearBack = new Integer(0); // G�r om str�ngar till integer

        int INTDBdateBack = controllDBdateBack.parseInt(useDBdateBack);
        int INTDBmounthBack = controllDBmonthBack.parseInt(call.DBmounthBack);
        int INTDByearBack = controllDByearBack.parseInt(call.DByearBack);

        Integer controllDBdate = new Integer(0); // G�r om str�ngar till integer
        Integer controllDBmonth = new Integer(0); // G�r om str�ngar till integer
        Integer controllDByear = new Integer(0); // G�r om str�ngar till integer

        Integer controllToDayDBdate = new Integer(0); // G�r om str�ngar till integer
        Integer controllToDayDBmounth = new Integer(0); // G�r om str�ngar till integer

        int INTDBdate = controllDBdate.parseInt(useDBdate);
        int INTDBmounth = controllDBmonth.parseInt(call.DBmounth);
        int INTDByear = controllDByear.parseInt(call.DByear);

        int INTdateToDay = controllToDayDBdate.parseInt(toDayDate);
        int INTmounthToDay = controllToDayDBmounth.parseInt(toDayMounth);

        if (INTDBdate <= INTdateToDay && INTDBmounth <= INTmounthToDay &&
            INTDByear == call.checkYear) {

            System.out.println("SANN SANN SANN SANN SANN ");

            rms.setTWO(); // Om m�nad och datum �r sann skriv in "2" i databasen plats 10...

        }
        if (INTmounthToDay == 0) { // Om INTmounthToDay har v�rdet '0' som �r januari

            INTDBmounthBack = 0; // D� inneh�ller installations-m�naden samma v�rde som nu-m�naden.

        }
        if (INTDBmounthBack > INTmounthToDay) { // Om installations-m�naden �r st�rre �n 'dagens' m�nad som �r satt i mobilen s� st�ng...

            rms.setTWO(); // Om m�nad och datum �r sann skriv in "2" i databasen plats 10...

        }
        if (INTDBmounthBack > INTmounthToDay && INTDByearBack < call.checkYear) { // Om installations-m�naden �r st�rre �n 'dagens' m�nad som �r satt i mobilen s� st�ng...

            rms.setTWO(); // Om m�nad och datum �r sann skriv in "2" i databasen plats 10...

        }
        if (INTDByearBack > call.checkYear) { // Om installations-�ret �r st�rre �n �ret som �r satt i mobilen. >> g�r bak�t i tiden...

            rms.setTWO(); // Om m�nad och datum �r sann skriv in "2" i databasen plats 10...

        }
        if (INTDBdateBack > INTdateToDay && INTDBmounthBack > INTmounthToDay &&
            INTDByearBack > call.checkYear) {

            rms.setTWO(); // Om m�nad och datum �r sann skriv in "2" i databasen plats 10...

        }
        if (INTDBmounthBack > INTmounthToDay && INTDByearBack > call.checkYear) {

            rms.setTWO(); // Om m�nad och datum �r sann skriv in "2" i databasen plats 10...

        }


    }


    public void setDBDate() throws RecordStoreNotOpenException,
            InvalidRecordIDException, RecordStoreException {

        countDay();

        System.out.println("Om 30 dagar �r det den >> " + call.dayAfter +
                           ", och m�nad >> " + call.monthAfter + " det �r �r >> " +
                           call.yearAfter);

        String convertDayAfter = Integer.toString(call.dayAfter); // konvertera int till string...
        String convertMounthAfter = Integer.toString(call.monthAfter);
        String convertYearAfter = Integer.toString(call.yearAfter);

        this.call.setDate = convertDayAfter;
        this.call.setMounth = convertMounthAfter;
        this.call.setYear = convertYearAfter;

    }

    public void setDBDateBack() {

        countThisDay();

        System.out.println("Kontrollerar dagens dautm >> " + call.dayBack +
                           ", och m�nad >> " + call.mounthBack + " det �r �r >> " +
                           call.yearBack);

        String convertDayBack = Integer.toString(call.dayBack); // konvertera int till string...
        String convertMounthBack = Integer.toString(call.mounthBack);
        String convertYearBack = Integer.toString(call.yearBack);

        this.call.setdayBack = convertDayBack;
        this.call.setmounthBack = convertMounthBack;
        this.call.setyearBack = convertYearBack;

    }

    public void countThisDay() {

        // Get today's day and month
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        System.out.println("Dagens datum �r den >> " + day +
                           ", �rets m�nad �r nummer >> " + month +
                           " det �r �r >> " + year);

        this.call.dayBack = day;
        this.call.mounthBack = month;
        this.call.yearBack = year;

    }

    public void countDay() {

        // Get today's day and month
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        System.out.println("Dagens datum �r den >> " + day +
                           ", �rets m�nad �r nummer >> " + month +
                           " det �r �r >> " + year);
        this.call.checkYear = year;

        // R�knar fram 30 dagar fram�t vilket datum �r osv...
        final long MILLIS_PER_DAY = 24 * 60 * 60 * 1000L;
        long offset = date.getTime();
        offset += call.antalDagar * MILLIS_PER_DAY;
        date.setTime(offset);
        cal.setTime(date);

        // Now get the adjusted date back
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        year = cal.get(Calendar.YEAR);

        this.call.dayAfter = day;
        this.call.monthAfter = month;
        this.call.yearAfter = year;

    }
    public String setViewDateString() throws InvalidRecordIDException,
            RecordStoreNotOpenException, RecordStoreException {

        //ViewDateString

        String e1 = rms.getDate();
        String e2 = setMounth();
        String e3 = rms.getYear();

        call.ViewDateString = e1 + " " + e2 + " " + e3;

        return call.ViewDateString;

    }

    public String setMounth() throws RecordStoreNotOpenException,
            InvalidRecordIDException, RecordStoreException {

        call.setViewMounth = rms.getMounth();

        if (call.setViewMounth.equals("0")) {

            this.call.setViewMounth = "Januari";
        }
        if (call.setViewMounth.equals("1")) {

            this.call.setViewMounth = "Februari";
        }
        if (call.setViewMounth.equals("2")) {

            this.call.setViewMounth = "Mars";
        }
        if (call.setViewMounth.equals("3")) {

            this.call.setViewMounth = "April";
        }
        if (call.setViewMounth.equals("4")) {

            this.call.setViewMounth = "Maj";
        }
        if (call.setViewMounth.equals("5")) {

            this.call.setViewMounth = "Juni";
        }
        if (call.setViewMounth.equals("6")) {

            this.call.setViewMounth = "Juli";
        }
        if (call.setViewMounth.equals("7")) {

            this.call.setViewMounth = "Augusti";
        }
        if (call.setViewMounth.equals("8")) {

            this.call.setViewMounth = "September";
        }
        if (call.setViewMounth.equals("9")) {

            this.call.setViewMounth = "Oktober";
        }
        if (call.setViewMounth.equals("10")) {

            this.call.setViewMounth = "November";
        }
        if (call.setViewMounth.equals("11")) {

            this.call.setViewMounth = "December";
        }

        String viewMounth = call.setViewMounth;

        return viewMounth;
    }

    public String checkDay() {

       String mobileClock = call.today.toString(); // Tilldelar mobileClock 'todays' datumv�rde, skickar och g�r om till en string av java.lang.string-typ

       String checkDayString = mobileClock.substring(8, 10); // plockar ut 'datum' tecken ur klockan

       if (checkDayString.equals("01")) {

           checkDayString = "1";

       } else if (checkDayString.equals("02")) {

           checkDayString = "2";

       } else if (checkDayString.equals("03")) {

           checkDayString = "3";

       } else if (checkDayString.equals("04")) {

           checkDayString = "4";

       } else if (checkDayString.equals("05")) {

           checkDayString = "5";

       } else if (checkDayString.equals("06")) {

           checkDayString = "6";

       } else if (checkDayString.equals("07")) {

           checkDayString = "7";

       } else if (checkDayString.equals("08")) {

           checkDayString = "8";

       } else if (checkDayString.equals("09")) {

           checkDayString = "9";

       }

       String useStringDate = checkDayString;

       return useStringDate;

   }

   public String checkMounth() {

       String mobileClock = call.today.toString(); // Tilldelar mobileClock 'todays' datumv�rde, skickar och g�r om till en string av java.lang.string-typ

       String checkMounthString = mobileClock.substring(4, 7); // plockar ut 'M�nad' tecken ur klockan

       if (checkMounthString.equals("Jan")) {

           checkMounthString = "0";

       } else if (checkMounthString.equals("Feb")) {

           checkMounthString = "1";

       } else if (checkMounthString.equals("Mar")) {

           checkMounthString = "2";

       } else if (checkMounthString.equals("Apr")) {

           checkMounthString = "3";

       } else if (checkMounthString.equals("May")) {

           checkMounthString = "4";

       } else if (checkMounthString.equals("Jun")) {

           checkMounthString = "5";

       } else if (checkMounthString.equals("Jul")) {

           checkMounthString = "6";

       } else if (checkMounthString.equals("Aug")) {

           checkMounthString = "7";

       } else if (checkMounthString.equals("Sep")) {

           checkMounthString = "8";

       } else if (checkMounthString.equals("Oct")) {

           checkMounthString = "9";

       } else if (checkMounthString.equals("Nov")) {

           checkMounthString = "10";

       } else if (checkMounthString.equals("Dec")) {

           checkMounthString = "11";

       }

       String useStringMounth = checkMounthString;

       return useStringMounth;

   }


}
