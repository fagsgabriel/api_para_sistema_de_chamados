package br.com.sistemas.chamados.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import br.com.sistemas.chamados.dto.ChamadoResponse;
import br.com.sistemas.chamados.entity.Prioridade;
import br.com.sistemas.chamados.entity.StatusChamado;
import br.com.sistemas.chamados.exception.RecursoNaoEncontradoException;
import br.com.sistemas.chamados.service.ChamadoService;

@WebMvcTest(ChamadoController.class)
class ChamadoControllerTest {

    private static final String JSON_VALIDO =
        "{\"titulo\":\"Sem rede\",\"descricao\":\"d\",\"prioridade\":\"ALTA\","
        + "\"clienteId\":1,\"categoriaId\":1}";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChamadoService service;

    @Test
    void criarDeveRetornar201() throws Exception {
        when(service.criar(any())).thenReturn(new ChamadoResponse(
            1L, "Sem rede", "d", LocalDateTime.now(), null,
            Prioridade.ALTA, StatusChamado.ABERTO, 1L, 1L, null));

        mockMvc.perform(post("/chamados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON_VALIDO))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/chamados/1"))
            .andExpect(jsonPath("$.status").value("ABERTO"));
    }

    @Test
    void criarSemCamposObrigatoriosDeveRetornar400() throws Exception {
        mockMvc.perform(post("/chamados")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.detalhes").isNotEmpty());
    }

    @Test
    void criarComClienteInexistenteDeveRetornar404() throws Exception {
        when(service.criar(any()))
            .thenThrow(new RecursoNaoEncontradoException("Cliente 1 não encontrado"));

        mockMvc.perform(post("/chamados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON_VALIDO))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.erro").value("Cliente 1 não encontrado"));
    }

    @Test
    void buscarInexistenteDeveRetornar404() throws Exception {
        when(service.buscar(99L))
            .thenThrow(new RecursoNaoEncontradoException("Chamado 99 não encontrado"));

        mockMvc.perform(get("/chamados/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void atribuirSemTecnicoDeveRetornar400() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .patch("/chamados/1/atribuir")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }
}
