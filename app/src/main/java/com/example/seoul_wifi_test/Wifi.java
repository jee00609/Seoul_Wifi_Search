package com.example.seoul_wifi_test;

public class Wifi {
    String place;
    float x;
    float y;
    String wifiname;

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getWifiname() {
        return wifiname;
    }

    public void setWifiname(String wifiname) {
        this.wifiname = wifiname;
    }

    @Override
    public String toString() {
        return "WifiList{" +
                "place='" + place + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", wifiname='" + wifiname + '\'' +
                '}';
    }
}
