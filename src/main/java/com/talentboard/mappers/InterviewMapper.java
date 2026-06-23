package com.talentboard.mappers;

import com.talentboard.entities.InterviewEntity;
import com.talentboard.models.Interview;
import org.springframework.stereotype.Component;

@Component
public class InterviewMapper {

    private final ApplicationMapper applicationMapper;
    private final UserMapper userMapper;

    InterviewMapper(ApplicationMapper applicationMapper, UserMapper userMapper){
        this.applicationMapper = applicationMapper;
        this.userMapper = userMapper;
    }

    public Interview toDomain (InterviewEntity entity){

        Interview domain = new Interview();

        domain.setId(entity.getId());
        domain.setApplication(applicationMapper.toDomain(entity.getApplication()));
        domain.setInterviewDate(entity.getInterviewDate());
        domain.setInterviewType(entity.getType());
        domain.setInterviewer(userMapper.toDomain(entity.getInterviewer()));
        domain.setResult(entity.getResult());
        domain.setObservations(entity.getNotes());

        return domain;

    }

    public InterviewEntity toEntity (Interview domain){

        InterviewEntity entity = new InterviewEntity();

        entity.setId(domain.getId());
        entity.setApplication(applicationMapper.toEntity(domain.getApplication()));
        entity.setInterviewDate(domain.getInterviewDate());
        entity.setType(domain.getInterviewType());
        entity.setInterviewer(userMapper.toEntity(domain.getInterviewer()));
        entity.setResult(domain.getResult());
        entity.setNotes(domain.getObservations());

        return entity;

    }

}
