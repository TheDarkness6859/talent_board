package com.talentboard.mappers;

import com.talentboard.entities.ApplicationEntity;
import com.talentboard.models.Application;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {

    private final VacancyMapper vacancyMapper;
    private final UserMapper userMapper;

    ApplicationMapper(VacancyMapper vacancyMapper, UserMapper userMapper){
        this.vacancyMapper = vacancyMapper;
        this.userMapper = userMapper;
    }

    public Application toDomain (ApplicationEntity entity){

        Application domain = new Application();

        domain.setId(entity.getId());
        domain.setCandidate(userMapper.toDomain(entity.getCandidate()));
        domain.setVacancy(vacancyMapper.toDomain(entity.getVacancy()));
        domain.setApplicationDate(entity.getAppliedAt());
        domain.setState(entity.getStatus());
        domain.setNotes(entity.getNotes());

        return domain;

    }

    public ApplicationEntity toEntity (Application domain){

        ApplicationEntity entity = new ApplicationEntity();

        entity.setId(domain.getId());
        entity.setCandidate(userMapper.toEntity(domain.getCandidate()));
        entity.setVacancy(vacancyMapper.toEntity(domain.getVacancy()));
        entity.setAppliedAt(domain.getApplicationDate());
        entity.setStatus(domain.getState());
        entity.setNotes(domain.getNotes());

        return entity;

    }

}
