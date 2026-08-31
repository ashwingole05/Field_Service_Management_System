package com.FieldService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailLogDTO {

    private String recepientEmail;
    private String subject;
    private String body;
}