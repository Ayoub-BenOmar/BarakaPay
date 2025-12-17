package org.ayoub.barakapay.model.mapper;

import org.ayoub.barakapay.model.dto.AccountDto;
import org.ayoub.barakapay.model.entity.Account;
import org.ayoub.barakapay.model.entity.Operation;
import org.ayoub.barakapay.model.entity.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AccountMapperTest {

    private final AccountMapper mapper = Mappers.getMapper(AccountMapper.class);

    @Test
    public void toDto_happyPath() {
        Account account = new Account();
        account.setId(10);
        account.setAccountNumber("ACC123");
        account.setBalance(BigDecimal.valueOf(1000));
        User client = new User();
        client.setId(7);
        account.setClient(client);
        Operation o1 = new Operation(); o1.setId(101);
        Operation o2 = new Operation(); o2.setId(102);
        account.setOutgoingOperations(List.of(o1));
        account.setIncomingOperations(List.of(o2));

        AccountDto dto = mapper.toDto(account);
        assertNotNull(dto);
        assertEquals(account.getId(), dto.getId());
        assertEquals(account.getClient().getId(), dto.getClientId());
        assertEquals(1, dto.getOutgoingOperationIds().size());
        assertEquals(1, dto.getIncomingOperationIds().size());
    }

    @Test
    public void toDto_nullLists() {
        Account account = new Account();
        account.setId(11);
        account.setClient(null);
        account.setOutgoingOperations(null);
        account.setIncomingOperations(null);

        AccountDto dto = mapper.toDto(account);
        assertNotNull(dto);
        assertNull(dto.getClientId());
        assertNull(dto.getOutgoingOperationIds());
        assertNull(dto.getIncomingOperationIds());
    }
}

