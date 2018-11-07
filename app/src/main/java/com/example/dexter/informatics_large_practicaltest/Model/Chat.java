package com.example.dexter.informatics_large_practicaltest.Model;

public class Chat {

    private String sender;
    private String receiver;
    private String message;
    private String time_stamp;

    public Chat(String sender, String receiver, String message, String time_stamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.time_stamp = time_stamp;
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

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }
}
