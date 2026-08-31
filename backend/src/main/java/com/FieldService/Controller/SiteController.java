package com.FieldService.Controller;

import com.FieldService.DTO.SiteRequestDTO;

import com.FieldService.Entity.Site;

import com.FieldService.Service.SiteService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sites")
public class SiteController {

    private final SiteService siteService;


    public SiteController(
            SiteService siteService) {

        this.siteService =
                siteService;
    }


    // =========================================
    // CUSTOMER / MANAGER / DISPATCHER - CREATE SITE
    // =========================================

    @PreAuthorize(
            "hasAuthority('CREATE_SITE')"
    )
    @PostMapping
    public ResponseEntity<Site> createSite(
            @Valid
            @RequestBody
            SiteRequestDTO dto,
            Authentication authentication) {

        return ResponseEntity
                .status(
                        HttpStatus.CREATED
                )
                .body(
                        siteService
                                .createSite(
                                        dto,
                                        authentication.getName()
                                )
                );
    }


    // =========================================
    // MANAGER / DISPATCHER - GET ALL
    // =========================================

    @PreAuthorize(
            "hasAuthority('VIEW_SITE')"
    )
    @GetMapping
    public ResponseEntity<List<Site>>
    getAllSites() {

        return ResponseEntity.ok(
                siteService
                        .getAllSites()
        );
    }


    // =========================================
    // CUSTOMER - GET MY SITES
    // =========================================

    @PreAuthorize(
            "isAuthenticated()"
    )
    @GetMapping("/mine")
    public ResponseEntity<List<Site>>
    getMySites(
            Authentication authentication) {

        return ResponseEntity.ok(
                siteService
                        .getMySites(
                                authentication
                                        .getName()
                        )
        );
    }


    // =========================================
    // MANAGER / DISPATCHER
    // =========================================

    @PreAuthorize(
            "hasAuthority('VIEW_SITE')"
    )
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Site>>
    getSitesByCustomer(
            @PathVariable
            Long customerId) {

        return ResponseEntity.ok(
                siteService
                        .getSitesByCustomer(
                                customerId
                        )
        );
    }


    // =========================================
    // GET BY ID
    // =========================================

    @PreAuthorize(
            "hasAuthority('VIEW_SITE')"
    )
    @GetMapping("/{id}")
    public ResponseEntity<Site>
    getSiteById(
            @PathVariable
            Long id,
            Authentication authentication) {

        return ResponseEntity.ok(
                siteService
                        .getSiteByIdForUser(
                                id,
                                authentication.getName()
                        )
        );
    }


    // =========================================
    // UPDATE
    // =========================================

    @PreAuthorize(
            "hasAuthority('UPDATE_SITE')"
    )
    @PutMapping("/{id}")
    public ResponseEntity<Site>
    updateSite(
            @PathVariable
            Long id,

            @Valid
            @RequestBody
            SiteRequestDTO dto,
            Authentication authentication) {

        return ResponseEntity.ok(
                siteService
                        .updateSite(
                                id,
                                dto,
                                authentication.getName()
                        )
        );
    }


    // =========================================
    // DELETE
    // =========================================

    @PreAuthorize(
            "hasAuthority('DELETE_SITE')"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String>
    deleteSite(
            @PathVariable
            Long id) {

        return ResponseEntity.ok(
                siteService
                        .deleteSite(
                                id
                        )
        );
    }
}
