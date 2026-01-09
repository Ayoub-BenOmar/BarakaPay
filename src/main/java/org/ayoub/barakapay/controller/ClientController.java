package org.ayoub.barakapay.controller;

import lombok.RequiredArgsConstructor;
import org.ayoub.barakapay.model.dto.OperationDto;
import org.ayoub.barakapay.model.mapper.UserMapper;
import org.ayoub.barakapay.service.UserService;
import org.ayoub.barakapay.service.impl.AccountServiceImpl;
import org.ayoub.barakapay.service.impl.DocumentServiceImpl;
import org.ayoub.barakapay.service.impl.OperationsServiceImpl;
import org.ayoub.barakapay.service.impl.UserServiceImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {

    private final UserServiceImpl userService;
    private final UserMapper userMapper;
    private final OperationsServiceImpl operationsService;
    private final DocumentServiceImpl documentService;

    @PostMapping("/deposit")
    public OperationDto deposit(@RequestParam BigDecimal amount) throws IOException {
        return operationsService.deposit(amount);
    }

    @PostMapping("/withdraw")
    public OperationDto withdraw(@RequestParam BigDecimal amount) {
        return operationsService.withdraw(amount);
    }

    @PostMapping("/transfer")
    public OperationDto transfer(@RequestParam String toAccount, @RequestParam BigDecimal amount) {
        return operationsService.transfer(toAccount, amount);
    }

    @PostMapping("/operations/{id}/documents")
    public void uploadOperationDocuments(@PathVariable Integer id, @RequestParam MultipartFile document) throws IOException {
        documentService.uploadDocument(id, document);
    }

}
