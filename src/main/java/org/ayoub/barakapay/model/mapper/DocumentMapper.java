package org.ayoub.barakapay.model.mapper;

import org.ayoub.barakapay.model.dto.DocumentDto;
import org.ayoub.barakapay.model.entity.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    @Mapping(source = "operation.id", target = "operationId")
    DocumentDto toDto(Document document);

    @Mapping(source = "operationId", target = "operation", qualifiedByName = "idToOperation")
    Document toEntity(DocumentDto dto);

    @Named("idToOperation")
    default org.ayoub.barakapay.model.entity.Operation idToOperation(Integer id) {
        if (id == null) return null;
        org.ayoub.barakapay.model.entity.Operation o = new org.ayoub.barakapay.model.entity.Operation();
        o.setId(id);
        return o;
    }
}

