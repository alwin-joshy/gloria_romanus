package unsw.gloriaromanus;

import java.io.Serializable;

public class Tax implements Serializable{
    private double rate;
    private int wealthGrowthDelta;

    public Tax(double rate, int wealthGrowthDelta) {
        this.rate = rate;
        this.wealthGrowthDelta = wealthGrowthDelta;
    }

    public double getRate() {
        return rate;
    }

    public boolean isVeryHighTax() {
        if (rate == 0.25) return true;
        return false;
    }
}
