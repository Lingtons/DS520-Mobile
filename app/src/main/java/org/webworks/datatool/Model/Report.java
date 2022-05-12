package org.webworks.datatool.Model;



public class Report {

    private int Id;
    private int Month;
    private int Year;
    private String Reports;
    private int Uploaded; //0=> not uploaded, 1=> uploaded
    private String Guid;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getMonth() {
        return Month;
    }

    public void setMonth(int month) {
        Month = month;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
    }

    public String getReports() {
        return Reports;
    }

    public void setReports(String reports) {
        Reports = reports;
    }

    public int getUploaded() {
        return Uploaded;
    }

    public void setUploaded(int uploaded) {
        Uploaded = uploaded;
    }

    public String getGuid() {
        return Guid;
    }

    public void setGuid(String guid) {
        Guid = guid;
    }
}
