package co.com.domain.transaction;

public final class TransactionException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private TransactionException() {
		super();
	}
	
	public TransactionException(final String msgError) {
		super(msgError);
	}

}
