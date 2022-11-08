package com.example.i_explore;

public class Upload {
    public String getJsonPayload() {
        return jsonpayload;
    }

    public void setJsonPayload(String jsonPayload) {
        this.jsonpayload = jsonPayload;
    }

    private String jsonpayload;


    private String  uploadResponseCode;
    private String  userid;
    private String  number;

    public String getUploadResponseCode() {
        return uploadResponseCode;
    }

    public void setUploadResponseCode(String uploadResponseCode) {
        this.uploadResponseCode = uploadResponseCode;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String  names;
    private String  message;
}
