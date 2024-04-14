package co.com.entities.enumeration;

public enum AccountType {

	CUENTA_CORRIENTE(33),
	CUENTA_AHORROS(53);

	private final int startNumber;

	private AccountType(int startNumber) {
		this.startNumber = startNumber;
	}

	public int getStartNumber() {
		return startNumber;
	}

}
