package co.com.service.mapper;

import java.util.List;

/**
 * Contract for a generic DTO to Entity mapper.
 *
 * @param <D> - DTO type parameter.
 * @param <E> - Entity type parameter.
 */

public interface QueriesMapper<D, E> {
    E toEntity(D dto);

    D toDto(E entity);

    List<E> toEntity(List<D> dtoList);

    List<D> toDto(List<E> entityList);
}
