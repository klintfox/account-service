package com.bank.kafka.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionFailedEvent {
    public String transactionId;
    public String sourceAccountNumber;
    public String destinationAccountNumber;
    public BigDecimal amount;
    public LocalDateTime timestamp;
    public String reason;
}

