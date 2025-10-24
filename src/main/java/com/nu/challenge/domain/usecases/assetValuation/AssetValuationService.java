package com.nu.challenge.domain.usecases.assetValuation;

import com.nu.challenge.domain.model.Operation;
import com.nu.challenge.ports.usecases.assetValuation.CalculateOperationProfitUseCase;
import com.nu.challenge.ports.usecases.assetValuation.CalculateOperationValueUseCase;
import com.nu.challenge.ports.usecases.assetValuation.CalculateWeightedAvgUseCase;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AssetValuationService implements CalculateWeightedAvgUseCase, CalculateOperationProfitUseCase, CalculateOperationValueUseCase {
    @Override
    public BigDecimal calculateOperationProfit(Operation operation, BigDecimal currWeightedAvg) {
        return (operation.unitCost().subtract(currWeightedAvg)).multiply(BigDecimal.valueOf(operation.quantity()))
                .setScale(2, RoundingMode.CEILING);
    }

    @Override
    public BigDecimal calculateOperationValue(Operation operation) {
        return operation.unitCost().multiply(BigDecimal.valueOf(operation.quantity()))
                .setScale(2, RoundingMode.CEILING);
    }

    @Override
    public BigDecimal calculateWeightedAvg(int totalQtt, BigDecimal currWeightedAvg, Operation operation) {
        BigDecimal numerator = calculateOperationValue(operation).add(currWeightedAvg.multiply(BigDecimal.valueOf(totalQtt)));
        BigDecimal denominator = BigDecimal.valueOf(totalQtt + operation.quantity());
        if (denominator.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.CEILING);
        }
        return numerator.divide(denominator, 2, RoundingMode.CEILING);
    }
}
