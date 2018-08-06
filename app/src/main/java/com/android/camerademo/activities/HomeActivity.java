package com.android.camerademo.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.camerademo.Model.HomeScreen;
import com.android.camerademo.Model.User;
import com.android.camerademo.R;
import com.android.camerademo.adapter.HomeAdapter;
import com.android.camerademo.common.API;
import com.android.camerademo.common.AppController;
import com.android.camerademo.common.Global;
import com.android.camerademo.common.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import static com.android.camerademo.common.Global.BASE_URL;
import static com.android.camerademo.common.Global.HOME_SCREEN;


/** Create a Class Home activity display product list  */
    public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recycler_view;
    Toolbar toolbar;
    SwipeRefreshLayout swipeLayout;
    ArrayList<HomeScreen> arrayList;
    EditText actv_search;
    FloatingActionButton fab_camera_button;
    ImageView retry_connection;
    Button internet_connection;

    /** Runtime permission Code*/
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;

    String[] permissionsRequired = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /** Method call below */
        init();

        setSupportActionBar(toolbar);
        init();

        /** if statement check the internet connection and else statement display the error message  */
        if (Utils.isConnectingToInternet(HomeActivity.this)){
            hit_home_property_api("");
            retry_connection.setVisibility(View.GONE);
            internet_connection.setVisibility(View.GONE);

        }
       else {
             retry_connection.setVisibility(View.VISIBLE);
             internet_connection.setVisibility(View.VISIBLE);
        }
    }

/** initialize method */
    private void init()
    {
        internet_connection=findViewById(R.id.internet_connection);
        retry_connection=findViewById(R.id.retry_connection);
        recycler_view = findViewById(R.id.recycler_view);
        fab_camera_button = findViewById(R.id.fab_camera_button);
        fab_camera_button.setBackgroundColor(Color.TRANSPARENT);
        actv_search = findViewById(R.id.actv_search);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        swipeLayout=findViewById(R.id.swipe_layout);
        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);
        swipeLayout.setEnabled(false);
        fab_camera_button.setOnClickListener(this);
        internet_connection.setOnClickListener(this);
        arrayList = new ArrayList();

        /** Using grid layout*/
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recycler_view.setLayoutManager(manager);

        actv_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                hit_home_property_api(charSequence+"");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

     /** Hit home property API*/
    private void hit_home_property_api(String keyword) {

      /** Getting the user data from user model*/
        Gson gson = new Gson();
        String json = Utils.getUserPreferences(HomeActivity.this, Global.USER);
        User obj = gson.fromJson(json, User.class);
        swipeLayout.setRefreshing(true);

        /** if statement check the internet connection and else statement display the error message  */
        if (Utils.isConnectingToInternet(HomeActivity.this)){
            swipeLayout.setRefreshing(true);
        }
        else {
            Utils.showShortToast(HomeActivity.this,"Internet not connected");
        }

        /** Using home screen api GET method used*/
        String url = BASE_URL + HOME_SCREEN+"/"+obj.user_id+"/"+obj.user_security_hash+"/"+keyword;
        Log.i("url",""+url);
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    /** Api response action*/
                    @Override
                    public void onResponse(String response) {
                        swipeLayout.setRefreshing(false);
                        try
                        {
                            JSONObject obj = new JSONObject(response);
                            /** Receiving response from api here*/
                            // if available then show the list and refresh the list if internet is working if not then show the list and show internet error.
                            if (Utils.rCode(obj))
                            {
                                Utils.storeUserPreferences(HomeActivity.this, Global.RESPONSE_HOMESCREEN, obj.getString("data"));

                                /** Json parsing*/
                                arrayList=new Gson().fromJson(API.getData(obj),new TypeToken<List<HomeScreen>>() {}.getType());

                                /** Using home adapter to save  the array list*/
                                HomeAdapter adapter = new HomeAdapter(HomeActivity.this, arrayList);

                                recycler_view.setAdapter(adapter);
                                if (arrayList.isEmpty())
                                {
                                    Utils.showShortToast(HomeActivity.this,"No Result Found");
                                }
                            }else {
                            }
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            /** method when no response from internet*/
            @Override
            public void onErrorResponse(VolleyError error) {
                /** Error log*/
                VolleyLog.d("", "Error: " + error.getMessage());
                swipeLayout.setRefreshing(true);
            }
        });
        /** Adding request to request queue*/
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

   /** Functionality Open camera button*/
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_camera_button:

                /** Call method below*/
                setClick();
                break;
            case R.id.internet_connection:

                /** if statement check the internet connection and else statement display the error message  */
                if (Utils.isConnectingToInternet(HomeActivity.this)){
                hit_home_property_api("");
                recycler_view.setVisibility(View.VISIBLE);
                retry_connection.setVisibility(View.GONE);
                internet_connection.setVisibility(View.GONE);
                }
                else {
                    Utils.showShortToast(HomeActivity.this,"Internet not connected");
                    retry_connection.setVisibility(View.VISIBLE);
                    internet_connection.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
    /** This method use  check the permissions*/
    private void setClick()
    {
        /** check for the permission of location to get the location of driver.*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(HomeActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(HomeActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(HomeActivity.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(HomeActivity.this, permissionsRequired[3]) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,permissionsRequired[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,permissionsRequired[1])
                        || ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,permissionsRequired[2])
                        || ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,permissionsRequired[3])){

                    /** Show Information about why you need the permission*/
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setTitle("Snapitup ");
                    builder.setMessage("We needs Camera and Location permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            /** Alert dialog box is canceled */
                            dialog.cancel();
                            ActivityCompat.requestPermissions(HomeActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            /** Alert dialog box is canceled*/
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else if (permissionStatus.getBoolean(permissionsRequired[0],false)) {

                    /** Previously Permission Request was cancelled with 'Dont Ask Again',*/
                    /**Redirect to Settings after showing Information about why you need the permission */
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setTitle("Snapitup");
                    builder.setMessage("we needs Camera and Location permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            /** Alert dialog box is canceled*/
                            dialog.cancel();
                            sentToSettings = true;
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                            Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera and Location", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            /** Alert dialog box is canceled*/
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }  else {
                    /** just request the permission*/
                    ActivityCompat.requestPermissions(HomeActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                }

                prermissionNotGranted();

                /** Edited the data in shared preferences*/
                SharedPreferences.Editor editor = permissionStatus.edit();
                editor.putBoolean(permissionsRequired[0],true);
                editor.commit();
            } else {
                /** You already have the permission, just go ahead */
                proceedAfterPermission();
            }
        } else{
            proceedAfterPermission();
            /** do something for phones running an SDK before lollipop*/
        }
    }

/** Check if all permissions are granted*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CALLBACK_CONSTANT)
        {
            /** check if all permissions are granted */
            boolean allgranted = false;
            for(int i=0;i<grantResults.length;i++)
            {
                if(grantResults[i]== PackageManager.PERMISSION_GRANTED)
                {
                    allgranted = true;
                } else
                {
                    allgranted = false;
                    break;
                }
            }
            if(allgranted)
            {
                proceedAfterPermission();
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,permissionsRequired[3])){
                prermissionNotGranted();
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Snapitup");
                builder.setMessage("We needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        /** Alert dialog box is canceled*/
                        dialog.cancel();
                        ActivityCompat.requestPermissions(HomeActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        /** Alert dialog box is canceled*/
                        dialog.cancel();
                    }
                });

                builder.show();
            }
    }}

   /** Check the permission settings*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(HomeActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {

                proceedAfterPermission();
            }
        }
        if(requestCode==2) {
            actv_search.setText("");

            /** if statement check the internet connection and else statement display the error message  */
            if (Utils.isConnectingToInternet(HomeActivity.this)){
                hit_home_property_api("");
            }
            else {
                Utils.showShortToast(HomeActivity.this,"Internet not connected");
            }
        }
        }

    /** Sent to the permissions settings*/
    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings)
        {
            if (ActivityCompat.checkSelfPermission(HomeActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                proceedAfterPermission();
            }
        }
    }

    /** After proceed the permission go to the next activity*/
    private void proceedAfterPermission()
    {
        /** if statement check the internet connection and else statement display the error message  */
        if(Utils.isConnectingToInternet(HomeActivity.this)){

            startActivityForResult(new Intent(HomeActivity.this,NeedActivity.class),2);

        }else {
            Utils.showShortToast(HomeActivity.this,"Internet not connected");
        }
    }

    /** Display the permissions you can not accept one permissions error message will be display "All permissions are required "*/
    private void prermissionNotGranted(){
        Utils.showShortToast(HomeActivity.this,"All permissions required");
    }

  /** Click on Back press method*/
    public void onBackPressed(){
        super.onBackPressed();

        /** Finish activity*/
        finish();
        finishAffinity();
    }
}
