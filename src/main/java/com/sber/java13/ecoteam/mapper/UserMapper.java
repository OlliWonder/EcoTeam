package com.sber.java13.ecoteam.mapper;

import com.sber.java13.ecoteam.dto.UserDTO;
import com.sber.java13.ecoteam.model.GenericModel;
import com.sber.java13.ecoteam.model.User;
import com.sber.java13.ecoteam.repository.OrderRepository;
import com.sber.java13.ecoteam.utils.DateFormatter;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper extends GenericMapper<User, UserDTO> {
    private final OrderRepository orderRepository;
    
    protected UserMapper(ModelMapper modelMapper, OrderRepository orderRepository) {
        super(modelMapper, User.class, UserDTO.class);
        this.orderRepository = orderRepository;
    }
    
    @PostConstruct
    @Override
    protected void setupMapper() {
        modelMapper.createTypeMap(User.class, UserDTO.class)
                .addMappings(m -> m.skip(UserDTO::setOrdersIds)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(UserDTO.class, User.class)
                .addMappings(m -> m.skip(User::setOrders)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(User::setBirthDate)).setPostConverter(toEntityConverter());
    }
    
    @Override
    protected void mapSpecificFields(UserDTO source, User destination) {
        if (!Objects.isNull(source.getOrdersIds())) {
            destination.setOrders(new HashSet<>(orderRepository.findAllById(source.getOrdersIds())));
        } else {
            destination.setOrders(Collections.emptySet());
        }
        destination.setBirthDate(DateFormatter.formatStringToDate(source.getBirthDate()));
    }
    
    @Override
    protected void mapSpecificFields(User source, UserDTO destination) {
        destination.setOrdersIds(getIds(source));
    }
    
    @Override
    protected Set<Long> getIds(User entity) {
        return Objects.isNull(entity) || Objects.isNull(entity.getOrders())
                ? null
                : entity.getOrders().stream()
                .map(GenericModel::getId)
                .collect(Collectors.toSet());
    }
}
