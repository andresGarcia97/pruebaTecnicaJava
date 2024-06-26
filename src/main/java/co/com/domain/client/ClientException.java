package co.com.domain.client;

public final class ClientException extends Exception {

	private static final long serialVersionUID = 1L;

	public static final String CLIENT_NOT_EXIST = "No existe un cliente con el ID proporcionado";

	@SuppressWarnings("unused")
	private ClientException() {
		super();
	}

	public ClientException(final String msgError) {
		super(msgError);
	}

}
