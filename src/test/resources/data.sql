INSERT INTO pauta (nome,resultado,status)
VALUES('pauta1', 'Indefinido', 'Aberta');

INSERT INTO pauta (nome,resultado,status)
VALUES('pauta2', 'Aprovada', 'Encerrada');

INSERT INTO associado (email,nome,pauta_id,voto_id)
VALUES('joao@hotmail.com', 'joao', null, null);

INSERT INTO associado (email,nome,pauta_id,voto_id)
VALUES('maria@hotmail.com', 'maria', null, null);

INSERT INTO associado (email,nome,pauta_id,voto_id)
VALUES('pedro@hotmail.com', 'pedro', null, null);

INSERT INTO voto(associado_id,pauta_id,voto)
VALUES('1', '1', 'Não');



