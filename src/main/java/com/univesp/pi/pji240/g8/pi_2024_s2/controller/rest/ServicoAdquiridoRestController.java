package com.univesp.pi.pji240.g8.pi_2024_s2.controller.rest;

import com.univesp.pi.pji240.g8.pi_2024_s2.domain.ServicoAdquirido;
import com.univesp.pi.pji240.g8.pi_2024_s2.service.ServicoAdquiridoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos-adquiridos")
@RequiredArgsConstructor
public class ServicoAdquiridoRestController {

    private static final Log logger = LogFactory.getLog(ServicoAdquiridoRestController.class);

    private final ServicoAdquiridoService servicoAdquiridoService;

    @GetMapping
    public List<ServicoAdquirido> getServicos(@RequestParam(required = false, defaultValue = "false") Boolean incluirInativos) {

        return servicoAdquiridoService.listar(incluirInativos);
    }

    @PostMapping
    public ServicoAdquirido salvar(@RequestBody @Valid ServicoAdquirido servico) {

        return servicoAdquiridoService.salvar(servico);
    }

    @PutMapping("/{idServico}")
    public ServicoAdquirido atualizar(@RequestBody @Valid ServicoAdquirido servico, @PathVariable Long idServicoAdquirido) {

        return servicoAdquiridoService.atualizar(servico, idServicoAdquirido);
    }

    @DeleteMapping
    public ResponseEntity<Void> inativar(@RequestParam List<Long> idsServico) {

        servicoAdquiridoService.inativar(idsServico);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/ativar")
    public ResponseEntity<Void> ativar(@RequestParam List<Long> idsServicoAdquirido) {

        servicoAdquiridoService.ativar(idsServicoAdquirido);
        return ResponseEntity.ok(null);
    }
}
