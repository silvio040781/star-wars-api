package com.br.api.restfull.starwars.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="filme")
public class Filme {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String titulo;
	
	@ManyToMany(mappedBy = "filmes")
	private List<Planeta> planetas = new ArrayList<>();
	
	
	
}
