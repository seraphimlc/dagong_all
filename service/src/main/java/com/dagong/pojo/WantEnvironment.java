package com.dagong.pojo;

public class WantEnvironment {
    private String id;

    private String userId;

    private String envIds;

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

    public String getEnvIds() {
        return envIds;
    }

    public void setEnvIds(String envIds) {
        this.envIds = envIds == null ? null : envIds.trim();
    }
}