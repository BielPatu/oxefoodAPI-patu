package br.com.ifpe.oxefood.modelo.estado;

import org.hibernate.annotations.SQLRestriction;

import br.com.ifpe.oxefood.modelo.cidade.Cidade;
import br.com.ifpe.oxefood.util.entity.EntidadeAuditavel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Estado")
@SQLRestriction("habilitado = true")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor



public class Estado extends EntidadeAuditavel {

    @Column
    private String nome;

    @Column String sigla;

    @PrimaryKeyJoinColumn
    @ManyToOne
    private Cidade cidade;


    
}
