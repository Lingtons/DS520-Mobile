package org.webworks.datatool.Model;

import java.util.Date;

public class ClientForm {
    private int Id;
    private String ClientName;
    private String ClientLastname;
    private String ClientCode, ClientIdentifier;
    private Date CreateDate;
    private String EncodedImage;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getClientCode() {
        return ClientCode;
    }

    public void setClientCode(String clientCode) {
        ClientCode = clientCode;
    }

    public String getClientIdentifier() { return ClientIdentifier;}
    public void setClientIdentifier(String clientIdentifier) { ClientIdentifier = clientIdentifier;}

    public String getClientLastname() {
        return ClientLastname;
    }

    public void setClientLastname(String clientLastname) {
        ClientLastname = clientLastname;
    }

    public Date getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(Date createDate) {
        CreateDate = createDate;
    }

    public String getEncodedImage() {
        return EncodedImage;
    }

    public void setEncodedImage(String encodedImage) {
        EncodedImage = encodedImage;
    }
}
