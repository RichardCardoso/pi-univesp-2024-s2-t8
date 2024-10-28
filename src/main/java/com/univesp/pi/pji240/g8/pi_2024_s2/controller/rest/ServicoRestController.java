package com.univesp.pi.pji240.g8.pi_2024_s2.controller.rest;

import com.univesp.pi.pji240.g8.pi_2024_s2.domain.Servico;
import com.univesp.pi.pji240.g8.pi_2024_s2.service.ServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
@RequiredArgsConstructor
public class ServicoRestController {

    private static final Log logger = LogFactory.getLog(ServicoRestController.class);

    private final ServicoService servicoService;

    @GetMapping
    public List<Servico> getServicos(@RequestParam(required = false, defaultValue = "false") Boolean incluirInativos) {

        return servicoService.listar(incluirInativos);
    }

    @PostMapping
    public Servico salvar(@RequestBody @Valid Servico servico) {

        return servicoService.salvar(servico);
    }

    @PutMapping("/{idServico}")
    public Servico atualizar(@RequestBody @Valid Servico servico, @PathVariable Long idServico) {

        return servicoService.atualizar(servico, idServico);
    }

    @DeleteMapping
    public ResponseEntity<Void> inativar(@RequestParam List<Long> idsServico) {

        servicoService.inativar(idsServico);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/ativar")
    public ResponseEntity<Void> ativar(@RequestParam List<Long> idsServico) {

        servicoService.ativar(idsServico);
        return ResponseEntity.ok(null);
    }
}
