package de.service;
import de.model.User;
import de.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;



@Service
public class XpService {


    @Autowired
    private UserRepository userRepository;


    public void addXp(User user, int amount) {
        user.setXp(user.getXp() + amount);


        int xpNeeded = 100 + (user.getLevel() - 1) * 50; // leveling curve
        while (user.getXp() >= xpNeeded) {
            user.setLevel(user.getLevel() + 1);
            user.setXp(user.getXp() - xpNeeded);
            xpNeeded = 100 + (user.getLevel() - 1) * 50;
        }


        userRepository.save(user);
    }
}