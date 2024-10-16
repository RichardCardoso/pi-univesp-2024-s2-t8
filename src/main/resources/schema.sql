create database if not exists univesp_pi_2024_s2;

create table if not exists univesp_pi_2024_s2.cliente (
	id_cliente SERIAL PRIMARY KEY,
    nome varchar(100) NOT NULL,
    cpf varchar(14) unique NOT NULL,
    telefone varchar(15) NOT NULL,
    email varchar(100) NOT NULL,
    endereco varchar(200) NOT NULL,
    data_exclusao timestamp
);
