package co.com.entities;

import static co.com.entities.AccountBankTestSamples.*;
import static co.com.entities.ClientTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import co.com.entities.AccountBankEntity;
import co.com.entities.ClientEntity;
import co.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientEntity.class);
        ClientEntity client1 = getClientSample1();
        ClientEntity client2 = new ClientEntity();
        assertThat(client1).isNotEqualTo(client2);

        client2.setId(client1.getId());
        assertThat(client1).isEqualTo(client2);

        client2 = getClientSample2();
        assertThat(client1).isNotEqualTo(client2);
    }

    @Test
    void accountBankTest() throws Exception {
        ClientEntity client = getClientRandomSampleGenerator();
        AccountBankEntity accountBankBack = getAccountBankRandomSampleGenerator();

        client.setAccountBank(accountBankBack);
        assertThat(client.getAccountBank()).isEqualTo(accountBankBack);
        assertThat(accountBankBack.getAccount()).isEqualTo(client);

        client.accountBank(null);
        assertThat(client.getAccountBank()).isNull();
        assertThat(accountBankBack.getAccount()).isNull();
    }
}
