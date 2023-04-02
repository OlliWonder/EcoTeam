package com.sber.java13.ecoteam.constants;

import java.util.List;

public interface SecurityConstants {
    List<String> RESOURCES_WHITE_LIST = List.of("/resources/**",
            "/js/**",
            "/css/**",
            "/",
            // -- Swagger UI v3 (OpenAPI)
            "/swagger-ui/**",
            "/webjars/bootstrap/5.0.2/**",
            "/v3/api-docs/**");
    
    List<String> COMPANIES_WHITE_LIST = List.of("/companies",
            "/companies/search",
            "/companies/{id}");
    
    List<String> USERS_WHITE_LIST = List.of("/login",
            "/users/registration",
            "/users/remember-password",
            "/users/change-password");
    
    List<String> WASTES_WHITE_LIST = List.of("/wastes",
            "/wastes/search",
            "/orders/search/waste",
            "/wastes/{id}");
    
    List<String> WASTES_PERMISSION_LIST = List.of("/wastes/add",
            "/wastes/update",
            "/wastes/delete");
}
