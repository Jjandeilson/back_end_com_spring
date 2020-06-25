package com.example.algamoney.api.service;

import java.util.List;

import com.example.algamoney.api.model.Categoria;

public interface CategoriaService {

	List<Categoria> listar();
	Categoria salvar(Categoria categoria);
	Categoria buscar(Long codigo);
	void remover(Long codigo);
	Categoria atualizar(Long codigo, Categoria categoria);
}
