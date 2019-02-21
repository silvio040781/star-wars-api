package com.br.api.restfull.starwars.dto;

import lombok.Data;

@Data
public class PlanetaDto {

	private String nome;
	
	private String clima;
	
	private String terreno;
	
	private Integer quantidadesAparicoes;
}
