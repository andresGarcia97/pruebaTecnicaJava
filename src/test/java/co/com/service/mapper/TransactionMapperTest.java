package co.com.service.mapper;

import org.junit.jupiter.api.BeforeEach;

import co.com.service.mapper.transaction.TransactionQueriesMapper;

class TransactionMapperTest {

    private TransactionQueriesMapper transactionMapper;

    @BeforeEach
    public void setUp() {
        transactionMapper = new TransactionMapperImpl();
    }
}
