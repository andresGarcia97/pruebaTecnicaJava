package co.com.service.mapper;

import java.util.List;

public interface DomainMapper<D, E> {
    E toDomain(D dto);

    D toDto(E entity);

    List<E> toDomain(List<D> dtoList);

    List<D> toDto(List<E> entityList);
   
}
