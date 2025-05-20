package com.enansin.exchenge_rates_bot.exception;

public class ServiceException extends Exception {
	private static final long serialVersionUID = 1L;

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
