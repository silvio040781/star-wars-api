package com.br.api.restfull.starwars.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.api.restfull.starwars.model.Planeta;

public interface PlanetaRepository extends JpaRepository<Planeta, Long> {

	Planeta findByNome(String nome);
	
	Planeta findById(Long id);
}
