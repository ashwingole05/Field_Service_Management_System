package com.FieldService.Service;

import com.FieldService.DTO.SiteRequestDTO;
import com.FieldService.Entity.Site;
import com.FieldService.Repository.SiteRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteService {

    private final SiteRepository siteRepository;

    public SiteService(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

    // CREATE
    public Site createSite(SiteRequestDTO dto) {

        Site site = Site.builder()
                .siteName(dto.getSiteName())
                .address(dto.getAddress())
                .city(dto.getCity())
                .state(dto.getState())
                .pincode(dto.getPincode())
                .customerId(dto.getCustomerId())
                .build();

        return siteRepository.save(site);
    }

    // GET ALL
    public List<Site> getAllSites() {

        return siteRepository.findAll();
    }

    // GET BY ID
    public Site getSiteById(Long id) {

        return siteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Site not found with id: " + id)
                );
    }

    // UPDATE
    public Site updateSite(Long id, SiteRequestDTO dto) {

        Site site = siteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Site not found with id: " + id)
                );

        site.setSiteName(dto.getSiteName());
        site.setAddress(dto.getAddress());
        site.setCity(dto.getCity());
        site.setState(dto.getState());
        site.setPincode(dto.getPincode());
        site.setCustomerId(dto.getCustomerId());

        return siteRepository.save(site);
    }

    // DELETE
    public String deleteSite(Long id) {

        Site site = siteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Site not found with id: " + id)
                );

        siteRepository.delete(site);

        return "Site deleted successfully";
    }

    // GET SITES BY CUSTOMER
    public List<Site> getSitesByCustomer(Long customerId) {

        return siteRepository.findByCustomerId(customerId);
    }
}