package org.galactic.empire.secret.software.licensing.exceptions;

public class LMException extends Exception {

	// comentario para cambiar el fichero 
	private static final long serialVersionUID = 1L;
	String message;

	public LMException(String message) {

		this.message = message;
	}

	public String getMessage() {

		return this.message;
	}

}
