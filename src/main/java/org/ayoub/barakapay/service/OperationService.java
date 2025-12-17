package org.ayoub.barakapay.service;

import org.ayoub.barakapay.enums.OperationStatus;
import org.ayoub.barakapay.model.dto.OperationDto;

import java.math.BigDecimal;

public interface OperationService {
    OperationDto deposit(Integer accountId, BigDecimal amount);
    OperationDto withdraw(Integer accountId, BigDecimal amount);
    OperationDto transfer(Integer fromAccountId, Integer toAccountId, BigDecimal amount);
    OperationDto getAllOperations();
    void updateOperationStatus(Integer operationId, OperationStatus status);
}
