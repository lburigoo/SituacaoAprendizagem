package com.empresa.epi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "emprestimos")
public class Emprestimo {

    // enum pro status do emprestimo
    public enum StatusEmprestimo {
        ATIVO, DEVOLVIDO, ATRASADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // relacionamento muitos pra um com colaborador
    @ManyToOne(fetch = FetchType.EAGER) // eager pq sempre vamos precisar do colaborador
    @JoinColumn(name = "colaborador_id", nullable = false)
    private Colaborador colaborador;

    // relacionamento muitos pra um com epi
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "epi_id", nullable = false)
    private Epi epi;

    @Column(name = "data_retirada", nullable = false)
    private LocalDate dataRetirada;

    @NotNull(message = "Data prevista de devolução é obrigatória")
    @Column(name = "data_prevista_devolucao", nullable = false)
    private LocalDate dataPrevistaDevolucao;

    @Column(name = "data_devolucao") // pode ser nulo ate devolver
    private LocalDate dataDevolucao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusEmprestimo status = StatusEmprestimo.ATIVO; // começa como ativo

    public Emprestimo() {}

    // getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Colaborador getColaborador() { return colaborador; }
    public void setColaborador(Colaborador colaborador) { this.colaborador = colaborador; }

    public Epi getEpi() { return epi; }
    public void setEpi(Epi epi) { this.epi = epi; }

    public LocalDate getDataRetirada() { return dataRetirada; }
    public void setDataRetirada(LocalDate dataRetirada) { this.dataRetirada = dataRetirada; }

    public LocalDate getDataPrevistaDevolucao() { return dataPrevistaDevolucao; }
    public void setDataPrevistaDevolucao(LocalDate dataPrevistaDevolucao) { this.dataPrevistaDevolucao = dataPrevistaDevolucao; }

    public LocalDate getDataDevolucao() { return dataDevolucao; }
    public void setDataDevolucao(LocalDate dataDevolucao) { this.dataDevolucao = dataDevolucao; }

    public StatusEmprestimo getStatus() { return status; }
    public void setStatus(StatusEmprestimo status) { this.status = status; }
}
