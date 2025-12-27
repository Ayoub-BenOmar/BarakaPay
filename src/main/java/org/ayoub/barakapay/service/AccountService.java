package org.ayoub.barakapay.service;

import org.ayoub.barakapay.model.entity.Account;
import org.ayoub.barakapay.model.entity.User;

public interface AccountService {
    Account addAccount(User user);
}
