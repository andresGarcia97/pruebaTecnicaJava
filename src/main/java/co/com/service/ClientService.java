package co.com.service;

import java.util.List;

import co.com.domain.client.ClientException;
import co.com.service.dto.ClientDTO;


public interface ClientService {
    /**
     * Save a client.
     *
     * @param clientDTO the entity to save.
     * @return the persisted entity.
     * @throws ClientException 
     */
    ClientDTO save(ClientDTO clientDTO) throws ClientException;

    /**
     * Updates a client.
     *
     * @param clientDTO the entity to update.
     * @return the persisted entity.
     */
    ClientDTO update(ClientDTO clientDTO);

    /**
     * Get all the clients.
     *
     * @return the list of entities.
     */
    List<ClientDTO> findAll();

    /**
     * Delete the "id" client.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
