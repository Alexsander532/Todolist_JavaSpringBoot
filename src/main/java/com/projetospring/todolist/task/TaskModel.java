package com.projetospring.todolist.task;

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

/*
     * ID
     * Usuário (ID_USUARIO)
     * Descrição
     * Título
     * Data de Inicio
     * Data de Término
     * Prioridade
     */

@Data //Coloca automaticamente os métodos getters e setters
@Entity(name = "tb_tasks") // Define o nome da tabela no banco de dados
public class TaskModel {
    
    @Id // Define o campo como chave primária
    @GeneratedValue(generator = "UUID") // Gera um UUID automaticamente
     private UUID id;
     private String description;

     @Column(length = 50) // Define o tamanho máximo do campo no banco de dados
     private String title;
     private LocalDate startAt;
     private LocalDate endAt;
     private String priority;

     @CreationTimestamp // Anotação do Hibernate que preenche automaticamente o campo com a data e hora de criação
     private LocalDateTime createdAt;
     private UUID idUser;

     public void setIdUser(UUID idUser) {
        this.idUser = idUser; // Define o ID do usuário na tarefa
      }
        public UUID getIdUser() {
            return idUser; // Retorna o ID do usuário associado à tarefa
        }
        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setTitle(String title) throws Exception {
            if(title.length() > 50) { // Verifica se o título não está vazio
                throw new Exception("Título não pode ser vazio ou maior que 50 caracteres"); // Lança exceção se o título for inválido
            }
        this.title = title; // Define o título da tarefa

}}
