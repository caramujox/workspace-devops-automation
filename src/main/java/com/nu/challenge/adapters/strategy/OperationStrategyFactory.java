package com.nu.challenge.adapters.strategy;

import com.nu.challenge.domain.usecases.assetValuation.AssetValuationService;
import com.nu.challenge.ports.strategy.OperationStrategy;

import java.util.Map;

import static com.nu.challenge.domain.constants.ApplicationConstants.BUY_OPERATION;
import static com.nu.challenge.domain.constants.ApplicationConstants.SELL_OPERATION;

public class OperationStrategyFactory{
    Map<String, OperationStrategy> strategyMap;

    public OperationStrategyFactory() {
        strategyMap = Map.of(
                BUY_OPERATION, new BuyOperationStrategy(new AssetValuationService()),
                SELL_OPERATION, new SellOperationStrategy(new AssetValuationService(), new AssetValuationService()));
    }

    public OperationStrategy getStrategy(String operation){
        if(operation == null || !strategyMap.containsKey(operation)){
            throw new IllegalArgumentException("This operation is not a valid operation");
        }
        return strategyMap.get(operation);
    }
}

