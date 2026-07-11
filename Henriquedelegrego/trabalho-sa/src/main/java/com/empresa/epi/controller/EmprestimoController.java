package com.empresa.epi.controller;

import com.empresa.epi.entity.Emprestimo;
import com.empresa.epi.service.EmprestimoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {

    @Autowired
    private EmprestimoService emprestimoService;

    // GET - listar todos
    @GetMapping
    public List<Emprestimo> listar() {
        return emprestimoService.listarTodos();
    }

    // GET - buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Emprestimo> buscarPorId(@PathVariable Long id) {
        Optional<Emprestimo> emp = emprestimoService.buscarPorId(id);
        if (emp.isPresent()) {
            return ResponseEntity.ok(emp.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // GET - buscar por colaborador
    @GetMapping("/colaborador/{colaboradorId}")
    public List<Emprestimo> buscarPorColaborador(@PathVariable Long colaboradorId) {
        return emprestimoService.buscarPorColaborador(colaboradorId);
    }

    // GET - buscar por EPI
    @GetMapping("/epi/{epiId}")
    public List<Emprestimo> buscarPorEpi(@PathVariable Long epiId) {
        return emprestimoService.buscarPorEpi(epiId);
    }

    // POST - realizar novo emprestimo
    @PostMapping
    public ResponseEntity<?> realizarEmprestimo(@Valid @RequestBody Emprestimo emprestimo) {
        try {
            Emprestimo salvo = emprestimoService.realizarEmprestimo(emprestimo);
            return ResponseEntity.ok(salvo);
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    // PUT - devolver EPI
    @PutMapping("/{id}/devolver")
    public ResponseEntity<?> devolver(@PathVariable Long id) {
        try {
            Emprestimo emprestimo = emprestimoService.realizarDevolucao(id);
            return ResponseEntity.ok(emprestimo);
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    // PUT - atualizar emprestimo
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody Emprestimo dados) {
        try {
            Emprestimo atualizado = emprestimoService.atualizar(id, dados);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    // DELETE - remover emprestimo
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            emprestimoService.excluir(id);
            Map<String, String> resposta = new HashMap<>();
            resposta.put("mensagem", "Empréstimo removido com sucesso");
            return ResponseEntity.ok(resposta);
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }
}
