package com.talentboard.controllers;

import com.talentboard.enums.ApplicationState;
import com.talentboard.models.Application;
import com.talentboard.services.ApplicationService;
import com.talentboard.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final UserService userService;

    public ApplicationController(ApplicationService applicationService, UserService userService) {
        this.applicationService = applicationService;
        this.userService = userService;
    }

    @GetMapping
    public String listApplications(Model model) {
        model.addAttribute("applications", applicationService.getAll());
        return "applications/list";
    }

    @GetMapping("/{id}")
    public String viewApplication(@PathVariable UUID id, Model model) {
        model.addAttribute("application", applicationService.findById(id));
        return "applications/details";
    }

    @GetMapping("/my")
    public String myApplications(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        UUID userId = userService.findByEmail(userDetails.getUsername()).getId();
        model.addAttribute("applications", applicationService.getByCandidate(userId));
        return "applications/list";
    }

    @GetMapping("/vacancy/{vacancyId}/apply")
    public String showApplyForm(@PathVariable UUID vacancyId, Model model) {
        model.addAttribute("vacancyId", vacancyId);
        return "applications/apply";
    }

    @PostMapping("/vacancy/{vacancyId}/apply")
    public String applyToVacancy(@PathVariable UUID vacancyId,
                                 @RequestParam String notes,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {
        try {
            UUID userId = userService.findByEmail(userDetails.getUsername()).getId();
            applicationService.apply(userId, vacancyId, notes);
            redirectAttributes.addFlashAttribute("success", "Application submitted successfully!");
            return "redirect:/applications/my";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/vacancies/" + vacancyId;
        }
    }

    @GetMapping("/{id}/update-status")
    public String showUpdateStatusForm(@PathVariable UUID id, Model model) {
        model.addAttribute("application", applicationService.findById(id));
        model.addAttribute("statuses", ApplicationState.values());
        return "applications/update-status";
    }

    @PostMapping("/{id}/update-status")
    public String updateStatus(@PathVariable UUID id,
                               @RequestParam ApplicationState status,
                               RedirectAttributes redirectAttributes) {
        try {
            applicationService.updateStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "Application status updated successfully!");
            return "redirect:/applications/" + id;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/applications/" + id + "/update-status";
        }
    }
}
