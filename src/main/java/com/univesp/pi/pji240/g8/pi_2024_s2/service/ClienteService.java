package com.univesp.pi.pji240.g8.pi_2024_s2.service;

import com.univesp.pi.pji240.g8.pi_2024_s2.controller.error.BadRequestException;
import com.univesp.pi.pji240.g8.pi_2024_s2.domain.Cliente;
import com.univesp.pi.pji240.g8.pi_2024_s2.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    public static final String CLIENTE_NAO_ENCONTRADO_ERRO = "Cliente não encontrado!";
    public static final String IDS_CLIENTE_NAO_INFORMADOS_ERRO = "É obrigatório informar um ou mais id dos clientes a inativar!";
    public static final String CLIENTE_JA_INATIVO_ERRO = "Cliente já está inativo!";
    public static final String CLIENTE_NAO_INATIVO_ERRO = "Cliente não está inativo!";
    public static final String CPF_DUPLICADO_ERRO = "Esse cpf já está cadastrado!";
    private static final String CLIENTE_INATIVO_ERRO = "Não é permitido editar um cliente inativo!";
    private final ClienteRepository clienteRepository;

    public List<Cliente> listar(boolean incluirInativos) {

        if (incluirInativos) {
            return clienteRepository.findAll();
        } else {
            return clienteRepository.findByDataExclusaoIsNull();
        }
    }

    public Cliente salvar(Cliente cliente) {

        Optional<Cliente> existente;

        existente = clienteRepository.findByCpf(cliente.getCpf());
        if (existente.isPresent()) {
            throw new BadRequestException(CPF_DUPLICADO_ERRO);
        }
        return clienteRepository.save(cliente);
    }

    public Cliente atualizar(Cliente cliente, Long idCliente) {

        Optional<Cliente> optExistente;
        Cliente atualizado;

        atualizado = new Cliente();
        optExistente = clienteRepository.findById(idCliente);

        if (optExistente.isEmpty()) {
            throw new BadRequestException(CLIENTE_NAO_ENCONTRADO_ERRO);
        } else if (Objects.nonNull(optExistente.get().getDataExclusao())) {
            throw new BadRequestException(CLIENTE_INATIVO_ERRO);
        }

        optExistente.ifPresent(existente -> {
            atualizado.setNome(cliente.getNome());
            atualizado.setCpf(cliente.getCpf());
            atualizado.setTelefone(cliente.getTelefone());
            atualizado.setEndereco(cliente.getEndereco());
            atualizado.setEmail(cliente.getEmail());
            atualizado.setId(idCliente);
        });

        return clienteRepository.save(atualizado);
    }

    public Cliente buscarPorId(Long id) {

        if (Objects.isNull(id)) {
            return null;
        }
        return clienteRepository.findById(id).orElse(null);
    }

    public void inativar(List<Long> idsCliente) {

        mudarEstado(idsCliente, true);
    }

    public void ativar(List<Long> idsCliente) {

        mudarEstado(idsCliente, false);
    }

    public void mudarEstado(List<Long> idsCliente, boolean inativar) {

        Optional<Cliente> optExistente;

        if (CollectionUtils.isEmpty(idsCliente)) {
            throw new BadRequestException(IDS_CLIENTE_NAO_INFORMADOS_ERRO);
        }

        for (Long idCliente : idsCliente) {
            String sufixoErro = " (id: " + idCliente + ")";
            optExistente = clienteRepository.findById(idCliente);
            Cliente existente = optExistente.orElseThrow(() -> new BadRequestException(
                    CLIENTE_NAO_ENCONTRADO_ERRO + sufixoErro
            ));
            if (inativar) {
                if (Objects.nonNull(existente.getDataExclusao())) {
                    throw new BadRequestException(CLIENTE_JA_INATIVO_ERRO + sufixoErro);
                }
                existente.setDataExclusao(LocalDate.now());
            } else {
                if (Objects.isNull(existente.getDataExclusao())) {
                    throw new BadRequestException(CLIENTE_NAO_INATIVO_ERRO + sufixoErro);
                }
                existente.setDataExclusao(null);
            }
            clienteRepository.save(existente);
        }
    }
}
