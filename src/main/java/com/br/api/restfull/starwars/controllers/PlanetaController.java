package com.br.api.restfull.starwars.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.api.restfull.starwars.dominio.MensagemResponse;
import com.br.api.restfull.starwars.dto.PlanetaDto;
import com.br.api.restfull.starwars.exception.BusinessException;
import com.br.api.restfull.starwars.exception.PlanetaNaoEncontradoException;
import com.br.api.restfull.starwars.model.Planeta;
import com.br.api.restfull.starwars.response.Response;
import com.br.api.restfull.starwars.services.PlanetaService;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@RestController
@RequestMapping("/api/planetas")
public class PlanetaController {
	
	private static final Logger log = LoggerFactory.getLogger(PlanetaController.class);
	
	private static final String URI_API_SWAPI = "https://swapi.co/api/planets/";

	private static final String PLANETA_NOT_FIND = "Planeta não localizado!";
	
	private static final String MSG_PLANETA_REMOVIDO_SUCESSO = "Planeta excluido com sucesso!";
	
	private static final String MSG_NOME_PLANETA_OBRIGATORIO = "Informe um nome para o novo Planeta!";
	
	private static final String MSG_ERRO_OBTER_LISTA_PLANETAS = "Ocorreu um erro ao obter a lista de Planetas.";
	
	private static final String MSG_ERRO_OBTER_LISTA_PLANETAS_API_SWAPI = "Ocorreu um erro ao obter a lista de Planetas da API SWAPI.";
	
	private static final String MSG_CONFLITO_NOME_PLANETA = "Nao é permitido salvar novo planeta com um nome já existente no banco de dados."; 

	@Autowired
    private PlanetaService planetaService;
	
	/**
	 * Retorna um planeta dado um Nome.
	 * 
	 * @param nome
	 * @return ResponseEntity<Response<PlanetaDto>>
	 */
	@GetMapping
	public ResponseEntity<Response<Planeta>> buscarPorNome(@RequestParam("nome") String nome) {
		log.info("Buscando planeta pelo Nome: {}", nome);
		Response<Planeta> response = new Response<Planeta>();
		Optional<Planeta> planeta = planetaService.buscarPorNome(nome);

		if (!planeta.isPresent()) {
			log.info("Planeta não encontrada com Nome: {}", nome);
			response.getErrors().add("Planeta não encontrada com Nome " + nome);
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(planeta.get());
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Retorna um planeta pelo ID.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<Planeta>>
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<Planeta>> buscarPorId(@PathVariable("id") Long id) {
		log.info("Buscando planeta pelo ID: {}", id);
		Response<Planeta> response = new Response<Planeta>();
		Optional<Planeta> planeta = planetaService.buscarPorId(id);

		if (!planeta.isPresent()) {
			log.info("Planeta não encontrada com ID: {}", id);
			response.getErrors().add("Planeta não encontrada com ID " + id);
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(planeta.get());
		return ResponseEntity.ok(response);
	}
	
	
	 /**
     * Adicionar novo Planeta
     *
     * @param planeta dados para o cadastro da solicitação
     * @return Retorna o Planeta criado
     */
	@ResponseBody
    @PostMapping
    public ResponseEntity<Response<Planeta>> adicionarPlaneta(@RequestBody Planeta planeta) {
		Response<Planeta> response = new Response<Planeta>();
		
		if(validarNomePlaneta(planeta.getNome())){
			response.getErrors().add(MSG_NOME_PLANETA_OBRIGATORIO);
			return ResponseEntity.ok().body(response);
		}

		if(planetaService.verificarDuplicidadeNomePlaneta(planeta.getNome())){
			response.getErrors().add(MSG_CONFLITO_NOME_PLANETA);
			return ResponseEntity.ok().body(response);
		}
		
        try {
        	Planeta novoPlaneta = planetaService.salvarPlaneta(planeta);
        	response.setData(novoPlaneta);
        }catch (PlanetaNaoEncontradoException e) {
        	response.getErrors().add("Não foi possível salvar o Planeta com Nome " + planeta.getNome() +", Planeta inexistente na API SWAPI.");
			return ResponseEntity.badRequest().body(response);
		}catch (BusinessException be) {
			response.getErrors().add(be.getMessage());
			return ResponseEntity.badRequest().body(response);
		}

        
        return ResponseEntity.ok(response);
    }
	
	 /**
     * Consulta todos os planetas criados no banco de dados
     *
     * @return Retorna lista de Planetas
     */
	@GetMapping(value = "/searchAll")
	public List<Planeta> consultarPlanetas(){
		List<Planeta> planetas = null;
		
		try{
			planetas = planetaService.consultarPlanetas();
		}catch (Exception e) {
			String message = MSG_ERRO_OBTER_LISTA_PLANETAS;
			log.error(this.getClass().getName() + ": " + message);
			log.error(e.getMessage());
			throw new BusinessException(message);
		}
		
		return planetas;
	}
	
	/**
     * Consulta todos os planetas da API SWAPI
     *
     * @return Retorna lista de Planetas da API SWAPI
     */
	@GetMapping(value = "/searchAllPlanetasApiSwapi")
	public List<PlanetaDto> consultarPlanetaApiSwapi(){
		List<PlanetaDto> planetas = null;
		
		try{
			planetas = listarPlanetaApiSwapi();
		}catch (Exception e) {
			String message = MSG_ERRO_OBTER_LISTA_PLANETAS_API_SWAPI;
			log.error(this.getClass().getName() + ": " + message);
			log.error(e.getMessage());
			throw new BusinessException(message);
		}
		
		return planetas;
	}
	
	/**
     * Remove planeta pelo ID do banco de dados
     * 
     * @param id do planeta
     * @return Retorna mensagem response
     */
	@DeleteMapping("/{id}")
	public ResponseEntity<MensagemResponse> removerPlaneta(@PathVariable Long id) {
		Optional<Planeta> planeta = planetaService.buscarPorId(id);
		
		MensagemResponse mensagemResponse = new MensagemResponse();

        if (planeta.isPresent()) {
        	 
        	planetaService.removerPlaneta(planeta.get());
        	
        	mensagemResponse.setCodigoResponse(200);
        	mensagemResponse.setMensagem(MSG_PLANETA_REMOVIDO_SUCESSO);
        	
        	 return ResponseEntity.ok(mensagemResponse);
           
        } else {
        	mensagemResponse.setCodigoResponse(1);
        	mensagemResponse.setMensagem(PLANETA_NOT_FIND);
        	return ResponseEntity.ok(mensagemResponse);
        }
    }
	
	/**
     * Valida o nome do planeta informado
     * 
     * @param nome planeta
     * @return boolean TRUE or FALSE
     */
	private boolean validarNomePlaneta(String nomePlaneta){
		return nomePlaneta.trim().isEmpty() ? true: false;
	}
	
	/**
     * Método responsavel por acessar a API SWAPI e retornar todos os Planetas
     * 
     * @return List<PlanetaDto> 
     */
	private List<PlanetaDto> listarPlanetaApiSwapi(){
		
		String urlApiSwapi = URI_API_SWAPI;
		
		List<PlanetaDto> planetas = new ArrayList<>();
		
		boolean hasNextPage = true;
		try {
			
			while(hasNextPage){
				
				String planetasApi = Unirest.get(urlApiSwapi)
				        .asJson()
				        .getBody()
				        .toString();
				
				JSONObject planetaJson = new JSONObject(planetasApi);
				
				urlApiSwapi = planetaJson.get("next").toString();
				
				hasNextPage = hasMorePage(urlApiSwapi);
				
				JSONArray results = planetaJson.getJSONArray("results");
				
				for(int i = 0; i < results.length(); i++ ){
					JSONObject resultJsonObject = results.getJSONObject(i);
					
					PlanetaDto planetaDto = new PlanetaDto();
					planetaDto.setNome(resultJsonObject.getString("name"));
					planetaDto.setClima(resultJsonObject.getString("climate"));
					planetaDto.setTerreno(resultJsonObject.getString("terrain"));
					planetaDto.setQuantidadesAparicoes(resultJsonObject.getJSONArray("films").length());
					
					planetas.add(planetaDto);
				}
			}
			
		} catch (UnirestException |  JSONException e) {
			log.error(this.getClass().getName() + ": " + e.getMessage());
		}
		
		return planetas;
	}
	
	/**
     * Valida se na consulta da API se existe paginação
     * 
     * @param nextPag com os dados da API
     * @return boolean TRUE or FALSE
     */
	private boolean hasMorePage(String nextPag){
		if("null".equals(nextPag)) {
			return false;
		}
		return true;
	}
}
