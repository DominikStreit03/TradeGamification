package de.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
public class TradeLog {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private boolean setupCorrect;
    private boolean entryCorrect;
    private boolean slCorrect;
    private boolean tpCorrect;
    private boolean respectedKill;
    private boolean avoidedImpulse;


    private LocalDateTime timestamp;


    @ManyToOne
    private User user;


    // Neue Felder: Instrument, Markt, Preise, Ergebnis, Kerzen-Timing
    private String instrument;
    private String market;

    private Double entryPrice;
    private Double slPrice;
    private Double tpPrice;

    private Double result; // z.B. Gewinn/Verlust in Preis- oder Pips

    private String candleTiming;


    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }


    public boolean isSetupCorrect() { return setupCorrect; }
    public void setSetupCorrect(boolean setupCorrect) { this.setupCorrect = setupCorrect; }
    public boolean isEntryCorrect() { return entryCorrect; }
    public void setEntryCorrect(boolean entryCorrect) { this.entryCorrect = entryCorrect; }
    public boolean isSlCorrect() { return slCorrect; }
    public void setSlCorrect(boolean slCorrect) { this.slCorrect = slCorrect; }
    public boolean isTpCorrect() { return tpCorrect; }
    public void setTpCorrect(boolean tpCorrect) { this.tpCorrect = tpCorrect; }
    public boolean isRespectedKill() { return respectedKill; }
    public void setRespectedKill(boolean respectedKill) { this.respectedKill = respectedKill; }
    public boolean isAvoidedImpulse() { return avoidedImpulse; }
    public void setAvoidedImpulse(boolean avoidedImpulse) { this.avoidedImpulse = avoidedImpulse; }


    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getInstrument() { return instrument; }
    public void setInstrument(String instrument) { this.instrument = instrument; }

    public String getMarket() { return market; }
    public void setMarket(String market) { this.market = market; }

    public Double getEntryPrice() { return entryPrice; }
    public void setEntryPrice(Double entryPrice) { this.entryPrice = entryPrice; }

    public Double getSlPrice() { return slPrice; }
    public void setSlPrice(Double slPrice) { this.slPrice = slPrice; }

    public Double getTpPrice() { return tpPrice; }
    public void setTpPrice(Double tpPrice) { this.tpPrice = tpPrice; }

    public Double getResult() { return result; }
    public void setResult(Double result) { this.result = result; }

    public String getCandleTiming() { return candleTiming; }
    public void setCandleTiming(String candleTiming) { this.candleTiming = candleTiming; }
}