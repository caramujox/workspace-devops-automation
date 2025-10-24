package com.nu.challenge;

import com.nu.challenge.adapters.inbound.FileScanner;
import com.nu.challenge.adapters.strategy.OperationStrategyFactory;
import com.nu.challenge.domain.usecases.ProcessOperationUseCaseImpl;
import com.nu.challenge.ports.usecases.ProcessOperationUseCase;

public class Main {
    public static void main(String[] args) {
        ProcessOperationUseCase processOperationUseCase = new ProcessOperationUseCaseImpl(new OperationStrategyFactory());
        FileScanner fileScanner = new FileScanner(processOperationUseCase);
        fileScanner.processInput();
    }
}