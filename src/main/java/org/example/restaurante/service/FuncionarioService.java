
package org.example.restaurante.service;

import org.example.restaurante.dto.FuncionarioDTO; // Importe o DTO
import org.example.restaurante.Model.Funcionario;
import org.example.restaurante.repository.FuncionarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final String CHAVE_MESTRE = "SEGREDO123";

    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    public Optional<FuncionarioDTO> autenticar(String username, String password) {
        Optional<Funcionario> funcionario = funcionarioRepository.findByUsername(username);
        if (funcionario.isPresent() && funcionario.get().getPassword().equals(password)) {
            return Optional.of(toFuncionarioDTO(funcionario.get()));
        }
        return Optional.empty();
    }

    public FuncionarioDTO registrarFuncionario(Funcionario funcionario, String chaveAcesso) throws IllegalArgumentException {
        if (!chaveAcesso.equals(CHAVE_MESTRE)) {
            throw new IllegalArgumentException("Chave de acesso inválida!");
        }
        Funcionario savedFuncionario = funcionarioRepository.save(funcionario);
        return toFuncionarioDTO(savedFuncionario);
    }

    public boolean validarChaveMestre(String chave) {
        return chave.equals(CHAVE_MESTRE);
    }

    // Método de conversão
    private FuncionarioDTO toFuncionarioDTO(Funcionario funcionario) {
        return new FuncionarioDTO(funcionario.getId(), funcionario.getUsername(), funcionario.getCargo());
    }
}