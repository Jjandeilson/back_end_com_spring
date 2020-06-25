package com.example.algamoney.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Categoria;
import com.example.algamoney.api.repository.CategoriaRepository;

@Service
public class CategoriaServiceImpl implements CategoriaService{

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Override
	public List<Categoria> listar() {
		return categoriaRepository.findAll();
	}

	@Override
	public Categoria salvar(Categoria categoria) {
		return categoriaRepository.save(categoria);
	}

	@Override
	public Categoria buscar(Long codigo) {
		Categoria categoria = buscarPeloCodigo(codigo);
		return categoria;
	}
	
	@Override
	public void remover(Long codigo) {
		Categoria categoria = buscarPeloCodigo(codigo);
		categoriaRepository.delete(categoria);
	}
	
	@Override
	public Categoria atualizar(Long codigo, Categoria categoria) {
		Categoria categoriaAtualizar = buscarPeloCodigo(codigo);
		BeanUtils.copyProperties(categoria, categoriaAtualizar, "codigo");
		return categoriaRepository.save(categoriaAtualizar);
	}

	private Categoria buscarPeloCodigo(Long codigo) {
		Optional<Categoria> categoria = categoriaRepository.findById(codigo);
		
		if(!categoria.isPresent()) {
			throw new EmptyResultDataAccessException(1);
		}
		
		return categoria.get();
	}
	
}
