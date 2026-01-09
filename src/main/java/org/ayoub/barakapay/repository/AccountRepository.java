package org.ayoub.barakapay.repository;

import org.ayoub.barakapay.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    public Account findByClientId(Integer clientId);
    public Optional<Account> findByAccountNumber(String accountNumber);
}
