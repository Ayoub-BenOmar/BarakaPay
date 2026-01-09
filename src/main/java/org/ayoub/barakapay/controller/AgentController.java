package org.ayoub.barakapay.controller;

import lombok.RequiredArgsConstructor;
import org.ayoub.barakapay.model.dto.OperationDto;
import org.ayoub.barakapay.service.impl.OperationsServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agent")
@RequiredArgsConstructor
public class AgentController {

    private final OperationsServiceImpl operationsService;

    @GetMapping("/operations/pending")
    public List<OperationDto> getPendingOperations() {
        return operationsService.getPendingOperations();
    }

    @PutMapping("/operations/{id}/approve")
    public void approveOperation(@PathVariable Integer id) {
        operationsService.approveOperation(id);
    }

    @PutMapping("/operations/{id}/reject")
    public void rejectOperation(@PathVariable Integer id) {
        operationsService.rejectOperation(id);
    }
}
