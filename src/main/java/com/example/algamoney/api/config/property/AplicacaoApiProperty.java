package com.example.algamoney.api.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("algamoney")
@Getter
public class AplicacaoApiProperty {
	
	private Seguranca seguranca = new Seguranca();
	private Endereco endereco = new Endereco();
	private Mail mail = new Mail();

	@Getter
	@Setter
	public static class Seguranca {
		
		private boolean enableHttps;
	}
	
	@Getter
	@Setter
	public static class Endereco {
		
		private String originPermitida;
	}
	
	@Getter
	@Setter
	public static class Mail {
		private String host;
		private Integer port;
		private String username;
		private String password;
	}
}
