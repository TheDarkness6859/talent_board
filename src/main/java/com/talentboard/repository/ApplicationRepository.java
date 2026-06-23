package com.talentboard.repository;

import com.talentboard.entities.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<ApplicationEntity, UUID> {

    @Query("SELECT a FROM Application a JOIN FETCH a.vacancy WHERE a.candidate.id = :candidateId")
    List<ApplicationEntity> findByCandidateId(@Param("candidateId") UUID candidateId);

    @Query("SELECT a FROM Application a JOIN FETCH a.candidate JOIN FETCH a.vacancy")
    List<ApplicationEntity> findAllWithDetails();

    boolean exitsByCandidateIdAndVacancyId (UUID userId, UUID vacancyId);

}
