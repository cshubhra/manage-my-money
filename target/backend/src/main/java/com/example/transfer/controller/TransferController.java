package com.example.transfer.controller;

import com.example.transfer.dto.TransferDTO;
import com.example.transfer.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {
    private final TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @GetMapping
    public ResponseEntity<List<TransferDTO>> getAllTransfers() {
        return ResponseEntity.ok(transferService.getAllTransfers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferDTO> getTransfer(@PathVariable Long id) {
        return ResponseEntity.ok(transferService.getTransferById(id));
    }

    @PostMapping
    public ResponseEntity<TransferDTO> createTransfer(@Valid @RequestBody TransferDTO transferDTO) {
        TransferDTO createdTransfer = transferService.createTransfer(transferDTO);
        return ResponseEntity.created(null).body(createdTransfer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransferDTO> updateTransfer(
            @PathVariable Long id,
            @Valid @RequestBody TransferDTO transferDTO) {
        return ResponseEntity.ok(transferService.updateTransfer(id, transferDTO));
    }
}