package co.com.service;

import java.util.List;

import co.com.domain.client.ClientException;
import co.com.service.dto.ClientDTO;

public interface ClientService {

	ClientDTO save(ClientDTO client) throws ClientException;

	ClientDTO update(ClientDTO client) throws ClientException;

	List<ClientDTO> findAll();

	void delete(Long clientId);
}
