package com.univesp.pi.pji240.g8.pi_2024_s2.controller.ui;

import com.univesp.pi.pji240.g8.pi_2024_s2.domain.Servico;
import com.univesp.pi.pji240.g8.pi_2024_s2.service.SeguradoraService;
import com.univesp.pi.pji240.g8.pi_2024_s2.service.ServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/ui/servicos")
@RequiredArgsConstructor
public class ServicoUiController {

    private final ServicoService servicoService;
    private final SeguradoraService seguradoraService;

    @GetMapping
    public String listarServicos(Model model, @RequestParam(required=false, defaultValue = "false") boolean incluirInativos) {

        List<Servico> servicos = servicoService.listar(incluirInativos);
        model.addAttribute("servicos", servicos);
        model.addAttribute("incluirInativos", incluirInativos);
        return "lista-servicos";
    }

    @GetMapping("/edit")
    public String exibirFormularioCriacao(Model model) {
        model.addAttribute("servico", new Servico());
        model.addAttribute("seguradoras", seguradoraService.listar(false));
        return "form-servico";
    }

    @GetMapping("/edit/{id}")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
        Servico servico = servicoService.buscarPorId(id);
        model.addAttribute("servico", servico);
        model.addAttribute("seguradoras", seguradoraService.listar(false));
        return "form-servico";
    }

    @PostMapping
    public String salvarServico(@ModelAttribute @Valid Servico servico, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return erroNaEdicao(servico, result, model);
        }

        try {
            servicoService.salvar(servico);
        } catch (Exception ex) {

            ObjectError error = new ObjectError("servico", ex.getMessage());

            result.addError(error);

            return erroNaEdicao(servico, result, model);
        }
        return "redirect:/ui/servicos";
    }

    private String erroNaEdicao(Servico servico, BindingResult result, Model model) {

        model.addAttribute("servico", servico);
        model.addAttribute("errors", result.getAllErrors());
        model.addAttribute("seguradoras", seguradoraService.listar(false));
        return "form-servico";
    }

    @PutMapping("/{idServico}")
    public String atualizarServico(@PathVariable Long idServico, @ModelAttribute @Valid Servico servico, BindingResult result, Model model) {

        if (result.hasErrors()) {
            servico.setId(idServico);
            return erroNaEdicao(servico, result, model);
        }

        servicoService.atualizar(servico, idServico);
        return "redirect:/ui/servicos";
    }
}
