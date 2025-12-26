package com.bank.exception;

import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {
    @Override
    public Response toResponse(ValidationException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("Validation error: " + exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    public static class ErrorResponse {
        public String error;
        public ErrorResponse(String error) {
            this.error = error;
        }
    }
}

