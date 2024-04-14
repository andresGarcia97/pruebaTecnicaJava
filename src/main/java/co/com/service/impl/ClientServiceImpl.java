package co.com.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.domain.client.Client;
import co.com.domain.client.ClientException;
import co.com.dto.ClientDTO;
import co.com.entities.AccountBankEntity;
import co.com.entities.ClientEntity;
import co.com.repository.ClientRepository;
import co.com.service.ClientService;
import co.com.service.mapper.client.ClientDomainMapper;
import co.com.service.mapper.client.ClientEntityMapper;
import co.com.service.mapper.client.ClientQueriesMapper;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

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

		return queriesMapper.toDto(saved);
	}

	@Override
	public ClientDTO update(final ClientDTO client) throws ClientException {

		final ClientEntity clientFound = clientRepository.findById(client.getId())
				.orElseThrow(() -> new ClientException(ClientException.CLIENT_NOT_EXIST));

		final Client toUpdate = domainMapper.toDomain(client).validateUpdate(entityMapper.toDomain(clientFound));        
		final ClientEntity updated = clientRepository.save(entityMapper.toEntity(toUpdate));

		return queriesMapper.toDto(updated);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ClientDTO> findAll() {
		return clientRepository.findAll().stream().map(queriesMapper::toDto).toList();
	}

	@Override
	public void delete(Long clientId) throws ClientException {

		final Optional<ClientEntity> clientExist = clientRepository.findById(clientId);

		if(clientExist.isEmpty()) {
			return;
		}

		final ClientEntity clientToDelete = clientExist.get();

		if(clientToDelete.getAccounts() != null && !clientToDelete.getAccounts().isEmpty()) {

			final List<Long> accountIds = clientToDelete.getAccounts().stream().map(AccountBankEntity::getNumber).toList();
			throw new ClientException("No se puede eliminar este cliente, ya que esta asociado a las siguientes cuentas: " + accountIds);
		}

		clientRepository.deleteById(clientId);
	}

	@Override
	@Transactional(readOnly = true)
	public ClientDTO findById(Long clientId) throws ClientException {
		return clientRepository.findById(clientId)
				.map(queriesMapper::toDto)
				.orElseThrow(() -> new ClientException(ClientException.CLIENT_NOT_EXIST));
	}
}
