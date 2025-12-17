package org.ayoub.barakapay.service;

import org.ayoub.barakapay.model.entity.Account;

public interface AccountService {
    Account addAccount(Account account);
    void activateAccount(Integer accountId);
}
