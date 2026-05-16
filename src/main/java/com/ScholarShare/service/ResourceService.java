package com.ScholarShare.service;

import com.ScholarShare.dao.ResourceDao;
import com.ScholarShare.dao.TopicDao;
import com.ScholarShare.dao.daoImpl.ResourceDaoImpl;
import com.ScholarShare.dao.daoImpl.TopicDaoImpl;
import com.ScholarShare.entity.Resource;
import com.ScholarShare.entity.Topic;
import com.ScholarShare.util.ValidationUtil;

import java.util.List;

public class ResourceService {

    private static final long MAX_FILE_SIZE_BYTES = 1024L * 1024L * 50L;

    private final ResourceDao resourceDao = new ResourceDaoImpl();
    private final TopicDao topicDao = new TopicDaoImpl();

    public List<Topic> getTopicsForUpload() {
        return topicDao.getAllTopics();
    }

    public Resource getResourceById(int resourceId) {
        if (resourceId <= 0) {
            return null;
        }
        return resourceDao.getResourceById(resourceId);
    }

    public String validateUpload(String title, String description, String resourceType,
                                 String topicCategory, boolean pledgeAgreed,
                                 String originalFileName, long fileSize) {

        if (!pledgeAgreed) {
            return "You must confirm the academic integrity pledge before uploading.";
        }

        if (ValidationUtil.isNullOrEmpty(title)
                || ValidationUtil.isNullOrEmpty(description)
                || ValidationUtil.isNullOrEmpty(resourceType)
                || ValidationUtil.isNullOrEmpty(topicCategory)) {
            return "All fields and the integrity pledge are required.";
        }

        try {
            int topicId = Integer.parseInt(topicCategory);
            if (topicId <= 0) {
                return "Please select a valid topic category.";
            }
        } catch (NumberFormatException e) {
            return "Please select a valid topic category.";
        }

        if (fileSize <= 0) {
            return "Please choose a file to upload.";
        }

        if (fileSize > MAX_FILE_SIZE_BYTES) {
            return "File size exceeds the 50MB limit.";
        }

        if (ValidationUtil.isNullOrEmpty(originalFileName)) {
            return "Invalid file name.";
        }

        String lowerName = originalFileName.toLowerCase();
        if (!lowerName.endsWith(".pdf") && !lowerName.endsWith(".docx")) {
            return "Only PDF and DOCX files are allowed.";
        }

        return null;
    }

    public boolean saveUploadedResource(int userId, String title, String description,
                                        String resourceType, String topicCategory,
                                        String relativeFilePath) {

        Resource resource = new Resource();
        resource.setUserId(userId);
        resource.setTopicId(Integer.parseInt(topicCategory));
        resource.setTitle(title.trim());
        resource.setDescription(description.trim());
        resource.setResourceType(resourceType);
        resource.setFilePath(relativeFilePath);
        resource.setSelfDeclaration(true);
        resource.setStatus("pending");

        return resourceDao.uploadResource(resource);
    }

    public boolean deleteResource(int resourceId, int userId, String webappRealPath) {
        return resourceDao.deleteResource(resourceId, userId, webappRealPath);
    }
}
