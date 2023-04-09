package com.sber.java13.ecoteam.service;

import com.sber.java13.ecoteam.dto.GenericDTO;
import com.sber.java13.ecoteam.exception.MyDeleteException;
import com.sber.java13.ecoteam.mapper.GenericMapper;
import com.sber.java13.ecoteam.model.GenericModel;
import com.sber.java13.ecoteam.repository.GenericRepository;
import com.sber.java13.ecoteam.service.userdetails.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class GenericTest<E extends GenericModel, D extends GenericDTO> {
    protected GenericService<E, D> service;
    protected GenericRepository<E> repository;
    protected GenericMapper<E, D> mapper;
    
    @BeforeEach
    void init() {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(CustomUserDetails
                .builder()
                .username("USER"),
                null,
                null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    
    protected abstract void getAll();
    
    protected abstract void getOne();
    
    protected abstract void create();
    
    protected abstract void update();
    
    protected abstract void delete() throws MyDeleteException;
    
    protected abstract void restore();
    
    protected abstract void getAllNotDeleted();
}
