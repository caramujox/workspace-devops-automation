package com.nu.challenge.domain.usecases.assetValuation;

import com.nu.challenge.domain.model.Operation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AssetValuationServiceTest {

    private AssetValuationService assetValuationService;

    @BeforeEach
    void setUp() {
        assetValuationService = new AssetValuationService();
    }

    @Test
    void testCalculateOperationProfit() {
        Operation operation = new Operation("sell", new BigDecimal("15.00"), 10);
        BigDecimal currWeightedAvg = new BigDecimal("10.00");

        BigDecimal profit = assetValuationService.calculateOperationProfit(operation, currWeightedAvg);

        assertEquals(new BigDecimal("50.00"), profit);
    }

    @Test
    void testCalculateOperationValue() {
        Operation operation = new Operation("buy", new BigDecimal("20.00"), 5);

        BigDecimal operationValue = assetValuationService.calculateOperationValue(operation);

        assertEquals(new BigDecimal("100.00"), operationValue);
    }

    @Test
    void testCalculateWeightedAvg() {
        int totalQtt = 10;
        BigDecimal currWeightedAvg = new BigDecimal("12.00");
        Operation operation = new Operation("buy", new BigDecimal("15.00"), 5);

        BigDecimal weightedAvg = assetValuationService.calculateWeightedAvg(totalQtt, currWeightedAvg, operation);

        assertEquals(new BigDecimal("13.00"), weightedAvg);
    }

    @Test
    void testCalculateWeightedAvgWithZeroDenominator() {
        int totalQtt = 0;
        BigDecimal currWeightedAvg = BigDecimal.ZERO;
        Operation operation = new Operation("buy", new BigDecimal("15.00"), 0);

        BigDecimal weightedAvg = assetValuationService.calculateWeightedAvg(totalQtt, currWeightedAvg, operation);

        assertEquals(BigDecimal.ZERO.setScale(2), weightedAvg);
    }
}