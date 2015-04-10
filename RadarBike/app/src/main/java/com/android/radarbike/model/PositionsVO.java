package com.android.radarbike.model;

/**
 * Created by vntalgo on 4/10/2015.
 */
public class PositionsVO {
    private double lat;
    private double lng;

    public PositionsVO(){

    }

    public PositionsVO(double lat, double lng){
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
