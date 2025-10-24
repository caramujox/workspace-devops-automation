package com.nu.challenge.adapters.strategy;

import com.nu.challenge.domain.model.Asset;
import com.nu.challenge.domain.model.Operation;
import com.nu.challenge.domain.model.Tax;
import com.nu.challenge.domain.model.Wallet;
import com.nu.challenge.ports.strategy.OperationStrategy;
import com.nu.challenge.ports.usecases.assetValuation.CalculateOperationProfitUseCase;
import com.nu.challenge.ports.usecases.assetValuation.CalculateOperationValueUseCase;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.nu.challenge.domain.constants.ApplicationConstants.*;

public class SellOperationStrategy implements OperationStrategy {

    private CalculateOperationValueUseCase calculateOperationValueUseCase;
    private CalculateOperationProfitUseCase calculateOperationProfitUseCase;

    public SellOperationStrategy(CalculateOperationValueUseCase calculateOperationValueUseCase, CalculateOperationProfitUseCase calculateOperationProfitUseCase) {
        this.calculateOperationValueUseCase = calculateOperationValueUseCase;
        this.calculateOperationProfitUseCase = calculateOperationProfitUseCase;
    }

    @Override
    public Tax executeOperation(Wallet wallet, Operation operation) {
        Asset asset = wallet.getAsset();
        BigDecimal walletLoss = wallet.getLoss();
        if (asset.getTotalQuantity() < operation.quantity()) {
            throw new IllegalArgumentException("The amount of asset shares you own is less than the requested operation");
        }

        int assetTotalQuantity = asset.getTotalQuantity() - operation.quantity();
        BigDecimal currWeightedAvg = asset.getCurrWeightedAvg();
        if (operation.unitCost().compareTo(currWeightedAvg) < 0) {
            BigDecimal loss = calculateOperationProfitUseCase.calculateOperationProfit(operation, currWeightedAvg);
            asset.updateAsset(assetTotalQuantity, currWeightedAvg);
            wallet.updateWallet(loss, asset);
            return new Tax(BD_ZERO);
        }
        BigDecimal sellSum = (calculateOperationValueUseCase.calculateOperationValue(operation));
        if (sellSum.compareTo(BigDecimal.valueOf(TAX_FREE_CEILING)) <= 0) {
            asset.updateAsset(assetTotalQuantity, currWeightedAvg);
            return new Tax(BD_ZERO);
        }

        if (walletLoss.add(calculateOperationProfitUseCase.calculateOperationProfit(operation, currWeightedAvg)).compareTo(BigDecimal.ZERO) <= 0) {
            walletLoss = walletLoss.add(calculateOperationProfitUseCase.calculateOperationProfit(operation, currWeightedAvg));
            asset.updateAsset(assetTotalQuantity, currWeightedAvg);
            wallet.updateWallet(walletLoss, asset);
            return new Tax(BD_ZERO);
        }

        BigDecimal taxValue = (walletLoss.add(calculateOperationProfitUseCase.calculateOperationProfit(operation, currWeightedAvg))).multiply(BigDecimal.valueOf(TAX_FEE));
        asset.updateAsset(assetTotalQuantity, currWeightedAvg);
        wallet.updateWallet(BD_ZERO, asset);
        return new Tax(taxValue.setScale(2, RoundingMode.CEILING));
    }
}
