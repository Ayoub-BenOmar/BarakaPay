package org.ayoub.barakapay.service;

import org.ayoub.barakapay.enums.OperationStatus;
import org.ayoub.barakapay.model.dto.OperationDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface OperationService {
    OperationDto deposit(BigDecimal amount) throws IOException;
    OperationDto withdraw(BigDecimal amount);
    OperationDto transfer(String toAccount, BigDecimal amount);
    List<OperationDto> getAllOperations();
    List<OperationDto> getPendingOperations();
    void approveOperation(Integer operationId);
    void rejectOperation(Integer operationId);
}
