package co.com.web.rest;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.com.repository.ClientRepository;
import co.com.service.ClientService;
import co.com.service.dto.ClientDTO;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/clients")
public class ClientResource {

    private static final Logger log = LoggerFactory.getLogger(ClientResource.class);

    private final ClientService clientService;

    private final ClientRepository clientRepository;

    public ClientResource(ClientService clientService, ClientRepository clientRepository) {
        this.clientService = clientService;
        this.clientRepository = clientRepository;
    }

    /**
     * {@code POST  /clients} : Create a new client.
     *
     * @param client the clientDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clientDTO, or with status {@code 400 (Bad Request)} if the client has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO client) {
        log.debug("REST request to save ClientEntity : {}", client);
        if (client.getId() != null) {
            throw new IllegalArgumentException("A new client cannot already have an ID, idexists");
        }
        return ResponseEntity.ok(clientService.save(client));
    }

    /**
     * {@code PUT  /clients/:id} : Updates an existing client.
     *
     * @param id the id of the clientDTO to save.
     * @param clientDTO the clientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clientDTO,
     * or with status {@code 400 (Bad Request)} if the clientDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ClientDTO clientDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ClientEntity : {}, {}", id, clientDTO);
        if (clientDTO.getId() == null) {
            throw new IllegalArgumentException("Invalid id, idnull");
        }
        if (!Objects.equals(id, clientDTO.getId())) {
            throw new IllegalArgumentException("Invalid ID, idinvalid");
        }

        if (!clientRepository.existsById(id)) {
            throw new IllegalArgumentException("Entity not found, idnotfound");
        }

        ClientDTO result = clientService.update(clientDTO);
        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code GET  /clients} : get all the clients.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clients in body.
     */
    @GetMapping("")
    public List<ClientDTO> getAllClients(@RequestParam(required = false) String filter) {
        log.debug("REST request to get all Clients");
        return clientService.findAll();
    }

    /**
     * {@code DELETE  /clients/:id} : delete the "id" client.
     *
     * @param id the id of the clientDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        log.debug("REST request to delete ClientEntity : {}", id);
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
