package com.kosinskyi.ecom.registry.mapping;

import com.kosinskyi.ecom.registry.entity.base.BaseEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;

@Component
@Transactional
@SuppressWarnings("unchecked")
public abstract class AbstractMapper<E extends BaseEntity, S> {

  private ModelMapper modelMapper;
  S service;

  @Autowired
  public void setModelMapper(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  @Autowired
  @SuppressWarnings("ALL")
  public void setService(S service) {
    this.service = service;
  }

  protected <I> E mapRequestDtoToEntity(I requestDto) {
    return requestDto != null
        ? modelMapper.map(requestDto, (Class<E>) ((ParameterizedType) getClass()
        .getGenericSuperclass()).getActualTypeArguments()[0])
        : null;
  }

  protected <O> O mapEntityToResponseDto(E entity, Class<O> responseClass) {
    return entity != null
        ? modelMapper.map(entity, responseClass)
        : null;
  }
}
