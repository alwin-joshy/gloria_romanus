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

    public int isVeryHighTax() {
        if (rate == 0.25)
            return 1;
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;
        Tax t = (Tax) obj;
        if (rate == t.getRate() && wealthGrowthDelta == t.getWealthGrowthDelta()) return true;
        return false;
    }

    
}
