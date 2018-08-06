package com.android.camerademo.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.camerademo.Model.HomeScreen;
import com.android.camerademo.Model.User;
import com.android.camerademo.R;
import com.android.camerademo.common.AppController;
import com.android.camerademo.common.Global;
import com.android.camerademo.common.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.text.NumberFormat;
import static com.android.camerademo.common.Global.BASE_URL;
import static com.android.camerademo.common.Global.Delete_Property;


/** Create preview activity for property details*/
public class ViewActivity extends AppCompatActivity {
    ImageView get_image;
    TextView location,bathcount,roomcount,garagecount,switch_garden,switch_swim_pool,minimum_range;
    Toolbar toolbar;
    HomeScreen propertyData;
    Button button_delete;
    ProgressDialog pd;
    String cancel_change_api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        /** Getting the response previous activity */
        if (getIntent() != null) {
            if (getIntent().getExtras().getString("prop") != null) {
                Log.e("AA", getIntent().getExtras().getString("prop"));
                propertyData = new Gson().fromJson(getIntent().getExtras().getString("prop"), HomeScreen.class);
            }
        }
        Int();
    }

    /** initialize method */
    private void Int() {
        button_delete=findViewById(R.id.button_delete);
        get_image = findViewById(R.id.get_image);
        location = findViewById(R.id.location);
        bathcount = findViewById(R.id.pdetails_bath_no);
        roomcount = findViewById(R.id.pdetails_bed_no);
        garagecount = findViewById(R.id.pdetails_garages_no);
        switch_garden = findViewById(R.id.pdetails_garden_check);
        switch_swim_pool = findViewById(R.id.pdetails_pool_check);
        minimum_range = findViewById(R.id.price_range);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isConnectingToInternet(ViewActivity.this)){

                }
                else {
                    Utils.showShortToast(ViewActivity.this,"Internet not connected");
                }
            }
        });

        /** toolbar*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        /** Call method below*/
        setData();
    }

    /** using delete api to delete the property details*/
    private void delete_Api() {
            /** Created progress dialog box */
            pd = new ProgressDialog(ViewActivity.this);
            pd.setMessage("Loading...");
            /** Using this line touch outside the screen,dialog box is not canceled */
            pd.setCanceledOnTouchOutside(false);
            pd.show();

            /** this gson is used for user model class to get the user id and user security hash*/
        Gson gson = new Gson();
        String json = Utils.getUserPreferences(ViewActivity.this, Global.USER);
        User obj = gson.fromJson(json, User.class);

        /** this gson is used for home screen model class to get the property id */
        Gson gson1 = new Gson();
        String json1 = Utils.getUserPreferences(ViewActivity.this, Global.USER);
        HomeScreen obj1 = gson1.fromJson(json1, HomeScreen.class);
        String url = BASE_URL + Delete_Property+"/87"+"/"+obj.user_security_hash+"/"+propertyData.property_id;
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try
                            {
                                JSONObject obj = new JSONObject(response);
                           /** if else function is used to pass the intent one screen to another screen*/
                                if (Utils.rCode(obj))
                                {
                                    Intent intent=new Intent(ViewActivity.this,HomeActivity.class);
                                    startActivity(intent);
                                }else
                                {
                                    Intent intent=new Intent(ViewActivity.this,ViewActivity.class);
                                    startActivity(intent);

                                }
                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                            /** Progress dialog box dismiss*/
                            pd.dismiss();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    /** Error log*/
                    VolleyLog.d("", "Error: " + error.getMessage());
                    pd.dismiss();
                }
            }) {
        };
            /** Adding request to request queue*/
            AppController.getInstance().addToRequestQueue(jsonObjReq, cancel_change_api);
        }


    /** Set the Text functionality*/
    private void setData(){
        Glide.with(this).load(propertyData.property_image_link).placeholder(R.drawable.loading).into(get_image);
        location.setText(propertyData.location_name);
        bathcount.setText(propertyData.no_of_bathroom+"");
        roomcount.setText(propertyData.no_of_bedroom+"");
        garagecount.setText(propertyData.gargage+"");

        /** Set the minimum and maximum price*/
        String min = NumberFormat.getIntegerInstance().format(propertyData.property_range_from);
        String max = NumberFormat.getIntegerInstance().format(propertyData.property_range_to);
        minimum_range.setText(min+" - "+max+"");

        /** Using if else functionality for switch button*/
        if(propertyData.swimming_pool==1){
            switch_swim_pool.setText("Y");
        }
        else{
            switch_swim_pool.setText("N");
        }
        if(propertyData.landscaped_garden==1){
            switch_garden.setText("Y");
        }
        else{
            switch_garden.setText("N");
        }
    }


  /** Method on Click back button*/
    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, null);

        /** Finish activity */
        finish();
    }
}
