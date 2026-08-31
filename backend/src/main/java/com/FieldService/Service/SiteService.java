package com.FieldService.Service;

import com.FieldService.DTO.SiteRequestDTO;

import com.FieldService.Entity.Site;
import com.FieldService.Entity.UserAuth;

import com.FieldService.ENUM.Role;

import com.FieldService.Repository.CustomerRepository;
import com.FieldService.Repository.SiteRepository;
import com.FieldService.Repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteService {

    private final SiteRepository siteRepository;

    private final CustomerRepository customerRepository;

    private final UserRepository userRepository;


    public SiteService(
            SiteRepository siteRepository,
            CustomerRepository customerRepository,
            UserRepository userRepository) {

        this.siteRepository =
                siteRepository;

        this.customerRepository =
                customerRepository;

        this.userRepository =
                userRepository;
    }


    // =========================================
    // CREATE
    // =========================================

    public Site createSite(
            SiteRequestDTO dto,
            String userEmail) {

        UserAuth user =
                getUser(userEmail);


        Long customerId =
                resolveCustomerIdForSiteWrite(
                        dto,
                        user
                );


        if (!customerRepository.existsById(customerId)) {

            throw new RuntimeException(
                    "Customer not found"
            );
        }


        Site site =
                Site.builder()
                        .siteName(
                                dto.getSiteName()
                        )
                        .address(
                                dto.getAddress()
                        )
                        .city(
                                dto.getCity()
                        )
                        .state(
                                dto.getState()
                        )
                        .pincode(
                                dto.getPincode()
                        )
                        .customerId(
                                customerId
                        )
                        .build();


        return siteRepository.save(
                site
        );
    }


    // =========================================
    // GET ALL
    // =========================================

    public List<Site> getAllSites() {

        return siteRepository.findAll();
    }


    // =========================================
    // GET BY ID
    // =========================================

    public Site getSiteById(
            Long id) {

        return siteRepository
                .findById(
                        id
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Site not found with id: "
                                        + id
                        )
                );
    }


    public Site getSiteByIdForUser(
            Long id,
            String userEmail) {

        Site site =
                getSiteById(id);


        UserAuth user =
                getUser(userEmail);


        if (user.getRole() == Role.CUSTOMER) {

            Long customerId =
                    requireLinkedCustomer(user);


            if (!customerId.equals(site.getCustomerId())) {

                throw new RuntimeException(
                        "Site does not belong to your customer account"
                );
            }
        }


        return site;
    }


    // =========================================
    // UPDATE
    // =========================================

    public Site updateSite(
            Long id,
            SiteRequestDTO dto,
            String userEmail) {

        Site site =
                getSiteById(
                        id
                );


        UserAuth user =
                getUser(userEmail);


        Long customerId =
                resolveCustomerIdForSiteWrite(
                        dto,
                        user
                );


        if (user.getRole() == Role.CUSTOMER
                && !customerId.equals(site.getCustomerId())) {

            throw new RuntimeException(
                    "You can update only your own site"
            );
        }


        if (!customerRepository.existsById(customerId)) {

            throw new RuntimeException(
                    "Customer not found"
            );
        }


        site.setSiteName(
                dto.getSiteName()
        );

        site.setAddress(
                dto.getAddress()
        );

        site.setCity(
                dto.getCity()
        );

        site.setState(
                dto.getState()
        );

        site.setPincode(
                dto.getPincode()
        );

        site.setCustomerId(
                customerId
        );


        return siteRepository.save(
                site
        );
    }


    // =========================================
    // DELETE
    // =========================================

    public String deleteSite(
            Long id) {

        Site site =
                getSiteById(
                        id
                );


        siteRepository.delete(
                site
        );


        return "Site deleted successfully";
    }


    // =========================================
    // MANAGER / DISPATCHER
    // GET SITES BY CUSTOMER ID
    // =========================================

    public List<Site> getSitesByCustomer(
            Long customerId) {

        if (!customerRepository
                .existsById(
                        customerId
                )) {

            throw new RuntimeException(
                    "Customer not found"
            );
        }


        return siteRepository
                .findByCustomerId(
                        customerId
                );
    }


    // =========================================
    // LOGGED-IN CUSTOMER
    // =========================================

    public List<Site> getMySites(
            String userEmail) {

        UserAuth user =
                getUser(userEmail);


        if (user.getRole()
                != Role.CUSTOMER) {

            throw new RuntimeException(
                    "Only customer accounts have customer sites"
            );
        }


        if (user.getCustomerId()
                == null) {

            throw new RuntimeException(
                    "Customer account is not linked to a customer record"
            );
        }


        return siteRepository
                .findByCustomerId(
                        user.getCustomerId()
                );
    }


    private UserAuth getUser(
            String userEmail) {

        return userRepository
                .findByUserEmail(
                        userEmail
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Logged-in user not found"
                        )
                );
    }


    private Long resolveCustomerIdForSiteWrite(
            SiteRequestDTO dto,
            UserAuth user) {

        if (user.getRole() == Role.CUSTOMER) {

            return requireLinkedCustomer(user);
        }


        if (dto.getCustomerId() == null) {

            throw new RuntimeException(
                    "Customer ID is required when staff creates a site"
            );
        }


        return dto.getCustomerId();
    }


    private Long requireLinkedCustomer(
            UserAuth user) {

        if (user.getCustomerId() == null) {

            throw new RuntimeException(
                    "Customer account is not linked to a customer record"
            );
        }


        return user.getCustomerId();
    }
}
