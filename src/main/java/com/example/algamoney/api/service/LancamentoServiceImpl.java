package com.example.algamoney.api.service;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.dto.LancamentoEstatisticaPessoa;
import com.example.algamoney.api.mail.Mailer;
import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.model.Usuario;
import com.example.algamoney.api.repository.LancamentoRepository;
import com.example.algamoney.api.repository.PessoaRepository;
import com.example.algamoney.api.repository.UsuarioRepository;
import com.example.algamoney.api.service.exception.PessoaInexistenteOuInativaException;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class LancamentoServiceImpl implements LancamentoService{
	
	private static final String DESTINATARIOS = "ROLE_PESQUISAR_LANCAMENTO";
	
	private static final Logger logger = LoggerFactory.getLogger(LancamentoServiceImpl.class);

	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private Mailer mailer;
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public List<Lancamento> listar() {
		return lancamentoRepository.findAll();
	}

	@Override
	public Lancamento buscar(Long codigo) {
		Lancamento lancamento = buscarPeloCodigo(codigo);
		return lancamento;
	}
	
	@Override
	public Lancamento salvar(Lancamento lancamento) {
		pessoaInativaOuInexistente(lancamento.getPessoa().getCodigo());
		return lancamentoRepository.save(lancamento);
	}
	
	@Override
	public void remover(Long codigo) {
		Lancamento lancamento = buscarPeloCodigo(codigo);
		lancamentoRepository.delete(lancamento);
	}
	
	@Override
	public Lancamento atualizar(Long codigo, Lancamento lancamento) {
		Lancamento lancamentoAtualizar = buscarPeloCodigo(codigo);
		pessoaInativaOuInexistente(lancamento.getPessoa().getCodigo());
		BeanUtils.copyProperties(lancamento, lancamentoAtualizar, "codigo");
		return lancamentoRepository.save(lancamentoAtualizar);
	}
	
	@Override
	public byte[] relatorioPorPessoa(LocalDate inicio, LocalDate fim) throws Exception {
		List<LancamentoEstatisticaPessoa> dados = lancamentoRepository.porPessoa(inicio, fim);

		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("DT_INICIO", Date.valueOf(inicio));
		parametros.put("DT_FIM", Date.valueOf(fim));
		parametros.put("REPORT LOCALE", new Locale("pt", "BR"));
		
		InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/lancamentos-por-pessoa.jasper");
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros, new JRBeanCollectionDataSource(dados));
		return JasperExportManager.exportReportToPdf(jasperPrint);
	}
	
	@Override
	@Scheduled(cron = "0 0 6 * * *")
//	@Scheduled(fixedDelay = 1000 * 60 *30)
	public void avisarSobreLancamentosVencidos() {
		if (logger.isDebugEnabled()) {			
			logger.debug("Preparando envio de e-mails de aviso de lançamentos vencidos");
		}
		List<Lancamento> vencidos = 
					lancamentoRepository.findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());
		if (vencidos .isEmpty()) {
			logger.info("Sem lançamentos vencidos para aviso.");
			return;
		}
		
		logger.info("existem {} lançamentos vencidos.", vencidos.size());
		
		List<Usuario> destinatarios = usuarioRepository.findByPermissoesDescricao(DESTINATARIOS);
		
		if (destinatarios.isEmpty()) {
			logger.warn("Existem lançamentos vencidos, mas o sistema não encontro destinatarios");
			return;
		}
		
		mailer.avisarSobreLancamentosVencidos(vencidos, destinatarios);
		
		logger.info("Envio de e-mail de aviso concluído");
	}

	private Lancamento buscarPeloCodigo(Long codigo) {
		Optional<Lancamento> lancamento = lancamentoRepository.findById(codigo);
		
		if(!lancamento.isPresent()) {
			throw new EmptyResultDataAccessException(1);
		}
		
		return lancamento.get();
	}

	private void pessoaInativaOuInexistente(Long codigo) {
		Optional<Pessoa> pessoa = pessoaRepository.findById(codigo);
		
		if(!pessoa.isPresent() || !pessoa.get().getAtivo()) {
			throw new PessoaInexistenteOuInativaException();
		}
	}
	
}
