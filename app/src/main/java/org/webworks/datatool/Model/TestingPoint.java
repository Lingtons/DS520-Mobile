package org.webworks.datatool.Model;

public class TestingPoint {
    private String testingPointCode, parentCode,testingPointName;

    public void setParentCode(String parentCode) { this.parentCode = parentCode; }

    public void setTestingPointCode(String testingPointCode) { this.testingPointCode = testingPointCode; }

    public void setTestingPointName(String testingPointName) { this.testingPointName = testingPointName; }

    public String getParentCode() { return parentCode; }

    public String getTestingPointCode() { return testingPointCode; }

    public String getTestingPointName() { return testingPointName; }
}
