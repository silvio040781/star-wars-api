package com.br.api.restfull.starwars.dominio;

import lombok.Data;

@Data
public class MensagemResponse {

	int codigoResponse;
	String mensagem;
	
	public MensagemResponse() {
		super();
	}
	
	public MensagemResponse(int codigoResponse) {
		super();
		this.codigoResponse = codigoResponse;
	}
	
	public MensagemResponse(int codigoResponse, String mensagem) {
		super();
		this.codigoResponse = codigoResponse;
		this.mensagem = mensagem;
	}
}
