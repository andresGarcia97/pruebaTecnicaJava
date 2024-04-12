package co.com.service.impl;

import java.time.ZonedDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.domain.Client;
import co.com.entities.ClientEntity;
import co.com.repository.ClientRepository;
import co.com.service.ClientService;
import co.com.service.dto.ClientDTO;
import co.com.service.mapper.client.ClientDomainMapper;
import co.com.service.mapper.client.ClientEntityMapper;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;

    private final ClientEntityMapper entityMapper;
    
    private final ClientDomainMapper domainMapper;

    public ClientServiceImpl(ClientRepository clientRepository,
    		ClientEntityMapper clientMapper,
    		ClientDomainMapper domainMapper) {
        this.clientRepository = clientRepository;
        this.entityMapper = clientMapper;
		this.domainMapper = domainMapper;
    }

    @Override
    public ClientDTO save(final ClientDTO client) {
        log.debug("Request to save Client : {}", client);
        final Client clientToValidate = domainMapper.toDomain(client);
        clientToValidate.setCreationDate(ZonedDateTime.now());
        return entityMapper.toDomain(clientRepository.save(entityMapper.toEntity(client)));
    }

    @Override
    public ClientDTO update(ClientDTO clientDTO) {
        log.debug("Request to update ClientEntity : {}", clientDTO);
        ClientEntity client = entityMapper.toEntity(clientDTO);
        client = clientRepository.save(client);
        return entityMapper.toDomain(client);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> findAll() {
        log.debug("Request to get all Clients");
        return clientRepository.findAll().stream().map(entityMapper::toDomain).toList();
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ClientEntity : {}", id);
        clientRepository.deleteById(id);
    }
}
