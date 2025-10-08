package br.com.ifpe.oxefood.modelo.estado;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class EstadoService {
    @Autowired
    private EstadoRepository repository;

    @Transactional
   public Estado save(Estado estado) {

       estado.setHabilitado(Boolean.TRUE);
       return repository.save(estado);
   }
   public List<Estado> listarTodos() {
  
        return repository.findAll();
    }
    public Estado obterPorID(Long id) {

        return repository.findById(id).get();
    }
   @Transactional
   public void delete(Long id) {

       Estado estado = repository.findById(id).get();
       estado.setHabilitado(Boolean.FALSE);

       repository.save(estado);
   }

   @Transactional
   public void update(Long id, Estado estadoAlterado) {

      Estado estado = repository.findById(id).get();
      estado.setNome(estadoAlterado.getNome());
      estado.setSigla(estadoAlterado.getSigla());
	    
      repository.save(estado);
  }
}
