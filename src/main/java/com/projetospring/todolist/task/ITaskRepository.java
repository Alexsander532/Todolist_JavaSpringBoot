package com.projetospring.todolist.task;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {
    List<TaskModel> findByIdUser(UUID idUser); // Método para encontrar tarefas pelo ID do usuário
    TaskModel findByIdAndIdUser(UUID id, UUID idUser); // Método para encontrar tarefa pelo ID e ID do usuário
    // O Spring Data JPA irá implementar esses métodos automaticamente
} 