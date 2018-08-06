package com.android.camerademo.Model;


/** Create HomeScreen Modal class*/

public class HomeScreen {
    public int property_id;
    public int   property_user_id;
    public String location_name;
    public String property_latitude;
    public String property_longitude;
    public String address;
    public String property_image_link;
    public int   no_of_bedroom;
    public int   no_of_bathroom;
    public int  gargage;
    public Double property_range_from;
    public Double property_range_to;
    public int   swimming_pool;
    public int   landscaped_garden;
    public String created_at;
    public int    property_status;

    public double getProperty_range_from() {
        return property_range_from;
    }
    public String getProperty_image_url() {
        return property_image_link;
    }

    public void setProperty_image_url(String property_image_url) {
        this.property_image_link = property_image_url;
    }

    public int getProperty_id() {
        return property_id;
    }

    public void setProperty_id(int property_id) {
        this.property_id = property_id;
    }

    public int getProperty_user_id() {
        return property_user_id;
    }

    public void setProperty_user_id(int property_user_id) {
        this.property_user_id = property_user_id;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getProperty_latitude() {
        return property_latitude;
    }

    public void setProperty_latitude(String property_latitude) {
        this.property_latitude = property_latitude;
    }

    public String getProperty_longitude() {
        return property_longitude;
    }

    public void setProperty_longitude(String property_longitude) {
        this.property_longitude = property_longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNo_of_bedroom() {
        return no_of_bedroom;
    }

    public void setNo_of_bedroom(int no_of_bedroom) {
        this.no_of_bedroom = no_of_bedroom;
    }

    public int getNo_of_bathroom() {
        return no_of_bathroom;
    }

    public void setNo_of_bathroom(int no_of_bathroom) {
        this.no_of_bathroom = no_of_bathroom;
    }

    public int getGargage() {
        return gargage;
    }

    public void setGargage(int gargage) {
        this.gargage = gargage;
    }

    public int getSwimming_pool() {
        return swimming_pool;
    }

    public void setSwimming_pool(int swimming_pool) {
        this.swimming_pool = swimming_pool;
    }

    public int getLandscaped_garden() {
        return landscaped_garden;
    }

    public void setLandscaped_garden(int landscaped_garden) {
        this.landscaped_garden = landscaped_garden;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getProperty_status() {
        return property_status;
    }

    public void setProperty_status(int property_status) {
        this.property_status = property_status;
    }

}
