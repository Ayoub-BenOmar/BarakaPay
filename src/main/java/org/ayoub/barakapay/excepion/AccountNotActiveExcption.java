package org.ayoub.barakapay.excepion;

public class AccountNotActiveExcption extends RuntimeException {
    public AccountNotActiveExcption(String message) {
        super(message);
    }
}
