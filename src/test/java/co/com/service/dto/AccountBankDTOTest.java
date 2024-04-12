package co.com.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountBankDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountBankDTO.class);
        AccountBankDTO accountBankDTO1 = new AccountBankDTO();
        accountBankDTO1.setId(1L);
        AccountBankDTO accountBankDTO2 = new AccountBankDTO();
        assertThat(accountBankDTO1).isNotEqualTo(accountBankDTO2);
        accountBankDTO2.setId(accountBankDTO1.getId());
        assertThat(accountBankDTO1).isEqualTo(accountBankDTO2);
        accountBankDTO2.setId(2L);
        assertThat(accountBankDTO1).isNotEqualTo(accountBankDTO2);
        accountBankDTO1.setId(null);
        assertThat(accountBankDTO1).isNotEqualTo(accountBankDTO2);
    }
}
