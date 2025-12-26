package com.bank.kafka.event;

import java.time.LocalDateTime;
import java.math.BigDecimal;

public class TransactionCompletedEvent {
    public String description;
    public LocalDateTime timestamp;
    public BigDecimal amount;
    public String destinationAccountNumber;
    public String sourceAccountNumber;
    public String transactionId;

    public TransactionCompletedEvent() {}

    public TransactionCompletedEvent(String description, LocalDateTime timestamp, BigDecimal amount, String destinationAccountNumber, String sourceAccountNumber, String transactionId) {
        this.description = description;
        this.timestamp = timestamp;
        this.amount = amount;
        this.destinationAccountNumber = destinationAccountNumber;
        this.sourceAccountNumber = sourceAccountNumber;
        this.transactionId = transactionId;
    }
}
