package com.sber.java13.ecoteam.mapper;

import com.sber.java13.ecoteam.dto.PointDTO;
import com.sber.java13.ecoteam.model.GenericModel;
import com.sber.java13.ecoteam.model.Point;
import com.sber.java13.ecoteam.repository.OrderRepository;
import com.sber.java13.ecoteam.repository.UserRepository;
import com.sber.java13.ecoteam.repository.WasteRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PointMapper extends GenericMapper<Point, PointDTO> {
    private final WasteRepository wasteRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    
    public PointMapper(ModelMapper modelMapper, WasteRepository wasteRepository, UserRepository userRepository, OrderRepository orderRepository) {
        super(modelMapper, Point.class, PointDTO.class);
        this.wasteRepository = wasteRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }
    
    @Override
    @PostConstruct
    protected void setupMapper() {
        modelMapper.createTypeMap(Point.class, PointDTO.class)
                .addMappings(m -> m.skip(PointDTO::setWastesIds)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(PointDTO::setUserId)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(PointDTO::setOrdersIds)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(PointDTO.class, Point.class)
                .addMappings(m -> m.skip(Point::setWastes)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Point::setUser)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Point::setOrders)).setPostConverter(toEntityConverter());
    }
    
    @Override
    protected void mapSpecificFields(PointDTO source, Point destination) {
        if (!Objects.isNull(source.getWastesIds())) {
            destination.setWastes(new HashSet<>(wasteRepository.findAllById(source.getWastesIds())));
        }
        else {
            destination.setWastes(Collections.emptySet());
        }
        
        if(!Objects.isNull(source.getUserId())) {
            destination.setUser(userRepository.findById(source.getUserId()).orElseThrow(
                    () -> new NotFoundException("Пользователь не найден!")));
        }
        else {
            destination.setUser(null);
        }
        
        if (!Objects.isNull(source.getOrdersIds())) {
            destination.setOrders(new HashSet<>(orderRepository.findAllById(source.getOrdersIds())));
        }
        else {
            destination.setOrders(Collections.emptySet());
        }
    }
    
    @Override
    protected void mapSpecificFields(Point source, PointDTO destination) {
        destination.setWastesIds(getIds(source));
        if (!Objects.isNull(source.getUser())) {
            destination.setUserId(source.getUser().getId());
        }
        else {
            destination.setUserId(null);
        }
        destination.setOrdersIds(getOrdersIds(source));
    }
    
    @Override
    protected Set<Long> getIds(Point point) {
        return Objects.isNull(point) || Objects.isNull(point.getWastes())
                ? null
                : point.getWastes().stream()
                .map(GenericModel::getId)
                .collect(Collectors.toSet());
    }
    
    protected Set<Long> getOrdersIds(Point point) {
        return Objects.isNull(point) || Objects.isNull(point.getOrders())
                ? null
                : point.getOrders().stream()
                .map(GenericModel::getId)
                .collect(Collectors.toSet());
    }
}
