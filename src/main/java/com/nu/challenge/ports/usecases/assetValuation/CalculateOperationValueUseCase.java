package com.nu.challenge.ports.usecases.assetValuation;

import com.nu.challenge.domain.model.Operation;

import java.math.BigDecimal;

public interface CalculateOperationValueUseCase {
    BigDecimal calculateOperationValue(Operation operation);
}
