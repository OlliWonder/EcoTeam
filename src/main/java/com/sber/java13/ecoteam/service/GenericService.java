package com.sber.java13.ecoteam.service;

import com.sber.java13.ecoteam.dto.GenericDTO;
import com.sber.java13.ecoteam.exception.MyDeleteException;
import com.sber.java13.ecoteam.mapper.GenericMapper;
import com.sber.java13.ecoteam.model.GenericModel;
import com.sber.java13.ecoteam.repository.GenericRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Абстрактный сервис, который хранит в себе реализацию CRUD операций по-умолчанию.
 * Если реализация отличная от того, что представлено в этом классе,
 * то она переопределяется в реализации конкретного сервиса.
 *
 * @param <T> - Сущность, с которой мы работаем.
 * @param <N> - DTO, которую мы будем отдавать/принимать дальше.
 */
public abstract class GenericService<T extends GenericModel, N extends GenericDTO> {
    protected final GenericRepository<T> repository;
    protected final GenericMapper<T, N> mapper;
    
    protected GenericService(GenericRepository<T> repository, GenericMapper<T, N> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    
    public List<N> listAll() {
        return mapper.toDTOs(repository.findAll());
    }
    
    public Page<N> listAll(Pageable pageable) {
        Page<T> objects = repository.findAll(pageable);
        List<N> result = mapper.toDTOs(objects.getContent());
        return new PageImpl<>(result, pageable, objects.getTotalElements());
    }
    
    public Page<N> listAllNotDeleted(Pageable pageable) {
        Page<T> preResult = repository.findAllByIsDeletedFalse(pageable);
        List<N> result = mapper.toDTOs(preResult.getContent());
        return new PageImpl<>(result, pageable, preResult.getTotalElements());
    }
    
    public List<N> listAllNotDeleted() {
        return mapper.toDTOs(repository.findAllByIsDeletedFalse());
    }
    
    public N getOne(Long id) {
        return mapper.toDTO(repository.findById(id).orElseThrow(() -> new NotFoundException("Данных по заданному id: " + id + " не найдены")));
    }
    
    public N create(N object) {
        object.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        object.setCreatedWhen(LocalDateTime.now());
        return mapper.toDTO(repository.save(mapper.toEntity(object)));
    }
    
    public N update(N object) {
        return mapper.toDTO(repository.save(mapper.toEntity(object)));
    }
    
    public void delete(Long id) throws MyDeleteException {
        repository.deleteById(id);
    }
    
    public void markAsDeleted(GenericModel genericModel) {
        genericModel.setDeleted(true);
        genericModel.setDeletedWhen(LocalDateTime.now());
        genericModel.setDeletedBy(SecurityContextHolder.getContext().getAuthentication().getName());
    }
    
    public void unMarkAsDeleted(GenericModel genericModel) {
        genericModel.setDeleted(false);
        genericModel.setDeletedWhen(null);
        genericModel.setDeletedBy(null);
    }
}
