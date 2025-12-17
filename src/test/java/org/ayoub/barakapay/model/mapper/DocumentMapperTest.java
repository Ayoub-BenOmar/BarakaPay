package org.ayoub.barakapay.model.mapper;

import org.ayoub.barakapay.model.dto.DocumentDto;
import org.ayoub.barakapay.model.entity.Document;
import org.ayoub.barakapay.model.entity.Operation;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentMapperTest {

    private final DocumentMapper mapper = Mappers.getMapper(DocumentMapper.class);

    @Test
    public void toDto_happyPath() {
        Document d = new Document();
        d.setId(7);
        Operation o = new Operation(); o.setId(55);
        d.setOperation(o);

        DocumentDto dto = mapper.toDto(d);
        assertNotNull(dto);
        assertEquals(d.getId(), dto.getId());
        assertEquals(d.getOperation().getId(), dto.getOperationId());
    }

    @Test
    public void toDto_nullOperation() {
        Document d = new Document();
        d.setId(8);
        d.setOperation(null);

        DocumentDto dto = mapper.toDto(d);
        assertNotNull(dto);
        assertNull(dto.getOperationId());
    }
}

