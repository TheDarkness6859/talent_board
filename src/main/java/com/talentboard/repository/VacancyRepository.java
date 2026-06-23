package com.talentboard.repository;

import com.talentboard.entities.VacancyEntity;
import com.talentboard.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VacancyRepository extends JpaRepository<VacancyEntity, UUID> {

    List<VacancyEntity> findByState(Status state);

    @Query("SELECT v FROM Vacancy v JOIN FETCH v.responsibleUser WHERE v.id = :id")
    Optional<VacancyEntity> findByIdWithResponsibleUser(@Param("id") Long id);

}
