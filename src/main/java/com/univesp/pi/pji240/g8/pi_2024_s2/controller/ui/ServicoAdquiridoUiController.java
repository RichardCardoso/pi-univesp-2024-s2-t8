package com.univesp.pi.pji240.g8.pi_2024_s2.controller.ui;

import com.univesp.pi.pji240.g8.pi_2024_s2.domain.ServicoAdquirido;
import com.univesp.pi.pji240.g8.pi_2024_s2.service.ClienteService;
import com.univesp.pi.pji240.g8.pi_2024_s2.service.ServicoAdquiridoService;
import com.univesp.pi.pji240.g8.pi_2024_s2.service.ServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/ui/servicos-adquiridos")
@RequiredArgsConstructor
public class ServicoAdquiridoUiController {

    private final ServicoAdquiridoService servicoAdquiridoService;
    private final ServicoService servicoService;
    private final ClienteService clienteService;

    @GetMapping
    public String listarServicos(Model model, @RequestParam(required = false, defaultValue = "false") boolean incluirInativos) {

        List<ServicoAdquirido> servicos = servicoAdquiridoService.listar(incluirInativos);
        model.addAttribute("servicosAdquiridos", servicos);
        model.addAttribute("incluirInativos", incluirInativos);
        return "lista-servicosAdquiridos";
    }

    @GetMapping("/edit")
    public String exibirFormularioCriacao(Model model) {

        popularModelo(new ServicoAdquirido(), model);
        return "form-servicoAdquirido";
    }

    @GetMapping("/edit/{id}")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model) {

        Optional<ServicoAdquirido> servico = servicoAdquiridoService.buscarPorId(id);
        if (servico.isEmpty()) {
            return "lista-servicosAdquiridos";
        }
        popularModelo(servico.get(), model);
        return "form-servicoAdquirido";
    }

    @PostMapping
    public String salvarServico(@ModelAttribute @Valid ServicoAdquirido servicoAdquirido, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return erroNaEdicao(servicoAdquirido, result, model);
        }

        try {
            servicoAdquiridoService.salvar(servicoAdquirido);
        } catch (Exception ex) {

            ObjectError error = new ObjectError("servicoAdquirido", ex.getMessage());

            result.addError(error);

            return erroNaEdicao(servicoAdquirido, result, model);
        }
        return "redirect:/ui/servicos-adquiridos";
    }


    @PutMapping("/{idServico}")
    public String atualizarServico(@PathVariable Long idServico, @ModelAttribute @Valid ServicoAdquirido servicoAdquirido, BindingResult result, Model model) {

        if (result.hasErrors()) {
            servicoAdquirido.setId(idServico);
            return erroNaEdicao(servicoAdquirido, result, model);
        }
        servicoAdquiridoService.atualizar(servicoAdquirido, idServico);
        return "redirect:/ui/servicos-adquiridos";
    }

    private String erroNaEdicao(ServicoAdquirido servico, BindingResult result, Model model) {

        model.addAttribute("errors", result.getAllErrors());
        popularModelo(servico, model);
        return "form-servicoAdquirido";
    }

    private void popularModelo(ServicoAdquirido servico, Model model) {
        model.addAttribute("servicoAdquirido", servico);
        model.addAttribute("servicos", servicoService.listar(false));
        model.addAttribute("clientes", clienteService.listar(false));
    }
}
