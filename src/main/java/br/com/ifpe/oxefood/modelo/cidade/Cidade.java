package br.com.ifpe.oxefood.modelo.cidade;

import java.time.LocalDate;

import org.hibernate.annotations.SQLRestriction;

import br.com.ifpe.oxefood.modelo.estado.Estado;
import br.com.ifpe.oxefood.util.entity.EntidadeAuditavel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "Cidade")
@SQLRestriction("habilitado = true")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Cidade extends EntidadeAuditavel{

    @Column
    private String nome;

    @OneToOne
    private Estado estado;

    @Column
    private String estadoNome;

    @Column
    private int qntPopulacao;

    @Column
    private boolean ehCapital;

    @Column 
    private LocalDate dataFundacao;
    
    
}
