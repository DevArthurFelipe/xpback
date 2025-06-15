
package org.example.restaurante.Controller;

import org.example.restaurante.dto.FuncionarioDTO;
import org.example.restaurante.Model.Funcionario;
import org.example.restaurante.service.FuncionarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registrar")
public class RegistroController {

    private final FuncionarioService funcionarioService;

    public RegistroController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @PostMapping
    public ResponseEntity<?> processRegistro(@RequestParam String chave, @RequestBody Funcionario funcionario) {
        try {
            FuncionarioDTO novoFuncionarioDTO = funcionarioService.registrarFuncionario(funcionario, chave);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoFuncionarioDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}