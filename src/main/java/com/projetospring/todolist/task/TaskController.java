package com.projetospring.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projetospring.todolist.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;


@RestController // Usada em API REST para criar uma rota
@RequestMapping("/tasks") // Define a rota base para as tarefas
public class TaskController {

    @Autowired // Injeção de dependência do repositório de tarefas
    private ITaskRepository taskRepository;
    
    @PostMapping("/") // Define o método HTTP POST para criar uma nova tarefa
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        // Método para criar uma nova tarefa
        // A implementação será fornecida pelo Spring Data JPA
        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID)idUser); // Define o ID do usuário na tarefa
        
        //Validação de data e horário, para não ter tarefas em que o dia já passou
        var currentDate = LocalDateTime.now();
        if(currentDate.toLocalDate().isAfter(taskModel.getStartAt()) || currentDate.toLocalDate().isAfter(taskModel.getEndAt())) { // Verifica se a data de início é no passado
            return ResponseEntity.status(400).body("Data de início/data de término inválida!"); // Retorna erro 400 se a data for inválida

        }

        if(taskModel.getStartAt().isAfter(taskModel.getEndAt())) { // Verifica se a data de início é no passado
            return ResponseEntity.status(400).body("Data de início deve ser menor que a data de término"); // Retorna erro 400 se a data for inválida

        }

        var task = this.taskRepository.save(taskModel); // Salva a tarefa no banco de dados
        return ResponseEntity.status(200).body(task); // Retorna a tarefa criada com status 201;
    }
  
        
    @GetMapping("/") // Define o método HTTP GET para listar as tarefas
    public List<TaskModel> list(HttpServletRequest request) {
        // Método para listar as tarefas
        // A implementação será fornecida pelo Spring Data JPA
        var idUser = request.getAttribute("idUser");
        var tasks = this.taskRepository.findByIdUser((UUID)idUser); // Retorna todas as tarefas do usuário
        return tasks; // Retorna a lista de tarefas
    }

    //http://localhost:8080/tasks/d583445f-652d-4faf-9d57-e7cfab1ad4fa
    @PutMapping("/{id}") // Define o método HTTP PUT para atualizar uma tarefa
    public ResponseEntity update(@PathVariable UUID id, @RequestBody TaskModel taskModel, HttpServletRequest request) {
        
        // Recupera o id do usuário autenticado do atributo da request
        var idUser = request.getAttribute("idUser");

        var taskOptional = this.taskRepository.findById(id);
        if (taskOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Tarefa não existe, verifique o id!");
        }

        var task = taskOptional.get();

        if (task.getIdUser() == null || !task.getIdUser().equals(idUser)) {
            return ResponseEntity.status(403).body("Você não tem permissão para atualizar esta tarefa");
        }

        Utils.copyNonNullProperties(taskModel, task); // Copia as propriedades não nulas de taskModel para task
        var taskUpdated = this.taskRepository.save(task); // Salva a tarefa atualizada no banco de dados
        return ResponseEntity.ok().body(taskUpdated); // Retorna a tarefa atualizada com status 200
    }
}


