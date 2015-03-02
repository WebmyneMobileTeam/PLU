package com.webmyne.paylabas.userapp.model;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.webmyne.paylabas.userapp.base.PrefUtils;

import org.apache.commons.logging.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Android on 10-12-2014.
 */
public class LanguageStringUtil {
    static boolean isEnglisSelected;
    static String newvalue;
    static String cultureString;
    static String dateString;

    public static String languageString(Context contex,String value) {
        isEnglisSelected = PrefUtils.isEnglishSelected(contex);
        newvalue = value;

        if(isEnglisSelected) {
            // for france
            //ch = ",";
            newvalue = newvalue.replaceAll("\\.", ",");

        }
        else {
            // for english
            //ch = ".";
            newvalue = newvalue.replaceAll("\\,", ".");

        }

        return newvalue;
    }

    public static String CultureString(Context contex) {
        isEnglisSelected = PrefUtils.isEnglishSelected(contex);
        if(isEnglisSelected) {
            // for france
            //ch = ",";
            cultureString="fr-FR";
        }
        else {
            // for english
            //ch = ".";
            cultureString="en-US";

        }

        return cultureString;
    }

    public static String DateString(Context contex,String olddate) {
        isEnglisSelected = PrefUtils.isEnglishSelected(contex);

        SimpleDateFormat OriginalFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        SimpleDateFormat EnglishFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm a");

        if(isEnglisSelected) {
            // for france
            //ch = ",";
            dateString=olddate;
        }
        else {
            // for english
            //ch = ".";

            try {

                String reformattedStr = EnglishFormat.format(OriginalFormat.parse(olddate));
                dateString=reformattedStr;
            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println("exc"+e.toString());


            }

        }

        return dateString;
    }


}
