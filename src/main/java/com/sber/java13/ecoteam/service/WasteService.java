package com.sber.java13.ecoteam.service;

import com.sber.java13.ecoteam.constants.Errors;
import com.sber.java13.ecoteam.dto.WasteDTO;
import com.sber.java13.ecoteam.dto.WasteWithPointsDTO;
import com.sber.java13.ecoteam.exception.MyDeleteException;
import com.sber.java13.ecoteam.mapper.WasteMapper;
import com.sber.java13.ecoteam.mapper.WasteWithPointsMapper;
import com.sber.java13.ecoteam.model.Order;
import com.sber.java13.ecoteam.model.Waste;
import com.sber.java13.ecoteam.repository.WasteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Set;

@Service
public class WasteService extends GenericService<Waste, WasteDTO> {
    private final WasteRepository wasteRepository;
    private final WasteWithPointsMapper wasteWithPointsMapper;
    
    public WasteService(WasteRepository wasteRepository, WasteMapper wasteMapper,
                        WasteWithPointsMapper wasteWithPointsMapper) {
        super(wasteRepository, wasteMapper);
        this.wasteRepository = wasteRepository;
        this.wasteWithPointsMapper = wasteWithPointsMapper;
    }
    
    public Page<WasteWithPointsDTO> getAllWastesWithPoints(Pageable pageable) {
        Page<Waste> wastePage = repository.findAll(pageable);
        List<WasteWithPointsDTO> result = wasteWithPointsMapper.toDTOs(wastePage.getContent());
        return new PageImpl<>(result, pageable, wastePage.getTotalElements());
    }
    
    public Page<WasteWithPointsDTO> getAllNotDeletedWastesWithPoints(Pageable pageable) {
        Page<Waste> wastePage = repository.findAllByIsDeletedFalse(pageable);
        List<WasteWithPointsDTO> result = wasteWithPointsMapper.toDTOs(wastePage.getContent());
        return new PageImpl<>(result, pageable, wastePage.getTotalElements());
    }
    
    public WasteWithPointsDTO getWasteWithPoints(Long id) {
        return wasteWithPointsMapper.toDTO(mapper.toEntity(super.getOne(id)));
    }
    
    public Page<WasteWithPointsDTO> findWastes(WasteDTO wasteDTO, Pageable pageable) {
        Page<Waste> wastePage = wasteRepository.searchWastes(wasteDTO.getTitle(),
                wasteDTO.getShortTitle(), wasteDTO.getCode(), pageable);
        List<WasteWithPointsDTO> result = wasteWithPointsMapper.toDTOs(wastePage.getContent());
        return new PageImpl<>(result, pageable, wastePage.getTotalElements());
    }
    
    @Override
    public void delete(Long wasteId) throws MyDeleteException {
        Waste waste = wasteRepository.findById(wasteId).orElseThrow(
                () -> new NotFoundException("Отхода с заданным id=" + wasteId + " не существует!"));
        boolean wasteCanBeDeleted = wasteRepository.checkWasteForDeletion(wasteId);
        if (wasteCanBeDeleted) {
            markAsDeleted(waste);
            Set<Order> orders = waste.getOrders();
            if (orders != null && orders.size() > 0) {
                orders.forEach(this::markAsDeleted);
            }
            wasteRepository.save(waste);
        }
        else {
            throw new MyDeleteException(Errors.Wastes.WASTE_DELETE_ERROR);
        }
    }
    
    public void restore(Long wasteId) {
        Waste waste = wasteRepository.findById(wasteId).orElseThrow(
                () -> new NotFoundException("Отхода с заданным id=" + wasteId + " не существует!"));
        unMarkAsDeleted(waste);
        Set<Order> orders = waste.getOrders();
        if (orders != null && orders.size() > 0) {
            orders.forEach(this::unMarkAsDeleted);
        }
        wasteRepository.save(waste);
    }
}
