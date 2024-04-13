package co.com.service.mapper.client;

import org.mapstruct.Mapper;

import co.com.dto.ClientDTO;
import co.com.entities.ClientEntity;
import co.com.service.mapper.QueriesMapper;

@Mapper(componentModel = "spring")
public interface ClientQueriesMapper extends QueriesMapper<ClientDTO, ClientEntity> {}
