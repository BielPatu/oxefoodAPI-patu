package br.com.ifpe.oxefood.api.cidade;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.ifpe.oxefood.modelo.cidade.Cidade;
import br.com.ifpe.oxefood.modelo.estado.Estado;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor



public class CidadeRequest {

    private String nome;

    private Estado estado;

    private String estadoNome;

    private int qntPopulacao;

    private boolean ehCapital;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataFundacao;


    public Cidade build()
    {
        return Cidade.builder()
        .nome(nome)
        .estado(estado)
        .estadoNome(estadoNome)
        .qntPopulacao(qntPopulacao)
        .ehCapital(ehCapital)
        .dataFundacao(dataFundacao)
        .build();
    }
    
}
