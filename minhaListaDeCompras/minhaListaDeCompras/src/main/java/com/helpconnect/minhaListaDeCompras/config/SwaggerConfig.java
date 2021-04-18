package com.helpconnect.minhaListaDeCompras.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {
	
	/* INFORMA PARA O SWEGGER, ONDE ESTAO LOCALIZADOS OS CONTROLLERS DA API PARA A REALIZACACAO DA DOCUMENTACAO / MAPEAMENTO DAS FUNCIONALIDADES*/
	@Bean
	public Docket docket() {
		return new Docket(DocumentationType.SWAGGER_2)
			.select()
			.apis( RequestHandlerSelectors.basePackage
			("com.helpconnect.minhaListaDeCompras.controller") )
			.paths(PathSelectors.any())
			.build()
			.apiInfo(apiInfo());
		
	}
	
	/* INFORMACOES DA API */
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
			.title("Minha lista de compras")
			.description("API para compor uma aplicacao de orcamento e criacao de lista de compras")
			.version("1.0")
			.contact(contact())
			.build();
		
	}
	
	/* APRESENTA NA DOCUMENTACAO OS DADOS DO DESENVOLVEDOR/DESENVOLVEDORES DA API */
	private Contact contact() {
		return new	 Contact("Kevin Alec Neri Lazzarotto",
		"https://github.com/Clamant96",
		"Estudante em ADS | Desenvolvedor Web Java Junior Full Stack");
		
	}
}
