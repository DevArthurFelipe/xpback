
package org.example.restaurante.Controller;

import org.example.restaurante.dto.FuncionarioDTO;
import org.example.restaurante.Model.Funcionario;
import org.example.restaurante.service.FuncionarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
/**
 * Controlador REST para gerenciar a autenticação de funcionários.
 * Expõe endpoints para login e logout.
 */
@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final FuncionarioService funcionarioService;

    public LoginController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }
    /**
     * Processa a tentativa de login de um funcionário.
     * @param funcionario Um objeto contendo o username e a password para autenticação.
     * @return Um ResponseEntity com o FuncionarioDTO em caso de sucesso (200 OK),
     * ou uma mensagem de erro em caso de falha (401 Unauthorized).
     */
    @PostMapping("/login")
    public ResponseEntity<?> processLogin(@RequestBody Funcionario funcionario) {
        Optional<FuncionarioDTO> userDTO = funcionarioService.autenticar(funcionario.getUsername(), funcionario.getPassword());

        if (userDTO.isPresent()) {
            return ResponseEntity.ok(userDTO.get());
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválidos!");
    }
    /**
     * Realiza o logout do usuário. Atualmente, apenas retorna uma mensagem de sucesso.
     * @return Um ResponseEntity com uma mensagem de confirmação (200 OK).
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Deslogado com sucesso");
    }
}