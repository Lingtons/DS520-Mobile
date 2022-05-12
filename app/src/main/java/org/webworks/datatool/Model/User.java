package org.webworks.datatool.Model;


public class User {

    private String email;
    private String username;
    private int id;
    private String guid;
    private String password;
    private String facility, state, lga;
    private int sessionEnded;//1=> ended, 0=> not ended

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public int getSessionEnded() {
        return sessionEnded;
    }

    public void setSessionEnded(int sessionEnded) {
        this.sessionEnded = sessionEnded;
    }

    public String getState() { return state; }

    public void setState(String state) { this.state = state; }

    public void setLga(String lga) { this.lga = lga; }

    public String getLga() { return lga; }
}
