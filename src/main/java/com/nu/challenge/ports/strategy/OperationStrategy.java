package com.nu.challenge.ports.strategy;

import com.nu.challenge.domain.model.Operation;
import com.nu.challenge.domain.model.Tax;
import com.nu.challenge.domain.model.Wallet;

public interface OperationStrategy {
    Tax executeOperation(Wallet wallet, Operation operation);
}
