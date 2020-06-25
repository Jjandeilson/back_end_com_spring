package com.example.algamoney.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Cidade;
import com.example.algamoney.api.repository.CidadeRepository;

@Service
public class CidadeServiceImpl implements CidadeService{

	@Autowired
	private CidadeRepository cidadeRepository;

	@Override
	public List<Cidade> listar() {
		return cidadeRepository.findAll();
	}
	
	
}
