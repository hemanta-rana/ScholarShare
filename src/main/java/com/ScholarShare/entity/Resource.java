package com.ScholarShare.entity;

import java.sql.Timestamp;
// map to resources table
public class Resource {
    private int resourceId;
    private int userId;
    private int topicId;
    private String title;
    private String description;
    private String filePath;
    private String resourceType;
    private String status ;
    private boolean selfDeclaration;
    private Timestamp uploadDate;

    public Resource(){}

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSelfDeclaration() {
        return selfDeclaration;
    }

    public void setSelfDeclaration(boolean selfDeclaration) {
        this.selfDeclaration = selfDeclaration;
    }

    public Timestamp getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Timestamp uploadDate) {
        this.uploadDate = uploadDate;
    }

    public boolean isPending(){
        return "pending".equals(this.status);
    }
    public boolean isApproved(){
        return "approved".equals(this.status);
    }
    public boolean isRejected(){
        return "rejected".equals(this.status);
    }
    public boolean isUnderReview(){
        return "under_review".equals(this.status);
    }
}
