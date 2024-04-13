package co.com.service.mapper.transaction;

import org.mapstruct.Mapper;

import co.com.domain.transaction.Transaction;
import co.com.entities.TransactionEntity;
import co.com.service.mapper.EntityMapper;

@Mapper(componentModel = "spring")
public interface TransactionEntityMapper extends EntityMapper<Transaction, TransactionEntity> {}
