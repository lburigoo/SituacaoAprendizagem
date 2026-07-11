package com.empresa.epi.controller;

import com.empresa.epi.entity.Epi;
import com.empresa.epi.service.EpiService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/epis")
public class EpiController {

    @Autowired
    private EpiService epiService;

    // GET - listar todos os EPIs ativos
    @GetMapping
    public List<Epi> listar() {
        return epiService.listarTodos();
    }

    // GET - listar todos (inclusive inativos)
    @GetMapping("/todos")
    public List<Epi> listarTodos() {
        return epiService.listarTodosInclusiveInativos();
    }

    // GET - buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Epi> buscarPorId(@PathVariable Long id) {
        Optional<Epi> epi = epiService.buscarPorId(id);
        if (epi.isPresent()) {
            return ResponseEntity.ok(epi.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // GET - buscar por nome
    @GetMapping("/buscar")
    public List<Epi> buscarPorNome(@RequestParam String nome) {
        return epiService.buscarPorNome(nome);
    }

    // POST - cadastrar novo EPI
    @PostMapping
    public ResponseEntity<?> salvar(@Valid @RequestBody Epi epi) {
        try {
            Epi salvo = epiService.salvar(epi);
            return ResponseEntity.ok(salvo);
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    // PUT - atualizar EPI
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody Epi dados) {
        try {
            Epi atualizado = epiService.atualizar(id, dados);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    // DELETE - desativar EPI
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            epiService.excluir(id);
            Map<String, String> resposta = new HashMap<>();
            resposta.put("mensagem", "EPI desativado com sucesso");
            return ResponseEntity.ok(resposta);
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }
}
