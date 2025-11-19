package de.controller;


import de.service.QuestService;
import de.repository.UserRepository;
import de.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class QuestController {

    @Autowired
    private QuestService questService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/quests")
    public String questsPage(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            session.invalidate();
            return "redirect:/login";
        }

        return "questOverview";
    }

    @PostMapping("/quest/complete/{questId}")
    public String completeQuest(@PathVariable Long questId,
                                @RequestParam(required = false) Long userId,
                                HttpSession session) {
        // If userId provided use it, otherwise try session
        Long uid = userId != null ? userId : (Long) session.getAttribute("userId");
        if (uid == null) {
            return "redirect:/login";
        }

        User user = userRepository.findById(uid).orElse(null);
        if (user == null) {
            session.invalidate();
            return "redirect:/login";
        }

        try {
            questService.completeQuest(questId, uid);
        } catch (Exception e) {
            // on any error, redirect back to dashboard (GlobalExceptionHandler will render error view if unexpected)
            return "redirect:/";
        }
        return "redirect:/";
    }
}