package com.sber.java13.ecoteam.controller;

import com.sber.java13.ecoteam.dto.RoleDTO;
import com.sber.java13.ecoteam.dto.UserDTO;
import com.sber.java13.ecoteam.dto.WasteDTO;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerTest extends CommonTest {
    
    @Test
    @Order(0)
    @WithAnonymousUser
    void registration() throws Exception {
        mvc.perform(get("/registration"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
    
    @Test
    @Order(1)
    @WithAnonymousUser
    void rememberPassword() throws Exception {
        mvc.perform(get("/remember-password"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
    
    @Test
    @Order(2)
    @WithMockUser(username = "olli", roles = "USER", password = "000")
    void changePassword() throws Exception {
        mvc.perform(get("/change-password")
                        .flashAttr("uuid", "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(redirectedUrl(null));
    }
    
    @Test
    @Order(3)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void listAllUsers() throws Exception {
        mvc.perform(get("/list")
                        .param("page", "1")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(redirectedUrl(null));
    }
    
    @Test
    void searchUsers() throws Exception {
    }
    
    @Test
    @Order(3)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void addModeratorPage() throws Exception {
        mvc.perform(get("/add-moderator"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(redirectedUrl(null));
    }
    
    @Test
    @Order(2)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void addModerator() throws Exception {
        UserDTO userDTO = new UserDTO("login3", "password3", "firstName3", "lastName3", "middleName3", "birthDate3", "phone3", "email3", "city3", "address3", null, "token3", null, null, 1L, false);
        RoleDTO roleDTO = new RoleDTO(2L, "moderator", "MODERATOR");
        userDTO.setRole(roleDTO);
        mvc.perform(get("/add-moderator")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .flashAttr("userForm", userDTO)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(redirectedUrl(null));
    }

}