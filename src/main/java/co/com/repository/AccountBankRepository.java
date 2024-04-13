package co.com.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import co.com.entities.AccountBankEntity;

@Repository
public interface AccountBankRepository extends JpaRepository<AccountBankEntity, Long> {

	Optional<AccountBankEntity> findByNumber(Long number);
	
	@Modifying
	@Transactional
	@Query("UPDATE AccountBankEntity ab SET ab.balance = ?1 WHERE ab.id = ?2")
	void updateBalanceById(BigDecimal balance, Long id);
	
}
