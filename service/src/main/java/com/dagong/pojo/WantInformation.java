package com.dagong.pojo;

public class WantInformation {
    private String id;

    private String userId;

    private String iKey;

    private String iValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getiKey() {
        return iKey;
    }

    public void setiKey(String iKey) {
        this.iKey = iKey;
    }

    public String getiValue() {
        return iValue;
    }

    public void setiValue(String iValue) {
        this.iValue = iValue;
    }
}