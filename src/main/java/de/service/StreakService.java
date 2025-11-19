package de.service;

import java.time.LocalDate;

import de.model.User;
import org.springframework.stereotype.Service;



@Service
public class StreakService {

    public void updateStreak(User user) {
        LocalDate today = LocalDate.now();

        if (user.getLastActiveDay() == null) {
            user.setLastActiveDay(today);
            user.setStreak(1);
            return;
        }

        // Already updated today
        if (user.getLastActiveDay().equals(today)) {
            return;
        }

        // Consecutive day → streak++
        if (user.getLastActiveDay().plusDays(1).equals(today)) {
            user.setStreak(user.getStreak() + 1);
        } else {
            user.setStreak(1); // reset
        }

        user.setLastActiveDay(today);
    }
}