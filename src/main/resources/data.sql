
-- Inserção de especialidades médicas no banco de dados
INSERT INTO medplanner.especialidade (nome, sigla) VALUES 
    ("Geral", "GR"),
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
    ("Bruno Silva", "12345678901", "bruno.silva@gmail.com", "$2a$10$OhNEEv7/DKkVbfolC5iWc.ZTBUh6Z2wuSvQsB8NmmyBO5kxbgA2y.", 'MEDICO', "A"),
    ("Maria Oliveira", "23456789012", "maria.oliveira@gmail.com", "$2a$10$OhNEEv7/DKkVbfolC5iWc.ZTBUh6Z2wuSvQsB8NmmyBO5kxbgA2y.", 'MEDICO', "A"),
    ("Carlos Pereira", "34567890123", "carlos.pereira@gmail.com", "$2a$10$OhNEEv7/DKkVbfolC5iWc.ZTBUh6Z2wuSvQsB8NmmyBO5kxbgA2y.", 'MEDICO', "A");

-- Inserção de dados na tabela profissional
-- Assumindo que os IDs de especialidade são conhecidos e correspondentes
INSERT INTO medplanner.profissional (id_usuario, num_Crm, uf_Crm, id_especialidade) VALUES
    (2, "12345", "SP", 1), -- Bruno Silva, Cardiologia
    (3, "67890", "RJ", 2), -- Maria Oliveira, Neurologia
    (4, "54321", "MG", 6); -- Carlos Pereira, Ortopedia


-- Inserção de dados na tabela ala
INSERT INTO medplanner.ala (nome, sigla, situacao) VALUES
    ("Triagem", "AT", "A");
INSERT INTO medplanner.ala (nome, sigla, situacao) VALUES
    ("Cardiologia", "AC", "A");
INSERT INTO medplanner.ala (nome, sigla, situacao) VALUES
    ("Pediatria", "AP", "A");
INSERT INTO medplanner.ala (nome, sigla, situacao) VALUES
    ("Ginecologia", "AG", "A");



-- Inserção de dados na tabela sala
INSERT INTO medplanner.sala (nome_sala, situacao, id_ala, andar) VALUES
    ("Consultório 1", "A", 1, 1),
    ("Consultório 2", "A", 1, 1),
    ("Consultório 3", "A", 2, 1),
    ("Consultório 4", "A", 2, 2),
    ("Consultório 5", "A", 3, 1),
    ("Consultório 6", "A", 3, 2),
    ("Consultório 7", "A", 4, 1),
    ("Consultório 8", "A", 4, 2);

-- Inserção de dados na tabela locacao
INSERT INTO medplanner.locacao (hora_inicio, dia, hora_final, id_usuario, id_sala, id_ala) VALUES
    ('2024-07-01 08:00:00', '2024-07-01', '2024-07-01 09:00:00', 2, 1, 1),
    ('2024-07-01 09:00:00', '2024-07-01', '2024-07-01 10:00:00', 3, 2, 1),
    ('2024-07-02 08:00:00', '2024-07-02', '2024-07-02 09:00:00', 4, 3, 2),
    ('2024-07-02 09:00:00', '2024-07-02', '2024-07-02 10:00:00', 2, 4, 2),
    ('2024-07-03 08:00:00', '2024-07-03', '2024-07-03 09:00:00', 3, 5, 3),
    ('2024-07-03 09:00:00', '2024-07-03', '2024-07-03 10:00:00', 4, 6, 3),
    ('2024-07-10 08:00:00', '2024-07-10', '2024-07-10 12:00:00', 2, 2, 2),
    ('2024-07-10 13:00:00', '2024-07-10', '2024-07-10 17:00:00', 2, 2, 2),
    ('2024-07-10 08:00:00', '2024-07-10', '2024-07-10 12:00:00', 3, 5, 3),
    ('2024-07-10 13:00:00', '2024-07-10', '2024-07-10 18:00:00', 3, 5, 3),
    ('2024-07-10 08:00:00', '2024-07-10', '2024-07-10 12:00:00', 4, 7, 4),
    ('2024-07-10 13:00:00', '2024-07-10', '2024-07-10 18:00:00', 4, 7, 4),
    ('2024-07-11 08:00:00', '2024-07-11', '2024-07-11 12:00:00', 2, 2, 2),
    ('2024-07-11 13:00:00', '2024-07-11', '2024-07-11 17:00:00', 2, 2, 2),
    ('2024-07-09 08:00:00', '2024-07-09', '2024-07-09 12:00:00', 3, 2, 2),
    ('2024-07-09 13:00:00', '2024-07-09', '2024-07-09 18:00:00', 3, 2, 2),
    ('2024-07-12 08:00:00', '2024-07-12', '2024-07-12 12:00:00', 4, 2, 2),
    ('2024-07-12 13:00:00', '2024-07-12', '2024-07-12 18:00:00', 4, 2, 2);

-- Inserção de dados na tabela recursos
INSERT INTO medplanner.recursos (id_sala, descricao, nome_recurso) VALUES
    (1, "Esfigmomanômetro", "Equipamento Médico"),
    (1, "Estetoscópio", "Equipamento Médico"),
    (2, "Estetoscópio", "Equipamento Médico"),
    (2, "Baçança", "Equipamento Médico"),
    (3, "Eletrocardiógrafo", "Equipamento Médico"),
    (3, "Monitor Holter", "Equipamento Médico"),
    (4, "Ecocardiograma", "Equipamento Médico"),
    (4, "Ergometria", "Equipamento Médico"),
    (5, "Estetoscópio Pediátrico", "Equipamento Médico"),
    (5, "Balança Pediátrica", "Equipamento Médico"),
    (6, "Monitor Multiparamétrico", "Equipamento Médico"),
    (6, "Nebulizador", "Equipamento Médico"),
    (7, "Maca Ginecológica", "Maca"),
    (7, "Ultrassom", "Equipamento Médico"),
    (8, "Colposcópio", "Maca"),
    (8, "Maca Ginecológica", "Maca");

