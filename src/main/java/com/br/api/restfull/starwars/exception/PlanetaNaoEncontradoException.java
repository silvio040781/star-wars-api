package com.br.api.restfull.starwars.exception;

public class PlanetaNaoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = -1927709247074989802L;

	public PlanetaNaoEncontradoException(String message) {
        super(message);
    }
}
