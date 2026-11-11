package br.com.sistemas.chamados.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import br.com.sistemas.chamados.entity.Categoria;
import br.com.sistemas.chamados.entity.Chamado;
import br.com.sistemas.chamados.entity.Cliente;
import br.com.sistemas.chamados.entity.Prioridade;
import br.com.sistemas.chamados.entity.StatusChamado;

@DataJpaTest
class ChamadoRepositoryTest {

    @Autowired
    private ChamadoRepository repository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    private Cliente ana;
    private Cliente bia;

    @BeforeEach
    void preparar() {
        ana = clienteRepository.save(new Cliente("Ana", "ana@email.com", null));
        bia = clienteRepository.save(new Cliente("Bia", "bia@email.com", null));
        Categoria rede = categoriaRepository.save(new Categoria("Rede", null));

        salvar("Sem rede", Prioridade.ALTA, StatusChamado.ABERTO, ana, rede);
        salvar("Lento", Prioridade.BAIXA, StatusChamado.ABERTO, ana, rede);
        salvar("Wi-Fi caiu", Prioridade.ALTA, StatusChamado.FECHADO, bia, rede);
    }

    private void salvar(String titulo, Prioridade p, StatusChamado s, Cliente c, Categoria cat) {
        Chamado chamado = new Chamado(titulo, "descricao", p, c, cat);
        chamado.setStatus(s);
        chamado.setDataAbertura(LocalDateTime.now());
        repository.save(chamado);
    }

    @Test
    void semFiltrosDeveRetornarTodos() {
        assertThat(repository.filtrar(null, null, null, PageRequest.of(0, 10)).getTotalElements())
            .isEqualTo(3);
    }

    @Test
    void deveFiltrarPorStatus() {
        assertThat(repository.filtrar(StatusChamado.ABERTO, null, null, PageRequest.of(0, 10))
            .getTotalElements()).isEqualTo(2);
    }

    @Test
    void deveFiltrarPorPrioridade() {
        assertThat(repository.filtrar(null, Prioridade.ALTA, null, PageRequest.of(0, 10))
            .getTotalElements()).isEqualTo(2);
    }

    @Test
    void deveFiltrarPorCliente() {
        assertThat(repository.filtrar(null, null, bia.getId(), PageRequest.of(0, 10))
            .getTotalElements()).isEqualTo(1);
    }

    @Test
    void deveCombinarFiltros() {
        assertThat(repository.filtrar(StatusChamado.ABERTO, Prioridade.ALTA, ana.getId(),
            PageRequest.of(0, 10)).getContent())
            .extracting(Chamado::getTitulo).containsExactly("Sem rede");
    }

    @Test
    void devePaginar() {
        var pagina = repository.filtrar(null, null, null, PageRequest.of(0, 2));
        assertThat(pagina.getContent()).hasSize(2);
        assertThat(pagina.getTotalPages()).isEqualTo(2);
    }
}
