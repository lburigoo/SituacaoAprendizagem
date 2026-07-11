-- ============================================
-- SISTEMA DE GERENCIAMENTO DE EPIs
-- Script para criar o banco de dados MySQL
-- ============================================

-- Passo 1: Criar o banco de dados
CREATE DATABASE IF NOT EXISTS trabalho_sa
DEFAULT CHARACTER SET utf8mb4
DEFAULT COLLATE utf8mb4_unicode_ci;

-- Passo 2: Selecionar o banco de dados
USE trabalho_sa;

-- Passo 3: Criar tabela de colaboradores
CREATE TABLE IF NOT EXISTS colaboradores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    cargo VARCHAR(100),
    setor VARCHAR(100),
    telefone VARCHAR(20),
    ativo BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Passo 4: Criar tabela de EPIs
CREATE TABLE IF NOT EXISTS epis (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    descricao TEXT,
    certificado_ca VARCHAR(50),
    fabricante VARCHAR(100),
    quantidade_estoque INT NOT NULL DEFAULT 0,
    data_validade DATE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Passo 5: Criar tabela de emprestimos
CREATE TABLE IF NOT EXISTS emprestimos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    colaborador_id BIGINT NOT NULL,
    epi_id BIGINT NOT NULL,
    data_retirada DATE NOT NULL,
    data_prevista_devolucao DATE NOT NULL,
    data_devolucao DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'ATIVO',
    CONSTRAINT fk_emprestimo_colaborador FOREIGN KEY (colaborador_id) REFERENCES colaboradores(id),
    CONSTRAINT fk_emprestimo_epi FOREIGN KEY (epi_id) REFERENCES epis(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Passo 6: Verificar se as tabelas foram criadas
SHOW TABLES;

-- Passo 7: Verificar a estrutura das tabelas
DESCRIBE colaboradores;
DESCRIBE epis;
DESCRIBE emprestimos;
