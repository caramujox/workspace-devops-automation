package com.nu.challenge.domain.usecases;

import com.nu.challenge.adapters.strategy.OperationStrategyFactory;
import com.nu.challenge.domain.model.Asset;
import com.nu.challenge.domain.model.Operation;
import com.nu.challenge.domain.model.Tax;
import com.nu.challenge.domain.model.Wallet;
import com.nu.challenge.ports.strategy.OperationStrategy;
import com.nu.challenge.ports.usecases.ProcessOperationUseCase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static com.nu.challenge.domain.constants.ApplicationConstants.*;

public class ProcessOperationUseCaseImpl implements ProcessOperationUseCase {

    private OperationStrategyFactory operationStrategyFactory;

    public ProcessOperationUseCaseImpl(OperationStrategyFactory operationStrategyFactory) {
        this.operationStrategyFactory = operationStrategyFactory;
    }

    @Override
    public List<Tax> processOperations(List<Operation> operationList) {
        BigDecimal loss = BD_ZERO;
        Wallet wallet = new Wallet(loss, new Asset(0, BD_ZERO));
        List<Tax> taxList = new ArrayList<>();

        for(Operation operation: operationList){
            OperationStrategy strategy = operationStrategyFactory.getStrategy(operation.operation());
            Tax tax = strategy.executeOperation(wallet, operation);
            taxList.add(tax);
        }
        return taxList;
    }

}
