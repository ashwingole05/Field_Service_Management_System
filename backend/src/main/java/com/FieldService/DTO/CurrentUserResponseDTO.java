package com.FieldService.DTO;

import com.FieldService.ENUM.Role;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentUserResponseDTO {

    private Long id;

    private String userName;

    private String userEmail;

    private String phone;

    private Role role;

    private Long customerId;
}