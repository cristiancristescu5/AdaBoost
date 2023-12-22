package org.example;

public class Instance {
    private final int x1;
    private final int x2;
    private final int y;
    private double distribution;

    public Instance(int x1, int x2, int y) {
        this.x1 = x1;
        this.x2 = x2;
        this.y = y;
    }

    public void setDistribution(double distribution) {
        this.distribution = distribution;
    }

    public double getDistribution() {
        return distribution;
    }

    public int getX1() {
        return x1;
    }

    public int getX2() {
        return x2;
    }

    public int getY() {
        return y;
    }
}
