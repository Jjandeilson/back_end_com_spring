package com.example.algamoney.api.repository.lancamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.filter.PessoaFilter;

public interface PessoaRepositoryQuery {

	Page<Pessoa> paginacao(PessoaFilter pessoaFilter, Pageable pageable);
}
