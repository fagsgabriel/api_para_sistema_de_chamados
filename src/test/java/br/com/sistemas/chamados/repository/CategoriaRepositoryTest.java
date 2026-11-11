package br.com.sistemas.chamados.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import br.com.sistemas.chamados.entity.Categoria;

@DataJpaTest
class CategoriaRepositoryTest {

    @Autowired
    private CategoriaRepository repository;

    @Test
    void existsByNomeDeveRetornarTrueQuandoNomeJaExiste() {
        repository.save(new Categoria("Rede", null));

        assertThat(repository.existsByNome("Rede")).isTrue();
        assertThat(repository.existsByNome("Impressora")).isFalse();
    }

    @Test
    void existsByNomeAndIdNotDeveIgnorarOProprioRegistro() {
        Categoria rede = repository.save(new Categoria("Rede", null));
        Categoria software = repository.save(new Categoria("Software", null));

        assertThat(repository.existsByNomeAndIdNot("Rede", rede.getId())).isFalse();
        assertThat(repository.existsByNomeAndIdNot("Rede", software.getId())).isTrue();
    }
}
