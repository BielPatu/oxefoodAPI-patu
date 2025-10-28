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

        Estado estado = estadoRepository.findById(cidade.getEstado().getId())
                .orElseThrow(() -> new RuntimeException("Estado não encontrado"));

        cidade.setEstado(estado);
        cidade.setEstadoNome(estado.getNome());

        return repository.save(cidade);
    }

    public List<Cidade> listarTodos() {
        return repository.findAll();
    }

    public Cidade obterPorID(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cidade não encontrada"));
    }

    @Transactional
    public void delete(Long id) {
        Cidade cidade = obterPorID(id);
        cidade.setHabilitado(Boolean.FALSE);
        repository.save(cidade);
    }

    @Transactional
    public void update(Long id, Cidade cidadeAlterada) {
        Cidade cidade = obterPorID(id);

        cidade.setNome(cidadeAlterada.getNome());
        cidade.setDataFundacao(cidadeAlterada.getDataFundacao());
        cidade.setQntPopulacao(cidadeAlterada.getQntPopulacao());
        cidade.setEhCapital(cidadeAlterada.isEhCapital());
        cidade.setEstadoNome(cidadeAlterada.getEstadoNome());

        if (cidadeAlterada.getEstado() != null && cidadeAlterada.getEstado().getId() != null) {
            Estado estado = estadoRepository.findById(cidadeAlterada.getEstado().getId())
                    .orElseThrow(() -> new RuntimeException("Estado não encontrado"));
            cidade.setEstado(estado);
        }

        repository.save(cidade);
    }
}
