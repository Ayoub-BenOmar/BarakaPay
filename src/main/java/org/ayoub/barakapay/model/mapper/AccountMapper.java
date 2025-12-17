package org.ayoub.barakapay.model.mapper;

import org.ayoub.barakapay.model.dto.AccountDto;
import org.ayoub.barakapay.model.entity.Account;
import org.ayoub.barakapay.model.entity.Operation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "outgoingOperations", target = "outgoingOperationIds", qualifiedByName = "operationsToIds")
    @Mapping(source = "incomingOperations", target = "incomingOperationIds", qualifiedByName = "operationsToIds")
    AccountDto toDto(Account account);

    @Mapping(source = "clientId", target = "client", qualifiedByName = "idToUser")
    @Mapping(source = "outgoingOperationIds", target = "outgoingOperations", qualifiedByName = "idsToOperations")
    @Mapping(source = "incomingOperationIds", target = "incomingOperations", qualifiedByName = "idsToOperations")
    Account toEntity(AccountDto dto);

    @Named("operationsToIds")
    default List<Integer> operationsToIds(List<Operation> operations) {
        if (operations == null) return null;
        List<Integer> ids = new ArrayList<>();
        for (Operation o : operations) {
            ids.add(o.getId());
        }
        return ids;
    }

    @Named("idsToOperations")
    default List<Operation> idsToOperations(List<Integer> ids) {
        if (ids == null) return null;
        List<Operation> ops = new ArrayList<>();
        for (Integer id : ids) {
            Operation o = new Operation();
            o.setId(id);
            ops.add(o);
        }
        return ops;
    }

    @Named("idToUser")
    default org.ayoub.barakapay.model.entity.User idToUser(Integer id) {
        if (id == null) return null;
        org.ayoub.barakapay.model.entity.User u = new org.ayoub.barakapay.model.entity.User();
        u.setId(id);
        return u;
    }
}

