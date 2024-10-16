package com.univesp.pi.pji240.g8.pi_2024_s2.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
public class Cliente {

    @Id
    @Column(name = "id_cliente")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome deve ser informado!")
    private String nome;

    @NotNull(message = "Cpf deve ser informado!")
    @Size(min=11, max=11, message = "Cpf deve conter 11 dígitos numéricos!")
    private String cpf;

    @NotNull(message = "Telefone deve ser informado!")
    @Size(min=10, max=11, message = "Telefone deve conter 10 ou 11 dígitos numéricos!")
    private String telefone;

    @NotNull(message = "E-mail deve ser informado!")
    @Email(message = "E-mail deve ser válido")
    private String email;

    @NotBlank(message = "Endereço deve ser informado!")
    private String endereco;

    @Column(name = "data_exclusao")
    private LocalDate dataExclusao;
}
