package com.univesp.pi.pji240.g8.pi_2024_s2.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class Servico {

    @Id
    @Column(name = "id_servico")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Descrição deve ser informada!")
    private String descricao;

    @NotNull(message = "Preço deve ser informado!")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior ou igual a 0.01!")
    private double preco;

    @NotBlank(message = "Tipo de serviço deve ser informado!")
    @Column(name = "tipo_servico")
    private String tipoServico;

    @ManyToOne
    @NotNull(message = "A seguradora deve ser informada!")
    @JoinColumn(name = "id_seguradora")
    private Seguradora seguradora;

    @Column(name = "data_exclusao")
    private LocalDate dataExclusao;
}
