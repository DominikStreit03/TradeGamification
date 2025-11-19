package de.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElement(NoSuchElementException ex, HttpServletRequest req, HttpSession session) {
        log.warn("NoSuchElementException for request {}: {}", req.getRequestURI(), ex.getMessage());
        try { session.invalidate(); } catch (Exception e) { /* ignore */ }
        return "redirect:/login";
    }

    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception ex, HttpServletRequest req, Model model) {
        log.error("Unhandled exception for request {}", req.getRequestURI(), ex);
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("exceptionClass", ex.getClass().getName());
        // include simple stack trace for debugging (can be toggled later)
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        model.addAttribute("stackTrace", sw.toString());
        return "error";
    }
}
