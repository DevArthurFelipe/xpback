
package org.example.restaurante.Controller;

import org.example.restaurante.dto.FuncionarioDTO;
import org.example.restaurante.Model.Funcionario;
import org.example.restaurante.service.FuncionarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final FuncionarioService funcionarioService;

    public LoginController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> processLogin(@RequestBody Funcionario funcionario) {
        Optional<FuncionarioDTO> userDTO = funcionarioService.autenticar(funcionario.getUsername(), funcionario.getPassword());

        if (userDTO.isPresent()) {
            return ResponseEntity.ok(userDTO.get());
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválidos!");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Deslogado com sucesso");
    }
}