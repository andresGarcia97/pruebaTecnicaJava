package co.com.service.mapper;

/**
 * Contract for a generic DTO to Domain mapper.
 *
 * @param <D> - DTO type parameter.
 * @param <B> - Domain type parameter.
 */

public interface DomainMapper<D, B> {
	B toDomain(D dto);

	D toDto(B domain);

}
