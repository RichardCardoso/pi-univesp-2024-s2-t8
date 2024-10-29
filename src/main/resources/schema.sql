create database if not exists univesp_pi_2024_s2;

create table if not exists univesp_pi_2024_s2.cliente (
	id_cliente int not null auto_increment PRIMARY KEY,
    nome varchar(100) NOT NULL,
    cpf varchar(14) unique NOT NULL,
    telefone varchar(15) NOT NULL,
    email varchar(100) NOT NULL,
    endereco varchar(200) NOT NULL,
    data_exclusao timestamp
);

CREATE TABLE IF NOT EXISTS univesp_pi_2024_s2.seguradora (
    id_seguradora INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cnpj VARCHAR(14) UNIQUE NOT NULL,
    telefone VARCHAR(15) NOT NULL,
    endereco VARCHAR(200) NOT NULL,
    data_exclusao TIMESTAMP
) ;

CREATE TABLE IF NOT EXISTS univesp_pi_2024_s2.servico (
    id_servico INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_seguradora INT NOT NULL,
    descricao VARCHAR(100) NOT NULL,
    preco DECIMAL(10, 2) NOT NULL,
    tipo_servico VARCHAR(50) NOT NULL,
    data_exclusao TIMESTAMP,
    FOREIGN KEY (id_seguradora) REFERENCES univesp_pi_2024_s2.seguradora(id_seguradora)
);

CREATE TABLE IF NOT EXISTS univesp_pi_2024_s2.servicoAdquirido (
    id_servico_adquirido INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_servico INT NOT NULL,
    id_cliente INT NOT NULL,
    data_aquisicao TIMESTAMP,
    valor_pago DECIMAL(10, 2) NOT NULL,
    data_exclusao TIMESTAMP,
    FOREIGN KEY (id_servico) REFERENCES univesp_pi_2024_s2.servico(id_servico),
    FOREIGN KEY (id_cliente) REFERENCES univesp_pi_2024_s2.cliente(id_cliente)
);

