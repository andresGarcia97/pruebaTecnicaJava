package co.com.service.mapper;

import java.util.List;

/**
 * Contract for a generic Domain to Entity mapper.
 *
 * @param <D> - Domain type parameter.
 * @param <E> - Entity type parameter.
 */

public interface EntityMapper<D, E> {
    E toEntity(D dto);

    D toDomain(E entity);

    List<E> toEntity(List<D> dtoList);

    List<D> toDomain(List<E> entityList);
}
