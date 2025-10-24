package com.nu.challenge.adapters.inbound;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nu.challenge.domain.model.Operation;
import com.nu.challenge.domain.model.Tax;
import com.nu.challenge.ports.usecases.ProcessOperationUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;

import static com.nu.challenge.domain.constants.ApplicationConstants.BD_ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FileScannerTest {

    private ProcessOperationUseCase processOperationUseCase;
    private FileScanner fileScanner;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        processOperationUseCase = mock(ProcessOperationUseCase.class);
        fileScanner = new FileScanner(processOperationUseCase);
        objectMapper = new ObjectMapper();
    }

    @Test
    void testProcessInputWithValidJson() throws JsonProcessingException {
        // Arrange
        String inputJson = "[{\"operation\":\"buy\",\"unit-cost\":10.00,\"quantity\":5}]";
        List<Operation> operations = List.of(new Operation("buy", new BigDecimal("10.00"), 5));
        List<Tax> taxes = List.of(new Tax(BD_ZERO));
        when(processOperationUseCase.processOperations(operations)).thenReturn(taxes);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputJson.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setIn(inputStream);
        System.setOut(new PrintStream(outputStream));

        // Act
        fileScanner.processInput();

        // Assert
        String expectedOutput = objectMapper.writeValueAsString(taxes) + System.lineSeparator();
        assertEquals(expectedOutput, outputStream.toString());
        verify(processOperationUseCase).processOperations(operations);
    }

    @Test
    void testProcessInputWithInvalidJson() {
        // Arrange
        String invalidJson = "invalid_json";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(invalidJson.getBytes());
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        System.setIn(inputStream);
        System.setErr(new PrintStream(errorStream));

        // Act
        fileScanner.processInput();

        // Assert
        String expectedError = "Invalid JSON input: " + invalidJson + System.lineSeparator();
        assertEquals(expectedError, errorStream.toString());
        verifyNoInteractions(processOperationUseCase);
    }
}