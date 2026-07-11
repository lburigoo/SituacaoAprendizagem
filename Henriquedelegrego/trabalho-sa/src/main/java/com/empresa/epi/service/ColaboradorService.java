package com.empresa.epi.service;

import com.empresa.epi.entity.Colaborador;
import com.empresa.epi.repository.ColaboradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Aqui fica a regra de negocio dos colaboradores
@Service
public class ColaboradorService {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    // lista todos os colaboradores que estao ativos
    public List<Colaborador> listarTodos() {
        return colaboradorRepository.findByAtivoTrue();
    }

    // lista tudo inclusive os inativos (caso precise)
    public List<Colaborador> listarTodosInclusiveInativos() {
        return colaboradorRepository.findAll();
    }

    // busca um colaborador pelo id
    public Optional<Colaborador> buscarPorId(Long id) {
        return colaboradorRepository.findById(id);
    }

    // busca por nome (qualquer parte do nome)
    public List<Colaborador> buscarPorNome(String nome) {
        return colaboradorRepository.findByNomeContainingIgnoreCase(nome);
    }

    // salva um novo colaborador
    public Colaborador salvar(Colaborador colaborador) {
        // verifica se ja existe CPF pra nao duplicar
        if (colaboradorRepository.existsByCpf(colaborador.getCpf())) {
            throw new RuntimeException("Já existe um colaborador com este CPF");
        }
        return colaboradorRepository.save(colaborador);
    }

    // atualiza os dados de um colaborador
    public Colaborador atualizar(Long id, Colaborador dadosAtualizados) {
        // procura o colaborador, se nao achar da erro
        Colaborador colaborador = colaboradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));

        // atualiza campo por campo
        colaborador.setNome(dadosAtualizados.getNome());
        colaborador.setCpf(dadosAtualizados.getCpf());
        colaborador.setCargo(dadosAtualizados.getCargo());
        colaborador.setSetor(dadosAtualizados.getSetor());
        colaborador.setTelefone(dadosAtualizados.getTelefone());
        colaborador.setAtivo(dadosAtualizados.getAtivo());

        return colaboradorRepository.save(colaborador);
    }

    // "exclui" o colaborador (na verdade so desativa)
    public void excluir(Long id) {
        Colaborador colaborador = colaboradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));
        // desativa em vez de deletar fisicamente
        colaborador.setAtivo(false);
        colaboradorRepository.save(colaborador);
    }
}
