package com.empresa.epi.controller;

import com.empresa.epi.entity.Colaborador;
import com.empresa.epi.service.ColaboradorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Controller do Colaborador - endpoints da API
@RestController
@RequestMapping("/api/colaboradores")
public class ColaboradorController {

    @Autowired
    private ColaboradorService colaboradorService;

    // GET - lista todos (so ativos)
    @GetMapping
    public List<Colaborador> listar() {
        return colaboradorService.listarTodos();
    }

    // GET - lista todos inclusive inativos
    @GetMapping("/todos")
    public List<Colaborador> listarTodos() {
        return colaboradorService.listarTodosInclusiveInativos();
    }

    // GET - busca por ID
    @GetMapping("/{id}")
    public ResponseEntity<Colaborador> buscarPorId(@PathVariable Long id) {
        Optional<Colaborador> colaborador = colaboradorService.buscarPorId(id);
        if (colaborador.isPresent()) {
            return ResponseEntity.ok(colaborador.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // GET - busca por nome
    @GetMapping("/buscar")
    public List<Colaborador> buscarPorNome(@RequestParam String nome) {
        return colaboradorService.buscarPorNome(nome);
    }

    // POST - cadastra novo
    @PostMapping
    public ResponseEntity<?> salvar(@Valid @RequestBody Colaborador colaborador) {
        try {
            Colaborador salvo = colaboradorService.salvar(colaborador);
            return ResponseEntity.ok(salvo);
        } catch (RuntimeException e) {
            // retorna erro amigavel
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    // PUT - atualiza
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody Colaborador dados) {
        try {
            Colaborador atualizado = colaboradorService.atualizar(id, dados);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    // DELETE - desativa
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            colaboradorService.excluir(id);
            Map<String, String> resposta = new HashMap<>();
            resposta.put("mensagem", "Colaborador desativado com sucesso");
            return ResponseEntity.ok(resposta);
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }
}
