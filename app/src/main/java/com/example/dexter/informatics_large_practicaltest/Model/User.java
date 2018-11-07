package com.example.dexter.informatics_large_practicaltest.Model;

public class User implements Cloneable{
    private String id;
    private String username;
    private String imageURL;
    private String status;
    private String search;

    public User(String id, String username, String imageURL, String status, String search) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.status = status;
        this.search = search;
    }

    public User() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {

        User u = new User();
        u.setId(this.id);
        u.setUsername(this.username);
        u.setImageURL(this.imageURL);
        u.setStatus(this.status);

        return u;
    }
}
