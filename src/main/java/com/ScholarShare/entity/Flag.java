package com.ScholarShare.entity;

import java.sql.Timestamp;

// map to flags table -- plagiarism report
public class Flag {
    private int flagId;
    private int resourceId;
    private int flaggedBy;
    private String reason;
    private String status;  // open, upheld, dismiss
    private Timestamp createdAt;

    // joined fields
    private String resourceTitle;
    private String flaggedByName;

    public Flag() {}

    public int getFlagId() {
        return flagId;
    }

    public void setFlagId(int flagId) {
        this.flagId = flagId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getFlaggedBy() {
        return flaggedBy;
    }

    public void setFlaggedBy(int flaggedBy) {
        this.flaggedBy = flaggedBy;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getResourceTitle() {
        return resourceTitle;
    }

    public void setResourceTitle(String resourceTitle) {
        this.resourceTitle = resourceTitle;
    }

    public String getFlaggedByName() {
        return flaggedByName;
    }

    public void setFlaggedByName(String flaggedByName) {
        this.flaggedByName = flaggedByName;
    }
    public boolean isOpen(){
        return "open".equals(this.status);
    }
    public boolean isUpheld(){
        return "upheld".equals(this.status);
    }
    public boolean idDismissed(){
        return "dismissed".equals(this.status);
    }
}
