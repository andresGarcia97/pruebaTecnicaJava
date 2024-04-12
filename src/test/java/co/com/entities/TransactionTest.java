package co.com.entities;

import static co.com.entities.AccountBankTestSamples.*;
import static co.com.entities.TransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import co.com.entities.AccountBankEntity;
import co.com.entities.TransactionEntity;
import co.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionEntity.class);
        TransactionEntity transaction1 = getTransactionSample1();
        TransactionEntity transaction2 = new TransactionEntity();
        assertThat(transaction1).isNotEqualTo(transaction2);

        transaction2.setId(transaction1.getId());
        assertThat(transaction1).isEqualTo(transaction2);

        transaction2 = getTransactionSample2();
        assertThat(transaction1).isNotEqualTo(transaction2);
    }

    @Test
    void originTest() throws Exception {
        TransactionEntity transaction = getTransactionRandomSampleGenerator();
        AccountBankEntity accountBankBack = getAccountBankRandomSampleGenerator();

        transaction.setOrigin(accountBankBack);
        assertThat(transaction.getOrigin()).isEqualTo(accountBankBack);

        transaction.origin(null);
        assertThat(transaction.getOrigin()).isNull();
    }
}
