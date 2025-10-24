package com.nu.challenge.domain.model;

import java.math.BigDecimal;

public class Wallet {
    BigDecimal loss;
    Asset asset;

    public Wallet(BigDecimal loss, Asset asset) {
        this.loss = loss;
        this.asset = asset;
    }

    public BigDecimal getLoss() {
        return loss;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setLoss(BigDecimal loss) {
        this.loss = loss;
    }

    public void updateWallet(BigDecimal loss, Asset asset){
        this.loss = loss;
        this.asset = asset;
    }
}
