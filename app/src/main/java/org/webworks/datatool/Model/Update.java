package org.webworks.datatool.Model;

public class Update {

    private int updateCode;
    private String updateVersion;
    private String updateDate;
    private String lastCheck; //date that shows when the app checked for update lasr

    public int getUpdateCode() {
        return updateCode;
    }

    public void setUpdateCode(int updateCode) {
        this.updateCode = updateCode;
    }

    public String getUpdateVersion() {
        return updateVersion;
    }

    public void setUpdateVersion(String updateVersion) {
        this.updateVersion = updateVersion;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getLastCheck() {
        return lastCheck;
    }

    public void setLastCheck(String lastCheck) {
        this.lastCheck = lastCheck;
    }
}
