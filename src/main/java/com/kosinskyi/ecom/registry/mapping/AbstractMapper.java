package com.kosinskyi.ecom.registry.mapping;

import com.kosinskyi.ecom.registry.entity.BaseEntity;
import com.kosinskyi.ecom.registry.service.CrudService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
@SuppressWarnings("unchecked")
public abstract class AbstractMapper<E extends BaseEntity, I, O> {

  private ModelMapper modelMapper;
  protected CrudService<E> crudService;

  @Autowired
  public void setModelMapper(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  @Autowired
  @SuppressWarnings("ALL")
  public void setCrudService(CrudService<E> crudService) {
    this.crudService = crudService;
  }

  public O getById(Long id) {
    return mapEntityToResponseDto(crudService.findById(id));
  }

  public List<O> getAll() {
    List<E> entities = crudService.findAll();
    return mapEntityListToResponseDtoList(entities);
  }

  public O create(I requestDto) {
    E entity = mapRequestDtoToEntity(requestDto);
    E createdEntity = crudService.create(entity);
    return mapEntityToResponseDto(createdEntity);
  }

  public O update(Long id, I requestDto) {
    E entity = mapRequestDtoToEntity(requestDto);
    E updatedEntity = crudService.update(id, entity);
    return mapEntityToResponseDto(updatedEntity);
  }

  public O delete(Long id) {
    E entity = crudService.delete(id);
    return mapEntityToResponseDto(entity);
  }

  protected O mapEntityToResponseDto(E entity) {
    return entity != null
        ? modelMapper.map(entity, (Class<O>) ((ParameterizedType) getClass()
        .getGenericSuperclass()).getActualTypeArguments()[2])
        : null;
  }

  protected E mapRequestDtoToEntity(I dto) {
    return dto != null
        ? modelMapper.map(dto, (Class<E>) ((ParameterizedType) getClass()
        .getGenericSuperclass()).getActualTypeArguments()[0])
        : null;
  }

  protected List<O> mapEntityListToResponseDtoList(List<E> entityList) {
    return entityList != null
        ? entityList
        .stream()
        .map(this::mapEntityToResponseDto)
        .collect(Collectors.toList())
        : new ArrayList<>();
  }

  protected Page<O> mapEntityListToResponseDtoList(Page<E> entityList) {
    return entityList != null
        ? entityList.map(this::mapEntityToResponseDto)
        : null;
  }
}
