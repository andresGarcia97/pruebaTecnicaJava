package co.com.service.mapper.client;

import org.mapstruct.Mapper;

import co.com.entities.ClientEntity;
import co.com.service.dto.ClientDTO;
import co.com.service.mapper.EntityMapper;

@Mapper(componentModel = "spring")
public interface ClientEntityMapper extends EntityMapper<ClientDTO, ClientEntity> {}
