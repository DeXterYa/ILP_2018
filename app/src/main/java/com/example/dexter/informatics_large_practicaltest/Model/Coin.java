package com.example.dexter.informatics_large_practicaltest.Model;

public class Coin {
    private String title;
    private String value;
    private int id;

    public Coin(String title, String value, int id) {
        this.title = title;
        this.value = value;
        this.id = id;
    }

    public Coin() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
