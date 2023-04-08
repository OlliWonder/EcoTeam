package com.sber.java13.ecoteam.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAgentOrderDTO extends UserDTO {
    private Set<OrderDTO> orders;
}
