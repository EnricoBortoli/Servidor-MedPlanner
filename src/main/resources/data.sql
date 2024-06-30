
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
    ("Administrador do sistema", "11122233344", "admin@gmail.com", "$2a$10$OhNEEv7/DKkVbfolC5iWc.ZTBUh6Z2wuSvQsB8NmmyBO5kxbgA2y.", 'ADMINISTRADOR', "A");

-- Inserção de usuários profissionais
INSERT INTO medplanner.usuario (nome, cpf, username, password, cargo, situacao) VALUES
    ("Dr. Bruno Silva", "12345678901", "bruno.silva@gmail.com", "$2a$10$OhNEEv7/DKkVbfolC5iWc.ZTBUh6Z2wuSvQsB8NmmyBO5kxbgA2y.", 'MEDICO', "A"),
    ("Dr. Maria Oliveira", "23456789012", "maria.oliveira@gmail.com", "$2a$10$OhNEEv7/DKkVbfolC5iWc.ZTBUh6Z2wuSvQsB8NmmyBO5kxbgA2y.", 'MEDICO', "A"),
    ("Dr. Carlos Pereira", "34567890123", "carlos.pereira@gmail.com", "$2a$10$OhNEEv7/DKkVbfolC5iWc.ZTBUh6Z2wuSvQsB8NmmyBO5kxbgA2y.", 'MEDICO', "A");

-- Inserção de dados na tabela profissional
-- Assumindo que os IDs de especialidade são conhecidos e correspondentes
INSERT INTO medplanner.profissional (id_usuario, num_Crm, uf_Crm, id_especialidade) VALUES
    (2, "12345", "SP", 1), -- João Silva, Cardiologia
    (3, "67890", "RJ", 2), -- Maria Oliveira, Neurologia
    (4, "54321", "MG", 6); -- Carlos Pereira, Ortopedia


-- Inserção de dados na tabela ala
INSERT INTO medplanner.ala (nome, sigla) VALUES
   ("Ala Norte", "AN");
INSERT INTO medplanner.ala (nome, sigla) VALUES
    ("Ala Sul", "AS");
INSERT INTO medplanner.ala (nome, sigla) VALUES
    ("Ala Leste", "AL");
INSERT INTO medplanner.ala (nome, sigla) VALUES
    ("Ala Oeste", "AO");

-- Inserção de dados na tabela sala
INSERT INTO medplanner.sala (nome_sala, situacao, id_ala, andar) values
    ("Sala de atendimento 1", "A", 1, 1);

INSERT INTO medplanner.locacao (hora_inicio, dia, hora_final, id_usuario, id_sala, id_ala) VALUES
    ('2024-07-01 08:00:00', '2024-07-01', '2024-07-01 09:00:00', 2, 1, 1);

    -- Inserção de dados na tabela recursos
INSERT INTO medplanner.recursos (id_sala, descricao, nome_recurso) VALUES
    (1, "Maca", "Maca de aço inoxidavel tamanho grande"),
    (1, "Esfigmomanômetro", "Equipamento Médico");
