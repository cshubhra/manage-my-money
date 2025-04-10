package com.epam.managemymoney.controller;

import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;
import javax.validation.Valid;
import com.epam.managemymoney.service.TransferService;
import com.epam.managemymoney.dto.TransferDTO;

@RestController
@RequestMapping("/api/transfers")
@Slf4j
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
        TransferDTO created = transferService.createTransfer(transferDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransferDTO> updateTransfer(
            @PathVariable Long id,
            @Valid @RequestBody TransferDTO transferDTO) {
        return ResponseEntity.ok(transferService.updateTransfer(id, transferDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransfer(@PathVariable Long id) {
        //transferService.deleteTransfer(id);

        return ResponseEntity.noContent().build();
    }
}