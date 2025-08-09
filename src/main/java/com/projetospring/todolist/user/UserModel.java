package com.projetospring.todolist.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

//@Data é uma anotação do Lombok que gera automaticamente os métodos getters, setters, equals, hashCode e toString para a classe.
//@Setter é uma anotação do Lombok que gera automaticamente o método setter para o campo especificado.
//@Getter é uma anotação do Lombok que gera automaticamente o método getter para o campo especificado.
@Data   //Coloca automaticamente os métodos getters e setters
@Entity(name = "tb_users") // Define o nome da tabela no banco de dados
public class UserModel {    //Modelo de Usuário



    @Id // Define o campo como chave primária
    @GeneratedValue(generator = "UUID") // Gera um UUID automaticamente
    private UUID id; // Adicionando um campo ID único

    @Column(unique = true) // Define o campo como único no banco de dados
    private String username;
    private String name;
    private String password;

    @CreationTimestamp // Anotação do Hibernate que preenche automaticamente o campo com a data e hora de criação
    private LocalDateTime createdAt;
}
