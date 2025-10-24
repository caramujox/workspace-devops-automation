package com.nu.challenge.adapters.strategy;

import com.nu.challenge.ports.strategy.OperationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.nu.challenge.domain.constants.ApplicationConstants.BUY_OPERATION;
import static com.nu.challenge.domain.constants.ApplicationConstants.SELL_OPERATION;
import static org.junit.jupiter.api.Assertions.*;

class OperationStrategyFactoryTest {

    private OperationStrategyFactory operationStrategyFactory;

    @BeforeEach
    void setUp() {
        operationStrategyFactory = new OperationStrategyFactory();
    }

    @Test
    void testGetStrategyWithBuyOperation() {
        OperationStrategy strategy = operationStrategyFactory.getStrategy(BUY_OPERATION);
        assertNotNull(strategy);
        assertTrue(strategy instanceof BuyOperationStrategy);
    }

    @Test
    void testGetStrategyWithSellOperation() {
        OperationStrategy strategy = operationStrategyFactory.getStrategy(SELL_OPERATION);
        assertNotNull(strategy);
        assertTrue(strategy instanceof SellOperationStrategy);
    }

    @Test
    void testGetStrategyWithInvalidOperation() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                operationStrategyFactory.getStrategy("invalid_operation"));
        assertEquals("This operation is not a valid operation", exception.getMessage());
    }

    @Test
    void testGetStrategyWithNullOperation() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                operationStrategyFactory.getStrategy(null));
        assertEquals("This operation is not a valid operation", exception.getMessage());
    }
}