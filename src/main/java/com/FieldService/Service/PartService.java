package com.FieldService.Service;

import com.FieldService.DTO.PartRequestDTO;
import com.FieldService.Entity.Part;
import com.FieldService.Repository.PartRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartService {

    private final PartRepository partRepository;

    public PartService(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    // CREATE
    public Part createPart(PartRequestDTO dto) {

        if (partRepository.existsBySku(dto.getSku())) {
            throw new RuntimeException(
                    "Part with SKU already exists: " + dto.getSku()
            );
        }

        Part part = Part.builder()
                .name(dto.getName())
                .sku(dto.getSku())
                .quantity(dto.getQuantity())
                .unitPrice(dto.getUnitPrice())
                .build();

        return partRepository.save(part);
    }

    // GET ALL
    public List<Part> getAllParts() {
        return partRepository.findAll();
    }

    // GET BY ID
    public Part getPartById(Long id) {

        return partRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Part not found with id: " + id
                        )
                );
    }

    // UPDATE
    public Part updatePart(
            Long id,
            PartRequestDTO dto) {

        Part part = getPartById(id);

        part.setName(dto.getName());
        part.setSku(dto.getSku());
        part.setQuantity(dto.getQuantity());
        part.setUnitPrice(dto.getUnitPrice());

        return partRepository.save(part);
    }

    // DELETE
    public String deletePart(Long id) {

        Part part = getPartById(id);

        partRepository.delete(part);

        return "Part deleted successfully";
    }

    // ADD STOCK
    public Part addStock(
            Long id,
            Integer quantity) {

        if (quantity == null || quantity <= 0) {
            throw new RuntimeException(
                    "Quantity must be greater than 0"
            );
        }

        Part part = getPartById(id);

        part.setQuantity(
                part.getQuantity() + quantity
        );

        return partRepository.save(part);
    }
}