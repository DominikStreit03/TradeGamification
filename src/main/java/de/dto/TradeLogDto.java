package de.dto;

/**
 * Einfaches DTO für Trade-Logging Formular.
 * Enthält die Checkbox-Felder und neue Felder für Preise und Meta.
 */
public class TradeLogDto {
    public boolean setupCorrect;
    public boolean entryCorrect;
    public boolean slCorrect;
    public boolean tpCorrect;
    public boolean respectedKill;
    public boolean avoidedImpulse;

    // Neue Felder
    public String instrument;
    public String market;

    // Numeric fields as strings to avoid binding errors when empty
    public String entryPrice;
    public String slPrice;
    public String tpPrice;

    public String result; // Gewinn/Verlust als String, wird im Controller geparst

    public String candleTiming;

    // Standardkonstruktor
    public TradeLogDto() {}

    // Getter / Setter
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

    public String getInstrument() { return instrument; }
    public void setInstrument(String instrument) { this.instrument = instrument; }

    public String getMarket() { return market; }
    public void setMarket(String market) { this.market = market; }

    public String getEntryPrice() { return entryPrice; }
    public void setEntryPrice(String entryPrice) { this.entryPrice = entryPrice; }

    public String getSlPrice() { return slPrice; }
    public void setSlPrice(String slPrice) { this.slPrice = slPrice; }

    public String getTpPrice() { return tpPrice; }
    public void setTpPrice(String tpPrice) { this.tpPrice = tpPrice; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public String getCandleTiming() { return candleTiming; }
    public void setCandleTiming(String candleTiming) { this.candleTiming = candleTiming; }
}