package com.example.dexter.informatics_large_practicaltest;

public class Users_profile {
    private String username, email, password;

    public Users_profile() {

    }

    public Users_profile(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
