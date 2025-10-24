package com.nu.challenge.ports.usecases;

import com.nu.challenge.domain.model.Operation;
import com.nu.challenge.domain.model.Tax;

import java.util.List;

public interface ProcessOperationUseCase {
    List<Tax> processOperations(List<Operation> operationList);
}
