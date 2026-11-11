package br.com.sistemas.chamados.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import br.com.sistemas.chamados.entity.Cliente;

@DataJpaTest
class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository repository;

    @Test
    void existsByEmailDeveRetornarTrueQuandoEmailJaExiste() {
        repository.save(new Cliente("Ana", "ana@email.com", null));

        assertThat(repository.existsByEmail("ana@email.com")).isTrue();
        assertThat(repository.existsByEmail("outro@email.com")).isFalse();
    }

    @Test
    void existsByEmailAndIdNotDeveIgnorarOProprioRegistro() {
        Cliente ana = repository.save(new Cliente("Ana", "ana@email.com", null));
        Cliente bia = repository.save(new Cliente("Bia", "bia@email.com", null));

        assertThat(repository.existsByEmailAndIdNot("ana@email.com", ana.getId())).isFalse();
        assertThat(repository.existsByEmailAndIdNot("ana@email.com", bia.getId())).isTrue();
    }
}
