package co.com.web.rest;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import co.com.domain.client.ClientException;
import co.com.service.ClientService;
import co.com.service.dto.ClientDTO;


@RestController
@RequestMapping("/api/clients")
public class ClientResource {

    private static final Logger log = LoggerFactory.getLogger(ClientResource.class);

    private final ClientService clientService;

    public ClientResource(ClientService clientService) {
        this.clientService = clientService;
    }
    
    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody(required = true) final ClientDTO client) {
        log.debug("REST request to save client: {}", client);
        if (client.getId() != null) {
            throw new IllegalArgumentException("A new client cannot already have an ID");
        }
        try {
			return ResponseEntity.ok(clientService.save(client));
		} catch (ClientException e) {
			log.error("Error to create client: {} ERROR: ", client, e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    }
    
    @PutMapping("")
    public ResponseEntity<?> update(@RequestBody(required = true) final ClientDTO client) {
        log.debug("REST request to update client: {}", client);
        if (client == null || client.getId() == null) {
            throw new IllegalArgumentException("Invalid ID");
        }
        try {
			return ResponseEntity.ok(clientService.update(client));
		} catch (ClientException e) {
			log.error("Error to update client: {} ERROR: ", client, e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    }
    
    @GetMapping("")
    public List<ClientDTO> getAll() {
        log.debug("REST request to get all clients");
        return clientService.findAll();
    }
    
    @DeleteMapping("/{clientId}")
    public ResponseEntity<?> delete(@PathVariable(required = true) Long clientId) {
        log.debug("REST request to delete client, clientId: {}", clientId);
        try {
			clientService.delete(clientId);
			return ResponseEntity.noContent().build();
		} catch (ClientException e) {
			log.error("Error to delete clientId: {} ERROR: ", clientId, e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    }
}
