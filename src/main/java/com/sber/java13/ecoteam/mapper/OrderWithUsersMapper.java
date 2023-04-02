package com.sber.java13.ecoteam.mapper;

import com.sber.java13.ecoteam.dto.OrderWithUsersDTO;
import com.sber.java13.ecoteam.model.GenericModel;
import com.sber.java13.ecoteam.model.Order;
import com.sber.java13.ecoteam.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderWithUsersMapper extends GenericMapper<Order, OrderWithUsersDTO> {
    private final UserRepository userRepository;
    
    protected OrderWithUsersMapper(ModelMapper modelMapper, UserRepository userRepository) {
        super(modelMapper, Order.class, OrderWithUsersDTO.class);
        this.userRepository = userRepository;
    }
    
    @PostConstruct
    @Override
    protected void setupMapper() {
        modelMapper.createTypeMap(Order.class, OrderWithUsersDTO.class)
                .addMappings(m -> m.skip(OrderWithUsersDTO::setUsersIds)).setPostConverter(toDtoConverter());
        
        modelMapper.createTypeMap(OrderWithUsersDTO.class, Order.class)
                .addMappings(m -> m.skip(Order::setUsers)).setPostConverter(toEntityConverter());
    }
    
    @Override
    protected void mapSpecificFields(OrderWithUsersDTO source, Order destination) {
        destination.setUsers(new HashSet<>(userRepository.findAllById(source.getUsersIds())));
    }
    
    @Override
    protected void mapSpecificFields(Order source, OrderWithUsersDTO destination) {
        destination.setUsersIds(getIds(source));
    }
    
    @Override
    protected Set<Long> getIds(Order entity) {
        return Objects.isNull(entity) || Objects.isNull(entity.getId())
                ? null
                : entity.getUsers().stream()
                .map(GenericModel::getId)
                .collect(Collectors.toSet());
    }
}
