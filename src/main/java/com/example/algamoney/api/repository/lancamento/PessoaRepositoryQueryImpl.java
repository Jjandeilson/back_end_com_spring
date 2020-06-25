package com.example.algamoney.api.repository.lancamento;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.model.Pessoa_;
import com.example.algamoney.api.repository.filter.PessoaFilter;

@Service
public class PessoaRepositoryQueryImpl implements PessoaRepositoryQuery{

	@PersistenceContext
	private EntityManager manager;

	@Override
	public Page<Pessoa> paginacao(PessoaFilter pessoaFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Pessoa> criteria = builder.createQuery(Pessoa.class);
		Root<Pessoa> root = criteria.from(Pessoa.class);
		
		Predicate[] predicates = criarRestricoes(builder, root, pessoaFilter);
		criteria.where(predicates);
		
		TypedQuery<Pessoa> query = manager.createQuery(criteria);
		adicionarRestricaoDaPagina(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(pessoaFilter));
	}

	private Predicate[] criarRestricoes(CriteriaBuilder builder, Root<Pessoa> root, PessoaFilter pessoaFilter) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(!StringUtils.isEmpty(pessoaFilter.getNome())) {
			predicates.add(builder.like(builder.lower(root.get(Pessoa_.nome)), "%" + pessoaFilter.getNome() + "%"));
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	private void adicionarRestricaoDaPagina(TypedQuery<Pessoa> query, Pageable pageable) {
		int primeiraPagina = pageable.getPageNumber();
		int totalPorPagina = pageable.getPageSize();
		int primeiroItemDaPagina = primeiraPagina * totalPorPagina;
		
		query.setFirstResult(primeiroItemDaPagina);
		query.setMaxResults(totalPorPagina);
	}

	private Long total(PessoaFilter pessoaFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Pessoa> root = criteria.from(Pessoa.class);
		
		Predicate[] predicates = criarRestricoes(builder, root, pessoaFilter);
		criteria.where(predicates);
		
		criteria.select(builder.count(root));
		
		return manager.createQuery(criteria).getSingleResult();
	}
}
