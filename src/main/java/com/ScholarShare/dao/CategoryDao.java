package com.ScholarShare.dao;

import com.ScholarShare.entity.Faculty;
import com.ScholarShare.entity.Subject;
import com.ScholarShare.entity.Topic;

import java.util.List;

/**
 * Data Access Object (DAO) interface for Category Management.
 * Provides methods for CRUD operations on Faculties, Subjects, and Topics.
 */
public interface CategoryDao {

    // ==========================================
    // Faculty Operations
    // ==========================================

    /**
     * Retrieves a list of all faculties from the database.
     * @return List of Faculty objects.
     */
    List<Faculty> getAllFaculties();

    /**
     * Gets total count of faculties.
     * @return total count.
     */
    int getFacultyCount();

    /**
     * Adds a new faculty to the database.
     * @param faculty The faculty to be added.
     * @return true if the operation is successful, false otherwise.
     */
    boolean addFaculty(Faculty faculty);

    /**
     * Updates an existing faculty in the database.
     * @param faculty The faculty with updated information.
     * @return true if the operation is successful, false otherwise.
     */
    boolean updateFaculty(Faculty faculty);

    /**
     * Deletes a faculty from the database by its ID.
     * Note: Depending on the database schema, this may cascade delete related subjects and topics.
     * @param facultyId The ID of the faculty to delete.
     * @return true if the operation is successful, false otherwise.
     */
    boolean deleteFaculty(int facultyId);

    // ==========================================
    // Subject Operations
    // ==========================================

    /**
     * Retrieves a list of all subjects associated with a specific faculty.
     * @param facultyId The ID of the faculty.
     * @return List of Subject objects.
     */
    List<Subject> getSubjectsByFacultyId(int facultyId);

    /**
     * Gets total count of subjects.
     * @return total count.
     */
    int getSubjectCount();

    /**
     * Adds a new subject to the database.
     * @param subject The subject to be added.
     * @return true if the operation is successful, false otherwise.
     */
    boolean addSubject(Subject subject);

    /**
     * Updates an existing subject in the database.
     * @param subject The subject with updated information.
     * @return true if the operation is successful, false otherwise.
     */
    boolean updateSubject(Subject subject);

    /**
     * Deletes a subject from the database by its ID.
     * Note: Depending on the database schema, this may cascade delete related topics.
     * @param subjectId The ID of the subject to delete.
     * @return true if the operation is successful, false otherwise.
     */
    boolean deleteSubject(int subjectId);

    // ==========================================
    // Topic Operations
    // ==========================================

    /**
     * Retrieves a list of all topics associated with a specific subject.
     * @param subjectId The ID of the subject.
     * @return List of Topic objects.
     */
    List<Topic> getTopicsBySubjectId(int subjectId);

    /**
     * Gets total count of topics.
     * @return total count.
     */
    int getTopicCount();

    /**
     * Adds a new topic to the database.
     * @param topic The topic to be added.
     * @return true if the operation is successful, false otherwise.
     */
    boolean addTopic(Topic topic);

    /**
     * Updates an existing topic in the database.
     * @param topic The topic with updated information.
     * @return true if the operation is successful, false otherwise.
     */
    boolean updateTopic(Topic topic);

    /**
     * Deletes a topic from the database by its ID.
     * @param topicId The ID of the topic to delete.
     * @return true if the operation is successful, false otherwise.
     */
    boolean deleteTopic(int topicId);

}
