package br.com.ifpe.oxefood.modelo.categoriaProduto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;


@Service
public class CategoriaProdutoService {
    @Autowired
    private CategoriaProdutoRepository repository;
        
        
    @Transactional
   public CategoriaProduto save(CategoriaProduto categoriaProduto) {

       categoriaProduto.setHabilitado(Boolean.TRUE);
       return repository.save(categoriaProduto);
   }
   public List<CategoriaProduto> listarTodos() {
  
        return repository.findAll();
    }

    public CategoriaProduto obterPorID(Long id) {

        return repository.findById(id).get();
    }
    

        @Transactional
        public void update(Long id, CategoriaProduto categoriaProdutoAlterado) {
        CategoriaProduto categoriaProduto = repository.findById(id).get();
        categoriaProduto.setNome(categoriaProdutoAlterado.getNome());
        repository.save(categoriaProduto);
    }
}
