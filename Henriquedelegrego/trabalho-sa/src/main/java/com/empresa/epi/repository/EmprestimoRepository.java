package com.empresa.epi.repository;

import com.empresa.epi.entity.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    // busca emprestimos pelo id do colaborador
    List<Emprestimo> findByColaboradorId(Long colaboradorId);

    // busca emprestimos pelo id do epi
    List<Emprestimo> findByEpiId(Long epiId);

    // filtra por status (ATIVO, DEVOLVIDO, ATRASADO)
    List<Emprestimo> findByStatus(Emprestimo.StatusEmprestimo status);

    // busca por nome do colaborador ou nome do EPI
    @Query("SELECT e FROM Emprestimo e WHERE " +
           "LOWER(e.colaborador.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(e.epi.nome) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<Emprestimo> buscarPorTermo(@Param("termo") String termo);
}
