package de.model;
import jakarta.persistence.*;


@Entity
public class Quest {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String text;
    private int xp;
    private boolean daily = true;
    private boolean completed = false;


    // getters/setters
    public Quest() {}
    public Quest(String text, int xp, boolean daily) {
        this.text = text; this.xp = xp; this.daily = daily;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public int getXp() { return xp; }
    public void setXp(int xp) { this.xp = xp; }
    public boolean isDaily() { return daily; }
    public void setDaily(boolean daily) { this.daily = daily; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}