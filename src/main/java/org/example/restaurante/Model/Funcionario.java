package org.example.restaurante.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome de usuário não pode estar em branco")
    @Size(min = 3, message = "O nome de usuário deve ter no mínimo 3 caracteres")
    private String username;

    @NotBlank(message = "A senha não pode estar em branco")
    private String password;

    private String cargo;
}