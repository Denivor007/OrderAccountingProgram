package com.example.orderaccountingprogram;

public class ChartInfo {
    private int profit;
    private int distance;
    private int intraveltime;
    private int offtraveltime;
    private int paidtrip;
    private int canceldtrip;

    public ChartInfo(int profit, int distance, int intraveltime, int offtraveltime, int paidtrip, int canceldtrip) {
        this.profit = profit;
        this.distance = distance;
        this.intraveltime = intraveltime;
        this.offtraveltime = offtraveltime;
        this.paidtrip = paidtrip;
        this.canceldtrip = canceldtrip;
    }

    public int getProfit() {
        return profit;
    }

    public int getDistance() {
        return distance;
    }

    public int getIntraveltime() {
        return intraveltime;
    }

    public int getOfftraveltime() {
        return offtraveltime;
    }

    public int getPaidtrip() {
        return paidtrip;
    }

    public int getCanceldtrip() {
        return canceldtrip;
    }
}
