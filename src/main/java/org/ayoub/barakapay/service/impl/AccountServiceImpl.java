package org.ayoub.barakapay.service.impl;

import lombok.RequiredArgsConstructor;
import org.ayoub.barakapay.helpers.GenerateAccountNumber;
import org.ayoub.barakapay.model.dto.AccountDto;
import org.ayoub.barakapay.model.entity.Account;
import org.ayoub.barakapay.model.entity.User;
import org.ayoub.barakapay.model.mapper.AccountMapper;
import org.ayoub.barakapay.repository.AccountRepository;
import org.ayoub.barakapay.service.AccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    public Account addAccount(User user) {
        Account account = new Account();
        account.setAccountNumber(GenerateAccountNumber.generateAccountNumber());
        account.setClient(user);
        account.setBalance(BigDecimal.valueOf(0.0));
        account = accountRepository.save(account);
        return account;
    }

    @Override
    public Account findById(Integer id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public Account findAccountByClientId(Integer clientId) {
        return accountRepository.findByClientId(clientId);
    }
}
