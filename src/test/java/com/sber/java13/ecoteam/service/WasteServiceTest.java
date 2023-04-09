package com.sber.java13.ecoteam.service;

import com.sber.java13.ecoteam.WasteTestData;
import com.sber.java13.ecoteam.dto.WasteDTO;
import com.sber.java13.ecoteam.exception.MyDeleteException;
import com.sber.java13.ecoteam.mapper.WasteMapper;
import com.sber.java13.ecoteam.mapper.WasteWithPointsMapper;
import com.sber.java13.ecoteam.model.Waste;
import com.sber.java13.ecoteam.repository.WasteRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class WasteServiceTest extends GenericTest<Waste, WasteDTO> {
    
    public WasteServiceTest() {
        super();
        WasteWithPointsMapper wasteWithPointsMapper = Mockito.mock(WasteWithPointsMapper.class);
        repository = Mockito.mock(WasteRepository.class);
        mapper = Mockito.mock(WasteMapper.class);
        service = new WasteService((WasteRepository) repository, (WasteMapper) mapper, wasteWithPointsMapper);
    }
    
    @Test
    @Override
    protected void getAll() {
        Mockito.when(repository.findAll()).thenReturn(WasteTestData.WASTE_LIST);
        Mockito.when(mapper.toDTOs(WasteTestData.WASTE_LIST)).thenReturn(WasteTestData.WASTE_DTO_LIST);
        List<WasteDTO> wasteDTOS = service.listAll();
        log.info("Testing getAll(): " + wasteDTOS);
        assertEquals(WasteTestData.WASTE_LIST.size(), wasteDTOS.size());
    }
    
    @Test
    @Override
    protected void getOne() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(WasteTestData.WASTE_1));
        Mockito.when(mapper.toDTO(WasteTestData.WASTE_1)).thenReturn(WasteTestData.WASTE_DTO_1);
        WasteDTO pointDTO = service.getOne(1L);
        log.info("Testing getOne(): " + pointDTO);
        assertEquals(WasteTestData.WASTE_DTO_1, pointDTO);
    }
    
    @Test
    @Override
    protected void create() {
        Mockito.when(mapper.toEntity(WasteTestData.WASTE_DTO_1)).thenReturn(WasteTestData.WASTE_1);
        Mockito.when(mapper.toDTO(WasteTestData.WASTE_1)).thenReturn(WasteTestData.WASTE_DTO_1);
        Mockito.when(repository.save(WasteTestData.WASTE_1)).thenReturn(WasteTestData.WASTE_1);
        WasteDTO wasteDTO = service.create(WasteTestData.WASTE_DTO_1);
        log.info("Testing create(): " + wasteDTO);
        assertEquals(WasteTestData.WASTE_DTO_1, wasteDTO);
    }
    
    @Test
    @Override
    protected void update() {
        Mockito.when(mapper.toEntity(WasteTestData.WASTE_DTO_1)).thenReturn(WasteTestData.WASTE_1);
        Mockito.when(mapper.toDTO(WasteTestData.WASTE_1)).thenReturn(WasteTestData.WASTE_DTO_1);
        Mockito.when(repository.save(WasteTestData.WASTE_1)).thenReturn(WasteTestData.WASTE_1);
        WasteDTO wasteDTO = service.update(WasteTestData.WASTE_DTO_1);
        log.info("Testing update(): " + wasteDTO);
        assertEquals(WasteTestData.WASTE_DTO_1, wasteDTO);
    }
    
    @Test
    @Override
    protected void delete() throws MyDeleteException {
        Mockito.when(((WasteRepository) repository).checkWasteForDeletion(1L)).thenReturn(true);
        Mockito.when(repository.save(WasteTestData.WASTE_1)).thenReturn(WasteTestData.WASTE_1);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(WasteTestData.WASTE_1));
        log.info("Testing delete() before: " + WasteTestData.WASTE_1.isDeleted());
        service.delete(1L);
        log.info("Testing delete() after: " + WasteTestData.WASTE_1.isDeleted());
        assertTrue(WasteTestData.WASTE_1.isDeleted());
    }
    
    @Test
    @Override
    protected void restore() {
        WasteTestData.WASTE_3.setDeleted(true);
        Mockito.when(repository.save(WasteTestData.WASTE_3)).thenReturn(WasteTestData.WASTE_3);
        Mockito.when(repository.findById(3L)).thenReturn(Optional.of(WasteTestData.WASTE_3));
        log.info("Testing restore() before: " + WasteTestData.WASTE_3.isDeleted());
        ((WasteService) service).restore(3L);
        log.info("Testing restore() after: " + WasteTestData.WASTE_3.isDeleted());
        assertFalse(WasteTestData.WASTE_3.isDeleted());
    }
    
    @Test
    @Override
    protected void getAllNotDeleted() {
        WasteTestData.WASTE_3.setDeleted(true);
        List<Waste> points = WasteTestData.WASTE_LIST.stream().filter(Predicate.not(Waste::isDeleted)).toList();
        Mockito.when(repository.findAllByIsDeletedFalse()).thenReturn(points);
        Mockito.when(mapper.toDTOs(points)).thenReturn(
                WasteTestData.WASTE_DTO_LIST.stream().filter(Predicate.not(WasteDTO::isDeleted)).toList());
        List<WasteDTO> wasteDTOS = service.listAllNotDeleted();
        log.info("Testing getAllNotDeleted(): " + wasteDTOS);
        assertTrue(points.size() != wasteDTOS.size());
    }
}
