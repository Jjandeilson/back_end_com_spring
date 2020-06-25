package com.example.algamoney.api.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "cidade")
public class Cidade {

	@Id
	@EqualsAndHashCode.Include
	private Long codigo;
	
	private String nome;
	
	@ManyToOne
	@JoinColumn(name = "codigo_estado")
	private Estado estado;
}
