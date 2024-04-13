package co.com.service.mapper.transaction;

import org.mapstruct.Mapper;

import co.com.domain.transaction.Transaction;
import co.com.dto.TransactionDTO;
import co.com.service.mapper.DomainMapper;

@Mapper(componentModel = "spring")
public interface TransactionDomainMapper extends DomainMapper<TransactionDTO, Transaction> {}
