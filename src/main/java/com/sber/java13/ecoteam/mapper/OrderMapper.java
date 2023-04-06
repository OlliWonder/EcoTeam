package com.sber.java13.ecoteam.mapper;

import com.sber.java13.ecoteam.dto.OrderDTO;
import com.sber.java13.ecoteam.model.Order;
import com.sber.java13.ecoteam.repository.UserRepository;
import com.sber.java13.ecoteam.repository.WasteRepository;
import com.sber.java13.ecoteam.service.WasteService;
import com.sber.java13.ecoteam.utils.DateFormatter;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;

import java.util.Set;

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
                .addMappings(m -> m.skip(OrderDTO::setUserId)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(OrderDTO::setWasteDTO)).setPostConverter(toDtoConverter());
    
        super.modelMapper.createTypeMap(OrderDTO.class, Order.class)
                .addMappings(m -> m.skip(Order::setUser)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Order::setWaste)).setPostConverter(toEntityConverter());
    }
    
    @Override
    protected void mapSpecificFields(OrderDTO source, Order destination) {
        destination.setUser(userRepository.findById(source.getUserId()).orElseThrow(
                    () -> new NotFoundException("Пользователь не найден!")));
     
        destination.setOnDate(DateFormatter.formatStringToDate(source.getOnDate()));
        destination.setWaste(wasteRepository.findById(source.getWasteId()).orElseThrow(
                () -> new NotFoundException("Такого типа мусора не найдено!")));
    }
    
    @Override
    protected void mapSpecificFields(Order source, OrderDTO destination) {
        destination.setWasteId(source.getWaste().getId());
        destination.setWasteDTO(wasteService.getOne(source.getWaste().getId()));
    }
    
    @Override
    protected Set<Long> getIds(Order order) {
        throw new UnsupportedOperationException("Метод недоступен");
    }
}
