package com.sber.java13.ecoteam.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WasteWithPointsDTO extends WasteDTO {
    private Set<PointDTO> points;
}
