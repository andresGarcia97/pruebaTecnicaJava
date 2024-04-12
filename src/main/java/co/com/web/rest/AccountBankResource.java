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
import org.springframework.web.bind.annotation.RestController;

import co.com.domain.accountbank.AccountBankException;
import co.com.repository.AccountBankRepository;
import co.com.service.AccountBankService;
import co.com.service.dto.AccountBankDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/account-banks")
public class AccountBankResource {

    private static final Logger log = LoggerFactory.getLogger(AccountBankResource.class);

    private final AccountBankService accountBankService;

    private final AccountBankRepository accountBankRepository;

    public AccountBankResource(AccountBankService accountBankService, AccountBankRepository accountBankRepository) {
        this.accountBankService = accountBankService;
        this.accountBankRepository = accountBankRepository;
    }
    
    @PostMapping("")
    public ResponseEntity<?> createAccountBank(@RequestBody(required = true) final AccountBankDTO accountBank) {
        log.debug("REST request to save accountBank: {}", accountBank);
        try {
			return ResponseEntity.ok(accountBankService.save(accountBank));
		} catch (AccountBankException e) {
			log.error("Error to create accountBank: {} ERROR: ", accountBank, e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AccountBankDTO> updateAccountBank(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AccountBankDTO accountBankDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AccountBank : {}, {}", id, accountBankDTO);
        if (accountBankDTO.getId() == null) {
            throw new IllegalArgumentException("Invalid id idnull");
        }
        if (!Objects.equals(id, accountBankDTO.getId())) {
            throw new IllegalArgumentException("Invalid ID, idinvalid");
        }

        if (!accountBankRepository.existsById(id)) {
            throw new IllegalArgumentException("Entity not found, idnotfound");
        }

        AccountBankDTO result = accountBankService.update(accountBankDTO);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("")
    public List<AccountBankDTO> getAll() {
        log.debug("REST request to get all accountBanks");
        return accountBankService.findAll();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountBank(@PathVariable Long id) {
        log.debug("REST request to delete AccountBank : {}", id);
        accountBankService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
