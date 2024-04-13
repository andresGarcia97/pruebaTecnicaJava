package co.com.service.mapper.client;

import org.mapstruct.Mapper;

import co.com.domain.client.Client;
import co.com.dto.ClientDTO;
import co.com.service.mapper.DomainMapper;

@Mapper(componentModel = "spring")
public interface ClientDomainMapper extends DomainMapper<ClientDTO, Client> {}
