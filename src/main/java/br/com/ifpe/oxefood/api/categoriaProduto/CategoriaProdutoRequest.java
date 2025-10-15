package br.com.ifpe.oxefood.api.categoriaProduto;

import br.com.ifpe.oxefood.modelo.categoriaProduto.CategoriaProduto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CategoriaProdutoRequest {

    private String nome;

    public CategoriaProduto build()
    {
        return CategoriaProduto.builder()
        .nome(nome)
        .build();
    }
}
