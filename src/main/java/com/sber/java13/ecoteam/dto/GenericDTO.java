package com.sber.java13.ecoteam.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class GenericDTO {
    
    private Long id;
    private String createdBy;
    private LocalDateTime createdWhen;
    private Boolean isDeleted;
    private LocalDateTime deletedWhen;
    private String deletedBy;
}
