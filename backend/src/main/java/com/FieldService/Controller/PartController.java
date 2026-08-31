package com.FieldService.Controller;

import com.FieldService.DTO.PartRequestDTO;
import com.FieldService.Entity.Part;
import com.FieldService.Service.PartService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parts")
public class PartController {

    private final PartService partService;

    public PartController(PartService partService) {
        this.partService = partService;
    }

    // CREATE
    @PreAuthorize("hasAuthority('ADD_PARTS')")
    @PostMapping
    public ResponseEntity<Part> createPart(
            @Valid @RequestBody PartRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(partService.createPart(dto));
    }

    // GET ALL
    @PreAuthorize("hasAuthority('VIEW_PARTS')")
    @GetMapping
    public ResponseEntity<List<Part>> getAllParts() {

        return ResponseEntity.ok(
                partService.getAllParts()
        );
    }

    // GET BY ID
    @PreAuthorize("hasAuthority('VIEW_PARTS')")
    @GetMapping("/{id}")
    public ResponseEntity<Part> getPartById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                partService.getPartById(id)
        );
    }

    // UPDATE
    @PreAuthorize("hasAuthority('UPDATE_PARTS')")
    @PutMapping("/{id}")
    public ResponseEntity<Part> updatePart(
            @PathVariable Long id,
            @Valid  @RequestBody PartRequestDTO dto) {

        return ResponseEntity.ok(
                partService.updatePart(id, dto)
        );
    }

    // ADD STOCK
    @PreAuthorize("hasAuthority('UPDATE_PARTS')")
    @PutMapping("/{id}/stock/{quantity}")
    public ResponseEntity<Part> addStock(
            @PathVariable Long id,
            @PathVariable Integer quantity) {

        return ResponseEntity.ok(
                partService.addStock(id, quantity)
        );
    }

    // DELETE
    @PreAuthorize("hasAuthority('DELETE_PART')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePart(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                partService.deletePart(id)
        );
    }
}
