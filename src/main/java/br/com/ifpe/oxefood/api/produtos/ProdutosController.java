package br.com.ifpe.oxefood.api.produtos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ifpe.oxefood.modelo.categoriaProduto.CategoriaProdutoService;
import br.com.ifpe.oxefood.modelo.produtos.Produto;
import br.com.ifpe.oxefood.modelo.produtos.ProdutoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/produto")
@CrossOrigin


public class ProdutosController {

@Autowired
   private ProdutoService produtoService;
   
   @Autowired
   private CategoriaProdutoService categoriaProdutoService;


    @GetMapping
    public List<Produto> listarTodos() {
        return produtoService.listarTodos();
    }

   @PostMapping
   public ResponseEntity<Produto> save(@RequestBody @Valid ProdutoRequest request) {

       Produto produtoNovo = request.build();
       produtoNovo.setCategoria(categoriaProdutoService.obterPorID(request.getIdCategoria()));
       Produto produto = produtoService.save(produtoNovo);
       return new ResponseEntity<Produto>(produto, HttpStatus.CREATED);
   }

   @PutMapping("/{id}")
   public ResponseEntity<Produto> update(@PathVariable("id") Long id, @RequestBody ProdutoRequest request) {

       Produto produto = request.build();
       produto.setCategoria(categoriaProdutoService.obterPorID(request.getIdCategoria()));
       produtoService.update(id, produto);
      
       return ResponseEntity.ok().build();
   }


    
}
