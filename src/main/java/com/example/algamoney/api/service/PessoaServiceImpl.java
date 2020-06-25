package com.example.algamoney.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.PessoaRepository;

@Service
public class PessoaServiceImpl implements PessoaService{

	@Autowired
	private PessoaRepository pessoaRepository;

	@Override
	public List<Pessoa> listar() {
		return pessoaRepository.findAll();
	}

	@Override
	public Pessoa salvar(Pessoa pessoa) {
		pessoa.getContatos().forEach(c -> c.setPessoa(pessoa));
		return pessoaRepository.save(pessoa);
	}

	@Override
	public Pessoa buscar(Long codigo) {
		Pessoa pessoa = buscarPeloCodigo(codigo);
		return pessoa;
	}
	
	@Override
	public void remover(Long codigo) {
		Pessoa pessoa = buscarPeloCodigo(codigo);
		pessoaRepository.delete(pessoa);
	}
	
	@Override
	public Pessoa atualizar(Long codigo, Pessoa pessoa) {		
		Pessoa pessoaAtualizar = buscarPeloCodigo(codigo);
		
		pessoaAtualizar.getContatos().clear();
		pessoaAtualizar.getContatos().addAll(pessoa.getContatos());
		pessoaAtualizar.getContatos().forEach(c -> c.setPessoa(pessoaAtualizar));
		
		BeanUtils.copyProperties(pessoa, pessoaAtualizar, "codigo", "contatos");
		return pessoaRepository.save(pessoaAtualizar);
	}
	
	@Override
	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
		Pessoa pessoa = buscarPeloCodigo(codigo);
		pessoa.setAtivo(ativo);
		pessoaRepository.save(pessoa);
	}


	private Pessoa buscarPeloCodigo(Long codigo) {
		Optional<Pessoa> pessoa = pessoaRepository.findById(codigo);
		
		if(!pessoa.isPresent()) {
			throw new EmptyResultDataAccessException(1);
		}
		
		return pessoa.get();
	}

}
