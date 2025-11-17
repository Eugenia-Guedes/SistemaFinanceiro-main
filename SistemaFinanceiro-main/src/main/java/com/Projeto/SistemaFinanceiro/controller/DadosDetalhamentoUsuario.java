package com.Projeto.SistemaFinanceiro.controller;

import com.Projeto.SistemaFinanceiro.domain.usuario.Usuario;

public record DadosDetalhamentoUsuario(Long id, String login) {

    public DadosDetalhamentoUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getLogin());
    }
}
