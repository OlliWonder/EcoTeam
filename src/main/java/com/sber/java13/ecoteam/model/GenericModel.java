package com.sber.java13.ecoteam.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
@ToString
public class GenericModel implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1;
    
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "default_generator")
    private Long id;
    
    @Column(name = "created_when")
    private LocalDateTime createdWhen;
    
    @Column(name = "created_by")
    private String createdBy;
    
    @Column(name = "is_deleted", columnDefinition = "boolean default false")
    private boolean isDeleted;
    
    @Column(name = "deleted_when")
    private LocalDateTime deletedWhen;
    
    @Column(name = "deleted_by")
    private String deletedBy;
}