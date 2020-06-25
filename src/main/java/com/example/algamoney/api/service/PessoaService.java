package com.example.algamoney.api.service;

import java.util.List;

import com.example.algamoney.api.model.Pessoa;

public interface PessoaService {

	List<Pessoa> listar();
	Pessoa salvar(Pessoa pessoa);
	Pessoa buscar(Long codigo);
	void remover(Long codigo);
	Pessoa atualizar(Long codigo, Pessoa pessoa);
	void atualizarPropriedadeAtivo(Long codigo, Boolean ativo);
}
