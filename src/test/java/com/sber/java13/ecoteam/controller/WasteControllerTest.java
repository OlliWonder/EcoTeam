package com.sber.java13.ecoteam.controller;

import com.sber.java13.ecoteam.dto.WasteDTO;
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
class WasteControllerTest extends CommonTest {
    
    @Test
    @Order(0)
    @WithAnonymousUser
    protected void getAll() throws Exception {
        mvc.perform(get("/wastes")
                        .param("page", "1")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("wastes/viewAllWastes"))
                .andExpect(model().attributeExists("wastes"))
                .andReturn();
    }
    
    @Test
    @Order(1)
    @WithAnonymousUser
    void getOne() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/wastes/{id}", "5")
                        .param("waste", "5"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("waste"))
                .andExpect(view().name("wastes/viewWaste"));
        
    }
    
    @Test
    @Order(2)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void create() throws Exception {
        //Создаем новый мусор для создания через контроллер (тест дата)
        WasteDTO wasteDTO = new WasteDTO("title1", "shortTitle1", "code1", "description1", null, null, false);
        
        mvc.perform(post("/wastes/add")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .flashAttr("wasteForm", wasteDTO)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/wastes"))
                .andExpect(redirectedUrlTemplate("/wastes"))
                .andExpect(redirectedUrl("/wastes"));
    }
    
    @Test
    @Order(3)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void update() throws Exception {
        WasteDTO wasteDTO = new WasteDTO("title2", "shortTitle2", "code2", "description2", null, null, false);
        
        mvc.perform(post("/wastes/update")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .flashAttr("wasteForm", wasteDTO)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/wastes"))
                .andExpect(redirectedUrlTemplate("/wastes"))
                .andExpect(redirectedUrl("/wastes"));
    }
    
    @Test
    @Order(4)
    @WithAnonymousUser
    void searchWastes() throws Exception {
        WasteDTO wasteDTO = new WasteDTO("title1", "shortTitle1", "code1", "description1", null, null, false);
        
        mvc.perform(post("/wastes/search")
                        .param("page", "1")
                        .param("size", "5")
                        .flashAttr("wasteSearchForm", wasteDTO)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("wastes"))
                .andExpect(view().name("wastes/viewAllWastes"))
                .andReturn();
    }
    
    @Test
    @Order(5)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void delete() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/wastes/delete/{id}", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/wastes"));
    }
    
    @Test
    @Order(6)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    void restore() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/wastes/restore/{id}", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/wastes"));
    }
}