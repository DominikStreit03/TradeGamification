package de.model;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@Table(name = "app_user")
public class User {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String username;


    private int level = 1;
    private int xp = 0;


    private int streak = 0;
    private LocalDate lastActiveDay;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TradeLog> trades = new ArrayList<>();


// constructors, getters, setters


    public User() {}


    public User(String username) { this.username = username; }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }


    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }


    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }


    public int getXp() { return xp; }
    public void setXp(int xp) { this.xp = xp; }


    public int getStreak() { return streak; }
    public void setStreak(int streak) { this.streak = streak; }


    public LocalDate getLastActiveDay() { return lastActiveDay; }
    public void setLastActiveDay(LocalDate lastActiveDay) { this.lastActiveDay = lastActiveDay; }


    public List<TradeLog> getTrades() {
        // return trades sorted by timestamp desc (newest first)
        return trades.stream()
                .sorted((a, b) -> {
                    if (a.getTimestamp() == null && b.getTimestamp() == null) return 0;
                    if (a.getTimestamp() == null) return 1;
                    if (b.getTimestamp() == null) return -1;
                    return b.getTimestamp().compareTo(a.getTimestamp());
                })
                .collect(Collectors.toList());
    }
    public void setTrades(List<TradeLog> trades) { this.trades = trades; }
}