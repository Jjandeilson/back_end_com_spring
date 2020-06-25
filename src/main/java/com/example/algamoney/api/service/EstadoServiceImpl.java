package com.example.algamoney.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Estado;
import com.example.algamoney.api.repository.EstadoRepository;

@Service
public class EstadoServiceImpl implements EstadoService{

	@Autowired
	private EstadoRepository estadoRepository;

	@Override
	public List<Estado> listar() {
		return estadoRepository.findAll();
	}
}
