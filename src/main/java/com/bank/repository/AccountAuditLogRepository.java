package com.bank.repository;

import com.bank.entity.AccountAuditLog;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AccountAuditLogRepository implements PanacheRepository<AccountAuditLog> {
}

