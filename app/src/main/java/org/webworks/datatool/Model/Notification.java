package org.webworks.datatool.Model;

import java.util.Date;

public class Notification {
    private int id, apiId, viewed, viewCount, actionType, synced;
    private String title, userId, deviceId, message;
    private String createdAt,  updatedAt;
    private Date viewedAt;

    public int getId() { return id; }

    public void setId(int id) { this.id = id;}

    public int getApiId() { return apiId; }

    public void setApiId(int apiId) { this.apiId = apiId; }

    public int getViewed() { return viewed; }

    public void setViewed(int viewed) { this.viewed = viewed; }

    public int getViewCount() { return viewCount; }

    public void setViewCount(int viewCount) { this.viewCount = viewCount; }

    public int getActionType() { return actionType; }

    public void setActionType(int actionType) { this.actionType = actionType; }

    public int getSynced() { return synced; }

    public void setSynced(int synced) { this.synced = synced; }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getDeviceId() { return deviceId; }

    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public String getCreatedAt() { return createdAt; }

    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public Date getViewedAt() { return viewedAt; }

    public void setViewedAt(Date viewedAt) { this.viewedAt = viewedAt; }

    public String getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public void setTitle(String title) { this.title = title; }

    public String getTitle() { return title; }
}
