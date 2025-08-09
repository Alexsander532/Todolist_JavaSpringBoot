# Anotações

- Controller é um componente utilizado para ser a camada entre a requisição e os outros componentes, ou seja, é o controller que irá receber as requisições do usuário.

## Getters e Setters

**Getters** e **Setters** são métodos especiais em Java usados para acessar e modificar os atributos (campos) privados de uma classe. Eles seguem o princípio de **encapsulamento** da programação orientada a objetos.

### Definições

- **Getter**: método que **obtém** (get) o valor de um atributo
- **Setter**: método que **define** (set) o valor de um atributo

### Por que usar?

1. **Segurança**: Controla como os dados são acessados e modificados
2. **Validação**: Permite validar dados antes de atribuí-los  
3. **Encapsulamento**: Mantém os atributos privados, expondo apenas métodos públicos
4. **Spring Boot**: Necessário para que o framework consiga fazer o binding automático de JSON para objetos

### Exemplo prático

```java
public class UserModel {
    // Atributos privados (não podem ser acessados diretamente)
    private String name;
    private String username;
    private String password;
    private int age;

    // GETTER para name - obtém o valor
    public String getName() {
        return name;
    }

    // SETTER para name - define o valor
    public void setName(String name) {
        this.name = name;
    }

    // GETTER para username
    public String getUsername() {
        return username;
    }

    // SETTER para username com validação
    public void setUsername(String username) {
        if (username != null && username.length() > 3) {
            this.username = username;
        } else {
            throw new IllegalArgumentException("Username deve ter mais de 3 caracteres");
        }
    }

    // GETTER para password
    public String getPassword() {
        return password;
    }

    // SETTER para password com validação
    public void setPassword(String password) {
        if (password != null && password.length() >= 6) {
            this.password = password;
        } else {
            throw new IllegalArgumentException("Password deve ter pelo menos 6 caracteres");
        }
    }

    // GETTER para age
    public int getAge() {
        return age;
    }

    // SETTER para age com validação
    public void setAge(int age) {
        if (age >= 0 && age <= 120) {
            this.age = age;
        } else {
            throw new IllegalArgumentException("Idade deve estar entre 0 e 120 anos");
        }
    }
}
```

### Como usar

```java
// Criando um objeto
UserModel user = new UserModel();

// Usando setters para definir valores
user.setName("João");
user.setUsername("joao123");
user.setPassword("senha123");
user.setAge(25);

// Usando getters para obter valores
String nome = user.getName();        // Retorna "João"
String username = user.getUsername(); // Retorna "joao123"
int idade = user.getAge();           // Retorna 25
```

### Importância no Spring Boot

- O Spring Boot usa **reflexão** para converter JSON em objetos Java
- Ele chama automaticamente os **setters** quando recebe dados via `@RequestBody`
- Ele chama automaticamente os **getters** quando retorna objetos como JSON
- **Sem getters/setters, o Spring Boot não consegue fazer essa conversão automaticamente**

## Explicação básica do @Data

A anotação `@Data` é fornecida pela biblioteca **Lombok** e é usada em classes Java para gerar automaticamente métodos comuns como **getters**, **setters**, `toString()`, `equals()`, `hashCode()` e um construtor padrão. Isso reduz a quantidade de código repetitivo (boilerplate) nas classes de modelo.

### Exemplo de uso

```java
import lombok.Data;

@Data
public class UserModel {
    private String name;
    private String username;
    private String password;
    private int age;
}
```

Com `@Data`, não é necessário escrever manualmente os métodos getters e setters, pois o Lombok gera esses métodos automaticamente em tempo de compilação.

### Vantagens

- **Menos código repetitivo**
- **Facilita a manutenção**
- **Melhora a legibilidade**

> **Observação:** Para usar o Lombok, é necessário adicionar a dependência no projeto e configurar o IDE para reconhecer as anotações.

## O que é Spring Data JPA

**Spring Data JPA** é um projeto do Spring que facilita a implementação de repositórios baseados em JPA (Java Persistence API). Ele fornece uma camada de abstração para acesso a dados, permitindo que você trabalhe com bancos de dados relacionais usando interfaces e métodos simples, sem precisar escrever SQL manualmente.

### Principais vantagens

- Criação automática de consultas a partir do nome dos métodos.
- Integração fácil com bancos de dados relacionais (MySQL, PostgreSQL, H2, etc).
- Suporte a operações CRUD (Create, Read, Update, Delete) prontas.
- Facilita o uso de entidades JPA no Spring Boot.

### Exemplo básico

```java
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    // Você pode criar métodos como:
    UserModel findByUsername(String username);
}
```

Com isso, você pode salvar, buscar, atualizar e deletar entidades no banco de dados de forma simples e rápida.

## O que é uma interface em Java

Uma **interface** em Java é um tipo especial de referência que define um conjunto de métodos (assinaturas), mas **não implementa** esses métodos. Ela serve como um **contrato**: qualquer classe que "implementa" uma interface se compromete a fornecer a implementação dos métodos definidos nela.

### Características principais

- Não possui implementação dos métodos (apenas as assinaturas).
- Pode conter constantes (variáveis `public static final`).
- Uma classe pode implementar várias interfaces (herança múltipla de tipos).
- Usada para definir comportamentos comuns a diferentes classes.

### Exemplo de interface

```java
public interface Animal {
    void emitirSom();
    void mover();
}
```

### Exemplo de implementação

```java
public class Cachorro implements Animal {
    @Override
    public void emitirSom() {
        System.out.println("Au Au!");
    }

    @Override
    public void mover() {
        System.out.println("O cachorro está correndo.");
    }
}
```

### Por que usar interfaces?

- **Abstração**: Permite definir comportamentos sem se preocupar com a implementação.
- **Polimorfismo**: Objetos de diferentes classes podem ser tratados de forma uniforme se implementarem a mesma interface.
- **Flexibilidade**: Facilita a manutenção e evolução do código.

> **Resumo:** Interfaces são contratos que garantem que as classes que as implementam forneçam determinados comportamentos, promovendo organização e reutilização de código.
