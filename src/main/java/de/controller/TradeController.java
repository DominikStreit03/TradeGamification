package de.controller;


import de.dto.TradeLogDto;
import de.model.TradeLog;
import de.model.User;
import de.repository.QuestRepository;
import de.repository.UserRepository;
import de.service.TradeLogService;
import de.service.StreakService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TradeController {

    private static final Logger log = LoggerFactory.getLogger(TradeController.class);

    @Autowired
    private TradeLogService tradeLogService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestRepository questRepository;

    @Autowired
    private StreakService streakService;

    @GetMapping("/trades")
    public String tradesPage(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        log.debug("GET /trades, session userId={}", userId);
        if (userId == null) {
            log.debug("No user in session, redirect to /login");
            return "redirect:/login";
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            log.warn("User id {} from session not found in DB, invalidating session", userId);
            // user no longer exists (e.g., DB reset) -> clear session and redirect to login
            session.invalidate();
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "trades";
    }

    @PostMapping("/trade/log")
    public String logTrade(TradeLogDto dto, HttpSession session, HttpServletRequest request, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        log.debug("POST /trade/log, session userId={}", userId);
        if (userId == null) {
            log.debug("No user in session for trade submission, redirecting to /login");
            return "redirect:/login";
        }

        // Parse numbers safely
        Double entry = parseDoubleSafe(dto.entryPrice);
        Double sl = parseDoubleSafe(dto.slPrice);
        Double tp = parseDoubleSafe(dto.tpPrice);
        Double res = parseDoubleSafe(dto.result);

        // validate required fields: entry, sl, tp, result
        Map<String, String> errors = new HashMap<>();
        if (entry == null) errors.put("entryPrice", "Entry price ist erforderlich und muss eine gültige Zahl sein");
        if (sl == null) errors.put("slPrice", "SL price ist erforderlich und muss eine gültige Zahl sein");
        if (tp == null) errors.put("tpPrice", "TP price ist erforderlich und muss eine gültige Zahl sein");
        if (res == null) errors.put("result", "Result ist erforderlich und muss eine gültige Zahl sein");

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            log.warn("User id {} not found during trade submission, invalidating session", userId);
            // user not found -> session invalid and redirect
            session.invalidate();
            return "redirect:/login";
        }

        if (!errors.isEmpty()) {
            log.debug("Validation errors on trade submission: {}", errors);
            // on validation error, provide the dto and errors back to the view
            model.addAttribute("user", user);
            model.addAttribute("tradeDto", dto);
            model.addAttribute("tradeErrors", errors);

            // decide which view to return based on referer path
            String referer = request.getHeader("Referer");
            String view = "trades";
            if (referer != null && !referer.isBlank()) {
                try {
                    URI uri = new URI(referer);
                    String path = uri.getPath();
                    if ("/".equals(path)) {
                        // dashboard needs quests and streak update
                        streakService.updateStreak(user);
                        userRepository.save(user);
                        model.addAttribute("quests", questRepository.findAll());
                        view = "dashboard";
                    } else if ("/trades".equals(path)) {
                        view = "trades";
                    } else {
                        // default to trades
                        view = "trades";
                    }
                } catch (URISyntaxException e) {
                    // fallback
                    view = "trades";
                }
            }

            return view;
        }

        // no validation errors -> create trade
        TradeLog trade = new TradeLog();
        trade.setSetupCorrect(dto.setupCorrect);
        trade.setEntryCorrect(dto.entryCorrect);
        trade.setSlCorrect(dto.slCorrect);
        trade.setTpCorrect(dto.tpCorrect);
        trade.setRespectedKill(dto.respectedKill);
        trade.setAvoidedImpulse(dto.avoidedImpulse);

        // Neue Felder setzen
        trade.setInstrument(safeTrim(dto.instrument));
        trade.setMarket(safeTrim(dto.market));
        trade.setEntryPrice(entry);
        trade.setSlPrice(sl);
        trade.setTpPrice(tp);
        trade.setResult(res);
        trade.setCandleTiming(safeTrim(dto.candleTiming));

        // ensure user still exists before saving
        user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            log.warn("User id {} disappeared before saving trade", userId);
            session.invalidate();
            return "redirect:/login";
        }

        try {
            tradeLogService.logTrade(userId, trade);
        } catch (Exception e) {
            log.error("Error while saving trade for user {}: {}", userId, e.getMessage(), e);
            // return to the same view with a general error message
            model.addAttribute("user", user);
            model.addAttribute("tradeDto", dto);
            errors.put("general", "Beim Speichern des Trades ist ein Fehler aufgetreten: " + e.getMessage());
            model.addAttribute("tradeErrors", errors);

            // decide which view to return based on referer
            String referer = request.getHeader("Referer");
            if (referer != null && !referer.isBlank()) {
                try {
                    URI uri = new URI(referer);
                    String path = uri.getPath();
                    if ("/".equals(path)) {
                        streakService.updateStreak(user);
                        userRepository.save(user);
                        model.addAttribute("quests", questRepository.findAll());
                        return "dashboard";
                    }
                } catch (URISyntaxException ex) {
                    // ignore
                }
            }
            return "trades";
        }

        String referer = request.getHeader("Referer");
        String redirectTarget = "/trades";
        if (referer != null && !referer.isBlank()) {
            try {
                URI uri = new URI(referer);
                String path = uri.getPath();
                String query = uri.getQuery();
                if (query != null && !query.isBlank()) {
                    redirectTarget = path + "?" + query;
                } else {
                    redirectTarget = path;
                }
            } catch (URISyntaxException e) {
                // ignore and fallback to /trades
            }
        }

        return "redirect:" + redirectTarget;
    }

    @PostMapping("/trade/remove/{tradeId}")
    public String removeTrade(Long tradeId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        log.debug("POST /trade/log to remove trade {}, session userId={}", tradeId, userId);
        if (userId == null) {
            log.debug("No user in session for trade removal, redirecting to /login");
            return "redirect:/login";
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            log.warn("User id {} not found during trade removal, invalidating session", userId);
            // user not found -> session invalid and redirect
            session.invalidate();
            return "redirect:/login";
        }

        try {
            tradeLogService.removeTrade(tradeId);
        } catch (Exception e) {
            log.error("Error while removing trade {} for user {}: {}", tradeId, userId, e.getMessage(), e);
            // on error, just redirect back to trades
        }

        return "redirect:/trades";
    }

    private Double parseDoubleSafe(String s) {
        if (s == null) return null;
        String t = s.trim();
        if (t.isEmpty()) return null;
        try {
            return Double.parseDouble(t);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String safeTrim(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}