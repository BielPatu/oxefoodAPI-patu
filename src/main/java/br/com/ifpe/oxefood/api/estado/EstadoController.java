package br.com.ifpe.oxefood.api.estado;

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

import br.com.ifpe.oxefood.modelo.cliente.Cliente;
import br.com.ifpe.oxefood.modelo.estado.Estado;
import br.com.ifpe.oxefood.modelo.estado.EstadoService;

@RestController
@RequestMapping("/api/estado")
@CrossOrigin

public class EstadoController {
    @Autowired
   private EstadoService estadoService;

   @PostMapping
   public ResponseEntity<Estado> save(@RequestBody EstadoRequest request) {

       Estado estado = estadoService.save(request.build());
       return new ResponseEntity<Estado>(estado, HttpStatus.CREATED);
       
   }
   @GetMapping
    public List<Estado> listarTodos() {
        return estadoService.listarTodos();
    }
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> update(@PathVariable("id") Long id, @RequestBody EstadoRequest request) {

       estadoService.update(id, request.build());
       return ResponseEntity.ok().build();
    }
   
    @DeleteMapping("/{id}")
   public ResponseEntity<Void> delete(@PathVariable Long id) {

       estadoService.delete(id);
       return ResponseEntity.ok().build();
   }
}
