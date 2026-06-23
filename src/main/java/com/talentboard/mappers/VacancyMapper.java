package com.talentboard.mappers;

import com.talentboard.entities.VacancyEntity;
import com.talentboard.models.Vacancy;
import org.springframework.stereotype.Component;

@Component
public class VacancyMapper {

    private final UserMapper mapper;

    VacancyMapper(UserMapper mapper){
        this.mapper = mapper;
    }

    public Vacancy toDomain (VacancyEntity entity){

        Vacancy domain = new Vacancy();

        domain.setId(entity.getId());
        domain.setTitle(entity.getTitle());
        domain.setDescription(entity.getDescription());
        domain.setCategory(entity.getCategory());
        domain.setWorkModality(entity.getWorkModality());
        domain.setSalary(entity.getSalary());
        domain.setCreationDate(entity.getCreationDate());
        domain.setStatus(entity.getStatus());
        domain.setResponsibleUser(mapper.toDomain(entity.getResponsibleUser()));

        return domain;

    }

    public VacancyEntity toEntity (Vacancy domain){

        VacancyEntity entity = new VacancyEntity();

        entity.setId(domain.getId());
        entity.setTitle(domain.getTitle());
        entity.setDescription(domain.getDescription());
        entity.setCategory(domain.getCategory());
        entity.setWorkModality(domain.getWorkModality());
        entity.setSalary(domain.getSalary());
        entity.setCreationDate(domain.getCreationDate());
        entity.setStatus(domain.getStatus());
        entity.setResponsibleUser(mapper.toEntity(domain.getResponsibleUser()));

        return entity;

    }

}
