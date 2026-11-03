-- Massa para a grade e o painel não nascerem vazios.
-- Cada cadastro tem a própria inscrição. O número não se repete entre produtor, fazenda e laboratório.
-- Vista Alegre entra em dois produtores. Sítio Boa Esperança também. O count do relatório não pode juntar essa tabela.

insert into entidade_agricola (id, nome, inscricao_fiscal, endereco) values
    ('00000000-0000-7000-8000-000000000001', 'Laboratório Modelo 1', '11.222.333/0001-44', 'Av. Central, 100'),
    ('00000000-0000-7000-8000-000000000002', 'Laboratório Serra Azul', '22.333.444/0001-55', 'Rua da Serra, 200'),
    ('00000000-0000-7000-8000-000000000003', 'Laboratório Campo Seco', '33.444.555/0001-66', 'Estrada do Campo, 30');

insert into laboratorio (id, codigo, taxa_operacional) values
    ('00000000-0000-7000-8000-000000000001', 1, 250.00),
    ('00000000-0000-7000-8000-000000000002', 2, 180.00),
    ('00000000-0000-7000-8000-000000000003', 3, 90.00);

insert into entidade_agricola (id, nome, inscricao_fiscal, endereco) values
    ('00000000-0000-7000-8000-000000000011', 'Fazenda Sol Nascente', '12.345.678/0001-90', 'Rodovia BR 101, Km 45'),
    ('00000000-0000-7000-8000-000000000012', 'Fazenda Vista Alegre', '98.765.432/0001-10', 'Estrada Municipal, Km 12'),
    ('00000000-0000-7000-8000-000000000013', 'Sítio Boa Esperança', '45.678.901/0001-22', 'Vicinal do Sítio, Km 3');

insert into propriedade (id, codigo, taxa_territorial) values
    ('00000000-0000-7000-8000-000000000011', 1, 75.00),
    ('00000000-0000-7000-8000-000000000012', 2, 40.00),
    ('00000000-0000-7000-8000-000000000013', 3, 25.00);

insert into entidade_agricola (id, nome, inscricao_fiscal, endereco) values
    ('00000000-0000-7000-8000-000000000021', 'Manoel da Silva Ribeiro', '390.533.447-05', 'Rua das Acácias, 120'),
    ('00000000-0000-7000-8000-000000000022', 'Ana Pereira', '401.222.333-44', 'Rua das Flores, 15'),
    ('00000000-0000-7000-8000-000000000023', 'Carlos Mendes', '512.333.444-55', 'Av. Brasil, 800'),
    ('00000000-0000-7000-8000-000000000024', 'Lucia Ferreira', '623.444.555-66', 'Rua do Comércio, 40');

insert into produtor (id, codigo, laboratorio_id, data_inicial, data_final, observacoes, comissao_producao) values
    ('00000000-0000-7000-8000-000000000021', 1, '00000000-0000-7000-8000-000000000001', '2026-06-11T12:00:00Z', '2026-06-15T18:00:00Z', 'Iniciando testes de integração', 1500.00),
    ('00000000-0000-7000-8000-000000000022', 2, '00000000-0000-7000-8000-000000000002', '2026-03-01T08:00:00Z', '2026-03-20T18:00:00Z', null, 800.00),
    ('00000000-0000-7000-8000-000000000023', 3, '00000000-0000-7000-8000-000000000002', '2026-05-02T08:00:00Z', '2026-05-18T18:00:00Z', null, 600.00),
    ('00000000-0000-7000-8000-000000000024', 4, '00000000-0000-7000-8000-000000000003', '2026-04-10T08:00:00Z', '2026-04-12T18:00:00Z', null, 400.00);

insert into produtor_propriedade (produtor_id, propriedade_id) values
    ('00000000-0000-7000-8000-000000000021', '00000000-0000-7000-8000-000000000011'),
    ('00000000-0000-7000-8000-000000000021', '00000000-0000-7000-8000-000000000012'),
    ('00000000-0000-7000-8000-000000000022', '00000000-0000-7000-8000-000000000013'),
    ('00000000-0000-7000-8000-000000000023', '00000000-0000-7000-8000-000000000013'),
    ('00000000-0000-7000-8000-000000000024', '00000000-0000-7000-8000-000000000012');

select setval('laboratorio_codigo_seq', (select max(codigo) from laboratorio));
select setval('propriedade_codigo_seq', (select max(codigo) from propriedade));
select setval('produtor_codigo_seq', (select max(codigo) from produtor));
