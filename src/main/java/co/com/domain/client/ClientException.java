package co.com.domain.client;

public class ClientException extends Exception {

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private ClientException() {
		super();
	}
	
	public ClientException(final String msgError) {
		super(msgError);
	}

}
