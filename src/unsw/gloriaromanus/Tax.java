package unsw.gloriaromanus;

import java.io.Serializable;

public class Tax implements Serializable {
    private double rate;
    private int wealthGrowthDelta;

    public Tax(double rate, int wealthGrowthDelta) {
        this.rate = rate;
        this.wealthGrowthDelta = wealthGrowthDelta;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getWealthGrowthDelta() {
        return wealthGrowthDelta;
    }

    public void setWealthGrowthDelta(int change) {
        this.wealthGrowthDelta = change;
    }

    public boolean isVeryHighTax() {
        if (rate == 0.25)
            return true;
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;
        Tax t = (Tax) obj;
        if (rate == t.getRate() && wealthGrowthDelta == t.getWealthGrowthDelta()) return true;
        return false;
    }

    @Override
    public String toString() {
        switch((int) (rate * 100)) {
            case 25 :
                return "VERY HIGH";
            case 20 :
                return "HIGH";
            case 15 :
                return "NORMAL";
            case 10 :
                return "LOW";
        }
        return null;
    }
}
