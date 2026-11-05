package br.com.sistemas.chamados.dto;

import java.time.LocalDateTime;
import java.util.List;

import br.com.sistemas.chamados.entity.Prioridade;
import br.com.sistemas.chamados.entity.StatusChamado;

public record ChamadoDetalheResponse(
    Long id,
    String titulo,
    String descricao,
    LocalDateTime dataAbertura,
    LocalDateTime dataFechamento,
    Prioridade prioridade,
    StatusChamado status,
    Long clienteId,
    Long categoriaId,
    Long tecnicoId,
    List<ComentarioResponse> comentarios
) {
    public static ChamadoDetalheResponse de(ChamadoResponse c, List<ComentarioResponse> comentarios) {
        return new ChamadoDetalheResponse(
            c.id(), c.titulo(), c.descricao(),
            c.dataAbertura(), c.dataFechamento(),
            c.prioridade(), c.status(),
            c.clienteId(), c.categoriaId(), c.tecnicoId(),
            comentarios);
    }
}
