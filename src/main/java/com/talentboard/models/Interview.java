package com.talentboard.models;

import com.talentboard.enums.InterviewType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class Interview {

    private UUID id;

    @NotNull(message = "The application is required")
    private Application application;

    @FutureOrPresent(message = "The date cannot be in the past")
    private LocalDateTime interviewDate;

    @NotNull(message = "The interview type is required")
    private InterviewType interviewType;

    private String result;

    private String observations;

}
