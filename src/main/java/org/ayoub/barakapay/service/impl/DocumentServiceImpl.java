package org.ayoub.barakapay.service.impl;

import lombok.RequiredArgsConstructor;
import org.ayoub.barakapay.enums.FileType;
import org.ayoub.barakapay.enums.Role;
import org.ayoub.barakapay.model.dto.DocumentDto;
import org.ayoub.barakapay.model.entity.Document;
import org.ayoub.barakapay.model.entity.Operation;
import org.ayoub.barakapay.model.entity.User;
import org.ayoub.barakapay.repository.DocumentRepository;
import org.ayoub.barakapay.repository.OperationRepository;
import org.ayoub.barakapay.service.DocumentService;
import org.ayoub.barakapay.service.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final OperationRepository operationRepository;
    private final DocumentRepository documentRepository;
    private final String uploadDir = "uploads/documents/";

    @Override
    public DocumentDto uploadDocument(Integer operationId, MultipartFile file) {

        Operation operation = operationRepository.findById(operationId)
                .orElseThrow(() -> new RuntimeException("Operation not found"));

        User currentUser = getUser();

        if (!operation.getAccountDestination().getClient().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not allowed to upload a document for this operation");
        }

        try {
            Files.createDirectories(Paths.get(uploadDir));

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName); // ✅ correct path

            Files.write(filePath, file.getBytes());

            Document document = new Document();
            document.setFileName(fileName);

            // map content type / extension to enum FileType
            FileType fileTypeEnum = mapContentTypeToEnum(file.getContentType(), file.getOriginalFilename());
            document.setFileType(fileTypeEnum);

            document.setStoragePath(filePath.toString());
            document.setOperation(operation);

            documentRepository.save(document);

            operation.getDocuments().add(document);
            operationRepository.save(operation);

            DocumentDto dto = new DocumentDto();
            dto.setFileName(document.getFileName());
            dto.setFileType(document.getFileType());
            dto.setUploadedAt(document.getUploadedAt());

            return dto;

        } catch (IOException e) {
            throw new RuntimeException("Error uploading file", e);
        }
    }


    private static User getUser() {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();

        if (authUser == null || !(authUser.getPrincipal() instanceof CustomUserDetails customUserDetails)) {
            throw new RuntimeException("User not authenticated");
        }

        User currentUser = customUserDetails.getUser();

        if (!currentUser.isActive() || currentUser.getRole() != Role.ROLE_CLIENT) {
            throw new RuntimeException("User not authorized to perform withdrawal");
        }
        return currentUser;
    }

    // utilitaire pour convertir le contentType / extension en FileType
    private FileType mapContentTypeToEnum(String contentType, String originalFilename) {
        String name = originalFilename == null ? "" : originalFilename.toLowerCase();
        if (contentType != null) {
            switch (contentType.toLowerCase()) {
                case "application/pdf":
                    return FileType.PDF;
                case "image/png":
                    return FileType.PNG;
                case "image/jpeg":
                case "image/jpg":
                    return FileType.JPG;
            }
        }
        if (name.endsWith(".pdf")) return FileType.PDF;
        if (name.endsWith(".png")) return FileType.PNG;
        if (name.endsWith(".jpg") || name.endsWith(".jpeg")) return FileType.JPG;

        throw new RuntimeException("Unsupported file type: " + contentType + " / " + originalFilename);
    }
}
