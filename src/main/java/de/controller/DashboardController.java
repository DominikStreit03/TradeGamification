package de.controller;


import de.model.User;
import de.repository.QuestRepository;
import de.repository.UserRepository;
import de.service.StreakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestRepository questRepository;

    @Autowired
    private StreakService streakService;

    @GetMapping("/")
    public String dashboard(Model model, HttpSession session) {
        // If not logged in, redirect to login page
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            session.invalidate();
            return "redirect:/login";
        }

        streakService.updateStreak(user);
        userRepository.save(user);

        model.addAttribute("user", user);
        model.addAttribute("quests", questRepository.findAll());
        return "dashboard";
    }
}