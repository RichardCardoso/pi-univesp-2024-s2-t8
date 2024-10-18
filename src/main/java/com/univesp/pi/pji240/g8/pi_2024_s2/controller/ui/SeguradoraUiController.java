package com.univesp.pi.pji240.g8.pi_2024_s2.controller.ui;

import com.univesp.pi.pji240.g8.pi_2024_s2.domain.Seguradora;
import com.univesp.pi.pji240.g8.pi_2024_s2.service.SeguradoraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/ui/seguradoras")
@RequiredArgsConstructor
public class SeguradoraUiController {

    private final SeguradoraService seguradoraService;

    @GetMapping
    public String listarSeguradoras(Model model, @RequestParam(required=false, defaultValue = "false") boolean incluirInativos) {

        List<Seguradora> seguradoras = seguradoraService.listar(incluirInativos);
        model.addAttribute("seguradoras", seguradoras);
        model.addAttribute("incluirInativos", incluirInativos);
        return "lista-seguradoras";
    }

    @GetMapping("/edit")
    public String exibirFormularioCriacao(Model model) {
        model.addAttribute("seguradora", new Seguradora());
        return "form-seguradora";
    }

    @GetMapping("/edit/{id}")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
        Seguradora seguradora = seguradoraService.buscarPorId(id);
        model.addAttribute("seguradora", seguradora);
        return "form-seguradora";
    }

    @PostMapping
    public String salvarSeguradora(@ModelAttribute @Valid Seguradora seguradora, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("seguradora", seguradora);
            model.addAttribute("errors", result.getAllErrors());
            return "form-seguradora";
        }

        try {
            seguradoraService.salvar(seguradora);
        } catch (Exception ex) {

            ObjectError error = new ObjectError("seguradora", ex.getMessage());

            result.addError(error);

            model.addAttribute("seguradora", seguradora);
            model.addAttribute("errors", result.getAllErrors());
            return "form-seguradora";
        }
        return "redirect:/ui/seguradoras";
    }

    @PutMapping("/{idSeguradora}")
    public String atualizarSeguradora(@PathVariable Long idSeguradora, @ModelAttribute @Valid Seguradora seguradora, BindingResult result, Model model) {

        if (result.hasErrors()) {
            seguradora.setId(idSeguradora);
            model.addAttribute("seguradora", seguradora);
            model.addAttribute("errors", result.getAllErrors());
            return "form-seguradora";
        }

        seguradoraService.atualizar(seguradora, idSeguradora);
        return "redirect:/ui/seguradoras";
    }
}
