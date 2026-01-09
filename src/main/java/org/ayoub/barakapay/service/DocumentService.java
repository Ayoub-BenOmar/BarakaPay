package org.ayoub.barakapay.service;

import org.ayoub.barakapay.model.dto.DocumentDto;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {
    public DocumentDto uploadDocument(Integer operationId, MultipartFile file);

}
