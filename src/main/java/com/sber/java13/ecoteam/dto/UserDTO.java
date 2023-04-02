package com.sber.java13.ecoteam.dto;

import com.sber.java13.ecoteam.model.Achievement;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO extends GenericDTO {
    private Boolean isCompany;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String middleName;
    private String birthDate;
    private String phone;
    private String email;
    private String city;
    private String address;
    private Achievement achievement;
    private String changePasswordToken;
    private RoleDTO role;
    private Set<Long> ordersIds;
}
