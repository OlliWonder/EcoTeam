package com.sber.java13.ecoteam.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserWithOrdersDTO extends UserDTO {
    private Set<OrderDTO> orders;
}
