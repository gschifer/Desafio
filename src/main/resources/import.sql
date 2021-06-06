insert into pautas(id, titulo, status, resultado, CREATED_AT) values(1, 'pauta 1', 'Aberta', 'Indefinida', NOW());
insert into pautas(id, titulo, status, resultado, CREATED_AT) values(2, 'pauta 2', 'Aberta', 'Indefinida', NOW());


insert into associados(id, nome, email, cpf, CREATED_AT) values(1, 'Gabriel', 'gabriel@hotmail.com', '03608898025', NOW());
insert into associados(id, nome, email, cpf, CREATED_AT) values(2, 'João', 'joao@hotmail.com', '92409183525', NOW());
insert into associados(id, nome, email, cpf, CREATED_AT) values(3, 'José', 'jose@hotmail.com', '72067294028', NOW());

insert into votos(id, descricao_voto, associado_id, pauta_id) values(1, 'Sim', 1, 1);
insert into votos(id, descricao_voto, associado_id, pauta_id) values(2, 'Não', 2, 1);
