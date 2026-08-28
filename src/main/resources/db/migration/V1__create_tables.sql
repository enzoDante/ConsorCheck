create table usuario(
    id bigint auto_increment primary key,
    nome varchar(100) not null,
    email varchar(100) not null unique,
    senha varchar(60) not null,
    documento varchar(14) not null unique, -- cpf/cnpj
    nome_dono varchar(100),
    data_criacao datetime default current_timestamp,
    role varchar(10) not null,
    -- role varchar(10) not null check (role in ('ADMIN','CLIENTE','EMPRESA')),
    ativo boolean default true
);

create table refresh_token(
    id bigint auto_increment primary key,
    id_usuario bigint not null,
    token varchar(255) not null,
    constraint uk_refresh_token unique (token),
    valido boolean default true,
    data_criacao datetime default current_timestamp,
    data_vencimento datetime not null,
    constraint fk_user_refresh foreign key (id_usuario) references usuario(id) on delete cascade
);

create table endereco(
    id_usuario bigint not null primary key,
    constraint fk_endereco_usuario foreign key (id_usuario) references usuario(id) on delete cascade,
    cep varchar(9) not null,
    rua varchar(100) not null,
    numero varchar(10) not null,
    bairro varchar(100) not null,
    cidade varchar(100) not null,
    uf varchar(2) not null,
    data_criacao datetime default current_timestamp,
    data_atualizacao datetime
);
create table dados_financeiros( -- dados financeiros do cliente
    id_usuario bigint primary key,
    constraint fk_dados_usuario foreign key (id_usuario) references usuario(id) on delete cascade,
    salario decimal(15,2) not null,
    data_criacao datetime default current_timestamp,
    data_atualizacao datetime
);
create table dados_consorcio( -- uma empresa pode ter vários consorcios
    id bigint auto_increment primary key,
    id_usuario bigint not null,
    constraint fk_dados_consorcio_usuario foreign key (id_usuario) references usuario(id) on delete cascade,
    nome varchar(100) not null,
    valor decimal(15, 2) not null,            -- valor total da carta de crédito
    numero_parcelas int not null,             -- prazo total do plano (ex: 180 meses)
    taxa_administracao decimal(5,2) not null, -- percentual sobre o valor da carta (ex: 18.50)
    fundo_reserva decimal(5,2) default 0,     -- percentual, opcional em muitos planos
    data_criacao datetime default current_timestamp,
    data_atualizacao datetime,
    data_inicio_consorcio datetime not null,
    data_fim_consorcio datetime not null,
    ativo boolean default true
);
/*
valor_parcela_base = valor / numero_parcelas
valor_parcela_com_taxa = valor_parcela_base * (1 + taxa_administracao/100)
percentual_fundo_formado = (soma_parcelas_pagas / valor) * 100
parcelas_restantes = numero_parcelas - count(parcela_paga do cliente)
*/
create table cliente_consorcio( -- cliente participa do consorcio
    id bigint auto_increment primary key,
    id_usuario bigint not null,
    id_consorcio bigint not null,
    constraint fk_cliente_cons_usuario foreign key (id_usuario) references usuario(id) on delete cascade,
    constraint fk_cliente_cons_cons foreign key (id_consorcio) references dados_consorcio(id) on delete cascade,
    constraint uk_cliente_consorcio unique (id_usuario, id_consorcio),
    data_criacao datetime default current_timestamp,
    data_atualizacao datetime,
    lance_esperado decimal(15, 2) not null,
    status varchar(20) not null default 'PENDENTE'
    -- status varchar(20) not null default 'PENDENTE' check (status in ('PENDENTE','APROVADO','REJEITADO','ATIVO','CANCELADO')),
);
create table parcela_paga(
    id bigint auto_increment primary key,
    id_cliente_consorcio bigint not null,
    numero_parcela int not null,       -- qual parcela é essa (1, 2, 3...)
    valor_pago decimal(15,2) not null,
    data_pagamento date not null,
    data_criacao datetime default current_timestamp,
    constraint fk_parcela_cliente_cons foreign key (id_cliente_consorcio) references cliente_consorcio(id) on delete cascade
);
create table lance_ofertado(
    id bigint auto_increment primary key,
    id_cliente_consorcio bigint not null,
    valor decimal(15,2) not null,
    data_oferta date not null,
    contemplado boolean default false, -- esse lance resultou em contemplação?
    constraint fk_lance_cliente_cons foreign key (id_cliente_consorcio) references cliente_consorcio(id) on delete cascade
);