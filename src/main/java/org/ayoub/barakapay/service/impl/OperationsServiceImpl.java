package org.ayoub.barakapay.service.impl;

import lombok.RequiredArgsConstructor;
import org.ayoub.barakapay.enums.OperationStatus;
import org.ayoub.barakapay.enums.OperationType;
import org.ayoub.barakapay.enums.Role;
import org.ayoub.barakapay.excepion.AccountNotFoundException;
import org.ayoub.barakapay.model.dto.OperationDto;
import org.ayoub.barakapay.model.entity.Account;
import org.ayoub.barakapay.model.entity.Operation;
import org.ayoub.barakapay.model.entity.User;
import org.ayoub.barakapay.model.mapper.OperationMapper;
import org.ayoub.barakapay.repository.AccountRepository;
import org.ayoub.barakapay.repository.OperationRepository;
import org.ayoub.barakapay.service.OperationService;
import org.ayoub.barakapay.service.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationsServiceImpl implements OperationService {

    private final OperationRepository operationRepository;
    private final OperationMapper operationMapper;
    private final AccountServiceImpl accountService;
    private final AccountRepository accountRepository;

    @Override
    public OperationDto deposit(BigDecimal amount) throws IOException {
        User currentUser = getUser();

        Account account = accountService.findAccountByClientId(currentUser.getId());

        Operation operation = new Operation();
        operation.setOperationType(OperationType.DEPOSIT);
        operation.setAmount(amount);
        operation.setOperationStatus(OperationStatus.PENDING);
        operation.setAccountDestination(account);

        if (amount.compareTo(new BigDecimal("10000")) <= 0) {
            account.setBalance(account.getBalance().add(amount));
            operation.setOperationStatus(OperationStatus.DONE);
        }
        else {
            operation.setOperationStatus(OperationStatus.PENDING);
        }

        operationRepository.save(operation);
        accountRepository.save(account);
        return operationMapper.toDto(operation);
    }

    @Override
    public OperationDto withdraw(BigDecimal amount) {
        User currentUser = getUser();

        Account account = accountService.findAccountByClientId(currentUser.getId());

        Operation operation = new Operation();
        operation.setOperationType(OperationType.WITHDRAWAL);
        operation.setAmount(amount);
        operation.setOperationStatus(OperationStatus.PENDING);
        operation.setAccountDestination(account);

        if(account.getBalance().compareTo(amount) < 0){
            throw new RuntimeException("Insufficient balance for withdrawal");
        }

        if (amount.compareTo(new BigDecimal("10000")) <= 0) {
            account.setBalance(account.getBalance().subtract(amount));
            operation.setOperationStatus(OperationStatus.DONE);
        }
        else {
            operation.setOperationStatus(OperationStatus.PENDING);
        }

        operationRepository.save(operation);
        accountRepository.save(account);
        return operationMapper.toDto(operation);
    }

    @Override
    public OperationDto transfer(String toAccount, BigDecimal amount) {
        User currentUser = getUser();

        Account sourceAccount = accountService.findAccountByClientId(currentUser.getId());
        Account destinationAccount = accountRepository
                .findByAccountNumber(toAccount)
                .orElseThrow(() -> new AccountNotFoundException("Destination account not found"));

        if (sourceAccount.equals(destinationAccount)) {
            throw new RuntimeException("Cannot transfer to the same account");
        }

        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        Operation operation = new Operation();
        operation.setOperationType(OperationType.TRANSFER);
        operation.setAmount(amount);
        operation.setAccountSource(sourceAccount);
        operation.setAccountDestination(destinationAccount);

        if (amount.compareTo(BigDecimal.valueOf(10000)) <= 0) {

            sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
            destinationAccount.setBalance(destinationAccount.getBalance().add(amount));

            accountRepository.save(sourceAccount);
            accountRepository.save(destinationAccount);

            operation.setOperationStatus(OperationStatus.DONE);

        }
        else {
            operation.setOperationStatus(OperationStatus.PENDING);
        }

        operationRepository.save(operation);

        return operationMapper.toDto(operation);
    }

    @Override
    public List<OperationDto> getAllOperations() {
        return operationRepository.findAll()
                .stream()
                .map(operationMapper::toDto)
                .toList();
    }

    @Override
    public List<OperationDto> getPendingOperations() {
        return operationRepository.findAll()
                .stream()
                .filter(operation -> operation.getOperationStatus().equals(OperationStatus.PENDING))
                .map(operationMapper::toDto)
                .toList();
    }

    @Override
    public void approveOperation(Integer operationId) {
        Operation operation = operationRepository.findById(operationId)
                .orElseThrow(() -> new RuntimeException("Operation not found with id: " + operationId)) ;

        if (operation.getOperationStatus() != OperationStatus.PENDING) {
            throw new RuntimeException("Operation is not pending");
        }

        switch (operation.getOperationType()) {
            case DEPOSIT -> {
                Account destinationAccount = operation.getAccountDestination();
                destinationAccount.setBalance(destinationAccount.getBalance().add(operation.getAmount()));
                accountRepository.save(destinationAccount);
            }
            case WITHDRAWAL -> {
                Account sourceAccount = operation.getAccountDestination();
                if (sourceAccount.getBalance().compareTo(operation.getAmount()) < 0) {
                    throw new RuntimeException("Insufficient balance for withdrawal");
                }
                sourceAccount.setBalance(sourceAccount.getBalance().subtract(operation.getAmount()));
                accountRepository.save(sourceAccount);
            }

            case TRANSFER -> {
                Account sourceAccount = operation.getAccountSource();
                Account destinationAccount = operation.getAccountDestination();

                if (sourceAccount.getBalance().compareTo(operation.getAmount()) < 0) {
                    throw new RuntimeException("Insufficient balance for transfer");
                }

                sourceAccount.setBalance(sourceAccount.getBalance().subtract(operation.getAmount()));
                destinationAccount.setBalance(destinationAccount.getBalance().add(operation.getAmount()));

                accountRepository.save(sourceAccount);
                accountRepository.save(destinationAccount);
            }
        }

        operation.setOperationStatus(OperationStatus.DONE);
        operation.setExecutedAt(LocalDateTime.now());
        operation.setValidatedAt(LocalDateTime.now());
        operationRepository.save(operation);
    }

    @Override
    public void rejectOperation(Integer operationId) {
        Operation operation = operationRepository.findById(operationId)
                .orElseThrow(() -> new RuntimeException("Operation not found with id: " + operationId)) ;

        if (operation.getOperationStatus() != OperationStatus.PENDING) {
            throw new RuntimeException("Operation is not pending");
        }

        operation.setOperationStatus(OperationStatus.REJECTED);
        operation.setExecutedAt(LocalDateTime.now());
        operation.setValidatedAt(LocalDateTime.now());
        operationRepository.save(operation);
    }


    private static User getUser() {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();

        if (authUser == null || !(authUser.getPrincipal() instanceof CustomUserDetails customUserDetails)) {
            throw new RuntimeException("User not authenticated");
        }

        User currentUser = customUserDetails.getUser();

        if (!currentUser.isActive() || currentUser.getRole() != Role.ROLE_CLIENT) {
            throw new RuntimeException("User not authorized to perform withdrawal");
        }
        return currentUser;
    }
}
