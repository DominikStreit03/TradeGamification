package de.service;

import de.model.TradeLog;
import de.model.User;
import de.repository.TradeLogRepository;
import de.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TradeLogService {

    @Autowired
    private TradeLogRepository tradeLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private XpService xpService;

    public void logTrade(Long userId, TradeLog trade) {
        User user = userRepository.findById(userId).orElseThrow();

        if (trade.isSetupCorrect()) xpService.addXp(user, 5);
        if (trade.isEntryCorrect()) xpService.addXp(user, 3);
        if (trade.isSlCorrect()) xpService.addXp(user, 2);
        if (trade.isTpCorrect()) xpService.addXp(user, 2);
        if (trade.isRespectedKill()) xpService.addXp(user, 10);
        if (trade.isAvoidedImpulse()) xpService.addXp(user, 10);

        trade.setTimestamp(LocalDateTime.now());
        trade.setUser(user);

        tradeLogRepository.save(trade);
        userRepository.save(user);
    }
}