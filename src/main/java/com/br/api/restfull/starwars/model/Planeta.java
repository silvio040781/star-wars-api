package com.br.api.restfull.starwars.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="planeta")
public class Planeta {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String nome;
	
	private String clima;
	
	private String terreno;
	
	private Integer quantidadesAparicoes;
	
	@ManyToMany
	@JoinTable(name = "PLANETA_FILME", 
			joinColumns = @JoinColumn(name = "planeta_id"),
				inverseJoinColumns = @JoinColumn(name = "filme_id")
	)
	private List<Filme> filmes = new ArrayList<>();
	
}
