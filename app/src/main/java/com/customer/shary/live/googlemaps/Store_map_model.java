package com.customer.shary.live.googlemaps;

public class Store_map_model {

    private String store_id;
    private String Store_name;
    private String Store_image;
    private String Store_description;
    private String latitude;
    private String longitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getStore_name() {
        return Store_name;
    }

    public void setStore_name(String store_name) {
        Store_name = store_name;
    }

    public String getStore_image() {
        return Store_image;
    }

    public void setStore_image(String store_image) {
        Store_image = store_image;
    }

    public String getStore_description() {
        return Store_description;
    }

    public void setStore_description(String store_description) {
        Store_description = store_description;
    }
}
