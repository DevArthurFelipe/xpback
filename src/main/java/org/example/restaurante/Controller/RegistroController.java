package org.example.restaurante.Controller;

import org.example.restaurante.dto.FuncionarioDTO;
import org.example.restaurante.Model.Funcionario;
import org.example.restaurante.service.FuncionarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 * Controlador REST responsável pelo processo de registro de novos funcionários no sistema.
 * Este endpoint é protegido por uma chave de acesso mestra.
 */
@RestController
@RequestMapping("/api/registrar")
public class RegistroController {

    private final FuncionarioService funcionarioService;

    public RegistroController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }
    /**
     * Processa a requisição para registrar um novo funcionário.
     * Apenas requisições com a chave de acesso correta são autorizadas.
     * @param chave A chave mestra de acesso necessária para autorizar a operação.
     * @param funcionario O corpo da requisição contendo os dados do novo funcionário (username, password, cargo).
     * @return Um ResponseEntity com o FuncionarioDTO do novo funcionário em caso de sucesso (201 CREATED).
     */
    @PostMapping
    public ResponseEntity<FuncionarioDTO> processRegistro(@RequestParam String chave, @RequestBody Funcionario funcionario) {
        FuncionarioDTO novoFuncionarioDTO = funcionarioService.registrarFuncionario(funcionario, chave);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoFuncionarioDTO);
    }
}