package co.com.service;

import java.util.List;
import java.util.UUID;

import co.com.service.dto.TransactionDTO;

public interface TransactionService {
    /**
     * Save a transaction.
     *
     * @param transactionDTO the entity to save.
     * @return the persisted entity.
     */
    TransactionDTO save(TransactionDTO transactionDTO);

    /**
     * Updates a transaction.
     *
     * @param transactionDTO the entity to update.
     * @return the persisted entity.
     */
    TransactionDTO update(TransactionDTO transactionDTO);

    /**
     * Get all the transactions.
     *
     * @return the list of entities.
     */
    List<TransactionDTO> findAll();

    /**
     * Delete the "id" transaction.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
