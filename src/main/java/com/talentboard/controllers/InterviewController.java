package com.talentboard.controllers;

import com.talentboard.enums.InterviewType;
import com.talentboard.models.Interview;
import com.talentboard.services.InterviewService;
import com.talentboard.services.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequestMapping("/interviews")
public class InterviewController {

    private final InterviewService interviewService;
    private final UserService userService;

    public InterviewController(InterviewService interviewService, UserService userService) {
        this.interviewService = interviewService;
        this.userService = userService;
    }

    @GetMapping
    public String listInterviews(Model model) {
        model.addAttribute("interviews", interviewService.getAll());
        return "interviews/list";
    }

    @GetMapping("/{id}")
    public String viewInterview(@PathVariable UUID id, Model model) {
        model.addAttribute("interview", interviewService.findById(id));
        return "interviews/details";
    }

    @GetMapping("/application/{applicationId}")
    public String interviewsByApplication(@PathVariable UUID applicationId, Model model) {
        model.addAttribute("interviews", interviewService.getInterviewsByApplication(applicationId));
        model.addAttribute("applicationId", applicationId);
        return "interviews/list";
    }

    @GetMapping("/my")
    public String myInterviews(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        UUID interviewerId = userService.findByEmail(userDetails.getUsername()).getId();
        model.addAttribute("interviews", interviewService.getInterviewsByInterviewer(interviewerId));
        return "interviews/list";
    }

    @GetMapping("/application/{applicationId}/schedule")
    public String showScheduleForm(@PathVariable UUID applicationId, Model model) {
        model.addAttribute("applicationId", applicationId);
        model.addAttribute("interviewTypes", InterviewType.values());
        return "interviews/schedule";
    }

    @PostMapping("/application/{applicationId}/schedule")
    public String scheduleInterview(@PathVariable UUID applicationId,
                                    @RequestParam UUID interviewerId,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime interviewDate,
                                    @RequestParam InterviewType type,
                                    @RequestParam String notes,
                                    RedirectAttributes redirectAttributes) {
        try {
            interviewService.scheduleInterview(applicationId, interviewerId, interviewDate, type, notes);
            redirectAttributes.addFlashAttribute("success", "Interview scheduled successfully!");
            return "redirect:/interviews/application/" + applicationId;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/interviews/application/" + applicationId + "/schedule";
        }
    }

    @GetMapping("/{id}/update-result")
    public String showUpdateResultForm(@PathVariable UUID id, Model model) {
        model.addAttribute("interview", interviewService.findById(id));
        return "interviews/update-result";
    }

    @PostMapping("/{id}/update-result")
    public String updateResult(@PathVariable UUID id,
                              @RequestParam String result,
                              @RequestParam String notes,
                              RedirectAttributes redirectAttributes) {
        try {
            interviewService.updateInterviewResult(id, result, notes);
            redirectAttributes.addFlashAttribute("success", "Interview result updated successfully!");
            return "redirect:/interviews/" + id;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/interviews/" + id + "/update-result";
        }
    }
}
