package de.service;

import de.model.Quest;
import de.model.User;
import de.repository.QuestRepository;
import de.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestService {

    @Autowired
    private QuestRepository repo;

    @Autowired
    private XpService xpService;

    @Autowired
    private UserRepository userRepo;

    public void completeQuest(Long questId, Long userId) {
        Quest q = repo.findById(questId).orElseThrow();
        User u = userRepo.findById(userId).orElseThrow();

        if (!q.isCompleted()) {
            xpService.addXp(u, q.getXp());
            q.setCompleted(true);
            userRepo.save(u);
            repo.save(q);
        }
    }
}