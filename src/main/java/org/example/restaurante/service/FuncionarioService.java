package org.example.restaurante.service;

import org.example.restaurante.dto.FuncionarioDTO;
import org.example.restaurante.Model.Funcionario;
import org.example.restaurante.repository.FuncionarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;

/**
 * Camada de serviço responsável pela lógica de negócio relacionada aos funcionários,
 * incluindo autenticação e registro.
 */
@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.security.master-key}")
    private String chaveMestre;

    public FuncionarioService(FuncionarioRepository funcionarioRepository, PasswordEncoder passwordEncoder) {
        this.funcionarioRepository = funcionarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Autentica um funcionário com base no nome de usuário e senha.
     * @param username O nome de usuário para autenticação.
     * @param password A senha do usuário.
     * @return um Optional contendo o FuncionarioDTO se a autenticação for bem-sucedida,
     * ou um Optional vazio caso contrário.
     */
    public Optional<FuncionarioDTO> autenticar(String username, String password) {
        Optional<Funcionario> funcionarioOpt = funcionarioRepository.findByUsername(username);
        if (funcionarioOpt.isPresent() && passwordEncoder.matches(password, funcionarioOpt.get().getPassword())) {
            return Optional.of(toFuncionarioDTO(funcionarioOpt.get()));
        }
        return Optional.empty();
    }

    /**
     * Registra um novo funcionário no sistema após validar a chave de acesso.
     * @param funcionario A entidade Funcionario com os dados a serem salvos.
     * @param chaveAcesso A chave mestra necessária para autorizar a operação.
     * @return O DTO do funcionário recém-criado.
     * @throws IllegalArgumentException se a chave de acesso for inválida.
     */
    public FuncionarioDTO registrarFuncionario(Funcionario funcionario, String chaveAcesso) throws IllegalArgumentException {
        if (!chaveAcesso.equals(this.chaveMestre)) {
            throw new IllegalArgumentException("Chave de acesso inválida!");
        }
        funcionario.setPassword(passwordEncoder.encode(funcionario.getPassword()));
        Funcionario savedFuncionario = funcionarioRepository.save(funcionario);
        return toFuncionarioDTO(savedFuncionario);
    }

    /**
     * Valida se a chave fornecida corresponde à chave mestra da aplicação.
     * @param chave A chave a ser validada.
     * @return true se a chave for válida, false caso contrário.
     */
    public boolean validarChaveMestre(String chave) {
        return chave.equals(this.chaveMestre);
    }

    /**
     * Converte uma entidade Funcionario para seu respectivo DTO, omitindo a senha.
     * @param funcionario A entidade a ser convertida.
     * @return O objeto FuncionarioDTO.
     */
    private FuncionarioDTO toFuncionarioDTO(Funcionario funcionario) {
        return new FuncionarioDTO(funcionario.getId(), funcionario.getUsername(), funcionario.getCargo());
    }
}