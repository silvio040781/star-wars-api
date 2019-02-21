package com.br.api.restfull.starwars;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.br.api.restfull.starwars.controllers.PlanetaController;
import com.br.api.restfull.starwars.model.Planeta;
import com.br.api.restfull.starwars.services.PlanetaService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StarWarsApplicationTests {

	//@Test
	public void contextLoads() {
	}

	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlanetaService planetaService;
    
    @Autowired
    private PlanetaController planetaController;

    
    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(planetaController).build();
    }
    
    @Test
    public void findAll() throws Exception {
        Planeta planeta = new Planeta();
        
        planeta.setId(1l);
        planeta.setNome("Naboo");
        planeta.setClima("Seco");
        planeta.setTerreno("não conhecido");
        planeta.setQuantidadesAparicoes(4);
        
        Optional<Planeta> op = Optional.of(planeta);
        
        given(planetaService.buscarPorId(1l)).willReturn(op);

        this.mockMvc.perform(get("/api/planetas"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id': 1,'name': 'Naboo';'terreno': 'não conhecido'}]"));
    }
}
