package com.univesp.pi.pji240.g8.pi_2024_s2.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicoAdquirido {

    @Id
    @Column(name = "id_servico_adquirido")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull(message = "O cliente deve ser informada!")
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne
    @NotNull(message = "O serviço deve ser informada!")
    @JoinColumn(name = "id_servico")
    private Servico servico;

    @NotNull(message = "A data de aquisição deve ser informada!")
    @Column(name = "data_aquisicao")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataAquisicao;

    @NotNull(message = "O valor pago deve ser informado!")
    @DecimalMin(value = "0.01", message = "O valor pago deve ser maior ou igual a 0.01!")
    @Column(name = "valor_pago")
    private double valorPago;

    @Column(name = "data_exclusao")
    private LocalDate dataExclusao;
}
