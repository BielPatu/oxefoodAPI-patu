package br.com.ifpe.oxefood.modelo.cidade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifpe.oxefood.modelo.estado.Estado;
import br.com.ifpe.oxefood.modelo.estado.EstadoRepository;
import jakarta.transaction.Transactional;


@Service
public class CidadeService {
    @Autowired
   private CidadeRepository repository;
    @Autowired
    private EstadoRepository estadoRepository;



   @Transactional
   public Cidade save(Cidade cidade) {

       cidade.setHabilitado(Boolean.TRUE);
       Estado estado = estadoRepository.save(cidade.getEstado());
       cidade.setEstadoNome(estado.getNome());
       return repository.save(cidade);
   }
   public List<Cidade> listarTodos() {
  
        return repository.findAll();
    }

    public Cidade obterPorID(Long id) {

        return repository.findById(id).get();
    }

    @Transactional
    public void delete(Long id)
    {
        Cidade cidade = repository.findById(id).get();
       cidade.setHabilitado(Boolean.FALSE);

       repository.save(cidade);
    }

    public void update(Long id, Cidade cidadeAlterada)
    {
        Cidade cidade = repository.findById(id).get();
        cidade.setNome(cidadeAlterada.getNome());
        cidade.setEstado(cidadeAlterada.getEstado());
        cidade.setDataFundacao(cidadeAlterada.getDataFundacao());
        cidade.setQntPopulacao(cidadeAlterada.getQntPopulacao());
        cidade.setEhCapital(cidadeAlterada.isEhCapital());
        cidade.setEstadoNome(cidadeAlterada.getEstadoNome());

        repository.save(cidade);
    }
}
