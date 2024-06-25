
-- Inserção de especialidades médicas no banco de dados
INSERT INTO medplanner.especialidade (nome, sigla) VALUES 
    ("Cardiologia", "CD"),
    ("Neurologia", "NR"),
    ("Pediatria", "PD"),
    ("Dermatologia", "DT"),
    ("Ginecologia", "GN"),
    ("Ortopedia", "OR"),
    ("Psiquiatria", "PS"),
    ("Oftalmologia", "OF"),
    ("Endocrinologia", "EN"),
    ("Urologia", "UR");

-- Inclusão de um usuário administrador padrão
INSERT INTO medplanner.usuario (nome, cpf, username, password, cargo, situacao) VALUES 
    ("Administrador do sistema", "11122233344", "admin", "$2a$10$OhNEEv7/DKkVbfolC5iWc.ZTBUh6Z2wuSvQsB8NmmyBO5kxbgA2y.", 'ADMINISTRADOR', "A");

-- Inserção de usuários profissionais
INSERT INTO medplanner.usuario (nome, cpf, username, password, cargo, situacao) VALUES
    ("Dr. João Silva", "12345678901", "joao.silva", "$2a$10$OhNEEv7/DKkVbfolC5iWc.ZTBUh6Z2wuSvQsB8NmmyBO5kxbgA2y.", 'MEDICO', "A"),
    ("Dr. Maria Oliveira", "23456789012", "maria.oliveira", "$2a$10$OhNEEv7/DKkVbfolC5iWc.ZTBUh6Z2wuSvQsB8NmmyBO5kxbgA2y.", 'MEDICO', "A"),
    ("Dr. Carlos Pereira", "34567890123", "carlos.pereira", "$2a$10$OhNEEv7/DKkVbfolC5iWc.ZTBUh6Z2wuSvQsB8NmmyBO5kxbgA2y.", 'MEDICO', "A");

-- Inserção de dados na tabela profissional
-- Assumindo que os IDs de especialidade são conhecidos e correspondentes
INSERT INTO medplanner.profissional (id_usuario, num_Crm, uf_Crm, id_especialidade) VALUES
    (2, "12345", "SP", 1), -- João Silva, Cardiologia
    (3, "67890", "RJ", 2), -- Maria Oliveira, Neurologia
    (4, "54321", "MG", 6); -- Carlos Pereira, Ortopedia


-- Inserção de dados na tabela ala
INSERT INTO medplanner.ala (nome, sigla, andar) values
   ("Ala Norte", "AN", 1);


-- Inserção de dados na tabela sala
INSERT INTO medplanner.sala (nome_sala, situacao, id_ala) values
    ("Consultório médico 1", "A", 1);