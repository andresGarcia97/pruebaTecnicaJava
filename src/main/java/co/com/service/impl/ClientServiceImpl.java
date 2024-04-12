package co.com.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.domain.client.Client;
import co.com.domain.client.ClientException;
import co.com.entities.ClientEntity;
import co.com.repository.ClientRepository;
import co.com.service.ClientService;
import co.com.service.dto.ClientDTO;
import co.com.service.mapper.client.ClientDomainMapper;
import co.com.service.mapper.client.ClientEntityMapper;
import co.com.service.mapper.client.ClientQueriesMapper;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;

    private final ClientQueriesMapper queriesMapper;
    private final ClientDomainMapper domainMapper;
    private final ClientEntityMapper entityMapper;

    public ClientServiceImpl(ClientRepository clientRepository,
    		ClientQueriesMapper queriesMapper,
    		ClientDomainMapper domainMapper,
    		ClientEntityMapper entityMapper) {
        this.clientRepository = clientRepository;
        this.queriesMapper = queriesMapper;
		this.domainMapper = domainMapper;
		this.entityMapper = entityMapper;
    }

    @Override
    public ClientDTO save(final ClientDTO client) throws ClientException {
    	
        final Client toValidate = domainMapper.toDomain(client).validateCreation();
        final ClientEntity saved = clientRepository.save(entityMapper.toEntity(toValidate));
        log.debug("save :: toValidate: {}", toValidate);
        log.debug("save :: saved: {}", saved);
		return queriesMapper.toDto(saved);
    }

    @Override
    public ClientDTO update(final ClientDTO client) throws ClientException {
    	
    	final ClientEntity clientFound = clientRepository.findById(client.getId())
    			.orElseThrow(() -> new ClientException("No existe un cliente con el ID proporcionado"));
    	
        final Client toUpdate = domainMapper.toDomain(client).validateUpdate(entityMapper.toDomain(clientFound));
        final ClientEntity updated = clientRepository.save(entityMapper.toEntity(toUpdate));
        log.debug("update :: toUpdate: {}", toUpdate);
        log.debug("update :: updated: {}", updated);
        return queriesMapper.toDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> findAll() {
        return clientRepository.findAll().stream().map(queriesMapper::toDto).toList();
    }

    @Override
    public void delete(Long clientId) {
        log.debug("delete :: clientId: {}", clientId);
        clientRepository.deleteById(clientId);
    }
}
