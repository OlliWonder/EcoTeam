package com.sber.java13.ecoteam.service;

import com.sber.java13.ecoteam.constants.Errors;
import com.sber.java13.ecoteam.dto.AddWasteToPointDTO;
import com.sber.java13.ecoteam.dto.PointDTO;
import com.sber.java13.ecoteam.exception.MyDeleteException;
import com.sber.java13.ecoteam.mapper.PointMapper;
import com.sber.java13.ecoteam.model.Point;
import com.sber.java13.ecoteam.model.Waste;
import com.sber.java13.ecoteam.repository.PointRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Set;

@Service
public class PointService extends GenericService<Point, PointDTO> {
    private final PointRepository pointRepository;
    private final WasteService wasteService;
    
    protected PointService(PointRepository pointRepository, PointMapper pointMapper, WasteService wasteService) {
        super(pointRepository, pointMapper);
        this.pointRepository = pointRepository;
        this.wasteService = wasteService;
    }
    
    public Page<PointDTO> searchPoints(final String city, Pageable pageable) {
        Page<Point> points = pointRepository.findAllByCityContainsIgnoreCaseAndIsDeletedFalse(city, pageable);
        List<PointDTO> result = mapper.toDTOs(points.getContent());
        return new PageImpl<>(result, pageable, points.getTotalElements());
    }
    
    public void addWasteToPoint(AddWasteToPointDTO addWasteToPointDTO) {
        PointDTO pointDTO = getOne(addWasteToPointDTO.getWasteId());
        wasteService.getOne(addWasteToPointDTO.getPointId());
        pointDTO.getWastesIds().add(addWasteToPointDTO.getWasteId());
        update(pointDTO);
    }
    
    @Override
    public void delete(Long objectId) throws MyDeleteException {
        Point point = pointRepository.findById(objectId).orElseThrow(
                () -> new NotFoundException("Пункта приёма с заданным id=" + objectId + " не существует."));
        boolean pointForDeletion = pointRepository.checkPointForDeletion(objectId);
        if (pointForDeletion) {
            markAsDeleted(point);
            Set<Waste> wastes = point.getWastes();
            if (wastes != null && wastes.size() > 0) {
                wastes.forEach(this::markAsDeleted);
            }
            pointRepository.save(point);
        }
        else {
            throw new MyDeleteException(Errors.Points.POINT_FORBIDDEN_ERROR);
        }
    }
    
    public void restore(Long objectId) {
        Point point = pointRepository.findById(objectId).orElseThrow(
                () -> new NotFoundException("Пункта приёма id=" + objectId + " не существует."));
        unMarkAsDeleted(point);
        Set<Waste> wastes = point.getWastes();
        if (wastes != null && wastes.size() > 0) {
            wastes.forEach(this::unMarkAsDeleted);
        }
        pointRepository.save(point);
    }
}
