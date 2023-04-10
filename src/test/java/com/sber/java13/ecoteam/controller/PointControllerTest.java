package com.sber.java13.ecoteam.controller;

import com.sber.java13.ecoteam.dto.PointDTO;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PointControllerTest extends CommonTest {
    
    @Test
    @Order(0)
    @WithAnonymousUser
    protected void getAll() throws Exception {
        mvc.perform(get("/points")
                        .param("page", "1")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("points/viewAllPoints"))
                .andExpect(model().attributeExists("points"))
                .andReturn();
    }
    
    @Test
    @Order(1)
    @WithAnonymousUser
    void getOne() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/points/{id}", "5")
                        .param("point", "5"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("point"))
                .andExpect(view().name("points/viewPoint"));
        
    }
    
    @Test
    @Order(1)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void create() throws Exception {
        PointDTO pointDTO = new PointDTO("title1", "city1", "address1", "phone1", "workingTime1", null, 1L, null, false);
        
        mvc.perform(post("/points/add")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .flashAttr("pointForm", pointDTO)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/points"))
                .andExpect(redirectedUrlTemplate("/points"))
                .andExpect(redirectedUrl("/points"));
    }
    
    @Test
    void addWaste() throws Exception {
    }
    
    
    @Test
    @Order(3)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void update() throws Exception {
        PointDTO pointDTO = new PointDTO("title2", "city2", "address2", "phone2", "workingTime3", null, 2L, null, false);
        
        mvc.perform(post("/points/update")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .flashAttr("pointForm", pointDTO)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/points"))
                .andExpect(redirectedUrlTemplate("/points"))
                .andExpect(redirectedUrl("/points"));
    }
    
    @Test
    @Order(4)
    @WithAnonymousUser
    void searchPoints() throws Exception {
        PointDTO pointDTO = new PointDTO("title1", "city1", "address1", "phone1", "workingTime1", null, 1L, null, false);
        
        mvc.perform(post("/points/search")
                        .param("page", "1")
                        .param("size", "5")
                        .flashAttr("pointSearchForm", pointDTO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("points"))
                .andExpect(view().name("points/viewAllPoints"))
                .andReturn();
    }
    
    @Test
    @Order(5)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void delete() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/points/delete/{id}", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/points"));
    }
    
    @Test
    @Order(6)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void restore() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/points/restore/{id}", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/points"));
    }
}