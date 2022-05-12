package org.webworks.datatool.Model;

public class Facility {
    int id, facilityId;
    String datimCode, facilityName, lgaCode;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getFacilityId() { return facilityId; }

    public void setFacilityId(int facilityId) { this.facilityId = facilityId; }

    public String getFacilityName() { return facilityName; }

    public void setFacilityName(String facilityName) { this.facilityName = facilityName; }

    public String getDatimCode() { return datimCode; }

    public void setDatimCode(String datimCode) { this.datimCode = datimCode; }

    public String getLgaCode() { return lgaCode; }

    public void setLgaCode(String lgaCode) { this.lgaCode = lgaCode; }
}
