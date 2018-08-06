package com.android.camerademo.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Create class Utils to check the internet permissions
 */

public class Utils {
    public static int CAMERA__CROP_REQUEST;

    /** Method to check the internet is connected or not*/
    /** true for connected and false for not connected*/
    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        /**  connected to the internet*/
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                /** connected to wifi*/
                return true;

            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                /** connected to the mobile provider's data plan*/
                return true;

            }
        } else {
            /** not connected to the internet*/
        }
        return false;
    }

    public static void showShortToast(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

/** Method to parse data to check the API result what it returns 0/1.*/
    public static boolean rCode(JSONObject obj) {
        boolean result = false;
        try {
            if (obj.getInt("code") == 1) {
                result = true;
            } else {
                result = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
  /** Method to parse data to get the resulted message*/
    public static String rMessage(JSONObject obj) {
        String result = "";
        try {
            result = obj.getString("message");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
   /**  Method to clear shared preference*/
    public static void clearData(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit();
    }

    /**  All the preferences saved are either String or boolean.Store String preferences here*/
    public static void storeUserPreferences(Context context, String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.commit();
    }

    /** All the preferences saved are either String or boolean.Store boolean preferences here*/
    public static void storeUserPreferencesBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /** All the preferences saved are either String or boolean.Get String preferences here*/
    public static String getUserPreferences(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String string = prefs.getString(key, null);
        return string;
    }

    /** All the preferences saved are either String or boolean.Get boolean preferences here*/
    public static boolean getUserPreferencesBoolean(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean bool = prefs.getBoolean(key, false);
        return bool;
    }

    /** Method to get the difference from given data till current date*/
    public static int dateDifferences(String date)
    {
        int result=0;
        String inputPattern = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(inputPattern);

        try {
            Date inDate = format.parse(date);
            Date currentDate = new Date();
            long diff =  inDate.getTime() - currentDate.getTime();
            result = (int) (diff / (1000 * 60 * 60 * 24));

            System.out.println(inDate+"A"+result);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    return result;
    }


    /** Method to change the date to given time format*/
    /** from yyyy-MM-dd HH:mm:ss to MMMM dd'st', EEEE eg. Nov 1st, Monday.*/

    public static String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);

            SimpleDateFormat format = new SimpleDateFormat("d");

            String date1 = format.format(date);

            if (date1.endsWith("1") && !date1.endsWith("11"))
                format = new SimpleDateFormat("MMMM dd'st', EEEE");
            else if (date1.endsWith("2") && !date1.endsWith("12"))
                format = new SimpleDateFormat("MMMM dd'nd', EEEE");
            else if (date1.endsWith("3") && !date1.endsWith("13"))
                format = new SimpleDateFormat("MMMM dd'rd', EEEE");
            else
                format = new SimpleDateFormat("MMMM dd'th', EEEE");
            str = format.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    /** to parse the date from full date to half date*/
    /** From yyyy-MM-dd HH:mm:ss to dd-MM-yyyy*/

    public static String parseDateforFees(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MM-yyyy";
        String date1="";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);

            SimpleDateFormat format = new SimpleDateFormat(outputPattern);

            date1 = format.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    /** To compare two dates for date difference*/
    public static boolean matchDate(String onedate, String twodate){

        boolean result=false;
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date one= null;
        Date two = null;

        try{
            one = inputFormat.parse(onedate);
            two = inputFormat.parse(twodate);

            String date1 = format.format(one);
            String date2 = format.format(two);

            if(date1.equalsIgnoreCase(date2))
            {
                result= true;
            }
            else
                {
                    result= false;
            }
        }catch (Exception e){e.printStackTrace();}

        return  result;
    }
}
