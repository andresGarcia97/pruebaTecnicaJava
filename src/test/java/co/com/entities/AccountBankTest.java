package co.com.entities;

import static co.com.entities.AccountBankTestSamples.*;
import static co.com.entities.ClientTestSamples.*;
import static co.com.entities.TransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import co.com.entities.AccountBankEntity;
import co.com.entities.ClientEntity;
import co.com.entities.TransactionEntity;
import co.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountBankTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountBankEntity.class);
        AccountBankEntity accountBank1 = getAccountBankSample1();
        AccountBankEntity accountBank2 = new AccountBankEntity();
        assertThat(accountBank1).isNotEqualTo(accountBank2);

        accountBank2.setId(accountBank1.getId());
        assertThat(accountBank1).isEqualTo(accountBank2);

        accountBank2 = getAccountBankSample2();
        assertThat(accountBank1).isNotEqualTo(accountBank2);
    }

    @Test
    void accountTest() throws Exception {
        AccountBankEntity accountBank = getAccountBankRandomSampleGenerator();
        ClientEntity clientBack = getClientRandomSampleGenerator();

        accountBank.setAccount(clientBack);
        assertThat(accountBank.getAccount()).isEqualTo(clientBack);

        accountBank.account(null);
        assertThat(accountBank.getAccount()).isNull();
    }

    @Test
    void transactionTest() throws Exception {
        AccountBankEntity accountBank = getAccountBankRandomSampleGenerator();
        TransactionEntity transactionBack = getTransactionRandomSampleGenerator();

        accountBank.setTransaction(transactionBack);
        assertThat(accountBank.getTransaction()).isEqualTo(transactionBack);
        assertThat(transactionBack.getOrigin()).isEqualTo(accountBank);

        accountBank.transaction(null);
        assertThat(accountBank.getTransaction()).isNull();
        assertThat(transactionBack.getOrigin()).isNull();
    }
}
