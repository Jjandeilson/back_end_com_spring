package com.example.algamoney.api.service;

import java.time.LocalDate;
import java.util.List;

import com.example.algamoney.api.model.Lancamento;

public interface LancamentoService {

	List<Lancamento> listar();
	Lancamento buscar(Long codigo);
	Lancamento salvar(Lancamento lancamento);
	void remover(Long codigo);
	Lancamento atualizar(Long codigo, Lancamento lancamento);
	byte[] relatorioPorPessoa(LocalDate inicio, LocalDate fim) throws Exception;
	void avisarSobreLancamentosVencidos();
}
