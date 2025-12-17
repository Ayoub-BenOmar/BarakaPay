package org.ayoub.barakapay.model.dto;

import lombok.Data;
import org.ayoub.barakapay.enums.FileType;

import java.time.LocalDateTime;

@Data
public class DocumentDto {
    private Integer id;
    private String fileName;
    private FileType fileType;
    private String storagePath;
    private LocalDateTime uploadedAt;
    private Integer operationId;
}

