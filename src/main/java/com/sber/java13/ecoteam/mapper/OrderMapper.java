package com.sber.java13.ecoteam.mapper;

import com.sber.java13.ecoteam.dto.OrderDTO;
import com.sber.java13.ecoteam.model.GenericModel;
import com.sber.java13.ecoteam.model.Order;
import com.sber.java13.ecoteam.repository.UserRepository;
import com.sber.java13.ecoteam.repository.WasteRepository;
import com.sber.java13.ecoteam.service.WasteService;
import com.sber.java13.ecoteam.utils.DateFormatter;
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
public class OrderMapper extends GenericMapper<Order, OrderDTO> {
    private final UserRepository userRepository;
    private final WasteService wasteService;
    private final WasteRepository wasteRepository;
    
    protected OrderMapper(ModelMapper mapper, UserRepository userRepository, WasteService wasteService, WasteRepository wasteRepository) {
        super(mapper, Order.class, OrderDTO.class);
        this.userRepository = userRepository;
        this.wasteService = wasteService;
        this.wasteRepository = wasteRepository;
    }
    
    @PostConstruct
    @Override
    public void setupMapper() {
        super.modelMapper.createTypeMap(Order.class, OrderDTO.class)
                .addMappings(m -> m.skip(OrderDTO::setUsersIds)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(OrderDTO::setWasteDTO)).setPostConverter(toDtoConverter());
    
        super.modelMapper.createTypeMap(OrderDTO.class, Order.class)
                .addMappings(m -> m.skip(Order::setUsers)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Order::setWaste)).setPostConverter(toEntityConverter());
    }
    
    @Override
    protected void mapSpecificFields(OrderDTO source, Order destination) {
        if (!Objects.isNull(source.getUsersIds())) {
            destination.setUsers(new HashSet<>(userRepository.findAllById(source.getUsersIds())));
        }
        else {
            destination.setUsers(Collections.emptySet());
        }
        destination.setOnDate(DateFormatter.formatStringToDate(source.getOnDate()));
        destination.setWaste(wasteRepository.findById(source.getWasteId()).orElseThrow(() -> new NotFoundException("Книги не найдено")));
    }
    
    @Override
    protected void mapSpecificFields(Order source, OrderDTO destination) {
        destination.setUsersIds(getIds(source));
        destination.setWasteId(source.getWaste().getId());
        destination.setWasteDTO(wasteService.getOne(source.getWaste().getId()));
    }
    
    @Override
    protected Set<Long> getIds(Order order) {
        return Objects.isNull(order) || Objects.isNull(order.getUsers())
                ? null
                : order.getUsers().stream()
                .map(GenericModel::getId)
                .collect(Collectors.toSet());
    }
}
