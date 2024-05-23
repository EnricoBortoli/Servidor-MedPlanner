
-- Inclusão de especialidades médicas no banco de dados
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

--Inclusão de um usuario administrador padrão

insert into medplanner.usuario (nome, cpf, username, password, situacao) values 
    ("Administrador do sistema", "11122233344", "admin", "$2a$10$OhNEEv7/DKkVbfolC5iWc.ZTBUh6Z2wuSvQsB8NmmyBO5kxbgA2y.", "A");