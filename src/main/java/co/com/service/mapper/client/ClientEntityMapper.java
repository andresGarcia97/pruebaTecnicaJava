package co.com.service.mapper.client;

import org.mapstruct.Mapper;

import co.com.domain.client.Client;
import co.com.entities.ClientEntity;
import co.com.service.mapper.EntityMapper;

@Mapper(componentModel = "spring")
public interface ClientEntityMapper extends EntityMapper<Client, ClientEntity> {}
