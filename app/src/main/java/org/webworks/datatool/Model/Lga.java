package org.webworks.datatool.Model;

/**
 * Created by Johnbosco on 20/02/2017.
 */

public class Lga {
    private String lga_code;
    private String state_code;
    private String lga_name;

    public String getLga_name() {
        return lga_name;
    }

    public void setLga_name(String lga_name) {
        this.lga_name = lga_name;
    }

    public String getLga_code() {
        return lga_code;
    }

    public void setLga_code(String lga_code) {
        this.lga_code = lga_code;
    }

    public String getState_code() {
        return state_code;
    }

    public void setState_code(String state_code) {
        this.state_code = state_code;
    }
}
