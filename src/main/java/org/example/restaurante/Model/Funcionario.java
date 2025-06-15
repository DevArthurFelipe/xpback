package org.example.restaurante.Model;

import jakarta.persistence.*;
import lombok.Data; // Adicionado
import lombok.NoArgsConstructor; // Adicionado
import lombok.AllArgsConstructor; // Adicionado

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String cargo;

}