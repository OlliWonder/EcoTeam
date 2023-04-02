package com.sber.java13.ecoteam.mapper;

import com.sber.java13.ecoteam.dto.WasteDTO;
import com.sber.java13.ecoteam.model.Waste;
import com.sber.java13.ecoteam.model.GenericModel;
import com.sber.java13.ecoteam.repository.OrderRepository;
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
    
    protected WasteMapper(ModelMapper modelMapper,
                          OrderRepository orderRepository) {
        super(modelMapper, Waste.class, WasteDTO.class);
        this.orderRepository = orderRepository;
    }
    
    @PostConstruct
    @Override
    protected void setupMapper() {
        modelMapper.createTypeMap(Waste.class, WasteDTO.class)
                .addMappings(m -> m.skip(WasteDTO::setOrdersIds)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(WasteDTO.class, Waste.class)
                .addMappings(m -> m.skip(Waste::setOrders)).setPostConverter(toEntityConverter());
    }
    
    @Override
    protected void mapSpecificFields(WasteDTO source, Waste destination) {
        if (!Objects.isNull(source.getOrdersIds())) {
            destination.setOrders(new HashSet<>(orderRepository.findAllById(source.getOrdersIds())));
        }
        else {
            destination.setOrders(Collections.emptySet());
        }
    }
    
    @Override
    protected void mapSpecificFields(Waste source, WasteDTO destination) {
        destination.setOrdersIds(getIds(source));
    }
    
    protected Set<Long> getIds(Waste author) {
        return Objects.isNull(author) || Objects.isNull(author.getOrders())
                ? null
                : author.getOrders().stream()
                .map(GenericModel::getId)
                .collect(Collectors.toSet());
    }
}
