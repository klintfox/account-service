package com.bank.kafka.consumer;

import com.bank.kafka.event.TransactionCompletedEvent;
import com.bank.kafka.event.TransactionFailedEvent;
import com.bank.entity.Account;
import com.bank.entity.AccountAuditLog;
import com.bank.repository.AccountAuditLogRepository;
import com.bank.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import java.util.Optional;

@ApplicationScoped
public class TransactionEventConsumer {
    @Inject
    AccountRepository accountRepository;

    @Inject
    AccountAuditLogRepository auditLogRepository;

    @Inject
    ObjectMapper objectMapper;

    private static final Logger LOG = Logger.getLogger(TransactionEventConsumer.class);

    @Incoming("transactions-completed")
    @Transactional
    public void onTransactionCompleted(String json) {
        try {
            TransactionCompletedEvent event = objectMapper.readValue(json, TransactionCompletedEvent.class);
            LOG.infof("Procesando evento de transferencia completada: %s", event.transactionId);
            // Debitar cuenta origen
            Optional<Account> sourceOpt = accountRepository.findByAccountNumber(event.sourceAccountNumber);
            if (sourceOpt.isPresent()) {
                Account source = sourceOpt.get();
                source.setBalance(source.getBalance().subtract(event.amount));
                source.setUpdatedAt(java.time.LocalDateTime.now());
                // Registrar auditoría
                AccountAuditLog log = new AccountAuditLog();
                log.setAccountNumber(source.getAccountNumber());
                log.setEventType("TRANSFER_OUT");
                log.setEventTimestamp(java.time.LocalDateTime.now());
                log.setDetails("Transferencia enviada. ID: " + event.transactionId + ", Monto: " + event.amount);
                auditLogRepository.persist(log);
            }
            // Acreditar cuenta destino
            Optional<Account> destOpt = accountRepository.findByAccountNumber(event.destinationAccountNumber);
            if (destOpt.isPresent()) {
                Account dest = destOpt.get();
                dest.setBalance(dest.getBalance().add(event.amount));
                dest.setUpdatedAt(java.time.LocalDateTime.now());
                // Registrar auditoría
                AccountAuditLog log = new AccountAuditLog();
                log.setAccountNumber(dest.getAccountNumber());
                log.setEventType("TRANSFER_IN");
                log.setEventTimestamp(java.time.LocalDateTime.now());
                log.setDetails("Transferencia recibida. ID: " + event.transactionId + ", Monto: " + event.amount);
                auditLogRepository.persist(log);
            }
        } catch (Exception e) {
            LOG.error("Error al procesar evento de transferencia completada", e);
        }
    }

    @Incoming("transactions-failed")
    @Transactional
    public void onTransactionFailed(String json) {
        try {
            TransactionFailedEvent event = objectMapper.readValue(json, TransactionFailedEvent.class);
            LOG.warnf("Transferencia fallida: %s. Motivo: %s", event.transactionId, event.reason);
            // Registrar auditoría para cuenta origen
            Optional<Account> sourceOpt = accountRepository.findByAccountNumber(event.sourceAccountNumber);
            if (sourceOpt.isPresent()) {
                AccountAuditLog log = new AccountAuditLog();
                log.setAccountNumber(event.sourceAccountNumber);
                log.setEventType("TRANSFER_OUT");
                log.setEventTimestamp(java.time.LocalDateTime.now());
                log.setDetails("Transferencia fallida. Motivo: " + event.reason + ", ID: " + event.transactionId);
                auditLogRepository.persist(log);
            }
            // Registrar auditoría para cuenta destino
            Optional<Account> destOpt = accountRepository.findByAccountNumber(event.destinationAccountNumber);
            if (destOpt.isPresent()) {
                AccountAuditLog log = new AccountAuditLog();
                log.setAccountNumber(event.destinationAccountNumber);
                log.setEventType("TRANSFER_IN");
                log.setEventTimestamp(java.time.LocalDateTime.now());
                log.setDetails("Transferencia fallida. Motivo: " + event.reason + ", ID: " + event.transactionId);
                auditLogRepository.persist(log);
            }
        } catch (Exception e) {
            LOG.error("Error al procesar evento de transferencia fallida", e);
        }
    }
}
