package br.com.controlepartidascs;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@SpringBootApplication(scanBasePackages= {"br.com.controlepartidascs.controller", "br.com.controlepartidascs.service"})
public class Configuracao {

	public static void main(String[] args) {
		SpringApplication.run(Configuracao.class, args);
	}

	@Bean
	public DataSource dataSource() { // Configuracao para inicializar conexao com banco
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/teste_keep_it_simple");
		dataSource.setUsername("root");
		dataSource.setPassword("1234");
		
		return dataSource;
	}

}
