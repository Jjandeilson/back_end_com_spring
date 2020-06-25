package com.example.algamoney.api.resource;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.api.event.AplicacaoEvent;
import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.PessoaRepository;
import com.example.algamoney.api.repository.filter.PessoaFilter;
import com.example.algamoney.api.service.PessoaService;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {
	
	@Autowired
	private PessoaService pessoaService;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;

//	@GetMapping
//	public List<Pessoa> listar() {
//		return pessoaService.listar();
//	}
	
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
	public Page<Pessoa> paginacao(PessoaFilter pessoaFilter, Pageable pageable) {
		return pessoaRepository.paginacao(pessoaFilter, pageable);
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
	public ResponseEntity<Pessoa> salvar(@RequestBody @Valid Pessoa pessoa, HttpServletResponse response) {
		Pessoa pessoaSalva = pessoaService.salvar(pessoa);
		publisher.publishEvent(new AplicacaoEvent(this, pessoaSalva.getCodigo(), response));
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
	}
	
	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
	public ResponseEntity<Pessoa> buscar(@PathVariable Long codigo) {
		Pessoa pessoa = pessoaService.buscar(codigo);
		return ResponseEntity.ok(pessoa);
	}
	
	@DeleteMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_REMOVER_PESSOA') and #oauth2.hasScope('write')")
	public ResponseEntity<Void> remover(@PathVariable Long codigo) {
		pessoaService.remover(codigo);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_REMOVER_PESSOA') and #oauth2.hasScope('write')")
	public ResponseEntity<Pessoa> atualizar(@PathVariable Long codigo, @RequestBody @Valid Pessoa pessoa) {
		Pessoa pessoaAtualizado = pessoaService.atualizar(codigo, pessoa);
		return ResponseEntity.ok(pessoaAtualizado);
	}
	
	@PutMapping("/{codigo}/ativo")
	@PreAuthorize("hasAuthority('ROLE_REMOVER_PESSOA') and #oauth2.hasScope('write')")
	public ResponseEntity<Void> atualizarPropriedadeAtivo(@PathVariable Long codigo, @RequestBody @Valid Boolean ativo) {
		pessoaService.atualizarPropriedadeAtivo(codigo, ativo);
		return ResponseEntity.noContent().build();
	}
}
