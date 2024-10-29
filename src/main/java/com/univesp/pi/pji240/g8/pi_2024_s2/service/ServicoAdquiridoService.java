package com.univesp.pi.pji240.g8.pi_2024_s2.service;

import com.univesp.pi.pji240.g8.pi_2024_s2.controller.error.BadRequestException;
import com.univesp.pi.pji240.g8.pi_2024_s2.domain.Cliente;
import com.univesp.pi.pji240.g8.pi_2024_s2.domain.Servico;
import com.univesp.pi.pji240.g8.pi_2024_s2.domain.ServicoAdquirido;
import com.univesp.pi.pji240.g8.pi_2024_s2.repository.ServicoAdquiridoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicoAdquiridoService {

    public static final String SERVICO_NAO_ENCONTRADO_ERRO = "ServicoAdquirido não encontrado!";
    public static final String IDS_SERVICO_NAO_INFORMADOS_ERRO = "É obrigatório informar um ou mais id dos servicoAdquiridos a inativar!";
    public static final String SERVICO_JA_INATIVO_ERRO = "ServicoAdquirido já está inativo!";
    public static final String SERVICO_NAO_INATIVO_ERRO = "ServicoAdquirido não está inativo!";
    public static final String SEGURADORA_INVALIDA_ERRO = "Seguradora inválida!";
    public static final String CLIENTE_INVALIDO_ERRO = "Cliente inválido!";
    private static final String SERVICO_INATIVO_ERRO = "Não é permitido editar um servicoAdquirido inativo!";
    private final ServicoAdquiridoRepository servicoAdquiridoRepository;
    private final ServicoService servicoService;
    private final ClienteService clienteService;

    public List<ServicoAdquirido> listar(boolean incluirInativos) {

        if (incluirInativos) {
            return servicoAdquiridoRepository.findAll();
        } else {
            return servicoAdquiridoRepository.findByDataExclusaoIsNull();
        }
    }

    public ServicoAdquirido salvar(ServicoAdquirido servicoAdquirido) {

        Servico servico;
        Cliente cliente;

        servico = getServico(servicoAdquirido);
        cliente = getCliente(servicoAdquirido);
        servicoAdquirido.setServico(servico);
        servicoAdquirido.setCliente(cliente);
        return servicoAdquiridoRepository.save(servicoAdquirido);
    }

    public ServicoAdquirido atualizar(ServicoAdquirido servicoAdquirido, Long idServicoAdquirido) {

        Optional<ServicoAdquirido> optExistente;
        ServicoAdquirido atualizado;
        Servico servico;
        Cliente cliente;

        atualizado = new ServicoAdquirido();
        optExistente = buscarPorId(idServicoAdquirido);

        if (optExistente.isEmpty()) {
            throw new BadRequestException(SERVICO_NAO_ENCONTRADO_ERRO);
        } else if (Objects.nonNull(optExistente.get().getDataExclusao())) {
            throw new BadRequestException(SERVICO_INATIVO_ERRO);
        }

        servico = getServico(servicoAdquirido);
        cliente = getCliente(servicoAdquirido);

        optExistente.ifPresent(existente -> {
            atualizado.setDataAquisicao(servicoAdquirido.getDataAquisicao());
            atualizado.setValorPago(servicoAdquirido.getValorPago());
            atualizado.setServico(servico);
            atualizado.setCliente(cliente);
            atualizado.setId(idServicoAdquirido);
        });

        return servicoAdquiridoRepository.save(atualizado);
    }

    private Servico getServico(ServicoAdquirido servicoAdquirido) {
        Servico servico;

        servico = servicoService.buscarPorId(servicoAdquirido.getServico().getId());
        if (Objects.isNull(servico)) {
            throw new BadRequestException(SEGURADORA_INVALIDA_ERRO);
        }
        return servico;
    }

    private Cliente getCliente(ServicoAdquirido servicoAdquirido) {
        Cliente servico;

        servico = clienteService.buscarPorId(servicoAdquirido.getCliente().getId());
        if (Objects.isNull(servico)) {
            throw new BadRequestException(CLIENTE_INVALIDO_ERRO);
        }
        return servico;
    }

    public Optional<ServicoAdquirido> buscarPorId(Long id) {

        if (Objects.isNull(id)) {
            return Optional.empty();
        }
        return servicoAdquiridoRepository.findByIdAndDataExclusaoIsNull(id);
    }

    public void inativar(List<Long> idsServicoAdquirido) {

        mudarEstado(idsServicoAdquirido, true);
    }

    public void ativar(List<Long> idsServicoAdquirido) {

        mudarEstado(idsServicoAdquirido, false);
    }

    public void mudarEstado(List<Long> idsServicoAdquirido, boolean inativar) {

        Optional<ServicoAdquirido> optExistente;

        if (CollectionUtils.isEmpty(idsServicoAdquirido)) {
            throw new BadRequestException(IDS_SERVICO_NAO_INFORMADOS_ERRO);
        }

        for (Long idServicoAdquirido : idsServicoAdquirido) {
            String sufixoErro = " (id: " + idServicoAdquirido + ")";
            optExistente = servicoAdquiridoRepository.findById(idServicoAdquirido);
            ServicoAdquirido existente = optExistente.orElseThrow(() -> new BadRequestException(
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
            servicoAdquiridoRepository.save(existente);
        }
    }
}
