-- Atributos comuns no pai. Cada filha compartilha o mesmo id (heranca JOINED).
-- codigo e o numero exibido; a URL da API usa o uuid.

create table entidade_agricola (
    id uuid primary key,
    nome varchar(150) not null,
    inscricao_fiscal varchar(32) not null,
    endereco varchar(255) not null,
    constraint uk_entidade_inscricao_fiscal unique (inscricao_fiscal)
);

create sequence laboratorio_codigo_seq start with 1 increment by 1;
create sequence propriedade_codigo_seq start with 1 increment by 1;
create sequence produtor_codigo_seq start with 1 increment by 1;

create table laboratorio (
    id uuid primary key references entidade_agricola (id) on delete cascade,
    codigo bigint not null,
    taxa_operacional numeric(15, 2) not null,
    constraint uk_laboratorio_codigo unique (codigo),
    constraint ck_laboratorio_taxa check (taxa_operacional >= 0)
);

create table propriedade (
    id uuid primary key references entidade_agricola (id) on delete cascade,
    codigo bigint not null,
    taxa_territorial numeric(15, 2) not null,
    constraint uk_propriedade_codigo unique (codigo),
    constraint ck_propriedade_taxa check (taxa_territorial >= 0)
);

create table produtor (
    id uuid primary key references entidade_agricola (id) on delete cascade,
    codigo bigint not null,
    laboratorio_id uuid not null references laboratorio (id) on delete restrict,
    data_inicial timestamptz not null,
    data_final timestamptz not null,
    observacoes text,
    comissao_producao numeric(15, 2) not null,
    constraint uk_produtor_codigo unique (codigo),
    constraint ck_produtor_periodo check (data_final >= data_inicial),
    constraint ck_produtor_comissao check (comissao_producao >= 0)
);

create index ix_produtor_laboratorio on produtor (laboratorio_id);
create index ix_produtor_data_inicial on produtor (data_inicial);

create table produtor_propriedade (
    produtor_id uuid not null references produtor (id) on delete cascade,
    propriedade_id uuid not null references propriedade (id) on delete restrict,
    primary key (produtor_id, propriedade_id)
);
