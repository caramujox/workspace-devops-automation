package com.nu.challenge.adapters.strategy;

import com.nu.challenge.domain.model.Asset;
import com.nu.challenge.domain.model.Operation;
import com.nu.challenge.domain.model.Tax;
import com.nu.challenge.domain.model.Wallet;
import com.nu.challenge.ports.strategy.OperationStrategy;
import com.nu.challenge.ports.usecases.assetValuation.CalculateWeightedAvgUseCase;

import java.math.BigDecimal;

import static com.nu.challenge.domain.constants.ApplicationConstants.BD_ZERO;

public class BuyOperationStrategy implements OperationStrategy {
    private CalculateWeightedAvgUseCase calculateWeightedAvgUseCase;

    public BuyOperationStrategy(CalculateWeightedAvgUseCase calculateWeightedAvgUseCase) {
        this.calculateWeightedAvgUseCase = calculateWeightedAvgUseCase;
    }

    @Override
    public Tax executeOperation(Wallet wallet, Operation operation) {
         Asset asset = wallet.getAsset();
         BigDecimal assetWeightedAvg = calculateWeightedAvgUseCase.calculateWeightedAvg(asset.getTotalQuantity(),
                 asset.getCurrWeightedAvg(), operation);
         int assetTotalQuantity = asset.getTotalQuantity() + operation.quantity();
         asset.updateAsset(assetTotalQuantity, assetWeightedAvg);
         return new Tax(BD_ZERO);
    }
}
