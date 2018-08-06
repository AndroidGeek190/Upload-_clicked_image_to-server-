package com.android.camerademo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.android.camerademo.Model.HomeScreen;
import com.android.camerademo.R;
import com.android.camerademo.activities.ViewActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import java.util.ArrayList;


/**
 * Create a home adapter for display images
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {
    ArrayList<HomeScreen> list;
    Context c;

    public HomeAdapter(Context c, ArrayList<HomeScreen> list){
        this.c=c;
        this.list=list;
    }
    @Override
    public int getItemCount()
    {
        return list.size();
    }

    /** Using glide set the image,location and using click button go to the next activity*/
    @Override
    public void onBindViewHolder(HomeHolder contactViewHolder,final int i) {

        contactViewHolder.title.setText(list.get(i).location_name);
        Glide.with(c).load(list.get(i).property_image_link).placeholder(R.drawable.loading).into(contactViewHolder.image);
        contactViewHolder.cardClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in =new Intent(c, ViewActivity.class);
                in.putExtra("prop",new Gson().toJson(list.get(i)));
                c.startActivity(in);
            }
        });
    }

    @Override
    public HomeHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_home_layout, viewGroup, false);
        return new HomeHolder(itemView);
    }

    /** Recycler view holder to show the items */
    public class HomeHolder extends RecyclerView.ViewHolder {
        AppCompatTextView title;
        ImageView image;
        CardView cardClick;
        public HomeHolder(View v) {
            super(v);
            title = v.findViewById(R.id.adapter_property_name);
            image = v.findViewById(R.id.preview_img);
            cardClick=v.findViewById(R.id.adapter_card_click);
        }
    }
}

