package com.epam.managemymoney.service;

import com.epam.managemymoney.dto.TransferDTO;
//import com.epam.managemymoney.dto.TransferItemDTO;
import com.epam.managemymoney.exception.ResourceNotFoundException;
import com.epam.managemymoney.model.Transfer;
import com.epam.managemymoney.repository.TransferRepository;
import org.modelmapper.ModelMapper;
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
        //transfer.setStatus("PENDING");

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
        transfer.setAmount(dto.getAmount());

        //transfer.setStatus(dto.getStatus());
        // Update other fields as needed
    }
}