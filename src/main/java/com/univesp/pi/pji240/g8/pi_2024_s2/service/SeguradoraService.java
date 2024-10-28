package com.univesp.pi.pji240.g8.pi_2024_s2.service;

import com.univesp.pi.pji240.g8.pi_2024_s2.controller.error.BadRequestException;
import com.univesp.pi.pji240.g8.pi_2024_s2.domain.Seguradora;
import com.univesp.pi.pji240.g8.pi_2024_s2.repository.SeguradoraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SeguradoraService {

    public static final String SEGURADORA_NAO_ENCONTRADO_ERRO = "Seguradora não encontrada!";
    public static final String IDS_SEGURADORA_NAO_INFORMADOS_ERRO = "É obrigatório informar um ou mais id das seguradoras a inativar!";
    public static final String SEGURADORA_JA_INATIVA_ERRO = "Seguradora já está inativa!";
    public static final String SEGURADORA_NAO_INATIVA_ERRO = "Seguradora não está inativa!";
    public static final String CNPJ_DUPLICADO_ERRO = "Esse cnpj já está cadastrado!";
    private static final String SEGURADORA_INATIVO_ERRO = "Não é permitido editar uma seguradora inativa!";
    private final SeguradoraRepository seguradoraRepository;

    public List<Seguradora> listar(boolean incluirInativos) {

        if (incluirInativos) {
            return seguradoraRepository.findAll();
        } else {
            return seguradoraRepository.findByDataExclusaoIsNull();
        }
    }

    public Seguradora salvar(Seguradora seguradora) {

        Optional<Seguradora> existente;

        existente = seguradoraRepository.findByCnpj(seguradora.getCnpj());
        if (existente.isPresent()) {
            throw new BadRequestException(CNPJ_DUPLICADO_ERRO);
        }
        return seguradoraRepository.save(seguradora);
    }

    public Seguradora atualizar(Seguradora seguradora, Long idSeguradora) {

        Optional<Seguradora> optExistente;
        Seguradora atualizado;

        atualizado = new Seguradora();
        optExistente = seguradoraRepository.findById(idSeguradora);

        if (optExistente.isEmpty()) {
            throw new BadRequestException(SEGURADORA_NAO_ENCONTRADO_ERRO);
        } else if (Objects.nonNull(optExistente.get().getDataExclusao())) {
            throw new BadRequestException(SEGURADORA_INATIVO_ERRO);
        }

        optExistente.ifPresent(existente -> {
            atualizado.setNome(seguradora.getNome());
            atualizado.setCnpj(seguradora.getCnpj());
            atualizado.setTelefone(seguradora.getTelefone());
            atualizado.setEndereco(seguradora.getEndereco());
            atualizado.setId(idSeguradora);
        });

        return seguradoraRepository.save(atualizado);
    }

    public Seguradora buscarPorId(Long id) {

        if (Objects.isNull(id)) {
            return null;
        }
        return seguradoraRepository.findById(id).orElse(null);
    }

    public void inativar(List<Long> idsSeguradora) {

        mudarEstado(idsSeguradora, true);
    }

    public void ativar(List<Long> idsSeguradora) {

        mudarEstado(idsSeguradora, false);
    }

    public void mudarEstado(List<Long> idsSeguradora, boolean inativar) {

        Optional<Seguradora> optExistente;

        if (CollectionUtils.isEmpty(idsSeguradora)) {
            throw new BadRequestException(IDS_SEGURADORA_NAO_INFORMADOS_ERRO);
        }

        for (Long idSeguradora : idsSeguradora) {
            String sufixoErro = " (id: " + idSeguradora + ")";
            optExistente = seguradoraRepository.findById(idSeguradora);
            Seguradora existente = optExistente.orElseThrow(() -> new BadRequestException(
                    SEGURADORA_NAO_ENCONTRADO_ERRO + sufixoErro
            ));
            if (inativar) {
                if (Objects.nonNull(existente.getDataExclusao())) {
                    throw new BadRequestException(SEGURADORA_JA_INATIVA_ERRO + sufixoErro);
                }
                existente.setDataExclusao(LocalDate.now());
            } else {
                if (Objects.isNull(existente.getDataExclusao())) {
                    throw new BadRequestException(SEGURADORA_NAO_INATIVA_ERRO + sufixoErro);
                }
                existente.setDataExclusao(null);
            }
            seguradoraRepository.save(existente);
        }
    }
}
