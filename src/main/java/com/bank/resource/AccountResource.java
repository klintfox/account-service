package com.bank.resource;

import com.bank.dto.AccountRequest;
import com.bank.dto.AccountUpdateRequest;
import com.bank.entity.Account;
import com.bank.repository.AccountRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Optional;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountResource {

    private static final Logger Log = Logger.getLogger(AccountResource.class);

    @Inject
    AccountRepository accountRepository;

    @GET
    @Path("/{accountNumber}")
    @RolesAllowed({"USER", "ADMIN"})
    public Response getAccountByNumber(@PathParam("accountNumber") String accountNumber) {
        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);
        if (account.isEmpty()) {
            throw new NotFoundException("Account with number " + accountNumber + " not found");
        }
        return Response.ok(account.get()).build();
    }

    @GET
    @RolesAllowed({"USER", "ADMIN"})
    public Response getAccountsByCustomerId(@QueryParam("customerId") java.util.UUID customerId) {
        if (customerId == null) {
            throw new BadRequestException("customerId is required");
        }
        List<Account> accounts = accountRepository.findByCustomerId(customerId);
        return Response.ok(accounts).build();
    }

    @GET
    @Path("/{accountNumber}/balance")
    @RolesAllowed({"USER", "ADMIN"})
    public Response getAccountBalance(@PathParam("accountNumber") String accountNumber) {
        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);
        if (account.isEmpty()) {
            throw new NotFoundException("Account with number " + accountNumber + " not found");
        }
        return Response.ok(account.get().getBalance()).build();
    }

    @POST
    @Transactional
    @RolesAllowed("ADMIN")
    public Response createAccount(@Valid AccountRequest dto) {
        Log.info("Creating account for customer ID: " + dto.getCustomerId());

        if (dto.getAccountNumber() == null || dto.getAccountNumber().isBlank()) {
            throw new BadRequestException("El número de cuenta es obligatorio");
        }
        if (dto.getCustomerId() == null) {
            throw new BadRequestException("El ID del cliente es obligatorio");
        }
        if (dto.getAccountType() == null || !(dto.getAccountType().equals("C")
                || dto.getAccountType().equals("M"))) {
            throw new BadRequestException("El tipo de cuenta debe ser 'C' o 'M'");
        }
        if (accountRepository.findByAccountNumber(dto.getAccountNumber()).isPresent()) {
            throw new BadRequestException("Ya existe una cuenta con ese número");
        }
        // Inicializar valores por defecto
        Account account = new Account();
        account.setAccountNumber(dto.getAccountNumber());
        account.setCustomerId(dto.getCustomerId());
        account.setAccountType(dto.getAccountType());
        account.setBalance(dto.getBalance() != null ? dto.getBalance() : java.math.BigDecimal.ZERO);
        account.setActive(true);
        account.setCreatedAt(java.time.LocalDateTime.now());
        account.setUpdatedAt(java.time.LocalDateTime.now());
        accountRepository.persist(account);
        return Response.status(Response.Status.CREATED).entity(account).build();
    }

    @PATCH
    @Path("/{accountNumber}/status")
    @Transactional
    @RolesAllowed("ADMIN")
    public Response changeAccountStatus(@PathParam("accountNumber") String accountNumber, @QueryParam("active") boolean active) {
        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);
        if (account.isEmpty()) {
            throw new NotFoundException("Account with number " + accountNumber + " not found");
        }
        account.get().setActive(active);
        account.get().setUpdatedAt(java.time.LocalDateTime.now());
        return Response.ok(account.get()).build();
    }

    @PUT
    @Path("/{accountNumber}")
    @Transactional
    @RolesAllowed("ADMIN")
    public Response updateAccount(@PathParam("accountNumber") String accountNumber, AccountUpdateRequest dto) {
        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);
        if (account.isEmpty()) {
            throw new NotFoundException("Account with number " + accountNumber + " not found");
        }
        Account acc = account.get();
        if (dto.getAccountType() != null && (dto.getAccountType().equals("C") || dto.getAccountType().equals("M"))) {
            acc.setAccountType(dto.getAccountType());
        }
        if (dto.getActive() != null) {
            acc.setActive(dto.getActive());
        }
        acc.setUpdatedAt(java.time.LocalDateTime.now());
        return Response.ok(acc).build();
    }
}
