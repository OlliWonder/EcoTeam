package com.sber.java13.ecoteam;

import com.sber.java13.ecoteam.dto.UserDTO;
import com.sber.java13.ecoteam.model.User;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public interface UserTestData {
    UserDTO USER_DTO_1 = new UserDTO("login1", "password1", "firstName1", "lastName1", "middleName1", "birthDate1", "phone1", "email1", "city1", "address1", null, "token1", null, null, 1L, false);
    UserDTO USER_DTO_2 = new UserDTO("login2", "password2", "firstName2", "lastName2", "middleName2", "birthDate2", "phone2", "email2", "city2", "address2", null, "token2", null, null, 2L, false);
    UserDTO USER_DTO_3 = new UserDTO("login3", "password3", "firstName3", "lastName3", "middleName3", "birthDate3", "phone3", "email3", "city3", "address3", null, "token3", null, null, 3L, true);
    List<UserDTO> USER_DTO_LIST = Arrays.asList(USER_DTO_1, USER_DTO_2, USER_DTO_3);
    User USER_1 = new User("login1", "password1", "firstName1", "lastName1", "middleName1", LocalDate.now(), "phone1", "email1", "city1", "address1", null, "token1", null, null, null);
    User USER_2 = new User("login2", "password2", "firstName2", "lastName2", "middleName2", LocalDate.now(), "phone2", "email2", "city2", "address2", null, "token2", null, null, null);
    User USER_3 = new User("login3", "password3", "firstName3", "lastName3", "middleName3", LocalDate.now(), "phone3", "email3", "city3", "address3", null, "token3", null, null, null);
    List<User> USER_LIST = Arrays.asList(USER_1, USER_2, USER_3);
}
