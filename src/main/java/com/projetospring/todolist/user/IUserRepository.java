package com.projetospring.todolist.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<UserModel, UUID>{
    UserModel findByUsername(String username); // Método para encontrar usuário pelo nome de usuário
    
} 
