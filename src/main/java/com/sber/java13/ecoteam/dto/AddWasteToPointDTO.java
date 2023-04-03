package com.sber.java13.ecoteam.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AddWasteToPointDTO {
    Long wasteId;
    Long pointId;
}
