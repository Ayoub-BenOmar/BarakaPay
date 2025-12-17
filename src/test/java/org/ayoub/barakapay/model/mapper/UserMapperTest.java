package org.ayoub.barakapay.model.mapper;

import org.ayoub.barakapay.enums.Role;
import org.ayoub.barakapay.model.dto.UserDto;
import org.ayoub.barakapay.model.entity.Account;
import org.ayoub.barakapay.model.entity.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    public void toDto_happyPath() {
        User user = new User();
        user.setId(1);
        user.setEmail("a@b.com");
        user.setFullName("A B");
        user.setRole(Role.CLIENT);
        user.setCreatedAt(LocalDateTime.now());
        Account account = new Account();
        account.setId(42);
        user.setAccount(account);

        UserDto dto = mapper.toDto(user);

        assertNotNull(dto);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getFullName(), dto.getFullName());
        assertEquals(user.getRole(), dto.getRole());
        assertEquals(user.getAccount().getId(), dto.getAccountId());
    }

    @Test
    public void toDto_nullAccount() {
        User user = new User();
        user.setId(2);
        user.setEmail("x@y.com");
        user.setAccount(null);

        UserDto dto = mapper.toDto(user);
        assertNotNull(dto);
        assertNull(dto.getAccountId());
    }
}

