package com.univesp.pi.pji240.g8.pi_2024_s2.service;

import com.univesp.pi.pji240.g8.pi_2024_s2.controller.error.BadRequestException;
import com.univesp.pi.pji240.g8.pi_2024_s2.domain.Seguradora;
import com.univesp.pi.pji240.g8.pi_2024_s2.domain.Servico;
import com.univesp.pi.pji240.g8.pi_2024_s2.repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicoService {

    public static final String SERVICO_NAO_ENCONTRADO_ERRO = "Servico não encontrado!";
    public static final String IDS_SERVICO_NAO_INFORMADOS_ERRO = "É obrigatório informar um ou mais id dos servicos a inativar!";
    public static final String SERVICO_JA_INATIVO_ERRO = "Servico já está inativo!";
    public static final String SERVICO_NAO_INATIVO_ERRO = "Servico não está inativo!";
    public static final String TIPO_E_DESCRICAO_DUPLICADOS_ERRO = "Essa combinação de tipo+descrição já está cadastrada!";
    public static final String SEGURADORA_INVALIDA_ERRO = "Seguradora inválida!";
    private static final String SERVICO_INATIVO_ERRO = "Não é permitido editar um servico inativo!";
    private final ServicoRepository servicoRepository;
    private final SeguradoraService seguradoraService;

    public List<Servico> listar(boolean incluirInativos) {

        if (incluirInativos) {
            return servicoRepository.findAll();
        } else {
            return servicoRepository.findByDataExclusaoIsNull();
        }
    }

    public Servico salvar(Servico servico) {

        Optional<Servico> existente;
        Seguradora seguradora;

        existente = servicoRepository.findByTipoServicoAndDescricao(servico.getTipoServico(), servico.getDescricao());
        if (existente.isPresent()) {
            throw new BadRequestException(TIPO_E_DESCRICAO_DUPLICADOS_ERRO);
        }
        seguradora = getSeguradora(servico);
        servico.setSeguradora(seguradora);
        return servicoRepository.save(servico);
    }

    public Servico atualizar(Servico servico, Long idServico) {

        Optional<Servico> optExistente;
        Servico atualizado;
        Seguradora seguradora;

        atualizado = new Servico();
        optExistente = servicoRepository.findById(idServico);

        if (optExistente.isEmpty()) {
            throw new BadRequestException(SERVICO_NAO_ENCONTRADO_ERRO);
        } else if (Objects.nonNull(optExistente.get().getDataExclusao())) {
            throw new BadRequestException(SERVICO_INATIVO_ERRO);
        }

        seguradora = getSeguradora(servico);

        optExistente.ifPresent(existente -> {
            atualizado.setDescricao(servico.getDescricao());
            atualizado.setTipoServico(servico.getTipoServico());
            atualizado.setPreco(servico.getPreco());
            atualizado.setSeguradora(seguradora);
            atualizado.setId(idServico);
        });

        return servicoRepository.save(atualizado);
    }

    private Seguradora getSeguradora(Servico servico) {
        Seguradora seguradora;

        seguradora = seguradoraService.buscarPorId(servico.getSeguradora().getId());
        if (Objects.isNull(seguradora)) {
            throw new BadRequestException(SEGURADORA_INVALIDA_ERRO);
        }
        return seguradora;
    }

    public Servico buscarPorId(Long id) {

        if (Objects.isNull(id)) {
            return null;
        }
        return servicoRepository.findById(id).orElse(null);
    }

    public void inativar(List<Long> idsServico) {

        mudarEstado(idsServico, true);
    }

    public void ativar(List<Long> idsServico) {

        mudarEstado(idsServico, false);
    }

    public void mudarEstado(List<Long> idsServico, boolean inativar) {

        Optional<Servico> optExistente;

        if (CollectionUtils.isEmpty(idsServico)) {
            throw new BadRequestException(IDS_SERVICO_NAO_INFORMADOS_ERRO);
        }

        for (Long idServico : idsServico) {
            String sufixoErro = " (id: " + idServico + ")";
            optExistente = servicoRepository.findById(idServico);
            Servico existente = optExistente.orElseThrow(() -> new BadRequestException(
                    SERVICO_NAO_ENCONTRADO_ERRO + sufixoErro
            ));
            if (inativar) {
                if (Objects.nonNull(existente.getDataExclusao())) {
                    throw new BadRequestException(SERVICO_JA_INATIVO_ERRO + sufixoErro);
                }
                existente.setDataExclusao(LocalDate.now());
            } else {
                if (Objects.isNull(existente.getDataExclusao())) {
                    throw new BadRequestException(SERVICO_NAO_INATIVO_ERRO + sufixoErro);
                }
                existente.setDataExclusao(null);
            }
            servicoRepository.save(existente);
        }
    }
}
