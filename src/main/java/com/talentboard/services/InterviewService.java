package com.talentboard.services;

import com.talentboard.entities.ApplicationEntity;
import com.talentboard.entities.InterviewEntity;
import com.talentboard.entities.UserEntity;
import com.talentboard.enums.InterviewType;
import com.talentboard.mappers.InterviewMapper;
import com.talentboard.models.Interview;
import com.talentboard.repository.ApplicationRepository;
import com.talentboard.repository.InterviewRepository;
import com.talentboard.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final InterviewMapper interviewMapper;
    private final ApplicationRepository appRepository;
    private final UserRepository userRepository;

    InterviewService(InterviewRepository interviewRepository,
                     InterviewMapper interviewMapper,
                     ApplicationRepository appRepository,
                     UserRepository userRepository){

        this.interviewRepository = interviewRepository;
        this.interviewMapper = interviewMapper;
        this.appRepository = appRepository;
        this.userRepository = userRepository;

    }

    @Transactional
    public Interview scheduleInterview(UUID applicationId, UUID interviewerId, LocalDateTime interviewDate, InterviewType type, String notes) {


        if (interviewDate == null || interviewDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Interviews cannot be scheduled in the past.");
        }

        ApplicationEntity foundApplication = appRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with ID: " + applicationId));


        UserEntity interviewer = userRepository.findById(interviewerId)
                .orElseThrow(() -> new RuntimeException("Interviewer not found with ID: " + interviewerId));


        InterviewEntity interview = new InterviewEntity();

        interview.setApplication(foundApplication);
        interview.setInterviewer(interviewer);
        interview.setInterviewDate(interviewDate);
        interview.setType(type);
        interview.setNotes(notes);
        interview.setResult("PENDING");

        return interviewMapper.toDomain(interviewRepository.save(interview));
    }


    @Transactional
    public Interview updateInterviewResult(UUID interviewId, String result, String notes) {

        if (interviewId == null) {
            throw new IllegalArgumentException("Interview ID cannot be null");
        }

        InterviewEntity foundInterview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found with ID: " + interviewId));

        foundInterview.setResult(result);
        foundInterview.setNotes(notes);

        return interviewMapper.toDomain(foundInterview);

    }

    @Transactional(readOnly = true)
    public List<Interview> getInterviewsByApplication(UUID applicationId) {

        return interviewRepository.findByApplicationId(applicationId)
                .stream()
                .map(interviewMapper::toDomain)
                .toList();

    }

    @Transactional(readOnly = true)
    public List<Interview> getInterviewsByInterviewer(UUID interviewerId) {

        return interviewRepository.findByInterviewerId(interviewerId)
                .stream()
                .map(interviewMapper::toDomain)
                .toList();

    }

    @Transactional(readOnly = true)
    public Interview findById(UUID id) {

        return interviewRepository.findById(id).map(interviewMapper::toDomain)
                .orElseThrow(() -> new RuntimeException("Interview not found with ID: " + id));

    }
}
