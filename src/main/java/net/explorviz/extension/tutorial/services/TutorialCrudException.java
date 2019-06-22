package net.explorviz.extension.tutorial.services;

public class TutorialCrudException extends Exception {
	/**
	 * Exception for all errors that occur during CRUD operations.
	 */
	public TutorialCrudException() {
		super();
	}

	public TutorialCrudException(final String msg) {
		super(msg);
	}

	public TutorialCrudException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
