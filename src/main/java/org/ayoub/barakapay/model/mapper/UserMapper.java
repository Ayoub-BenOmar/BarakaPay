package org.ayoub.barakapay.model.mapper;

import org.ayoub.barakapay.model.dto.UserDto;
import org.ayoub.barakapay.model.entity.Account;
import org.ayoub.barakapay.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "account.id", target = "accountId")
    UserDto toDto(User user);

    @Mapping(source = "accountId", target = "account", qualifiedByName = "idToAccount")
    User toEntity(UserDto dto);

    @Named("idToAccount")
    default Account idToAccount(Integer id) {
        if (id == null) return null;
        Account a = new Account();
        a.setId(id);
        return a;
    }
}

