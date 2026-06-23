package com.talentboard.services;

import com.talentboard.entities.ApplicationEntity;
import com.talentboard.entities.UserEntity;
import com.talentboard.entities.VacancyEntity;
import com.talentboard.enums.ApplicationState;
import com.talentboard.enums.Status;
import com.talentboard.mappers.ApplicationMapper;
import com.talentboard.models.Application;
import com.talentboard.repository.ApplicationRepository;
import com.talentboard.repository.UserRepository;
import com.talentboard.repository.VacancyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ApplicationService {

    private final ApplicationRepository appRepository;
    private final VacancyRepository vacancyRepository;
    private final UserRepository userRepository;
    private final ApplicationMapper appMapper;

    ApplicationService(ApplicationRepository appRepository,
                       VacancyRepository vacancyRepository,
                       UserRepository userRepository,
                       ApplicationMapper appMapper){

        this.appRepository = appRepository;
        this.vacancyRepository =vacancyRepository;
        this.userRepository = userRepository;
        this.appMapper = appMapper;

    }

    @Transactional(readOnly = true)
    public List<Application> getAll () {

        return appRepository.findAllWithDetails()
                .stream()
                .map(appMapper::toDomain)
                .toList();

    }

    @Transactional(readOnly = true)
    public Application findById (UUID id) {

        return appRepository.findById(id).map(appMapper::toDomain)
                .orElseThrow(() -> new RuntimeException("Application not found with ID: " + id));

    }

    @Transactional(readOnly = true)
    public List<Application> getByCandidate (UUID id){

        return appRepository.findByCandidateId(id)
                .stream()
                .map(appMapper::toDomain)
                .toList();

    }

    @Transactional
    public Application apply (UUID userId, UUID vacancyId, String notes){

        VacancyEntity foundVacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new RuntimeException("Vacancy not found with ID: " + vacancyId));

        if (foundVacancy.getStatus() == Status.CLOSED){

            throw new IllegalStateException("Cannot apply to this vacancy because it is currently CLOSED.");

        }

        UserEntity foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        boolean applying = appRepository.exitsByCandidateIdAndVacancyId(userId, vacancyId);

        if (applying){

            throw new IllegalArgumentException("The candidate has already applied to this vacancy.");

        }

        ApplicationEntity appEntity = new ApplicationEntity();

        appEntity.setCandidate(foundUser);
        appEntity.setVacancy(foundVacancy);
        appEntity.setAppliedAt(LocalDateTime.now());
        appEntity.setStatus(ApplicationState.APPLIED);
        appEntity.setNotes(notes);


        return appMapper.toDomain(appRepository.save(appEntity));

    }

    @Transactional
    public Application updateStatus(UUID applicationId, ApplicationState newStatus) {

        if (newStatus == null) {
            throw new IllegalArgumentException("New status cannot be null");
        }

        ApplicationEntity foundApplication = appRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with ID: " + applicationId));


        if (foundApplication.getStatus() == ApplicationState.ACCEPTED || foundApplication.getStatus() == ApplicationState.REJECTED) {
            throw new IllegalStateException("Cannot change the status of a finalized application process.");
        }

        foundApplication.setStatus(newStatus);

        return appMapper.toDomain(foundApplication);

    }

}
