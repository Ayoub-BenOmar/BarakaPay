package org.ayoub.barakapay.model.mapper;

import org.ayoub.barakapay.model.dto.OperationDto;
import org.ayoub.barakapay.model.entity.Document;
import org.ayoub.barakapay.model.entity.Operation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OperationMapper {

    @Mapping(source = "accountSource.id", target = "accountSourceId")
    @Mapping(source = "accountDestination.id", target = "accountDestinationId")
    @Mapping(source = "documents", target = "documentIds", qualifiedByName = "documentsToIds")
    OperationDto toDto(Operation operation);

    @Mapping(source = "accountSourceId", target = "accountSource", qualifiedByName = "idToAccount")
    @Mapping(source = "accountDestinationId", target = "accountDestination", qualifiedByName = "idToAccount")
    @Mapping(source = "documentIds", target = "documents", qualifiedByName = "idsToDocuments")
    Operation toEntity(OperationDto dto);

    @Named("documentsToIds")
    default List<Integer> documentsToIds(List<Document> documents) {
        if (documents == null) return null;
        List<Integer> ids = new ArrayList<>();
        for (Document d : documents) ids.add(d.getId());
        return ids;
    }

    @Named("idsToDocuments")
    default List<Document> idsToDocuments(List<Integer> ids) {
        if (ids == null) return null;
        List<Document> docs = new ArrayList<>();
        for (Integer id : ids) {
            Document d = new Document();
            d.setId(id);
            docs.add(d);
        }
        return docs;
    }

    @Named("idToAccount")
    default org.ayoub.barakapay.model.entity.Account idToAccount(Integer id) {
        if (id == null) return null;
        org.ayoub.barakapay.model.entity.Account a = new org.ayoub.barakapay.model.entity.Account();
        a.setId(id);
        return a;
    }
}

