package com.sber.java13.ecoteam.dto;

import lombok.*;

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
    private Integer weight;
    private Long wasteId;
    private Long pointId;
    private boolean isDeleted;
}
