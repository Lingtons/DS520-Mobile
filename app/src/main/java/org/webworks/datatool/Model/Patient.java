package org.webworks.datatool.Model;

/**
 * Created by Arinze on 2/8/2018.
 */

public class Patient {
    private String ClientName;
    private String ClientLastname;
    private String ClientAddress;
    private String ClientPhone;
    private String Age;
    private String Dob;
    private String EstimatedDob;
    private int Sex;

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getClientLastname() {
        return ClientLastname;
    }

    public void setClientLastname(String clientLastname) {
        ClientLastname = clientLastname;
    }


    public String getClientAddress() {
        return ClientAddress;
    }

    public void setClientAddress(String clientAddress) {
        ClientAddress = clientAddress;
    }

    public String getClientPhone() {
        return ClientPhone;
    }

    public void setClientPhone(String clientPhone) {
        ClientPhone = clientPhone;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getDob() {
        return Dob;
    }

    public void setDob(String dob) {
        Dob = dob;
    }

    public String getEstimatedDob() {
        return EstimatedDob;
    }

    public void setEstimatedDob(String estimatedDob) {
        EstimatedDob = estimatedDob;
    }

    public int getSex() {
        return Sex;
    }

    public void setSex(int sex) {
        Sex = sex;
    }
}
