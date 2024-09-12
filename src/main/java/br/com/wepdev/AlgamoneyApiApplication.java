package br.com.wepdev;

import br.com.wepdev.config.property.AlgamoneyApiProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

// Habilita o comando algamoney.seguranca.enable-https para ser setado no properties
//@EnableConfigurationProperties(value = AlgamoneyApiProperty.class) // nao mais necessario colocar essa config
@SpringBootApplication
public class AlgamoneyApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(AlgamoneyApiApplication.class, args);
	}


	/**
	 * Metodo que libera CORs de todos os endPoints somente da origem http://localhost:8080
	 * @return
	 */
//	@Bean
//	public WebMvcConfigurer corsConfigure(){
//		return new WebMvcConfigurerAdapter() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//			   registry.addMapping("/*").allowedOrigins("http://localhost:8080");
//			}
//		};
//	}

}
