package com.example.mainactivity;

public class MembersDataObject {

    String strID;
    String strName;
    String strAddress;
    String amountOfMoney;
    String strNID;

    public MembersDataObject()
    {

    }

    public MembersDataObject(String strID, String strName,String strAddress, String amountOfMoney,String strNID) {
        this.strID = strID;
        this.strName = strName;
        this.strAddress=strAddress;
        this.amountOfMoney=amountOfMoney;
        this.strNID=strNID;
    }

    public String getStrID() {
        return strID;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrID(String strID) {
        this.strID = strID;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }
}
