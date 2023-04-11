package com.sber.java13.ecoteam.controller;

import com.sber.java13.ecoteam.dto.OrderDTO;
import com.sber.java13.ecoteam.service.userdetails.CustomUserDetails;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultHandlers.exportTestSecurityContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlTemplate;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest extends CommonTest {
    @Test
    @Order(0)
    @WithUserDetails
    @WithMockUser(username = "olli", roles = "USER", password = "000")
    void orderFromWaste() throws Exception {
        OrderDTO orderDTO = new OrderDTO(2L, "onDate1", false, false, 1, 4L, 5L, false);
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderDTO.setUserId(Long.valueOf(customUserDetails.getUserId()));
        mvc.perform(post("/order/fromWaste")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .flashAttr("orderDTO", orderDTO)
                        .with(csrf()))
                .andDo(exportTestSecurityContext())
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlTemplate("/order/user-orders/"));
    }
}