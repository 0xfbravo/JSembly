package com.jsembly.extras;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Utilidades {
	public static ArrayList<String> LerArquivo(String caminhoArquivo){
		File arquivo = new File(caminhoArquivo);
		ArrayList<String> linhasLidas = new ArrayList<>();
		String p;
		try {
		/*
		 * 	Ler o Arquivo
		 *    com Informações Gerais
		 */
		if (!arquivo.exists()) { arquivo.createNewFile(); }
		InputStream arquivoLer = new FileInputStream(arquivo.getAbsoluteFile());
		InputStreamReader arq = new InputStreamReader(arquivoLer);
		BufferedReader br = new BufferedReader(arq);
		p = br.readLine();
		while (p != null) {
			linhasLidas.add(p);
			p = br.readLine();
		}
		arquivoLer.close();
		arq.close();
		br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return linhasLidas;
	}	
	/* Buscar Ícones dentro do Projeto */
	public static ImageIcon buscarIcone(String caminho){
		ImageIcon a = new ImageIcon(Utilidades.class.getClassLoader().getResource(caminho));
		return a;
	}
}
