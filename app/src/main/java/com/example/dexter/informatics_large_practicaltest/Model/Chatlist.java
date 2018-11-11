package com.example.dexter.informatics_large_practicaltest.Model;

import java.util.Date;

public class Chatlist {
    public String id;
    public Date time_stamp;

    public Chatlist(String id, Date time_stamp) {
        this.id = id;
        this.time_stamp = time_stamp;
    }

    public Chatlist() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(Date time_stamp) {
        this.time_stamp = time_stamp;
    }
}
