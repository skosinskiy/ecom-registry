package com.kosinskyi.ecom.registry.mapping;

import com.kosinskyi.ecom.registry.entity.BaseEntity;
import com.kosinskyi.ecom.registry.service.CrudService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;

@Component
@Transactional
@SuppressWarnings("unchecked")
public abstract class AbstractMapper<E extends BaseEntity, I, O> {

  private ModelMapper modelMapper;
  CrudService<E> crudService;

  @Autowired
  public void setModelMapper(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  @Autowired
  @SuppressWarnings("ALL")
  public void setCrudService(CrudService<E> crudService) {
    this.crudService = crudService;
  }

  protected O mapEntityToResponseDto(E entity) {
    return entity != null
        ? modelMapper.map(entity, (Class<O>) ((ParameterizedType) getClass()
        .getGenericSuperclass()).getActualTypeArguments()[2])
        : null;
  }
}
