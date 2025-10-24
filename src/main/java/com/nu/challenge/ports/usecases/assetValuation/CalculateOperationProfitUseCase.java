package com.nu.challenge.ports.usecases.assetValuation;

import com.nu.challenge.domain.model.Operation;

import java.math.BigDecimal;

public interface CalculateOperationProfitUseCase {
    BigDecimal calculateOperationProfit(Operation operation, BigDecimal currWeightedAvg);
}
