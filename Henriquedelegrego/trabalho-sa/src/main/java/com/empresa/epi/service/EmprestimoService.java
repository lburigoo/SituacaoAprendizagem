package com.empresa.epi.service;

import com.empresa.epi.entity.Emprestimo;
import com.empresa.epi.entity.Emprestimo.StatusEmprestimo;
import com.empresa.epi.repository.EmprestimoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Service responsavel pelos emprestimos
@Service
public class EmprestimoService {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private EpiService epiService; // pra controlar o estoque

    @Autowired
    private ColaboradorService colaboradorService; // pra verificar se colaborador existe

    // lista todos os emprestimos
    public List<Emprestimo> listarTodos() {
        return emprestimoRepository.findAll();
    }

    // busca um emprestimo pelo id
    public Optional<Emprestimo> buscarPorId(Long id) {
        return emprestimoRepository.findById(id);
    }

    // busca emprestimos de um colaborador especifico
    public List<Emprestimo> buscarPorColaborador(Long colaboradorId) {
        return emprestimoRepository.findByColaboradorId(colaboradorId);
    }

    // busca emprestimos de um EPI especifico
    public List<Emprestimo> buscarPorEpi(Long epiId) {
        return emprestimoRepository.findByEpiId(epiId);
    }

    // faz o emprestimo de um EPI pra um colaborador
    // transactional pra garantir que tudo execute junto
    @Transactional
    public Emprestimo realizarEmprestimo(Emprestimo emprestimo) {
        // verifica se o colaborador existe
        colaboradorService.buscarPorId(emprestimo.getColaborador().getId())
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));

        // diminui o estoque quando empresta
        epiService.diminuirEstoque(emprestimo.getEpi().getId(), 1);

        // seta a data de retirada como a data atual
        emprestimo.setDataRetirada(LocalDate.now());
        emprestimo.setStatus(StatusEmprestimo.ATIVO);

        return emprestimoRepository.save(emprestimo);
    }

    // registra a devolucao do EPI
    @Transactional
    public Emprestimo realizarDevolucao(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));

        // se ja foi devolvido, nao deixa devolver de novo
        if (emprestimo.getStatus() == StatusEmprestimo.DEVOLVIDO) {
            throw new RuntimeException("Este empréstimo já foi devolvido");
        }

        // aumenta o estoque de volta
        epiService.aumentarEstoque(emprestimo.getEpi().getId(), 1);

        // registra a data da devolucao
        emprestimo.setDataDevolucao(LocalDate.now());
        emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);

        return emprestimoRepository.save(emprestimo);
    }

    // exclui um emprestimo
    public void excluir(Long id) {
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));

        // se ainda ta ativo, precisa devolver o estoque antes
        if (emprestimo.getStatus() == StatusEmprestimo.ATIVO) {
            epiService.aumentarEstoque(emprestimo.getEpi().getId(), 1);
        }

        emprestimoRepository.delete(emprestimo);
    }

    // atualiza os dados do emprestimo
    public Emprestimo atualizar(Long id, Emprestimo dadosAtualizados) {
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));

        emprestimo.setColaborador(dadosAtualizados.getColaborador());
        emprestimo.setEpi(dadosAtualizados.getEpi());
        emprestimo.setDataRetirada(dadosAtualizados.getDataRetirada());
        emprestimo.setDataPrevistaDevolucao(dadosAtualizados.getDataPrevistaDevolucao());
        emprestimo.setDataDevolucao(dadosAtualizados.getDataDevolucao());
        emprestimo.setStatus(dadosAtualizados.getStatus());

        return emprestimoRepository.save(emprestimo);
    }
}
