package com.univesp.pi.pji240.g8.pi_2024_s2.controller.rest;

import com.univesp.pi.pji240.g8.pi_2024_s2.domain.Seguradora;
import com.univesp.pi.pji240.g8.pi_2024_s2.service.SeguradoraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seguradoras")
@RequiredArgsConstructor
public class SeguradoraRestController {

    private static final Log logger = LogFactory.getLog(SeguradoraRestController.class);

    private final SeguradoraService seguradoraService;

    @GetMapping
    public List<Seguradora> getSeguradoras(@RequestParam(required = false, defaultValue = "false") Boolean incluirInativos) {

        return seguradoraService.listar(incluirInativos);
    }

    @PostMapping
    public Seguradora salvar(@RequestBody @Valid Seguradora seguradora) {

        return seguradoraService.salvar(seguradora);
    }

    @PutMapping("/{idSeguradora}")
    public Seguradora atualizar(@RequestBody @Valid Seguradora seguradora, @PathVariable Long idSeguradora) {

        return seguradoraService.atualizar(seguradora, idSeguradora);
    }

    @DeleteMapping
    public ResponseEntity<Void> inativar(@RequestParam List<Long> idsSeguradora) {

        seguradoraService.inativar(idsSeguradora);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/ativar")
    public ResponseEntity<Void> ativar(@RequestParam List<Long> idsSeguradora) {

        seguradoraService.ativar(idsSeguradora);
        return ResponseEntity.ok(null);
    }
}
