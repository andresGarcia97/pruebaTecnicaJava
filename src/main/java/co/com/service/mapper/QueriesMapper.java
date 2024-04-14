package co.com.service.mapper;

import java.util.List;

/**
 * Contract for a generic DTO to Entity mapper.
 *
 * @param <D> - DTO type parameter.
 * @param <E> - Entity type parameter.
 */

public interface QueriesMapper<D, E> {

	D toDto(E entity);

	List<D> toDto(List<E> entityList);
}
