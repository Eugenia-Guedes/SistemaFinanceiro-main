package com.Projeto.SistemaFinanceiro.controller;

import com.Projeto.SistemaFinanceiro.domain.usuario.Usuario;
import com.Projeto.SistemaFinanceiro.domain.usuario.UsuarioRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("cadastros")
@SecurityRequirement(name = "bearer-key")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroUsuario dados,
                                   UriComponentsBuilder uriBuilder) {
        // Verificar se o login já está cadastrado
        if (repository.findByLogin(dados.login()) != null) {
            return ResponseEntity.badRequest().body("Login já cadastrado.");
        }
        var senhaCriptografada = passwordEncoder.encode(dados.senha());

        // Criar o usuário com o login e senha criptografada
        // foi criado um construtor en Usuario
        var usuario = new Usuario(dados.login(), senhaCriptografada);
        repository.save(usuario);
        // Criar o URI para o novo usuário
        var uri = uriBuilder.path("/usuarios/{id}")
                .buildAndExpand(usuario.getId()).toUri();

        // Retornar resposta com status 201 e os dados do usuário criado
        return ResponseEntity.created(uri).body(new DadosDetalhamentoUsuario(usuario));
    }
}


