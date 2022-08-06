package br.com.wepdev.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorSenha {
	
	public static void main(String[] args) {
		
		// Gera senha encodada
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println("Senha 1 -> " + encoder.encode("@ngul@r0"));
		System.out.println("Senha 2 -> " + encoder.encode("admin"));
	}

}
