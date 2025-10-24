package com.nu.challenge.adapters.inbound;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nu.challenge.domain.model.Operation;
import com.nu.challenge.domain.model.Tax;
import com.nu.challenge.ports.usecases.ProcessOperationUseCase;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public record FileScanner(ProcessOperationUseCase processOperationUseCase) {

    public void processInput() {
        ObjectMapper objectMapper = new ObjectMapper();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            try {
                List<Operation> operationList = objectMapper.readValue(
                        line, new TypeReference<>(){}
                );
               List<Tax> taxList = processOperationUseCase.processOperations(operationList);

                System.out.println(objectMapper.writeValueAsString(taxList));
            } catch (IOException ex) {
                System.err.println("Invalid JSON input " + line);
            }
        }
        scanner.close();
    }
}
