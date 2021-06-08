insert into pautas(titulo, status, resultado, CREATED_AT) values('pauta 1', 'Aberta', 'Indefinida', NOW());
insert into pautas(titulo, status, resultado, CREATED_AT) values('pauta 2', 'Aberta', 'Indefinida', NOW());


insert into associados(nome, email, cpf, CREATED_AT) values('Gabriel', 'gabriel@hotmail.com', '03608898025', NOW());
insert into associados(nome, email, cpf, CREATED_AT) values('João', 'joao@hotmail.com', '92409183525', NOW());
insert into associados(nome, email, cpf, CREATED_AT) values('José', 'jose@hotmail.com', '72067294028', NOW());


insert into votos(descricao_voto, associado_id, pauta_id) values('Sim', 1, 1);
insert into votos(descricao_voto, associado_id, pauta_id) values('Não', 2, 1);


insert into role(nome) values ('ROLE_USER');
insert into role(nome) values ('ROLE_ADMIN');

insert into user(nome,email,login,senha) values ('Admin','admin@gmail.com','admin','$2a$10$HKveMsPlst41Ie2LQgpijO691lUtZ8cLfcliAO1DD9TtZxEpaEoJe');
insert into user(nome,email,login,senha) values ('User','user@gmail.com','user','$2a$10$HKveMsPlst41Ie2LQgpijO691lUtZ8cLfcliAO1DD9TtZxEpaEoJe');


insert into user_roles(user_id,role_id) values(1, 1);
insert into user_roles(user_id,role_id) values(1, 2);
insert into user_roles(user_id,role_id) values(2, 1);

