package com.talentboard.services;

import com.talentboard.entities.UserEntity;
import com.talentboard.mappers.UserMapper;
import com.talentboard.models.User;
import com.talentboard.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;

    UserService(UserRepository repository, UserMapper mapper, PasswordEncoder encoder){
        this.repository = repository;
        this.mapper = mapper;
        this.encoder = encoder;
    }

    public List<User> findAll () {

        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();

    }

    @Transactional(readOnly = true)
    public User findById (UUID id) {

        if (id == null){

            throw new RuntimeException("The id cannot be empty");

        }

        return repository.findById(id).map(mapper::toDomain)
                .orElseThrow(() -> new RuntimeException("couldn't find user with id: " + id));

    }

    @Transactional
    public User registerUser (User user){

        if (user == null){

            throw new RuntimeException("The user cannot be empty");

        }

        if (repository.findByEmail(user.getEmail()).isPresent()){

            throw new RuntimeException("An account with this email already exists.");

        }

        UserEntity entity = mapper.toEntity(user);

        entity.setPassword(encoder.encode(entity.getPassword()));

        return mapper.toDomain(repository.save(entity));

    }

    @Transactional
    public User edit (UUID userId, User user){

        if (userId == null){

            throw new RuntimeException("The id cannot be empty");

        }

        if (user == null){

            throw new RuntimeException("The user cannot be empty");

        }

        UserEntity entity = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("couldn't find user with id: " + userId));

        entity.setFullName(user.getFullName());
        entity.setRole(user.getRole());
        entity.setEmail(user.getEmail());

        return mapper.toDomain(entity);

    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return repository.findByEmail(email)
                .map(mapper::toDomain)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

}
