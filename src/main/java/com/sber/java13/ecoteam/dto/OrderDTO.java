package com.sber.java13.ecoteam.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDTO extends GenericDTO {
    private Long userId;
    private String onDate;
    private Boolean isCompleted;
    private Boolean isInWork;
    private WasteDTO wasteDTO;
    private Integer weight;
    private Long wasteId;
}
