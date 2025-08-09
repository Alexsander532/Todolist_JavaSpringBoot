package com.projetospring.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.projetospring.todolist.user.IUserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component // Define a classe como um componente do Spring
public class FilterTaskAuth extends OncePerRequestFilter{ // Extende a classe OncePerRequestFilter para garantir que o filtro seja executado uma vez por requisição

    @Autowired
    private IUserRepository userRepository; // Injeção de dependência do repositório de usuários



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
       
                var servletPath = request.getServletPath(); // Pega o caminho do servlet
                if (servletPath.startsWith("/tasks/")) {

        //Pegar a autenticação (usuario e senha)
        var authorization = request.getHeader("Authorization"); //Pega o cabeçalho da requisição
        if (authorization == null) {
            filterChain.doFilter(request, response);
            return;
        }

        var authEncoded = authorization.substring("Basic".length()).trim(); // Pega a string após o "Basic "
        byte[] authDecode = Base64.getDecoder().decode(authEncoded); // Decodifica a string Base64
        var authString = new String(authDecode); // Converte o array de bytes para string
        String[] credentials = authString.split(":");
        String username = credentials[0];
        String password = credentials[1];
        System.out.println("Usuário: " + username);
        System.out.println("Senha: " + password);

        //Validar usuário
        var user = this.userRepository.findByUsername(username); // Busca o usuário no repositório
        if (user == null) {
            response.sendError(401); // Retorna erro 401 se o usuário não for encontrado
        } else {
            //Validar senha
            var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
            if (!passwordVerify.verified) {
                    response.sendError(401); // Senha inválida
                }
                //Segue viagem
                request.setAttribute("idUser", user.getId()); // Define o ID do usuário no atributo da requisição
                filterChain.doFilter(request, response); // Continua a cadeia de filtros
                }    
    } else{
        // Se não for uma rota de tarefa, apenas continua a cadeia de filtros
        filterChain.doFilter(request, response);
    }
  }
}
