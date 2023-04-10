package com.sber.java13.ecoteam.service;

import com.sber.java13.ecoteam.UserTestData;
import com.sber.java13.ecoteam.dto.UserDTO;
import com.sber.java13.ecoteam.exception.MyDeleteException;
import com.sber.java13.ecoteam.mapper.UserMapper;
import com.sber.java13.ecoteam.model.User;
import com.sber.java13.ecoteam.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class UserServiceTest extends GenericTest<User, UserDTO> {
    
    public UserServiceTest() {
        super();
        BCryptPasswordEncoder bCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        JavaMailSender javaMailSender = Mockito.mock(JavaMailSender.class);
        repository = Mockito.mock(UserRepository.class);
        mapper = Mockito.mock(UserMapper.class);
        service = new UserService((UserRepository) repository, (UserMapper) mapper, bCryptPasswordEncoder, javaMailSender);
    }
    
    @Test
    @Override
    protected void getAll() {
        Mockito.when(repository.findAll()).thenReturn(UserTestData.USER_LIST);
        Mockito.when(mapper.toDTOs(UserTestData.USER_LIST)).thenReturn(UserTestData.USER_DTO_LIST);
        List<UserDTO> userDTOS = service.listAll();
        log.info("Testing getAll(): " + userDTOS);
        assertEquals(UserTestData.USER_LIST.size(), userDTOS.size());
    }
    
    @Test
    @Override
    protected void getOne() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(UserTestData.USER_1));
        Mockito.when(mapper.toDTO(UserTestData.USER_1)).thenReturn(UserTestData.USER_DTO_1);
        UserDTO userDTO = service.getOne(1L);
        log.info("Testing getOne(): " + userDTO);
        assertEquals(UserTestData.USER_DTO_1, userDTO);
    }
    
    @Test
    @Override
    protected void create() {
        Mockito.when(mapper.toEntity(UserTestData.USER_DTO_1)).thenReturn(UserTestData.USER_1);
        Mockito.when(mapper.toDTO(UserTestData.USER_1)).thenReturn(UserTestData.USER_DTO_1);
        Mockito.when(repository.save(UserTestData.USER_1)).thenReturn(UserTestData.USER_1);
        UserDTO userDTO = service.create(UserTestData.USER_DTO_1);
        log.info("Testing create(): " + userDTO);
        assertEquals(UserTestData.USER_DTO_1, userDTO);
    }
    
    @Test
    @Override
    protected void update() {
        Mockito.when(mapper.toEntity(UserTestData.USER_DTO_1)).thenReturn(UserTestData.USER_1);
        Mockito.when(mapper.toDTO(UserTestData.USER_1)).thenReturn(UserTestData.USER_DTO_1);
        Mockito.when(repository.save(UserTestData.USER_1)).thenReturn(UserTestData.USER_1);
        UserDTO userDTO = service.update(UserTestData.USER_DTO_1);
        log.info("Testing update(): " + userDTO);
        assertEquals(UserTestData.USER_DTO_1, userDTO);
    }
    
    @Test
    @Override
    protected void delete() throws MyDeleteException {
        Mockito.when(((UserRepository) repository).checkUserForDeletion(1L)).thenReturn(true);
        Mockito.when(repository.save(UserTestData.USER_1)).thenReturn(UserTestData.USER_1);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(UserTestData.USER_1));
        log.info("Testing delete() before: " + UserTestData.USER_1.isDeleted());
        service.delete(1L);
        log.info("Testing delete() after: " + UserTestData.USER_1.isDeleted());
        assertTrue(UserTestData.USER_1.isDeleted());
    }
    
    @Test
    @Override
    protected void restore() {
        UserTestData.USER_3.setDeleted(true);
        Mockito.when(repository.save(UserTestData.USER_3)).thenReturn(UserTestData.USER_3);
        Mockito.when(repository.findById(3L)).thenReturn(Optional.of(UserTestData.USER_3));
        log.info("Testing restore() before: " + UserTestData.USER_3.isDeleted());
        ((UserService) service).restore(3L);
        log.info("Testing restore() after: " + UserTestData.USER_3.isDeleted());
        assertFalse(UserTestData.USER_3.isDeleted());
    }
    
    @Test
    @Override
    protected void getAllNotDeleted() {
        UserTestData.USER_3.setDeleted(true);
        List<User> points = UserTestData.USER_LIST.stream().filter(Predicate.not(User::isDeleted)).toList();
        Mockito.when(repository.findAllByIsDeletedFalse()).thenReturn(points);
        Mockito.when(mapper.toDTOs(points)).thenReturn(
                UserTestData.USER_DTO_LIST.stream().filter(Predicate.not(UserDTO::isDeleted)).toList());
        List<UserDTO> userDTOS = service.listAllNotDeleted();
        log.info("Testing getAllNotDeleted(): " + userDTOS);
        assertTrue(points.size() != userDTOS.size());
    }
}
