package com.br.api.restfull.starwars.services.impl;

import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.api.restfull.starwars.dto.PlanetaDto;
import com.br.api.restfull.starwars.exception.BusinessException;
import com.br.api.restfull.starwars.exception.PlanetaNaoEncontradoException;
import com.br.api.restfull.starwars.model.Planeta;
import com.br.api.restfull.starwars.repositories.PlanetaRepository;
import com.br.api.restfull.starwars.services.PlanetaService;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Service
public class PlanetaServiceImpl implements PlanetaService {

private static final Logger log = LoggerFactory.getLogger(PlanetaServiceImpl.class);
	
	@Autowired
	private PlanetaRepository planetaRepository;
	
	private static final String NOT_FOUND = "0";
	
	private static final String MESSAGE_NOT_FOUND = "Não encontrado";
	
	private static final String API_SWAPI_INDISPONIVEL = "API SWAPI indisponível";
	
	@Override
	public Optional<Planeta> buscarPorNome(String nome) {
		log.info("Buscando um planeta pelo Nome {}", nome);
		return Optional.ofNullable(planetaRepository.findByNome(nome));
	}
	
	@Override
	public Optional<Planeta> buscarPorId(Long id) {
		log.info("Buscando um planeta pelo ID {}", id);
		return Optional.ofNullable(planetaRepository.findById(id));
	}

	@Override
	public Planeta salvarPlaneta(Planeta planeta) {
		
		if(!verificarDuplicidadeNomePlaneta(planeta.getNome())){
			Integer aparicoes = verificaPlanetaNaApiSwapi(planeta.getNome());
			planeta.setQuantidadesAparicoes(aparicoes);
			
			planeta = planetaRepository.save(planeta);
		}
		
		return planeta;
	}
	
	@Override
	public boolean verificarDuplicidadeNomePlaneta(String nomePlaneta){
		
		Optional<Planeta> planetaVerificado = buscarPorNome(nomePlaneta);
		
		return planetaVerificado.isPresent() ? true : false;
	}
	
	@Override
	public List<Planeta> consultarPlanetas() {
		List<Planeta> planetas = planetaRepository.findAll();
		
		return planetas;
	}
	
	@Override
	public void removerPlaneta(Planeta planeta) {
		planetaRepository.delete(planeta);
	}
	
	private Integer verificaPlanetaNaApiSwapi(String nome) {
		
		 Integer quantidadesAparicoes = 0;
		
		try {
			String planetaApi = Unirest.get("https://swapi.co/api/planets/?search={name}")
					.routeParam("name", nome)
			        .asJson()
			        .getBody()
			        .toString();
			
			JSONObject planetaJson = new JSONObject(planetaApi);
            
            String count = planetaJson.get("count").toString();
            
            if(NOT_FOUND.equals(count)) {
            	throw new PlanetaNaoEncontradoException(MESSAGE_NOT_FOUND);
            }
            
            JSONArray results = planetaJson.getJSONArray("results");
            
            JSONObject filmesJson = results.getJSONObject(0);
            
            quantidadesAparicoes = filmesJson.getJSONArray("films").length();
            
		} catch (UnirestException  | JSONException ex) {
			throw new BusinessException(API_SWAPI_INDISPONIVEL);
		}
		 
		return quantidadesAparicoes;
	}

}
