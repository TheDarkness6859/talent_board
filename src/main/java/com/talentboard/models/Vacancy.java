package com.talentboard.models;

import com.talentboard.enums.Categories;
import com.talentboard.enums.Status;
import com.talentboard.enums.WorkModality;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Vacancy {

    private UUID id;

    @NotBlank(message = "The title is required")
    @Size(min = 5, max = 120)
    private String title;

    @NotBlank(message = "The description is required")
    private String description;

    @NotNull(message = "The category is required")
    private Categories category;

    @NotNull(message = "The work modality is required")
    private WorkModality workModality;

    @NotNull(message = "The salary is required")
    private double salary;

    @FutureOrPresent(message = "The vacant not be in the past")
    private LocalDate creationDate;

    @NotNull(message = "The status cannot be empty")
    private Status status;

    private User responsibleUser;

}
