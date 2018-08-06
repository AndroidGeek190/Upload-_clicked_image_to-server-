package com.android.camerademo.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.snapit.up.Model.User;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Class to upload images of add item class
 */
public class AddImageAPI extends AsyncTask<File,Integer, String> {
    private URL connectURL;
    private String response,jResponse;
    Context c;
    DataOutputStream dos=null;
    User user;
    File imgFile;
    ImagesAddedListener ial;
    Map<String, String> params;
    ProgressDialog loader;
    public AddImageAPI(Context mContext, File imgFile, Map<String, String> params, ImagesAddedListener ial) {

        this.c = mContext;
        this.ial=ial;
        this.imgFile=imgFile;
        this.params=params;
        loader=new ProgressDialog(c);
        loader.setMessage("Loading..");
        loader.setCancelable(false);
        loader.show();
        Gson gson = new Gson();
        String json = Utils.getUserPreferences(c, Global.USER);
        user = gson.fromJson(json, User.class);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try
        {
            connectURL = new URL(Global.BASE_URL+Global.ADD_PROPERTY_DETAILS);
        }
        catch (Exception ex)
        {
            Log.i("URL FORMATION", "MALFORMATED URL");
        }
    }
    /** Async task to do task in background for quick processing*/

    @Override
    protected String doInBackground(File... param) {
        FileInputStream fileInputStream = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String Tag = "3rd";
        String existingfilename="";

        try {

            HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            /*
            ** Sending parameters to server
            * @params user_security_hash
            * @params no_of_bedroom
            * @params no_of_bathroom
            * @params gargage
            * @params swimming_pool
            * @params landscaped_garden
            * @params property_latitude
            * @params property_longitude
            * @params property_range_from
            * @params property_range_to
            * @params location_name
            * @params user_id
            * */

            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"user_security_hash\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(params.get("user_security_hash") + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"no_of_bedroom\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(params.get("no_of_bedroom") + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"no_of_bathroom\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(params.get("no_of_bathroom") + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"gargage\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(params.get("gargage") + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"swimming_pool\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(params.get("swimming_pool") + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"landscaped_garden\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(params.get("landscaped_garden") + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"property_latitude\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(params.get("property_latitude") + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"property_longitude\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(params.get("property_longitude") + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"property_range_from\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(params.get("property_range_from") + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"property_range_to\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(params.get("property_range_to") + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"location_name\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(params.get("location_name") + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"user_id\"" + lineEnd);
            dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(params.get("user_id") + lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
                fileInputStream = new FileInputStream(imgFile);
                existingfilename = imgFile.getName();
                dos.writeBytes("Content-Disposition: form-data; name=\"property_image\"; filename=\"" + existingfilename + "\"" + lineEnd);

                if (existingfilename.endsWith(".jpg"))
                {
                    dos.writeBytes("Content-type: image/jpg;" + lineEnd);
                }

                if (existingfilename.endsWith(".png"))
                {
                    dos.writeBytes("Content-type: image/png;" + lineEnd);
                }

                if (existingfilename.endsWith(".gif"))
                {
                    dos.writeBytes("Content-type: image/gif;" + lineEnd);
                }

                if (existingfilename.endsWith(".jpeg"))
                {
                    dos.writeBytes("Content-type: image/jpeg;" + lineEnd);
                }

                dos.writeBytes(lineEnd);
                int bytesAvailable = fileInputStream.available();
                int maxBufferSize = 8024;
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                byte[] buffer = new byte[bufferSize];

                int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0)
                {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                fileInputStream.close();

            InputStream is = conn.getInputStream();

            int ch;

            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1)
            {
                b.append((char) ch);
            }
            jResponse = b.toString();

            response = "true";
            dos.close();

            dos.flush();
            Log.d("Pic Upload ", jResponse);
        }

       catch (MalformedURLException ex)
       {
            Log.e(Tag, "error: " + ex.getMessage(), ex);
        }

        catch (IOException ioe) {
            Log.e(Tag, "error: " + ioe.getMessage(), ioe);
        }
        return jResponse;
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        ial.onImageAdded(s);
        if(c!=null){
            if(loader.isShowing()){
                loader.dismiss();
            }
        }
    }
}