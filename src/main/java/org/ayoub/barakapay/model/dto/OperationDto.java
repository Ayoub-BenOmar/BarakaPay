package org.ayoub.barakapay.model.dto;

import lombok.Data;
import org.ayoub.barakapay.enums.OperationStatus;
import org.ayoub.barakapay.enums.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OperationDto {
    private Integer id;
    private OperationType operationType;
    private BigDecimal amount;
    private OperationStatus operationStatus;
    private LocalDateTime createdAt;
    private LocalDateTime validatedAt;
    private LocalDateTime executedAt;
    private Integer accountSourceId;
    private Integer accountDestinationId;
    private List<Integer> documentIds;
}

