package com.kosinskyi.ecom.registry.mapping;

import com.kosinskyi.ecom.registry.entity.BaseEntity;
import com.kosinskyi.ecom.registry.service.AbstractCrudService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public abstract class AbstractCrudMapper
    <E extends BaseEntity, S extends AbstractCrudService<E, ? extends JpaRepository<E, Long>>, I, O> {

  private ModelMapper modelMapper;
  protected S crudService;

  private Class<E> entityClass =
      (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

  private Class<O> responseClass = (Class<O>) ((ParameterizedType) getClass()
        .getGenericSuperclass()).getActualTypeArguments()[3];

  public AbstractCrudMapper(S crudService) {
    this.crudService = crudService;
  }

  @Autowired
  public void setModelMapper(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public O findById(Long id) {
    return mapEntityToResponseDto(crudService.findById(id));
  }

  public List<O> findAll() {
    return crudService.findAll().stream().map(this::mapEntityToResponseDto).collect(Collectors.toList());
  }

  public Page<O> findAll(Pageable pageable) {
    return crudService.findAll(pageable).map(this::mapEntityToResponseDto);
  }

  public O create(I requestDto) {
    return mapEntityToResponseDto(crudService.create(mapRequestDtoToEntity(requestDto)));
  }

  public O update(Long id, I requestDto) {
    return mapEntityToResponseDto(crudService.update(id, mapRequestDtoToEntity(requestDto)));
  }

  public O delete(Long id) {
    return mapEntityToResponseDto(crudService.delete(id));
  }

  protected E mapRequestDtoToEntity(I requestDto) {
    return modelMapper.map(requestDto, entityClass);
  }

  protected O mapEntityToResponseDto(E entity) {
    return modelMapper.map(entity, responseClass);
  }

}
