package com.sber.java13.ecoteam.mapper;

import com.sber.java13.ecoteam.dto.PointWithWastesDTO;
import com.sber.java13.ecoteam.model.GenericModel;
import com.sber.java13.ecoteam.model.Point;
import com.sber.java13.ecoteam.repository.WasteRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PointWithWastesMapper extends GenericMapper<Point, PointWithWastesDTO> {
    private final WasteRepository wasteRepository;
    
    protected PointWithWastesMapper(ModelMapper modelMapper, WasteRepository wasteRepository) {
        super(modelMapper, Point.class, PointWithWastesDTO.class);
        this.wasteRepository = wasteRepository;
    }
    
    @Override
    @PostConstruct
    protected void setupMapper() {
        modelMapper.createTypeMap(Point.class, PointWithWastesDTO.class)
                .addMappings(m -> m.skip(PointWithWastesDTO::setWastesIds)).setPostConverter(toDtoConverter());
        
        modelMapper.createTypeMap(PointWithWastesDTO.class, Point.class)
                .addMappings(m -> m.skip(Point::setWastes)).setPostConverter(toEntityConverter());
    }
    
    @Override
    protected void mapSpecificFields(PointWithWastesDTO source, Point destination) {
        destination.setWastes(new HashSet<>(wasteRepository.findAllById(source.getWastesIds())));
    }
    
    @Override
    protected void mapSpecificFields(Point source, PointWithWastesDTO destination) {
        destination.setWastesIds(getIds(source));
    }
    
    @Override
    protected Set<Long> getIds(Point entity) {
        return Objects.isNull(entity) || Objects.isNull(entity.getId())
                ? null
                : entity.getWastes().stream()
                .map(GenericModel::getId)
                .collect(Collectors.toSet());
    }
}
