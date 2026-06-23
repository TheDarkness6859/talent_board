package com.talentboard.repository;

import com.talentboard.entities.InterviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface InterviewRepository extends JpaRepository<InterviewEntity, UUID> {

    List<InterviewEntity> findByApplicationId(Long applicationId);

    @Query("SELECT i FROM Interview i JOIN FETCH i.application a JOIN FETCH a.vacancy WHERE i.interviewer.id = :interviewerId")
    List<InterviewEntity> findByInterviewerId(@Param("interviewerId") Long interviewerId);

}
