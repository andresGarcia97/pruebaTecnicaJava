package co.com.domain.accountbank;

public final class AccountBankException extends Exception {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private AccountBankException() {
		super();
	}

	public AccountBankException(final String msgError) {
		super(msgError);
	}

}
