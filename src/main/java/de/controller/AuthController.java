package de.controller;

import de.model.User;
import de.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
    public String showRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@RequestParam String username, Model model, HttpSession session) {
        if (username == null || username.trim().isEmpty()) {
            model.addAttribute("error", "Username darf nicht leer sein");
            return "register";
        }

        Optional<User> existing = userRepository.findByUsername(username.trim());
        if (existing.isPresent()) {
            model.addAttribute("error", "Username bereits vergeben");
            return "register";
        }

        User user = new User(username.trim());
        User saved = userRepository.save(user);

        // set session
        session.setAttribute("userId", saved.getId());
        session.setAttribute("username", saved.getUsername());

        return "redirect:/";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username, Model model, HttpSession session) {
        if (username == null || username.trim().isEmpty()) {
            model.addAttribute("error", "Username darf nicht leer sein");
            return "login";
        }

        Optional<User> user = userRepository.findByUsername(username.trim());
        if (user.isEmpty()) {
            model.addAttribute("error", "User nicht gefunden");
            return "login";
        }

        User u = user.get();
        session.setAttribute("userId", u.getId());
        session.setAttribute("username", u.getUsername());
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}

