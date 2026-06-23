package com.talentboard.services;

import com.talentboard.entities.UserEntity;
import com.talentboard.entities.VacancyEntity;
import com.talentboard.enums.Status;
import com.talentboard.mappers.UserMapper;
import com.talentboard.mappers.VacancyMapper;
import com.talentboard.models.Vacancy;
import com.talentboard.repository.UserRepository;
import com.talentboard.repository.VacancyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class VacancyService {

    private final VacancyRepository vacancyRepository;
    private final UserRepository userRepository;
    private final VacancyMapper vacancyMapper;
    private final UserMapper userMapper;

    VacancyService(VacancyRepository vacancyRepository,
                   UserRepository userRepository,
                   VacancyMapper vacancyMapper,
                   UserMapper userMapper){

        this.vacancyRepository =vacancyRepository;
        this.userRepository = userRepository;
        this.vacancyMapper = vacancyMapper;
        this.userMapper = userMapper;

    }

    @Transactional(readOnly = true)
    public List<Vacancy> getAll () {

        return vacancyRepository.findAll()
                .stream()
                .map(vacancyMapper::toDomain)
                .toList();

    }

    @Transactional(readOnly = true)
    public Vacancy findById (UUID id){

        return vacancyRepository.findById(id).map(vacancyMapper::toDomain)
                .orElseThrow(() -> new RuntimeException("Vacancy not found with ID: " + id));

    }

    @Transactional(readOnly = true)
    public List<Vacancy> getActive () {

        return vacancyRepository.findByState(Status.PUBLISHED)
                .stream()
                .map(vacancyMapper::toDomain)
                .toList();

    }

    @Transactional
    public Vacancy create (Vacancy vacancy, UUID responsibleId){

        if (vacancy == null){

            throw new RuntimeException("Vacancy data cannot be null");

        }

        UserEntity userEntity = userRepository.findById(responsibleId)
                .orElseThrow(() -> new RuntimeException("Responsible user not found with ID: " + responsibleId));

        VacancyEntity vacancyEntity = vacancyMapper.toEntity(vacancy);

        vacancyEntity.setResponsibleUser(userEntity);
        vacancyEntity.setCreationDate(LocalDate.now());

        if (vacancyEntity.getStatus() == null){

            vacancyEntity.setStatus(Status.PUBLISHED);

        }

        return vacancyMapper.toDomain(vacancyRepository.save(vacancyEntity));

    }

    @Transactional
    public Vacancy editVacancy(UUID vacancyId, Vacancy updatedData) {

        if (vacancyId == null) {

            throw new RuntimeException("The vacancy ID cannot be null");

        }

        if (updatedData == null) {

            throw new RuntimeException("Updated vacancy data cannot be null");

        }


        VacancyEntity vacancyEntity = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new RuntimeException("Vacancy not found with ID: " + vacancyId));


        vacancyEntity.setTitle(updatedData.getTitle());
        vacancyEntity.setDescription(updatedData.getDescription());
        vacancyEntity.setCategory(updatedData.getCategory());
        vacancyEntity.setWorkModality(updatedData.getWorkModality());
        vacancyEntity.setSalary(updatedData.getSalary());
        vacancyEntity.setStatus(updatedData.getStatus());


        return vacancyMapper.toDomain(vacancyEntity);

    }

}
