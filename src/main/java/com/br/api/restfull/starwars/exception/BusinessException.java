package com.br.api.restfull.starwars.exception;

public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = -2018215468271766853L;

	public BusinessException() {
		super();
	}
	
	public BusinessException(String message) {
		super(message);
	}
}
