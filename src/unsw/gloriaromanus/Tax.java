package unsw.gloriaromanus;

public class Tax {
    private double rate;
    private int wealthGrowthDelta;

    public Tax(double rate, int wealthGrowthDelta) {
        this.rate = rate;
        this.wealthGrowthDelta = wealthGrowthDelta;
    }

    public double getRate() {
        return rate;
    }
}
