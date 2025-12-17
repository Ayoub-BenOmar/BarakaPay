package org.ayoub.barakapay.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AccountDto {
    private Integer id;
    private String accountNumber;
    private BigDecimal balance;
    private Integer clientId;
    private List<Integer> outgoingOperationIds;
    private List<Integer> incomingOperationIds;
}
