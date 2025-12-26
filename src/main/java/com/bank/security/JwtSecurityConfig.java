package com.bank.security;

import io.quarkus.security.Authenticated;
import jakarta.ws.rs.ext.Provider;

/**
 * Configuración base para habilitar autenticación JWT en Quarkus.
 * No requiere implementación adicional, pero sirve como referencia para futuras extensiones.
 */
@Provider
@Authenticated
public class JwtSecurityConfig {
    // Clase de marcador para activar la seguridad JWT
}
