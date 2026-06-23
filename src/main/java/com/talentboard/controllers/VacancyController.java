package com.talentboard.controllers;

import com.talentboard.enums.Categories;
import com.talentboard.enums.Status;
import com.talentboard.enums.WorkModality;
import com.talentboard.models.Vacancy;
import com.talentboard.services.VacancyService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/vacancies")
public class VacancyController {

    private final VacancyService vacancyService;

    public VacancyController(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    @GetMapping
    public String listVacancies(Model model) {
        model.addAttribute("vacancies", vacancyService.getAll());
        return "vacancies/list";
    }

    @GetMapping("/active")
    public String listActiveVacancies(Model model) {
        model.addAttribute("vacancies", vacancyService.getActive());
        return "vacancies/list";
    }

    @GetMapping("/{id}")
    public String viewVacancy(@PathVariable UUID id, Model model) {
        model.addAttribute("vacancy", vacancyService.findById(id));
        return "vacancies/details";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("vacancy", new Vacancy());
        model.addAttribute("categories", Categories.values());
        model.addAttribute("workModalities", WorkModality.values());
        model.addAttribute("statuses", Status.values());
        return "vacancies/create";
    }

    @PostMapping("/create")
    public String createVacancy(@ModelAttribute Vacancy vacancy,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        try {
            UUID responsibleId = UUID.fromString(userDetails.getUsername());
            vacancyService.create(vacancy, responsibleId);
            redirectAttributes.addFlashAttribute("success", "Vacancy created successfully!");
            return "redirect:/vacancies";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/vacancies/create";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable UUID id, Model model) {
        model.addAttribute("vacancy", vacancyService.findById(id));
        model.addAttribute("categories", Categories.values());
        model.addAttribute("workModalities", WorkModality.values());
        model.addAttribute("statuses", Status.values());
        return "vacancies/edit";
    }

    @PostMapping("/{id}/edit")
    public String editVacancy(@PathVariable UUID id, @ModelAttribute Vacancy vacancy, 
                              RedirectAttributes redirectAttributes) {
        try {
            vacancyService.editVacancy(id, vacancy);
            redirectAttributes.addFlashAttribute("success", "Vacancy updated successfully!");
            return "redirect:/vacancies/" + id;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/vacancies/" + id + "/edit";
        }
    }
}
