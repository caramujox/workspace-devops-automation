package com.nu.challenge.adapters.strategy;

import com.nu.challenge.domain.model.Asset;
import com.nu.challenge.domain.model.Operation;
import com.nu.challenge.domain.model.Tax;
import com.nu.challenge.domain.model.Wallet;
import com.nu.challenge.ports.usecases.assetValuation.CalculateWeightedAvgUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static com.nu.challenge.domain.constants.ApplicationConstants.BD_ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BuyOperationStrategyTest {

    private BuyOperationStrategy buyOperationStrategy;
    private CalculateWeightedAvgUseCase calculateWeightedAvgUseCase;

    @BeforeEach
    void setUp() {
        calculateWeightedAvgUseCase = mock(CalculateWeightedAvgUseCase.class);
        buyOperationStrategy = new BuyOperationStrategy(calculateWeightedAvgUseCase);
    }

    @Test
    void testExecuteOperation() {
        // Arrange
        Asset asset = mock(Asset.class);
        Wallet wallet = mock(Wallet.class);
        Operation operation = new Operation("buy", new BigDecimal("15.00"), 10);

        when(wallet.getAsset()).thenReturn(asset);
        when(asset.getTotalQuantity()).thenReturn(20);
        when(asset.getCurrWeightedAvg()).thenReturn(new BigDecimal("10.00"));
        when(calculateWeightedAvgUseCase.calculateWeightedAvg(20, new BigDecimal("10.00"), operation))
                .thenReturn(new BigDecimal("12.50"));

        // Act
        Tax tax = buyOperationStrategy.executeOperation(wallet, operation);

        // Assert
        assertEquals(BD_ZERO, tax.value());
        verify(asset).updateAsset(30, new BigDecimal("12.50"));
        verify(wallet).getAsset();

    }

    @Test
    void testExecuteOperationWithZeroQuantity() {
        // Arrange
        Asset asset = mock(Asset.class);
        Wallet wallet = mock(Wallet.class);
        Operation operation = new Operation("buy", new BigDecimal("15.00"), 0);

        when(wallet.getAsset()).thenReturn(asset);
        when(asset.getTotalQuantity()).thenReturn(0);
        when(asset.getCurrWeightedAvg()).thenReturn(BigDecimal.ZERO);
        when(calculateWeightedAvgUseCase.calculateWeightedAvg(0, BigDecimal.ZERO, operation))
                .thenReturn(BigDecimal.ZERO);

        // Act
        Tax tax = buyOperationStrategy.executeOperation(wallet, operation);

        // Assert
        assertEquals(BD_ZERO, tax.value());
        verify(asset).updateAsset(0, BigDecimal.ZERO);
        verify(wallet).getAsset();
    }
}