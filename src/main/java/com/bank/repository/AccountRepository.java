package com.bank.repository;

import com.bank.entity.Account;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class AccountRepository implements PanacheRepository<Account> {
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return find("accountNumber", accountNumber).firstResultOptional();
    }

    public List<Account> findByCustomerId(UUID customerId) {
        return list("customerId", customerId);
    }
}
