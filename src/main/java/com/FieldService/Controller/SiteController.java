package com.FieldService.Controller;

import com.FieldService.DTO.SiteRequestDTO;
import com.FieldService.Entity.Site;
import com.FieldService.Service.SiteService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sites")
public class SiteController {

    private final SiteService siteService;

    public SiteController(SiteService siteService) {
        this.siteService = siteService;
    }

    // CREATE SITE
    @PreAuthorize("hasAuthority('CREATE_SITE')")
    @PostMapping
    public ResponseEntity<Site> createSite(
            @RequestBody SiteRequestDTO dto) {

        Site site = siteService.createSite(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(site);
    }

    // GET ALL SITES
    @PreAuthorize("hasAuthority('VIEW_SITE')")
    @GetMapping
    public ResponseEntity<List<Site>> getAllSites() {

        return ResponseEntity.ok(
                siteService.getAllSites()
        );
    }

    // GET SITE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Site> getSiteById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                siteService.getSiteById(id)
        );
    }

    // UPDATE SITE
    @PreAuthorize("hasAuthority('UPDATE_SITE')")
    @PutMapping("/{id}")
    public ResponseEntity<Site> updateSite(
            @PathVariable Long id,
            @RequestBody SiteRequestDTO dto) {

        return ResponseEntity.ok(
                siteService.updateSite(id, dto)
        );
    }

    // DELETE SITE
    @PreAuthorize("hasAuthority('DELETE_SITE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSite(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                siteService.deleteSite(id)
        );
    }

    // GET SITES BY CUSTOMER
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Site>> getSitesByCustomer(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(
                siteService.getSitesByCustomer(customerId)
        );
    }
}