package org.example.restaurante.service;

import org.example.restaurante.dto.FuncionarioDTO;
import org.example.restaurante.Model.Funcionario;
import org.example.restaurante.repository.FuncionarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    @Value("${app.security.master-key}")
    private String chaveMestre;

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
        if (!chaveAcesso.equals(this.chaveMestre)) {
            throw new IllegalArgumentException("Chave de acesso inválida!");
        }
        Funcionario savedFuncionario = funcionarioRepository.save(funcionario);
        return toFuncionarioDTO(savedFuncionario);
    }

    public boolean validarChaveMestre(String chave) {
        return chave.equals(this.chaveMestre);
    }

    private FuncionarioDTO toFuncionarioDTO(Funcionario funcionario) {
        return new FuncionarioDTO(funcionario.getId(), funcionario.getUsername(), funcionario.getCargo());
    }
}