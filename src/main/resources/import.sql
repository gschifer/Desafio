insert into pautas(id, titulo, status, resultado, CREATED_AT) values(1, 'pauta 1', 'Aberta', 'Indefinido', NOW());
insert into pautas(id, titulo, status, resultado, CREATED_AT) values(2, 'pauta 2', 'Aberta', 'Indefinido', NOW());


insert into associados(id, nome, email, cpf, CREATED_AT) values(1, 'Gabriel', 'gabriel@hotmail.com', '03608898025', NOW());
insert into associados(id, nome, email, cpf, CREATED_AT) values(2, 'João', 'joao@hotmail.com', '92409183525', NOW());
insert into associados(id, nome, email, cpf, CREATED_AT) values(3, 'José', 'jose@hotmail.com', '72067294028', NOW());

insert into votos(id, descricao_voto, associado_id, pauta_id) values(1, 'Sim', 1, 1);
insert into votos(id, descricao_voto, associado_id, pauta_id) values(2, 'Não', 2, 1);


insert into role(id,nome) values (1, 'ROLE_USER');
insert into role(id,nome) values (2, 'ROLE_ADMIN');

insert into user(id,nome,email,login,senha) values (1,'Admin','admin@gmail.com','admin','$2a$10$HKveMsPlst41Ie2LQgpijO691lUtZ8cLfcliAO1DD9TtZxEpaEoJe');
insert into user(id,nome,email,login,senha) values (2,'User','user@gmail.com','user','$2a$10$HKveMsPlst41Ie2LQgpijO691lUtZ8cLfcliAO1DD9TtZxEpaEoJe');


insert into user_roles(user_id,role_id) values(1, 1);
insert into user_roles(user_id,role_id) values(1, 2);
insert into user_roles(user_id,role_id) values(2, 1);
