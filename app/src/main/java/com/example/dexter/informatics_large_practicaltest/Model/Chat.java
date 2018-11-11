package com.example.dexter.informatics_large_practicaltest.Model;

import java.sql.Timestamp;
import java.util.Date;

public class Chat {

    private String sender;
    private String receiver;
    private String message;
    public Date time_stamp;
    private boolean isseen;


    public Chat(String sender, String receiver, String message, Date time_stamp, boolean isseen) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.time_stamp = time_stamp;
        this.isseen = isseen;
    }

    public Chat() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(Date time_stamp) {
        this.time_stamp = time_stamp;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }
}
