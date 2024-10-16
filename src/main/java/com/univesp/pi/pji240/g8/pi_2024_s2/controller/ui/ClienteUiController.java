package com.univesp.pi.pji240.g8.pi_2024_s2.controller.ui;

import com.univesp.pi.pji240.g8.pi_2024_s2.controller.error.ValidationErrorResponse;
import com.univesp.pi.pji240.g8.pi_2024_s2.domain.Cliente;
import com.univesp.pi.pji240.g8.pi_2024_s2.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/ui/clientes")
@RequiredArgsConstructor
public class ClienteUiController {

    private final ClienteService clienteService;

    @GetMapping
    public String listarClientes(Model model, @RequestParam(required=false, defaultValue = "false") boolean incluirInativos) {

        List<Cliente> clientes = clienteService.listar(incluirInativos);
        model.addAttribute("clientes", clientes);
        model.addAttribute("incluirInativos", incluirInativos);
        return "lista-clientes";
    }

    @GetMapping("/edit")
    public String exibirFormularioCriacao(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "form-cliente";
    }

    @GetMapping("/edit/{id}")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
        Cliente cliente = clienteService.buscarPorId(id);
        model.addAttribute("cliente", cliente);
        return "form-cliente";
    }

    @PostMapping
    public String salvarCliente(@ModelAttribute @Valid Cliente cliente, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("cliente", cliente);
            model.addAttribute("errors", result.getAllErrors());
            return "form-cliente";
        }

        try {
            clienteService.salvar(cliente);
        } catch (Exception ex) {

            ObjectError error = new ObjectError("cliente", ex.getMessage());

            result.addError(error);

            model.addAttribute("cliente", cliente);
            model.addAttribute("errors", result.getAllErrors());
            return "form-cliente";
        }
        return "redirect:/ui/clientes";
    }

    @PutMapping("/{idCliente}")
    public String atualizarCliente(@PathVariable Long idCliente, @ModelAttribute @Valid Cliente cliente, BindingResult result, Model model) {

        if (result.hasErrors()) {
            cliente.setId(idCliente);
            model.addAttribute("cliente", cliente);
            model.addAttribute("errors", result.getAllErrors());
            return "form-cliente";
        }

        clienteService.atualizar(cliente, idCliente);
        return "redirect:/ui/clientes";
    }
}
