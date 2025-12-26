package com.bank.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "account_audit_log")
public class AccountAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "account_number", nullable = false, length = 30)
    public String accountNumber;

    @Column(name = "event_type", nullable = false, length = 30)
    public String eventType;

    @Column(name = "event_timestamp", nullable = false)
    public LocalDateTime eventTimestamp;

    @Column(name = "details")
    public String details;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public LocalDateTime getEventTimestamp() { return eventTimestamp; }
    public void setEventTimestamp(LocalDateTime eventTimestamp) { this.eventTimestamp = eventTimestamp; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}

