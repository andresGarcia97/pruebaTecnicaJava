package co.com.web.rest;

import java.net.URISyntaxException;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * {@code POST  /account-banks} : Create a new accountBank.
     *
     * @param accountBankDTO the accountBankDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountBankDTO, or with status {@code 400 (Bad Request)} if the accountBank has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AccountBankDTO> createAccountBank(@Valid @RequestBody AccountBankDTO accountBankDTO) throws URISyntaxException {
        log.debug("REST request to save AccountBank : {}", accountBankDTO);
        AccountBankDTO result = accountBankService.save(accountBankDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * {@code PUT  /account-banks/:id} : Updates an existing accountBank.
     *
     * @param id the id of the accountBankDTO to save.
     * @param accountBankDTO the accountBankDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountBankDTO,
     * or with status {@code 400 (Bad Request)} if the accountBankDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountBankDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
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

    /**
     * {@code DELETE  /account-banks/:id} : delete the "id" accountBank.
     *
     * @param id the id of the accountBankDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountBank(@PathVariable Long id) {
        log.debug("REST request to delete AccountBank : {}", id);
        accountBankService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
