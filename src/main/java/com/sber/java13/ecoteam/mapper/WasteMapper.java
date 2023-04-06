package com.sber.java13.ecoteam.mapper;

import com.sber.java13.ecoteam.dto.WasteDTO;
import com.sber.java13.ecoteam.model.Waste;
import com.sber.java13.ecoteam.model.GenericModel;
import com.sber.java13.ecoteam.repository.OrderRepository;
import com.sber.java13.ecoteam.repository.PointRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class WasteMapper extends GenericMapper<Waste, WasteDTO> {
    private final OrderRepository orderRepository;
    private final PointRepository pointRepository;
    
    protected WasteMapper(ModelMapper modelMapper, OrderRepository orderRepository, PointRepository pointRepository) {
        super(modelMapper, Waste.class, WasteDTO.class);
        this.orderRepository = orderRepository;
        this.pointRepository = pointRepository;
    }
    
    @PostConstruct
    @Override
    protected void setupMapper() {
        modelMapper.createTypeMap(Waste.class, WasteDTO.class)
                .addMappings(m -> m.skip(WasteDTO::setOrdersIds)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(WasteDTO::setPointsIds)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(WasteDTO.class, Waste.class)
                .addMappings(m -> m.skip(Waste::setOrders)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Waste::setPoints)).setPostConverter(toEntityConverter());
    }
    
    @Override
    protected void mapSpecificFields(WasteDTO source, Waste destination) {
        if (!Objects.isNull(source.getOrdersIds())) {
            destination.setOrders(new HashSet<>(orderRepository.findAllById(source.getOrdersIds())));
        }
        else {
            destination.setOrders(Collections.emptySet());
        }
        
        if (!Objects.isNull(source.getPointsIds())) {
            destination.setPoints(new HashSet<>(pointRepository.findAllById(source.getPointsIds())));
        }
        else {
            destination.setPoints(Collections.emptySet());
        }
    }
    
    @Override
    protected void mapSpecificFields(Waste source, WasteDTO destination) {
        destination.setOrdersIds(getIds(source));
        destination.setPointsIds(getIdsPoints(source));
    }
    
    @Override
    protected Set<Long> getIds(Waste waste) {
        return Objects.isNull(waste) || Objects.isNull(waste.getOrders())
                ? null
                : waste.getOrders().stream()
                .map(GenericModel::getId)
                .collect(Collectors.toSet());
    }
    
    protected Set<Long> getIdsPoints(Waste waste) {
        return Objects.isNull(waste) || Objects.isNull(waste.getPoints())
                ? null
                : waste.getPoints().stream()
                .map(GenericModel::getId)
                .collect(Collectors.toSet());
    }
}
