package com.sber.java13.ecoteam.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WasteDTO extends GenericDTO {
    private String title;
    private String shortTitle;
    private String code;
    private String description;
    private Set<Long> ordersIds;
    private Set<Long> pointsIds;
    private boolean isDeleted;
}
