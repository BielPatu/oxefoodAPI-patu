package br.com.ifpe.oxefood.modelo.categoriaproduto;

import org.springframework.beans.factory.annotation.Autowired;

public class CategoriaProdutoService {
    @Autowired CategoriaRepository repository;


    public CategoriaProduto obterPorID(Long id)
    {
        return repository.findById(id).get();
    }
    
}
