package com.example.transfer.service;

import com.example.transfer.model.Transfer;
import com.example.transfer.repository.TransferRepository;
import com.example.transfer.dto.TransferDTO;
import com.example.transfer.dto.TransferItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransferService {
    private final TransferRepository transferRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TransferService(TransferRepository transferRepository, ModelMapper modelMapper) {
        this.transferRepository = transferRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public List<TransferDTO> getAllTransfers() {
        return transferRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TransferDTO getTransferById(Long id) {
        Transfer transfer = transferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transfer not found with id: " + id));
        return convertToDTO(transfer);
    }

    @Transactional
    public TransferDTO createTransfer(TransferDTO transferDTO) {
        Transfer transfer = convertToEntity(transferDTO);
        transfer.setStatus("PENDING");
        transfer.setCreatedAt(LocalDateTime.now());
        transfer.getItems().forEach(item -> item.setTransfer(transfer));
        
        Transfer savedTransfer = transferRepository.save(transfer);
        return convertToDTO(savedTransfer);
    }

    @Transactional
    public TransferDTO updateTransfer(Long id, TransferDTO transferDTO) {
        Transfer existingTransfer = transferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transfer not found with id: " + id));
        
        updateTransferFields(existingTransfer, transferDTO);
        Transfer updatedTransfer = transferRepository.save(existingTransfer);
        return convertToDTO(updatedTransfer);
    }

    private TransferDTO convertToDTO(Transfer transfer) {
        return modelMapper.map(transfer, TransferDTO.class);
    }

    private Transfer convertToEntity(TransferDTO dto) {
        return modelMapper.map(dto, Transfer.class);
    }

    private void updateTransferFields(Transfer transfer, TransferDTO dto) {
        transfer.setTransferDate(dto.getTransferDate());
        transfer.setTotalAmount(dto.getTotalAmount());
        transfer.setStatus(dto.getStatus());
        // Update other fields as needed
    }
}