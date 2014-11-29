package com.jsembly.funcoes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.jsembly.extras.Utilidades;
import com.jsembly.main.ArraysLists;

public class Cores {
	public static Color gerarCores(){
		Random rand = new Random();
		float r = rand.nextFloat();
		float g = rand.nextFloat();
		float b = rand.nextFloat();
		
		Color randomColor = new Color(r, g, b);
		return randomColor;
	}
	
	public static void buscarCores(String temp,JTextPane linguagemMIPS){
	ArrayList<String> linhasLidas = Utilidades.LerArquivo(temp);
	for(String linhas : linhasLidas){
		
		Pattern operador = Pattern.compile("\\b\\w{1,4} [$]|\\b\\w{1,4} \\d",Pattern.CASE_INSENSITIVE);
		Pattern registrador = Pattern.compile("[$]([a-z][0-9])|[$]([a-z][a-z])", Pattern.CASE_INSENSITIVE);
		Pattern label = Pattern.compile("\\b\\w+[:]", Pattern.CASE_INSENSITIVE);
		Pattern comentario = Pattern.compile("[#].*.", Pattern.CASE_INSENSITIVE);
		
		Matcher matcher = operador.matcher(linhas);
		Matcher matcher2 = registrador.matcher(linhas);
		Matcher matcher3 = label.matcher(linhas);
		Matcher matcher4 = comentario.matcher(linhas);

		// Busca de Operadores
		if(matcher.find()){
			//System.out.println("Achei um: "+matcher.group().substring(matcher.start(),matcher.end()-2));
			for(int i = 0; i < ArraysLists.operadores.size(); i++){
				if(matcher.group().toString().substring(matcher.start(),matcher.end()-2).toLowerCase().equals(ArraysLists.operadores.get(i).toString())){
					SimpleAttributeSet corOperador = new SimpleAttributeSet();
					StyleConstants.setBold(corOperador, false);
					StyleConstants.setForeground(corOperador, new Color(160,43,4));
					linguagemMIPS.getStyledDocument().setCharacterAttributes(matcher.start(), matcher.group().length()-2, corOperador, true);
				}
			}
		}
   
		// Busca de Label
		if(matcher3.find()) {
			SimpleAttributeSet corLabel = new SimpleAttributeSet(); 
			StyleConstants.setBold(corLabel, true);
			StyleConstants.setForeground(corLabel, new Color(238,84,0));
			//System.out.println();
			linguagemMIPS.getStyledDocument().setCharacterAttributes(matcher3.start()-2, matcher3.group().length(), corLabel, true);
		}
    
			// Busca de Comentários
		if(matcher4.find()) {
			SimpleAttributeSet corComentario = new SimpleAttributeSet(); 
			StyleConstants.setBold(corComentario, true);
			StyleConstants.setForeground(corComentario, new Color(84,84,84));
			linguagemMIPS.getStyledDocument().setCharacterAttributes(matcher4.start()-1, matcher4.group().length(), corComentario, true);
		}
   
	// Busca de ArraysLists.registradores
	while(matcher2.find()) {
		for(int j = 0; j < ArraysLists.registradores.size(); j++){
			if(matcher2.group().toLowerCase().equals(ArraysLists.registradores.get(j).toString())){
				SimpleAttributeSet corRegistrador = new SimpleAttributeSet();
				StyleConstants.setBold(corRegistrador, false);
				StyleConstants.setForeground(corRegistrador, new Color(60,38,150));
				linguagemMIPS.getStyledDocument().setCharacterAttributes(matcher2.start(), matcher2.group().length(), corRegistrador, true);
				j = ArraysLists.operadores.size();
			}
		}
	}
	}
	}
}
