package com.bank.dto;

public class AccountUpdateRequest {
    private String accountType;
    private Boolean active;

    public AccountUpdateRequest() {}

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}

