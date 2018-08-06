package com.android.camerademo.common;


import org.json.JSONObject;


/** Create Api class*/

public class API {

    /** Method to parse data to check the API result what it returns 0/1*/
    public static boolean success(JSONObject obj) {
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

    /** Method to parse data to get the result message*/
    public static String rMessage(JSONObject obj) {
        String result = "";
        try {
            result = obj.getString("message");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /** Method to get json data*/
    public static String getData(JSONObject obj) {
        String result = "";
        try {
            result = obj.getString("data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
