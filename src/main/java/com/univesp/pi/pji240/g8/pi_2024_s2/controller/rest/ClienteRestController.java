package com.univesp.pi.pji240.g8.pi_2024_s2.controller.rest;

import com.univesp.pi.pji240.g8.pi_2024_s2.domain.Cliente;
import com.univesp.pi.pji240.g8.pi_2024_s2.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteRestController {

    private static final Log logger = LogFactory.getLog(ClienteRestController.class);

    private final ClienteService clienteService;

    @GetMapping
    public List<Cliente> getClientes(@RequestParam(required = false, defaultValue = "false") Boolean incluirInativos) {

        return clienteService.listar(incluirInativos);
    }

    @PostMapping
    public Cliente salvar(@RequestBody @Valid Cliente cliente) {

        return clienteService.salvar(cliente);
    }

    @PutMapping("/{idCliente}")
    public Cliente atualizar(@RequestBody @Valid Cliente cliente, @PathVariable Long idCliente) {

        return clienteService.atualizar(cliente, idCliente);
    }

    @DeleteMapping
    public ResponseEntity<Void> inativar(@RequestParam List<Long> idsCliente) {

        clienteService.inativar(idsCliente);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/ativar")
    public ResponseEntity<Void> ativar(@RequestParam List<Long> idsCliente) {

        clienteService.ativar(idsCliente);
        return ResponseEntity.ok(null);
    }
}
