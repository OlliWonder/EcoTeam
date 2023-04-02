package com.sber.java13.ecoteam.mapper;

import com.sber.java13.ecoteam.dto.GenericDTO;
import com.sber.java13.ecoteam.model.GenericModel;

import java.util.List;

public interface Mapper<E extends GenericModel, D extends GenericDTO> {
    
    E toEntity(D dto);
    
    D toDTO(E entity);
    
    List<E> toEntities(List<D> dtoList);
    
    List<D> toDTOs(List<E> entitiesList);
}
