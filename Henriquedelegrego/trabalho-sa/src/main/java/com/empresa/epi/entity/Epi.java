package com.empresa.epi.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "epis")
public class Epi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false, length = 150)
    private String nome;

    @Column(columnDefinition = "TEXT") // texto longo pra descricao
    private String descricao;

    @Column(name = "certificado_ca", length = 50) // mudei o nome da coluna pra ter underscore
    private String certificadoCA;

    @Column(length = 100)
    private String fabricante;

    @Min(value = 0, message = "Quantidade não pode ser negativa") // nao pode ter estoque negativo
    @Column(nullable = false)
    private Integer quantidadeEstoque = 0;

    @Column(name = "data_validade")
    private LocalDate dataValidade;

    @Column(nullable = false)
    private Boolean ativo = true;

    // construtor padrao
    public Epi() {}

    // getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getCertificadoCA() { return certificadoCA; }
    public void setCertificadoCA(String certificadoCA) { this.certificadoCA = certificadoCA; }

    public String getFabricante() { return fabricante; }
    public void setFabricante(String fabricante) { this.fabricante = fabricante; }

    public Integer getQuantidadeEstoque() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(Integer quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }

    public LocalDate getDataValidade() { return dataValidade; }
    public void setDataValidade(LocalDate dataValidade) { this.dataValidade = dataValidade; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}
