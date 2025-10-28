package br.com.ifpe.oxefood.api.cidade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ifpe.oxefood.modelo.cidade.Cidade;
import br.com.ifpe.oxefood.modelo.cidade.CidadeService;

@RestController
@RequestMapping("/api/cidade")
@CrossOrigin


public class CidadeController {

    @Autowired
   private CidadeService cidadeService;

   @PostMapping
   public ResponseEntity<Cidade> save(@RequestBody CidadeRequest request) {

        Cidade cidade = cidadeService.save(request.build());
       return new ResponseEntity<Cidade>(cidade, HttpStatus.CREATED);
       
   }
   
   @GetMapping
    public List<Cidade> listarTodos() {
        return cidadeService.listarTodos();
    }
    @PutMapping("/{id}")
    public ResponseEntity<Cidade> update(@PathVariable("id") Long id, @RequestBody CidadeRequest request) {

       cidadeService.update(id, request.build());
       return ResponseEntity.ok().build();
 }

    
    @DeleteMapping("/{id}")
   public ResponseEntity<Void> delete(@PathVariable Long id) {

       cidadeService.delete(id);
       return ResponseEntity.ok().build();
   }

   @GetMapping("/{id}")
    public ResponseEntity<Cidade> getById(@PathVariable Long id) {
    Cidade cidade = cidadeService.obterPorID(id); 
    if (cidade != null) {
        return ResponseEntity.ok(cidade);
    } else {
        return ResponseEntity.notFound().build();
    }
}
 
}
