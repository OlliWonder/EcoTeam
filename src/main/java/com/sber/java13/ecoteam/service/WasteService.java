package com.sber.java13.ecoteam.service;

import com.sber.java13.ecoteam.constants.Errors;
import com.sber.java13.ecoteam.dto.WasteDTO;
import com.sber.java13.ecoteam.exception.MyDeleteException;
import com.sber.java13.ecoteam.mapper.WasteMapper;
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
    
    public WasteService(WasteRepository wasteRepository, WasteMapper wasteMapper) {
        super(wasteRepository, wasteMapper);
        this.wasteRepository = wasteRepository;
    }
    
    public Page<WasteDTO> findWastes(final String title, Pageable pageable) {
        Page<Waste> wastes = wasteRepository.findAllByTitleContainsIgnoreCaseAndDeletedFalse(title, pageable);
        List<WasteDTO> result = mapper.toDTOs(wastes.getContent());
        return new PageImpl<>(result, pageable, wastes.getTotalElements());
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
