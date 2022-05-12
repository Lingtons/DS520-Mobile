package org.webworks.datatool.Model;

public class Contact {
    private String bio_data,
            RelationshipName, ClientId, TestDate, FinalTestResults,
            VisitStatus, locationInfo, nameInfo, ContactId,  CommentsAfterVisit;
    private int contactUploaded, contactTraced, server_id, TesterAssignStatus;

    public String getContactId() { return ContactId;  }
    public void setContactId(String ContactId) {    this.ContactId = ContactId;   }

    public String getBio_data() {
        return bio_data;
    }

    public void setBio_data(String bio_data) {
        this.bio_data = bio_data;
    }

    public String getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(String locationInfo) {
        this.locationInfo = locationInfo;
    }

    public String getNameInfo() {
        return nameInfo;
    }

    public void setNameInfo(String nameInfo) {
        this.nameInfo = nameInfo;
    }

    public String getRelationshipName() { return RelationshipName;  }
    public void setRelationshipName(String RelationshipName) {    this.RelationshipName = RelationshipName;   }

    public String getClientId() { return ClientId; }
    public void setClientId(String ClientId) { this.ClientId = ClientId; }

    public String getTestDate() { return TestDate; }
    public void setTestDate(String TestDate) { this.TestDate = TestDate; }


    public String getVisitStatus() { return VisitStatus; }
    public void setVisitStatus(String VisitStatus) { this.VisitStatus = VisitStatus; }

    public String getFinalTestResults() { return FinalTestResults; }
    public void setFinalTestResults(String FinalTestResults) { this.FinalTestResults = FinalTestResults; }

    public String getCommentsAfterVisit() { return CommentsAfterVisit; }
    public void setCommentsAfterVisit(String CommentsAfterVisit) { this.CommentsAfterVisit = CommentsAfterVisit; }

    public int getContactTraced() { return contactTraced; }
    public void setContactTraced(int contactTraced) { this.contactTraced = contactTraced; }

    public int getContactUploaded() { return contactUploaded; }
    public void setContactUploaded(int contactUploaded) { this.contactUploaded = contactUploaded; }

    public int getServer_id() { return server_id; }
    public void setServer_id(int server_id) { this.server_id = server_id; }

    public int getTesterAssignStatus() { return TesterAssignStatus; }

    public void setTesterAssignStatus(int testerAssignStatus) { TesterAssignStatus = testerAssignStatus;}
}
