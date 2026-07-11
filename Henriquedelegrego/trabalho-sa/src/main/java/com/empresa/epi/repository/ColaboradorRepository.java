package com.empresa.epi.repository;

import com.empresa.epi.entity.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repository do Colaborador - faz as operacoes no banco
@Repository
public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {

    // busca por nome (ignorando maiusculo/minusculo)
    List<Colaborador> findByNomeContainingIgnoreCase(String nome);

    // pega so os ativos
    List<Colaborador> findByAtivoTrue();

    // verifica se ja tem um CPF cadastrado
    boolean existsByCpf(String cpf);
}
