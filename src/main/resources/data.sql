
--Inclusão de um usuario administrador padrão

insert into medplanner.usuario (nome, cpf, username, password, situacao) values 
    ("Administrador do sistema", "11122233344", "admin", "$2a$10$OhNEEv7/DKkVbfolC5iWc.ZTBUh6Z2wuSvQsB8NmmyBO5kxbgA2y.", "A");