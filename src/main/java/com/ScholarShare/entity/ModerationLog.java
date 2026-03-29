package com.ScholarShare.entity;

import java.sql.Timestamp;
// map to moderation_logs -- store the action by admin
public class ModerationLog {
    private int logId;
    private int resourceId;
    private int adminId;
    private String action ;
    private String note;
    private Timestamp actionAt;
    private String adminName;
    private String resourceTitle;

    public ModerationLog() {}

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Timestamp getActionAt() {
        return actionAt;
    }

    public void setActionAt(Timestamp actionAt) {
        this.actionAt = actionAt;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getResourceTitle() {
        return resourceTitle;
    }

    public void setResourceTitle(String resourceTitle) {
        this.resourceTitle = resourceTitle;
    }
}
