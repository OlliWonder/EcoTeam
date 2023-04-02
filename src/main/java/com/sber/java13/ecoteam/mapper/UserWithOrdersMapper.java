package com.sber.java13.ecoteam.mapper;

import com.sber.java13.ecoteam.dto.UserWithOrdersDTO;
import com.sber.java13.ecoteam.model.GenericModel;
import com.sber.java13.ecoteam.model.User;
import com.sber.java13.ecoteam.repository.OrderRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserWithOrdersMapper extends GenericMapper<User, UserWithOrdersDTO> {
    private final OrderRepository orderRepository;
    
    protected UserWithOrdersMapper(ModelMapper modelMapper, OrderRepository orderRepository) {
        super(modelMapper, User.class, UserWithOrdersDTO.class);
        this.orderRepository = orderRepository;
    }
    
    @PostConstruct
    @Override
    protected void setupMapper() {
        modelMapper.createTypeMap(User.class, UserWithOrdersDTO.class)
                .addMappings(m -> m.skip(UserWithOrdersDTO::setOrdersIds)).setPostConverter(toDtoConverter());
        
        modelMapper.createTypeMap(UserWithOrdersDTO.class, User.class)
                .addMappings(m -> m.skip(User::setOrders)).setPostConverter(toEntityConverter());
    }
    
    @Override
    protected void mapSpecificFields(UserWithOrdersDTO source, User destination) {
        destination.setOrders(new HashSet<>(orderRepository.findAllById(source.getOrdersIds())));
    }
    
    @Override
    protected void mapSpecificFields(User source, UserWithOrdersDTO destination) {
        destination.setOrdersIds(getIds(source));
    }
    
    @Override
    protected Set<Long> getIds(User entity) {
        return Objects.isNull(entity) || Objects.isNull(entity.getId())
                ? null
                : entity.getOrders()
                .stream()
                .map(GenericModel::getId)
                .collect(Collectors.toSet());
    }
}
