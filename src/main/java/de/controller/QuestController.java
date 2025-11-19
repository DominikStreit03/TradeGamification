package de.controller;


import de.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class QuestController {

    @Autowired
    private QuestService questService;

    @PostMapping("/quest/complete/{questId}")
    public String completeQuest(@PathVariable Long questId,
                                @RequestParam(required = false) Long userId) {
        // Default Demo-Benutzer, falls userId nicht übergeben wird
        Long uid = userId != null ? userId : 1L;
        questService.completeQuest(questId, uid);
        return "redirect:/";
    }
}