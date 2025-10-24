package com.nu.challenge.adapters.strategy;

import com.nu.challenge.domain.model.Asset;
import com.nu.challenge.domain.model.Operation;
import com.nu.challenge.domain.model.Tax;
import com.nu.challenge.domain.model.Wallet;
import com.nu.challenge.ports.usecases.assetValuation.CalculateOperationProfitUseCase;
import com.nu.challenge.ports.usecases.assetValuation.CalculateOperationValueUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.nu.challenge.domain.constants.ApplicationConstants.BD_ZERO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SellOperationStrategyTest {

    private SellOperationStrategy sellOperationStrategy;
    private CalculateOperationValueUseCase calculateOperationValueUseCase;
    private CalculateOperationProfitUseCase calculateOperationProfitUseCase;

    @BeforeEach
    void setUp() {
        calculateOperationValueUseCase = mock(CalculateOperationValueUseCase.class);
        calculateOperationProfitUseCase = mock(CalculateOperationProfitUseCase.class);
        sellOperationStrategy = new SellOperationStrategy(calculateOperationValueUseCase, calculateOperationProfitUseCase);
    }

    @Test
    void testExecuteOperationWithInsufficientShares() {
        Wallet wallet = mock(Wallet.class);
        Asset asset = mock(Asset.class);
        Operation operation = new Operation("sell", new BigDecimal("15.00"), 10);

        when(wallet.getAsset()).thenReturn(asset);
        when(asset.getTotalQuantity()).thenReturn(5);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                sellOperationStrategy.executeOperation(wallet, operation));

        assertEquals("The amount of asset shares you own is less than the requested operation", exception.getMessage());
    }

    @Test
    void testExecuteOperationWithLoss() {
        Wallet wallet = mock(Wallet.class);
        Asset asset = mock(Asset.class);
        Operation operation = new Operation("sell", new BigDecimal("8.00"), 10);

        when(wallet.getAsset()).thenReturn(asset);
        when(asset.getTotalQuantity()).thenReturn(20);
        when(asset.getCurrWeightedAvg()).thenReturn(new BigDecimal("10.00"));
        when(calculateOperationProfitUseCase.calculateOperationProfit(operation, new BigDecimal("10.00")))
                .thenReturn(new BigDecimal("-20.00"));

        Tax tax = sellOperationStrategy.executeOperation(wallet, operation);

        assertEquals(BD_ZERO, tax.value());
        verify(asset).updateAsset(10, new BigDecimal("10.00"));
        verify(wallet).updateWallet(new BigDecimal("-20.00"), asset);
    }

    @Test
    void testExecuteOperationWithTaxFreeSell() {
        Wallet wallet = mock(Wallet.class);
        Asset asset = mock(Asset.class);
        Operation operation = new Operation("sell", new BigDecimal("15.00"), 10);

        when(wallet.getAsset()).thenReturn(asset);
        when(asset.getTotalQuantity()).thenReturn(20);
        when(asset.getCurrWeightedAvg()).thenReturn(new BigDecimal("10.00"));
        when(calculateOperationValueUseCase.calculateOperationValue(operation))
                .thenReturn(new BigDecimal("140.00"));

        Tax tax = sellOperationStrategy.executeOperation(wallet, operation);

        assertEquals(BD_ZERO, tax.value());
        verify(asset).updateAsset(10, new BigDecimal("10.00"));
    }

    @Test
    void testExecuteOperationWithTaxableProfit() {
        Wallet wallet = mock(Wallet.class);
        Asset asset = mock(Asset.class);
        Operation operation = new Operation("sell", new BigDecimal("20.00"), 10);

        when(wallet.getAsset()).thenReturn(asset);
        when(asset.getTotalQuantity()).thenReturn(20);
        when(asset.getCurrWeightedAvg()).thenReturn(new BigDecimal("10.00"));
        when(wallet.getLoss()).thenReturn(BD_ZERO);
        when(calculateOperationProfitUseCase.calculateOperationProfit(operation, new BigDecimal("10.00")))
                .thenReturn(new BigDecimal("100.00"));
        when(calculateOperationValueUseCase.calculateOperationValue(operation))
                .thenReturn(new BigDecimal("200000.00"));

        Tax tax = sellOperationStrategy.executeOperation(wallet, operation);

        assertEquals(new BigDecimal("20.00"), tax.value());
        verify(asset).updateAsset(10, new BigDecimal("10.00"));
        verify(wallet).updateWallet(BD_ZERO, asset);
    }
}