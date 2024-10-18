package com.univesp.pi.pji240.g8.pi_2024_s2.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seguradora {

    @Id
    @Column(name = "id_seguradora")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome deve ser informado!")
    private String nome;

    @NotNull(message = "Cnpj deve ser informado!")
    @Size(min=14, max=14, message = "Cnpj deve conter 14 dígitos numéricos!")
    private String cnpj;

    @NotNull(message = "Telefone deve ser informado!")
    @Size(min=10, max=11, message = "Telefone deve conter 10 ou 11 dígitos numéricos!")
    private String telefone;

    @NotBlank(message = "Endereço deve ser informado!")
    private String endereco;

    @Column(name = "data_exclusao")
    private LocalDate dataExclusao;
}
