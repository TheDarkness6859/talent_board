package com.talentboard.mappers;

import com.talentboard.entities.UserEntity;
import com.talentboard.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain (UserEntity entity){

        User domain = new User();

        domain.setId(entity.getId());
        domain.setFullName(entity.getFullName());
        domain.setEmail(entity.getEmail());
        domain.setPassword(entity.getPassword());
        domain.setRole(entity.getRole());

        return domain;

    }

    public UserEntity toEntity (User domain){

        UserEntity entity = new UserEntity();

        entity.setId(domain.getId());
        entity.setFullName(domain.getFullName());
        entity.setEmail(domain.getEmail());
        entity.setPassword(domain.getPassword());
        entity.setRole(domain.getRole());

        return entity;

    }

}
