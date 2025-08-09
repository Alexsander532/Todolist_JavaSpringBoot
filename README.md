# API RESTful To-Do List - Java Spring Boot

Este projeto é uma API RESTful para gerenciamento de tarefas (To-Do List) desenvolvida com Java Spring Boot. A aplicação permite que usuários se cadastrem, criem tarefas, atualizem e listem suas tarefas, com autenticação básica e persistência de dados.

## Visão Geral da Aplicação

A aplicação To-Do List permite:

- Criar e gerenciar usuários
- Autenticar usuários usando Basic Authentication
- Criar, listar e atualizar tarefas específicas de cada usuário
- Validar datas de início e término das tarefas
- Tratar erros com respostas HTTP apropriadas

## Estrutura do Projeto

```
todolist/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── projetospring/
│   │   │           └── todolist/
│   │   │               ├── controller/
│   │   │               ├── errors/
│   │   │               ├── filter/
│   │   │               ├── task/
│   │   │               ├── user/
│   │   │               ├── utils/
│   │   │               └── TodolistApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── pom.xml
```

## Tecnologias Utilizadas

- **Java 17**: Linguagem de programação principal
- **Spring Boot 3.5.4**: Framework para desenvolvimento de aplicações Java
- **Spring Data JPA**: Para persistência de dados
- **H2 Database**: Banco de dados em memória
- **BCrypt**: Para criptografia de senhas
- **Lombok**: Para redução de código boilerplate
- **Maven**: Gerenciamento de dependências

## Conceitos Aprendidos em Java Spring Boot

### 1. Anotações e Decoradores (@)

#### Anotações do Spring Boot
- `@SpringBootApplication`: Combina @Configuration, @EnableAutoConfiguration e @ComponentScan
```java
@SpringBootApplication
public class TodolistApplication {
    public static void main(String[] args) {
        SpringApplication.run(TodolistApplication.class, args);
    }
}
```

#### Anotações para Controllers
- `@RestController`: Marca a classe como um controlador REST
- `@RequestMapping`: Define a URL base para o controlador
- `@PostMapping`, `@GetMapping`, `@PutMapping`: Define métodos HTTP específicos
```java
@RestController
@RequestMapping("/users")
public class UserController {
    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel){
        // Implementação do método
    }
}
```

#### Anotações para JPA
- `@Entity`: Mapeia a classe para uma tabela no banco de dados
- `@Id`: Define o campo como chave primária
- `@GeneratedValue`: Define a estratégia de geração do valor da chave primária
- `@Column`: Personaliza a coluna do banco de dados
```java
@Entity(name = "tb_users")
public class UserModel {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    
    @Column(unique = true)
    private String username;
}
```

#### Outras Anotações
- `@Autowired`: Injeção de dependência automática
- `@Data` (Lombok): Gera getters, setters, equals, hashCode e toString
- `@ControllerAdvice`: Define um controlador global para tratamento de exceções
- `@ExceptionHandler`: Mapeia exceções para métodos específicos

### 2. Estruturas e Componentes Spring Boot

#### Controllers
Controladores são responsáveis por lidar com as requisições HTTP e gerar respostas adequadas.

```java
@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private ITaskRepository taskRepository;
    
    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        // Lógica de criação
    }
}
```

#### Repositories
Interfaces que estendem JpaRepository para operações de banco de dados.

```java
public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {
    List<TaskModel> findByIdUser(UUID idUser);
    TaskModel findByIdAndIdUser(UUID id, UUID idUser);
}
```

#### Models (Entities)
Classes que representam tabelas no banco de dados.

```java
@Data
@Entity(name = "tb_tasks")
public class TaskModel {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String description;
    
    @Column(length = 50)
    private String title;
    private LocalDate startAt;
    private LocalDate endAt;
    private String priority;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    private UUID idUser;
}
```

#### Filters
Filtros para interceptar e processar requisições antes que cheguem aos controladores.

```java
@Component
public class FilterTaskAuth extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Lógica de autenticação
    }
}
```

### 3. API REST e Métodos HTTP

A aplicação implementa os princípios RESTful com os seguintes métodos:

#### POST
Utilizado para criar novos recursos (usuários e tarefas).
```java
@PostMapping("/")
public ResponseEntity create(@RequestBody UserModel userModel){
    // Validações e lógica de criação
    return ResponseEntity.status(201).body(userCreated);
}
```

#### GET
Utilizado para recuperar recursos existentes (listar tarefas).
```java
@GetMapping("/")
public List<TaskModel> list(HttpServletRequest request) {
    var idUser = request.getAttribute("idUser");
    return this.taskRepository.findByIdUser((UUID)idUser);
}
```

#### PUT
Utilizado para atualizar recursos existentes (atualizar tarefas).
```java
@PutMapping("/{id}")
public ResponseEntity update(@PathVariable UUID id, @RequestBody TaskModel taskModel, HttpServletRequest request) {
    // Validações e lógica de atualização
    return ResponseEntity.ok().body(taskUpdated);
}
```

### 4. Autenticação Básica (Basic Auth)

A aplicação implementa autenticação básica HTTP no filtro:

```java
@Component
public class FilterTaskAuth extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var authorization = request.getHeader("Authorization");
        if (authorization == null) {
            filterChain.doFilter(request, response);
            return;
        }
        
        var authEncoded = authorization.substring("Basic".length()).trim();
        byte[] authDecode = Base64.getDecoder().decode(authEncoded);
        var authString = new String(authDecode);
        String[] credentials = authString.split(":");
        String username = credentials[0];
        String password = credentials[1];
        
        // Validação do usuário e senha
        // ...
    }
}
```

### 5. Tratamento de Erros

A aplicação possui um controlador global para tratamento de exceções:

```java
@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(400).body(e.getMostSpecificCause().getMessage());    
    }
}
```

### 6. Persistência de Dados com JPA

Utilização do Spring Data JPA para operações CRUD:

```java
// Salvar um usuário
var userCreated = this.userRepository.save(userModel);

// Buscar um usuário pelo username
var user = this.userRepository.findByUsername(username);

// Buscar tarefas pelo ID do usuário
var tasks = this.taskRepository.findByIdUser((UUID)idUser);
```

### 7. Classes Utilitárias

A aplicação possui uma classe utilitária para copiar propriedades não nulas entre objetos:

```java
public class Utils {
    public static void copyNonNullProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }
    
    public static String[] getNullPropertyNames(Object source){
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        
        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
           Object srcValue = src.getPropertyValue(pd.getName());
           if (srcValue == null) {
               emptyNames.add(pd.getName());
           }
        }
        
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
```

## Documentação da API

### Rotas da API

| Método | Rota | Descrição | Autenticação | Corpo da Requisição | Resposta |
|--------|------|-----------|--------------|---------------------|----------|
| POST | /users/ | Criar um novo usuário | Não | `{"username": "string", "name": "string", "password": "string"}` | `201 Created` com dados do usuário |
| POST | /tasks/ | Criar uma nova tarefa | Sim (Basic) | `{"description": "string", "title": "string", "priority": "string", "startAt": "YYYY-MM-DD", "endAt": "YYYY-MM-DD"}` | `200 OK` com dados da tarefa |
| GET | /tasks/ | Listar tarefas do usuário | Sim (Basic) | Nenhum | `200 OK` com array de tarefas |
| PUT | /tasks/{id} | Atualizar uma tarefa | Sim (Basic) | `{"description": "string", "title": "string", "priority": "string", "startAt": "YYYY-MM-DD", "endAt": "YYYY-MM-DD"}` (campos opcionais) | `200 OK` com dados da tarefa atualizada |

### Códigos de Resposta

- `200 OK`: Requisição bem-sucedida
- `201 Created`: Recurso criado com sucesso
- `400 Bad Request`: Erro de validação (datas inválidas, formatação incorreta)
- `401 Unauthorized`: Credenciais de autenticação inválidas
- `403 Forbidden`: Usuário não tem permissão para acessar o recurso
- `404 Not Found`: Recurso não encontrado (tarefa inexistente)

## Validações Implementadas

1. **Unicidade de username**: Não permite criar usuários com o mesmo nome de usuário
2. **Criptografia de senha**: As senhas são armazenadas criptografadas com BCrypt
3. **Validação de datas**: Não permite criar tarefas com datas no passado ou com data de início posterior à data de término
4. **Validação de propriedade**: Verifica se o usuário é dono da tarefa antes de permitir a atualização
5. **Validação de tamanho**: O título da tarefa tem um tamanho máximo definido (50 caracteres)

## Configuração do Banco de Dados

O projeto utiliza o H2 Database, um banco de dados em memória:

```properties
spring.datasource.url=jdbc:h2:~/todolist
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=admin
spring.datasource.password=admin
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=update
```

## Deploy

O projeto foi implantado online utilizando o **Render**, uma plataforma de hospedagem em nuvem que permite o deploy de aplicações web de forma simples e eficiente. O Render oferece:

- Deploy automatizado a partir do repositório Git
- Escalabilidade automática
- SSL gratuito
- Opções de persistência de dados
- Monitoramento e logs integrados

Este processo de deploy permitiu que a API ficasse disponível online para acesso por qualquer cliente HTTP.

## Conclusão

Este projeto demonstra a implementação de uma API RESTful completa utilizando Java Spring Boot, com autenticação, persistência de dados, validações e tratamento de erros. As principais lições aprendidas incluem:

1. Criação de APIs RESTful com Spring Boot
2. Uso de anotações para mapear entidades e endpoints
3. Implementação de autenticação básica
4. Operações CRUD com Spring Data JPA
5. Tratamento de exceções e respostas HTTP apropriadas
6. Validação de dados de entrada
7. Organização de código em camadas (modelos, repositórios, controladores, filtros)
8. Implantação de aplicações Java Spring Boot no ambiente de nuvem (Render)
