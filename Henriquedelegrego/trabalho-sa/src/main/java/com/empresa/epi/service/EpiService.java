package com.empresa.epi.service;

import com.empresa.epi.entity.Epi;
import com.empresa.epi.repository.EpiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EpiService {

    @Autowired
    private EpiRepository epiRepository;

    // lista todos os EPIs ativos
    public List<Epi> listarTodos() {
        return epiRepository.findByAtivoTrue();
    }

    // lista tudo (inclusive inativos)
    public List<Epi> listarTodosInclusiveInativos() {
        return epiRepository.findAll();
    }

    // busca EPI pelo ID
    public Optional<Epi> buscarPorId(Long id) {
        return epiRepository.findById(id);
    }

    // busca por nome
    public List<Epi> buscarPorNome(String nome) {
        return epiRepository.findByNomeContainingIgnoreCase(nome);
    }

    // salva um EPI novo
    public Epi salvar(Epi epi) {
        return epiRepository.save(epi);
    }

    // atualiza os dados
    public Epi atualizar(Long id, Epi dadosAtualizados) {
        Epi epi = epiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EPI não encontrado"));

        epi.setNome(dadosAtualizados.getNome());
        epi.setDescricao(dadosAtualizados.getDescricao());
        epi.setCertificadoCA(dadosAtualizados.getCertificadoCA());
        epi.setFabricante(dadosAtualizados.getFabricante());
        epi.setQuantidadeEstoque(dadosAtualizados.getQuantidadeEstoque());
        epi.setDataValidade(dadosAtualizados.getDataValidade());
        epi.setAtivo(dadosAtualizados.getAtivo());

        return epiRepository.save(epi);
    }

    // desativa o EPI (exclusao logica)
    public void excluir(Long id) {
        Epi epi = epiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EPI não encontrado"));
        epi.setAtivo(false);
        epiRepository.save(epi);
    }

    // diminui o estoque quando faz um emprestimo
    public void diminuirEstoque(Long epiId, int quantidade) {
        Epi epi = epiRepository.findById(epiId)
                .orElseThrow(() -> new RuntimeException("EPI não encontrado"));

        // verifica se tem estoque suficiente
        if (epi.getQuantidadeEstoque() < quantidade) {
            throw new RuntimeException("Estoque insuficiente");
        }

        epi.setQuantidadeEstoque(epi.getQuantidadeEstoque() - quantidade);
        epiRepository.save(epi);
    }

    // aumenta o estoque quando devolve o EPI
    public void aumentarEstoque(Long epiId, int quantidade) {
        Epi epi = epiRepository.findById(epiId)
                .orElseThrow(() -> new RuntimeException("EPI não encontrado"));
        epi.setQuantidadeEstoque(epi.getQuantidadeEstoque() + quantidade);
        epiRepository.save(epi);
    }
}
