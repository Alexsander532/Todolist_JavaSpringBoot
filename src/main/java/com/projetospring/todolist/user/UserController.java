package com.projetospring.todolist.user;



import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController //Usado para API REST
@RequestMapping("/users")   //Rota users
public class UserController {

    @Autowired // Injeção de dependência do repositório de usuários
    private IUserRepository userRepository;
    

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel){ //ResponseEntity permite retornar status code
       var user = this.userRepository.findByUsername(userModel.getUsername());
        if (user != null) {
            System.out.println("Usuário já existe!"); // Lança uma exceção se o usuário já existir
            //Mensagem de erro
            //Status Code
            return ResponseEntity.status(400).body("Usuário já existe!"); // Retorna um erro 400 com mensagem
        }

        var passwordHashed = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray()); // Criptografa a senha do usuário
        userModel.setPassword(passwordHashed); // Define a senha do usuário com a senha criptografada
        var userCreated = this.userRepository.save(userModel);    // Salva o usuário no banco de dados
        return ResponseEntity.status(201).body(userCreated); // Retorna um erro 201 com mensagem
    }
}
