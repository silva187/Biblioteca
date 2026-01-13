-- Tabela: aluno
CREATE TABLE aluno (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    matricula VARCHAR(20) UNIQUE NOT NULL,
    senha VARCHAR(100) -- hash da senha
);

-- Tabela: livro
CREATE TABLE livro (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    codigo VARCHAR(10) UNIQUE NOT NULL,
    disponivel BOOLEAN DEFAULT TRUE
);

-- Tabela: emprestimo
CREATE TABLE emprestimo (
    id SERIAL PRIMARY KEY,

    livro_id INT NOT NULL,
    aluno_id INT NOT NULL,

    data_emprestimo DATE NOT NULL,
    hora_emprestimo TIME NOT NULL,
    data_limite DATE NOT NULL,
    renovacoes INT DEFAULT 0,

    -- Chaves estrangeiras com nomes definidos
    CONSTRAINT fk_emprestimo_livro FOREIGN KEY (livro_id)
        REFERENCES livro(id) ON DELETE CASCADE,

    CONSTRAINT fk_emprestimo_aluno FOREIGN KEY (aluno_id)
        REFERENCES aluno(id) ON DELETE CASCADE,

    -- Regra de negócio: aluno só pode ter 1 empréstimo do mesmo livro ativo
    CONSTRAINT unq_aluno_livro_ativo UNIQUE (livro_id, aluno_id)
);
