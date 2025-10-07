package br.com.ifpe.oxefood.modelo.cliente;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class ClienteService {

   @Autowired
   private ClienteRepository repository;

   @Transactional
   public Cliente save(Cliente cliente) {

       cliente.setHabilitado(Boolean.TRUE);
       return repository.save(cliente);
   }
   public List<Cliente> listarTodos() {
  
        return repository.findAll();
    }

    public Cliente obterPorID(Long id) {

        return repository.findById(id).get();
    }

    @Transactional
   public void update(Long id, Cliente clienteAlterado) {

      Cliente cliente = repository.findById(id).get();
      if(clienteAlterado.getNome() != null)
      {
      cliente.setNome(clienteAlterado.getNome());
        }
      if(clienteAlterado.getDataNascimento() != null){
      cliente.setDataNascimento(clienteAlterado.getDataNascimento());
        }
        if(clienteAlterado.getCpf() != null){ 
      cliente.setCpf(clienteAlterado.getCpf());
    }
        if(clienteAlterado.getFoneCelular() != null){
      cliente.setFoneCelular(clienteAlterado.getFoneCelular());
    }
    if(clienteAlterado.getFoneFixo() != null){
      cliente.setFoneFixo(clienteAlterado.getFoneFixo());
    }
	    
      repository.save(cliente);
  }

    @Transactional
   public void delete(Long id) {

       Cliente cliente = repository.findById(id).get();
       cliente.setHabilitado(Boolean.FALSE);

       repository.save(cliente);
   }


}
