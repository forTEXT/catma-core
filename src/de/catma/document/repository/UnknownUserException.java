package de.catma.document.repository;

import java.io.IOException;

public class UnknownUserException extends IOException {

	public UnknownUserException(String userIdentification) {
		super("User " + userIdentification + " unknown!");
	}
	
}
