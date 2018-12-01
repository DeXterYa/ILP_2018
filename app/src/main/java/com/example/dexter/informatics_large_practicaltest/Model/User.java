package com.example.dexter.informatics_large_practicaltest.Model;

public class User implements Cloneable{
    private String id;
    private String username;
    private String imageURL;
    private String status;
    private String search;
    private Double SHIL;
    private Double DOLR;
    private Double QUID;
    private Double PENY;
    private Double GOLD;
    private Double level;


    public User(String id, String username, String imageURL, String status, String search,
                Double SHIL, Double DOLR, Double QUID, Double PENY, Double GOLD, Double level) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.status = status;
        this.search = search;
        this.SHIL = SHIL;
        this.DOLR = DOLR;
        this.QUID = QUID;
        this.PENY = PENY;
        this.GOLD = GOLD;
        this.level = level;

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

    public Double getSHIL() {
        return SHIL;
    }

    public void setSHIL(Double SHIL) {
        this.SHIL = SHIL;
    }

    public Double getDOLR() {
        return DOLR;
    }

    public void setDOLR(Double DOLR) {
        this.DOLR = DOLR;
    }

    public Double getQUID() {
        return QUID;
    }

    public void setQUID(Double QUID) {
        this.QUID = QUID;
    }

    public Double getPENY() {
        return PENY;
    }

    public void setPENY(Double PENY) {
        this.PENY = PENY;
    }

    public Double getGOLD() {
        return GOLD;
    }

    public void setGOLD(Double GOLD) {
        this.GOLD = GOLD;
    }

    public Double getLevel() {
        return level;
    }

    public void setLevel(Double level) {
        this.level = level;
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
