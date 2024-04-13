package co.com.domain.transaction;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.domain.accountbank.AccountBank;
import co.com.domain.accountbank.AccountBankException;
import co.com.entities.enumeration.TransactionType;

public class Transaction {
	
    private static final Logger log = LoggerFactory.getLogger(Transaction.class);

	private UUID id;
	
    private TransactionType transactionType;
    
    private ZonedDateTime transactionDate;
    
    private BigDecimal amount;

    private AccountBank origin;
    
    private AccountBank destiny;
    
    public Transaction() {
		super();
	}
    
	public Transaction validateCreation() throws TransactionException, AccountBankException {
		
		final TransactionType type = this.transactionType;
		this.validateObligatoryFields(type);
		
		this.setTransactionDate(ZonedDateTime.now());
		
		String accountObligatory = "";
		
		final boolean destinyAccountExist = this.destiny != null;
		final boolean originAccountExist = this.origin != null;
		
		if(TransactionType.TRANSFERENCIA.equals(type) && destinyAccountExist && originAccountExist) {
			
			if(this.destiny.getId().equals(this.origin.getId())) {
				throw new TransactionException(new StringBuilder()
						.append("Una transaccion de tipo: ").append(type)
						.append(" el origen y el destino No pueden ser iguales")
						.append(" cuenta con ID: ").append(this.origin.getId())
						.append(" y numero: ").append(this.origin.getNumber())
						.toString());
			}
			
			this.origin.substractAmountToBalanceAccount(this.amount);
			this.destiny.addAmountToBalanceAccount(this.amount);
			return this;
		} 
		else {
			accountObligatory = "origen y destino";		
		}

		if(TransactionType.CONSIGNACION.equals(type) && destinyAccountExist) {
			
			if(originAccountExist) {
				log.warn("validateCreation :: una {} no puede tener una cuenta de origen, borrando cuenta: {}",
						type, this.origin);				
				this.setOrigin(null);
			}
			this.destiny.addAmountToBalanceAccount(this.amount);
			return this;
		}
		else {
			accountObligatory = "origen";			
		}
		
		if(TransactionType.RETIRO.equals(type) && originAccountExist) {
			
			if(destinyAccountExist) {
				log.warn("validateCreation :: un {} no puede tener una cuenta de destino, borrando cuenta: {}",
						type, this.destiny);				
				this.setDestiny(null);
			}
			this.origin.substractAmountToBalanceAccount(this.amount);
			return this;
		}
		else {
			accountObligatory = "destino";			
		}
		
		log.error("validateCreation :: type: {}, accountObligatory: {}, destinyAccountExist: {}, originAccountExist: {}",
				type, accountObligatory, destinyAccountExist, originAccountExist);
		throw new TransactionException("Las transaciones de tipo: " + type.toString() + " Deben contar con la cuenta de " + accountObligatory);
		
	}
	
	private void validateObligatoryFields(final TransactionType type) throws TransactionException {
		if(type == null) {
			throw new TransactionException("El tipo de transacción es obligatoria");
		}
		
		if(this.amount == null || BigDecimal.ZERO.compareTo(this.amount) >= 0) {
			throw new TransactionException("El monto de la transacción es obligatorio y debe ser mayor o igual a Zero");
		}
	}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public ZonedDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(ZonedDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public AccountBank getOrigin() {
        return origin;
    }

    public void setOrigin(AccountBank origin) {
        this.origin = origin;
    }
    
    public AccountBank getDestiny() {
		return destiny;
	}

	public void setDestiny(AccountBank destiny) {
		this.destiny = destiny;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(amount, destiny, id, origin, transactionDate, transactionType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		return Objects.equals(amount, other.amount) && Objects.equals(destiny, other.destiny)
				&& Objects.equals(id, other.id) && Objects.equals(origin, other.origin)
				&& Objects.equals(transactionDate, other.transactionDate) && transactionType == other.transactionType;
	}

	// prettier-ignore
    @Override
    public String toString() {
        return "Transaction{" +
            "id='" + getId() + "'" +
            ", transactionType='" + getTransactionType() + "'" +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", amount=" + getAmount() +
            ", origin=" + getOrigin() +
            ", destiny=" + getDestiny() +
            "}";
    }
    
}
