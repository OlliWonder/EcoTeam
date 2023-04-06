package com.sber.java13.ecoteam.mapper;

import com.sber.java13.ecoteam.dto.WasteWithPointsDTO;
import com.sber.java13.ecoteam.model.GenericModel;
import com.sber.java13.ecoteam.model.Waste;
import com.sber.java13.ecoteam.repository.PointRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class WasteWithPointsMapper extends GenericMapper<Waste, WasteWithPointsDTO> {
    private final PointRepository pointRepository;
    
    protected WasteWithPointsMapper(ModelMapper modelMapper, PointRepository pointRepository) {
        super(modelMapper, Waste.class, WasteWithPointsDTO.class);
        this.pointRepository = pointRepository;
    }
    
    @Override
    @PostConstruct
    protected void setupMapper() {
        modelMapper.createTypeMap(Waste.class, WasteWithPointsDTO.class)
                .addMappings(m -> m.skip(WasteWithPointsDTO::setPointsIds)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(WasteWithPointsDTO.class, Waste.class)
                .addMappings(m -> m.skip(Waste::setPoints)).setPostConverter(toEntityConverter());
    }
    
    @Override
    protected void mapSpecificFields(WasteWithPointsDTO source, Waste destination) {
        destination.setPoints(new HashSet<>(pointRepository.findAllById(source.getPointsIds())));
    }
    
    @Override
    protected void mapSpecificFields(Waste source, WasteWithPointsDTO destination) {
        destination.setPointsIds(getIds(source));
    }
    
    @Override
    protected Set<Long> getIds(Waste entity) {
        return Objects.isNull(entity) || Objects.isNull(entity.getId())
                ? null
                : entity.getPoints().stream()
                .map(GenericModel::getId)
                .collect(Collectors.toSet());
    }
}