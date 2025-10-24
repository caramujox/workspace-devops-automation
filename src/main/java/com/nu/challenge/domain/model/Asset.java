package com.nu.challenge.domain.model;

import java.math.BigDecimal;

public class Asset {
    int totalQuantity;
    BigDecimal currWeightedAvg;

    public Asset(int totalQuantity, BigDecimal currWeightedAvg) {
        this.totalQuantity = totalQuantity;
        this.currWeightedAvg = currWeightedAvg;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public BigDecimal getCurrWeightedAvg() {
        return currWeightedAvg;
    }

    public void updateAsset(int newTotalQuantity, BigDecimal newCurrWeightedAvg){
        this.currWeightedAvg = newCurrWeightedAvg;
        this.totalQuantity = newTotalQuantity;
    }
}
