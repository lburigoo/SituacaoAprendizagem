package com.empresa.epi.repository;

import com.empresa.epi.entity.Epi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EpiRepository extends JpaRepository<Epi, Long> {

    // busca EPI pelo nome
    List<Epi> findByNomeContainingIgnoreCase(String nome);

    // lista so os que estao ativos
    List<Epi> findByAtivoTrue();
}
