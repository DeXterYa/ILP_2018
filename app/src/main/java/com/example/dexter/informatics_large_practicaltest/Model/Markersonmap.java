package com.example.dexter.informatics_large_practicaltest.Model;

public class Markersonmap {

    private String color;
    private String currency;
    private String id;
    private Double latitude;
    private Double longitude;
    private String symbol;
    private String value;
    private int isCollected_1;
    private int isStored;


    public Markersonmap(String color, String currency, String id,  Double latitude, Double longitude, String symbol, String value, int isCollected_1, int isStored) {
        this.color = color;
        this.currency = currency;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.symbol = symbol;
        this.value = value;
        this.isCollected_1 = isCollected_1;
        this.isStored = isStored;
    }

    public Markersonmap() {
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public int getIsCollected_1() {
        return isCollected_1;
    }

    public void setIsCollected_1(int isCollected_1) {
        this.isCollected_1 = isCollected_1;
    }

    public int getIsStored() {
        return isStored;
    }

    public void setIsStored(int isStored) {
        this.isStored = isStored;
    }
}
