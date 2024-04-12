package co.com.service;

import java.util.List;

import co.com.service.dto.AccountBankDTO;

public interface AccountBankService {
    /**
     * Save a accountBank.
     *
     * @param accountBankDTO the entity to save.
     * @return the persisted entity.
     */
    AccountBankDTO save(AccountBankDTO accountBankDTO);

    /**
     * Updates a accountBank.
     *
     * @param accountBankDTO the entity to update.
     * @return the persisted entity.
     */
    AccountBankDTO update(AccountBankDTO accountBankDTO);

    /**
     * Get all the accountBanks.
     *
     * @return the list of entities.
     */
    List<AccountBankDTO> findAll();

    /**
     * Delete the "id" accountBank.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
