package co.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.com.entities.AccountBankEntity;

@Repository
public interface AccountBankRepository extends JpaRepository<AccountBankEntity, Long> {}
