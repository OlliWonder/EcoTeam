package com.sber.java13.ecoteam.service;

import com.sber.java13.ecoteam.PointTestData;
import com.sber.java13.ecoteam.dto.PointDTO;
import com.sber.java13.ecoteam.exception.MyDeleteException;
import com.sber.java13.ecoteam.mapper.PointMapper;
import com.sber.java13.ecoteam.mapper.PointWithWastesMapper;
import com.sber.java13.ecoteam.model.Point;
import com.sber.java13.ecoteam.repository.PointRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class PointServiceTest extends GenericTest<Point, PointDTO>{
    public PointServiceTest() {
        super();
        PointWithWastesMapper pointWithWastesMapper = Mockito.mock(PointWithWastesMapper.class);
        WasteService wasteService = Mockito.mock(WasteService.class);
        repository = Mockito.mock(PointRepository.class);
        mapper = Mockito.mock(PointMapper.class);
        service = new PointService((PointRepository) repository, (PointMapper) mapper, wasteService, pointWithWastesMapper);
    }
    
    @Test
    @Override
    protected void getAll() {
        Mockito.when(repository.findAll()).thenReturn(PointTestData.POINT_LIST);
        Mockito.when(mapper.toDTOs(PointTestData.POINT_LIST)).thenReturn(PointTestData.POINT_DTO_LIST);
        List<PointDTO> pointDTOS = service.listAll();
        log.info("Testing getAll(): " + pointDTOS);
        assertEquals(PointTestData.POINT_LIST.size(), pointDTOS.size());
    }
    
    @Test
    @Override
    protected void getOne() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(PointTestData.POINT_1));
        Mockito.when(mapper.toDTO(PointTestData.POINT_1)).thenReturn(PointTestData.POINT_DTO_1);
        PointDTO pointDTO = service.getOne(1L);
        log.info("Testing getOne(): " + pointDTO);
        assertEquals(PointTestData.POINT_DTO_1, pointDTO);
    }
    
    @Test
    @Override
    protected void create() {
        Mockito.when(mapper.toEntity(PointTestData.POINT_DTO_1)).thenReturn(PointTestData.POINT_1);
        Mockito.when(mapper.toDTO(PointTestData.POINT_1)).thenReturn(PointTestData.POINT_DTO_1);
        Mockito.when(repository.save(PointTestData.POINT_1)).thenReturn(PointTestData.POINT_1);
        PointDTO pointDTO = service.create(PointTestData.POINT_DTO_1);
        log.info("Testing create(): " + pointDTO);
        assertEquals(PointTestData.POINT_DTO_1, pointDTO);
    }
    
    @Test
    @Override
    protected void update() {
        Mockito.when(mapper.toEntity(PointTestData.POINT_DTO_1)).thenReturn(PointTestData.POINT_1);
        Mockito.when(mapper.toDTO(PointTestData.POINT_1)).thenReturn(PointTestData.POINT_DTO_1);
        Mockito.when(repository.save(PointTestData.POINT_1)).thenReturn(PointTestData.POINT_1);
        PointDTO pointDTO = service.update(PointTestData.POINT_DTO_1);
        log.info("Testing update(): " + pointDTO);
        assertEquals(PointTestData.POINT_DTO_1, pointDTO);
    }
    
    @Test
    @Override
    protected void delete() throws MyDeleteException {
        Mockito.when(((PointRepository) repository).checkPointForDeletion(1L)).thenReturn(true);
//        Mockito.when(pointRepository.checkPointForDeletion(2L)).thenReturn(false);
        Mockito.when(repository.save(PointTestData.POINT_1)).thenReturn(PointTestData.POINT_1);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(PointTestData.POINT_1));
        log.info("Testing delete() before: " + PointTestData.POINT_1.isDeleted());
        service.delete(1L);
        log.info("Testing delete() after: " + PointTestData.POINT_1.isDeleted());
        assertTrue(PointTestData.POINT_1.isDeleted());
    }
    
    @Test
    @Override
    protected void restore() {
        PointTestData.POINT_3.setDeleted(true);
        Mockito.when(repository.save(PointTestData.POINT_3)).thenReturn(PointTestData.POINT_3);
        Mockito.when(repository.findById(3L)).thenReturn(Optional.of(PointTestData.POINT_3));
        log.info("Testing restore() before: " + PointTestData.POINT_3.isDeleted());
        ((PointService) service).restore(3L);
        log.info("Testing restore() after: " + PointTestData.POINT_3.isDeleted());
        assertFalse(PointTestData.POINT_3.isDeleted());
    }
    
    @Test
    @Override
    protected void getAllNotDeleted() {
        PointTestData.POINT_3.setDeleted(true);
        List<Point> points = PointTestData.POINT_LIST.stream().filter(Predicate.not(Point::isDeleted)).toList();
        Mockito.when(repository.findAllByIsDeletedFalse()).thenReturn(points);
        Mockito.when(mapper.toDTOs(points)).thenReturn(
                PointTestData.POINT_DTO_LIST.stream().filter(Predicate.not(PointDTO::isDeleted)).toList());
        List<PointDTO> pointDTOS = service.listAllNotDeleted();
        log.info("Testing getAllNotDeleted(): " + pointDTOS);
        assertTrue(points.size() != pointDTOS.size());
    }
}