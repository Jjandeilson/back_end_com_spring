package com.example.algamoney.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.algamoney.api.config.property.AplicacaoApiProperty;

@EnableConfigurationProperties(AplicacaoApiProperty.class)
@SpringBootApplication
public class AlgamoneyApiAtualizadoApplication {

	// método que inica aplicação executando a classe responsevél por inicar o projeto
	public static void main(String[] args) {
		SpringApplication.run(AlgamoneyApiAtualizadoApplication.class, args);
	}

}
