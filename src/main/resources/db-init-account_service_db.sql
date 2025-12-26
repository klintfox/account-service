-- Script de creación de tablas para account_service_db (PostgreSQL)

-- Tabla principal de cuentas
CREATE TABLE account (
    account_number VARCHAR(30) PRIMARY KEY,
    customer_id UUID NOT NULL,
    balance NUMERIC(18,2) NOT NULL DEFAULT 0,
    account_type CHAR(1) NOT NULL CHECK (account_type IN ('C', 'M')),
    is_active BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índice para búsquedas por cliente
CREATE INDEX idx_account_customer_id ON account(customer_id);

-- Tabla de auditoría de operaciones sobre cuentas (opcional, para trazabilidad)
CREATE TABLE account_audit_log (
    id SERIAL PRIMARY KEY,
    account_number VARCHAR(30) NOT NULL,
    event_type VARCHAR(30) NOT NULL CHECK (event_type IN (
        'CREATE',
        'UPDATE',
        'DEPOSIT',
        'WITHDRAWAL',
        'STATUS_CHANGE',
        'CLOSE',
        'TRANSFER_IN',
        'TRANSFER_OUT'
    )),
    event_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    details TEXT,
    CONSTRAINT fk_audit_account FOREIGN KEY (account_number) REFERENCES account(account_number)
);

-- Restricciones adicionales pueden agregarse según reglas de negocio (por ejemplo, saldo >= 0, status válido, etc.)
