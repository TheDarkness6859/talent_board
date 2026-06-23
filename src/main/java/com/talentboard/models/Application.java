package com.talentboard.models;

import com.talentboard.enums.ApplicationState;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Application {

    @NotNull(message = "The candidate is required")
    private User candidate;

    @NotNull(message = "The vacancy is required")
    private Vacancy vacancy;

    @FutureOrPresent(message = "The application date not be in the past")
    private LocalDate applicationDate;

    @NotNull(message = "The application status is required")
    private ApplicationState state;

    private String notes;

}
