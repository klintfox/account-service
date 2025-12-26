package com.bank.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountRequest {
    private String accountNumber;
    private UUID customerId;
    private String accountType;
    private BigDecimal balance;

    public AccountRequest() {}

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public UUID getCustomerId() { return customerId; }
    public void setCustomerId(UUID customerId) { this.customerId = customerId; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}
