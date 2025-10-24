package com.nu.challenge.domain.usecases;

import com.nu.challenge.adapters.strategy.OperationStrategyFactory;
import com.nu.challenge.domain.model.Operation;
import com.nu.challenge.domain.model.Tax;
import com.nu.challenge.domain.model.Wallet;
import com.nu.challenge.ports.strategy.OperationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.nu.challenge.domain.constants.ApplicationConstants.BD_ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProcessOperationUseCaseImplTest {

    private OperationStrategyFactory operationStrategyFactory;
    private ProcessOperationUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        operationStrategyFactory = mock(OperationStrategyFactory.class);
        useCase = new ProcessOperationUseCaseImpl(operationStrategyFactory);
    }

    @Test
    void testBuyOperation() {
        Operation buy = new Operation("buy", new BigDecimal("10.00"), 5);
        OperationStrategy mockStrategy = mock(OperationStrategy.class);

        when(operationStrategyFactory.getStrategy("buy")).thenReturn(mockStrategy);
        when(mockStrategy.executeOperation(any(Wallet.class), eq(buy))).thenReturn(new Tax(BD_ZERO));

        List<Tax> taxes = useCase.processOperations(List.of(buy));

        assertEquals(1, taxes.size());
        assertEquals(BD_ZERO, taxes.get(0).value());
        verify(operationStrategyFactory).getStrategy("buy");
        verify(mockStrategy).executeOperation(any(Wallet.class), eq(buy));
    }

    @Test
    void testSellWithProfit() {
        Operation sell = new Operation("sell", new BigDecimal("30.00"), 10);
        OperationStrategy mockStrategy = mock(OperationStrategy.class);

        when(operationStrategyFactory.getStrategy("sell")).thenReturn(mockStrategy);
        when(mockStrategy.executeOperation(any(Wallet.class), eq(sell))).thenReturn(new Tax(new BigDecimal("15.00")));

        List<Tax> taxes = useCase.processOperations(List.of(sell));

        assertEquals(1, taxes.size());
        assertEquals(new BigDecimal("15.00"), taxes.get(0).value());
        verify(operationStrategyFactory).getStrategy("sell");
        verify(mockStrategy).executeOperation(any(Wallet.class), eq(sell));
    }
}