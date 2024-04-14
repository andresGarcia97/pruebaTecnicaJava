package co.com.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import co.com.dto.TransactionDTO;
import co.com.service.TransactionService;

@DirtiesContext
@WebMvcTest(TransactionResource.class)
class TransactionResourceTest {

	private static final String ENTITY_API_URL = "/api/transactions";

	@MockBean
	private TransactionService transactionService;

	@Autowired
	private MockMvc restTransactionMockMvc;

	private TransactionDTO transaction;

	@BeforeEach
	public void initTest() {
		transaction = new TransactionDTO();
	}

	@Test
	void createTransaction() throws Exception {

		restTransactionMockMvc
		.perform(
				post(ENTITY_API_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(transaction))
				)
		.andExpect(status().isOk());

		final ArgumentCaptor<TransactionDTO> captor = ArgumentCaptor.forClass(TransactionDTO.class);
		verify(transactionService, times(1)).saveAndFlush(captor.capture());
		assertNotNull(captor.getValue());
	}

	@Test
	void getAllAccountBanks() throws Exception {

		restTransactionMockMvc
		.perform(
				get(ENTITY_API_URL)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());

		verify(transactionService, times(1)).findAll();
	}

}
