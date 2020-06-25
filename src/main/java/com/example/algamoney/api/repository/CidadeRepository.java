package com.example.algamoney.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.algamoney.api.model.Cidade;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long>{

	List<Cidade> findByEstadoCodigo(Long codigo);
}
