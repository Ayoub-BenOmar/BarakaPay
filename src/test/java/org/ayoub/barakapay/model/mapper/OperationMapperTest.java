package org.ayoub.barakapay.model.mapper;

import org.ayoub.barakapay.model.dto.OperationDto;
import org.ayoub.barakapay.model.entity.Document;
import org.ayoub.barakapay.model.entity.Operation;
import org.ayoub.barakapay.model.entity.Account;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OperationMapperTest {

    private final OperationMapper mapper = Mappers.getMapper(OperationMapper.class);

    @Test
    public void toDto_happyPath() {
        Operation op = new Operation();
        op.setId(5);
        op.setAmount(BigDecimal.valueOf(250));
        Account src = new Account(); src.setId(20);
        Account dst = new Account(); dst.setId(30);
        op.setAccountSource(src);
        op.setAccountDestination(dst);
        Document d = new Document(); d.setId(99);
        op.setDocuments(List.of(d));

        OperationDto dto = mapper.toDto(op);
        assertNotNull(dto);
        assertEquals(op.getId(), dto.getId());
        assertEquals(20, dto.getAccountSourceId());
        assertEquals(30, dto.getAccountDestinationId());
        assertEquals(1, dto.getDocumentIds().size());
    }

    @Test
    public void toDto_nulls() {
        Operation op = new Operation();
        op.setId(6);
        op.setAccountSource(null);
        op.setAccountDestination(null);
        op.setDocuments(null);

        OperationDto dto = mapper.toDto(op);
        assertNotNull(dto);
        assertNull(dto.getAccountSourceId());
        assertNull(dto.getAccountDestinationId());
        assertNull(dto.getDocumentIds());
    }
}

