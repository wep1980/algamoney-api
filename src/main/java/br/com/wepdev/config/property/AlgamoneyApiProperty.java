package br.com.wepdev.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties("algamoney") // nome dessa configuracao, e necessario uma dependencia no pom.xml
public class AlgamoneyApiProperty {

	private String originPermitida = "http://localhost:8000";

	private final Seguranca seguranca = new Seguranca();




	@Getter
	@Setter
	public static class Seguranca {

		private boolean enableHttps; // por padrao esse valor ja e false

	}


}
