package com.jsembly.main;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import com.jsembly.extras.Utilidades;
import com.jsembly.mips.Instrucao;
import com.jsembly.mips.Registrador;


public class Montador {
	/**
	 * op - Contador definido do Operador
	 * re - Contador definido de Registradores
	 * lb - Contador definido de Labels
	 **/
	public static int linhaAtual;
	public static String enderecoLabel = new String();
	public static Pattern operador = Pattern.compile("\\b[a-zA-Z]{1,5}[ ]{1,9999}", Pattern.CASE_INSENSITIVE);	
	public static Pattern registrador = Pattern.compile("[$]\\w{1,4}", Pattern.CASE_INSENSITIVE);	
	public static Pattern endereco = Pattern.compile("\\b[,]{0,1}[ ]{0,999}\\d+\\b|"
													+ "\\b[,]{0,1}[ ]{0,999}[-]\\d+\\b", Pattern.CASE_INSENSITIVE);	
	public static Pattern label = Pattern.compile("\\bj(|al)\\b[ ]{0,999}\\w+|"
												+ "\\b[,][ ]{0,999}\\w+\\b", Pattern.CASE_INSENSITIVE);	
	public static Pattern labelDefinida = Pattern.compile("\\b\\w+[:]", Pattern.CASE_INSENSITIVE);
	
	public Montador(){
		linhaAtual = 0;
		Janela.Reiniciar(); // Limpa toda a atividade na Janela (Arrays, Mem�ria, Registradores etc)
		ArraysLists.instrucoes.clear();
		
		ArrayList<String> linhasLidas = Utilidades.LerArquivo(Janela.temp.getAbsolutePath());
		if(linhasLidas.size() != 0){
			for(String linha : linhasLidas){
				// Contador de Linhas
				linhaAtual++;
				ArraysLists.regEncontrados.clear();
			
				BuscarLabelDefinida(linha);
				BuscarOperador(linha);
			}
		} else {
			JOptionPane.showMessageDialog(null,
				"<html>"
				+ "Prezado usu�rio, nenhum comando v�lido foi encontrado.<br>"
				+ "<i>Favor revisar as informa��es digitadas.</i>"
				+ "</html>",
				"U�, cad� teu c�digo?",
			JOptionPane.ERROR_MESSAGE);
		}
		
	}

	/**
	 *	BuscarOperador(String linha)
	 *		Procura na linha um operador v�lido
	 *		e inicia a busca por registradores,labels,endere�os e etc.
	 *		Caso n�o seja encontrado nenhum operador v�lido,
	 *		umas mensagem � disparada para o usu�rio;
	 *	@author insidemybrain
	 **/
	public void BuscarOperador(String linha){
		Matcher mtchOp = operador.matcher(linha);
		if(mtchOp.find()){
			for(int op = 0; op < ArraysLists.operadores.size(); op++){
				// O OPERADOR exite?
				// 	Se sim, buscamos todos os registradores, endere�os & labels
				//	Caso contr�rio, uma mensagem � enviada ao Usu�rio.
				if(mtchOp.group().replaceAll("[\\^ ]", "").toLowerCase().equals(ArraysLists.operadores.get(op).toString())){
					if(ArraysLists.operadores.get(op).getId() >= 999){
		  				JOptionPane.showMessageDialog(
		  						null,
		  						"<html>"
		  						+ "Lamentamos, mas o operador <b style='font-size:9px; color: red;'>"+ArraysLists.operadores.get(op)+"</b>, localizado na linha <b style='font-size:9px; color: red;'>"+linhaAtual+"</b>,<br>"
		  						+ " n�o poder� ser executado.<br>"
		  						+ "O mesmo ainda n�o foi completamente habilitado.<br>"
		  						+ "<i>Tente utiliz�-lo na pr�xima revis�o do sistema.</i>"
		  						+ "</html>",
		  						"Operador n�o implementado",
		  						JOptionPane.ERROR_MESSAGE);
		  				break;
    		  		}
					
					BuscarRegistradores(linha);
					if(ArraysLists.regEncontrados.size() > 0) if(Verificar$Zero(linha)) break;
					BuscarEnderecosOperador(linha);
					BuscarLabelOperador(linha);
					VerificarTipoOP(op);
				}
			}
		}
	}

	/**
	 *	VerificarTipoOP(int op)
	 *		Verifica qual o tipo da Opera��o encontrada
	 *		e chama o m�todo correto para cada instru��o
	 *	 @author insidemybrain
	 **/
	@SuppressWarnings("unused")
	public void VerificarTipoOP(int op){
		switch(ArraysLists.operadores.get(op).getTipoIntrucao()){
		// TIPO I
		case 0:
			switch(ArraysLists.operadores.get(op).getId()){
				// LUI
				case 9:
					Instrucao lui = new Instrucao(
							ArraysLists.operadores.get(op),		// Operador
							ArraysLists.regEncontrados.get(0),	// Primeiro Registrador Encontrado
							enderecoLabel,						// Label|Endere�o|Valor Encontrado
							linhaAtual);						// C�digo da Linha
				break;
				default:
					Instrucao tipoI = new Instrucao(
							ArraysLists.operadores.get(op),		// Operador
							ArraysLists.regEncontrados.get(0),	// Primeiro Registrador Encontrado
							ArraysLists.regEncontrados.get(1),	// Segundo Registrador Encontrado
							enderecoLabel,						// Label|Endere�o|Valor Encontrado
							linhaAtual);						// C�digo da Linha
				break;
			}
			break;
			
		// TIPO J
		case 1:
			Instrucao tipoJ = new Instrucao(
					ArraysLists.operadores.get(op),		// Operador
					enderecoLabel,						// Label|Endere�o Encontrado
					linhaAtual);						// C�digo da Linha
			break;
			
		// TIPO R
		case 2:
			switch(ArraysLists.operadores.get(op).getId()){
				// SLL & SRL
				case 15:
				case 16:
					Instrucao sllSRL = new Instrucao(
							ArraysLists.operadores.get(op),		// Operador
							ArraysLists.regEncontrados.get(0),	// Primeiro Registrador Encontrado
							ArraysLists.regEncontrados.get(1),	// Segundo Registrador Encontrado
							enderecoLabel,						// Valor encontrado
							linhaAtual);						// C�digo da Linha
					break;
				// MFHI & MFLO
				case 102:
				case 103:
					Instrucao mfHILO = new Instrucao(
							ArraysLists.operadores.get(op),		// Operador
							ArraysLists.regEncontrados.get(0),	// Primeiro Registrador Encontrado
							linhaAtual);						// C�digo da Linha
					break;
				// Tipo R (default)
				default:
					Instrucao tipoR = new Instrucao(
							ArraysLists.operadores.get(op),		// Operador
							ArraysLists.regEncontrados.get(0),	// Primeiro Registrador Encontrado
							ArraysLists.regEncontrados.get(1),	// Segundo Registrador Encontrado
							ArraysLists.regEncontrados.get(2),	// Terceiro Registrador Encontrado
							linhaAtual);						// C�digo da Linha
					break;
			}
			break;
			
		// TIPO I (LOAD/STORE)
		case 3:
			Instrucao tipoI_LoadStore = new Instrucao(
					ArraysLists.operadores.get(op),		// Operador
					ArraysLists.regEncontrados.get(0),	// Primeiro Registrador Encontrado
					enderecoLabel,						// Endere�o Encontrado
					ArraysLists.regEncontrados.get(1),	// Segundo Registrador Encontrado
					linhaAtual);						// C�digo da Linha
			break;
			
		// TIPO R (JUMP/BRANCH)
		case 4:
			Instrucao tipoR_JumpBranch = new Instrucao(
					ArraysLists.operadores.get(op),		// Operador
					ArraysLists.regEncontrados.get(0),	// Primeiro Registrador Encontrado
					linhaAtual);						// C�digo da Linha
			break;
			
		// TIPO I (JUMP/BRANCH)
		case 5:
			switch(ArraysLists.operadores.get(op).getId()){
				default:
					Instrucao tipoI = new Instrucao(
							ArraysLists.operadores.get(op),		// Operador
							ArraysLists.regEncontrados.get(0),	// Primeiro Registrador Encontrado
							ArraysLists.regEncontrados.get(1),	// Segundo Registrador Encontrado
							enderecoLabel,						// Label|Endere�o Encontrado
							linhaAtual);						// C�digo da Linha
					break;
				}
			break;
	}
	}
	/**
	 *	BuscarLabelDefinida(String linha)
	 *		Procura na linha Labels que foram definidas
	 *		e as adiciona em um ArrayList.
	 *		Verificando tamb�m, se as mesmas j� foram
	 *		adicionadas anteriormente e alertando o usu�rio;
	 *	 @author insidemybrain
	 **/
	public void BuscarLabelDefinida(String linha){
		Matcher mtchLblDef = labelDefinida.matcher(linha);
		if(mtchLblDef.find()){
			String lbl = mtchLblDef.group().substring(mtchLblDef.start(),mtchLblDef.end()-1);
			for(int i = 0; i < ArraysLists.arrLabel.size(); i ++){
				if(ArraysLists.arrLabel.get(i).equals(lbl)){
					JOptionPane.showMessageDialog(null,
						"<html>"
						+ "Prezado usu�rio, a label: <b style='color:red;'>"+lbl+"</b> j� foi definida anteriormente no sistema.<br>"
						+ "<i>Favor revisar as informa��es digitadas.</i>"
						+ "</html>",
						"Label duplicada",
						JOptionPane.ERROR_MESSAGE);
					ArraysLists.arrLabel.remove(lbl);
				}
			}
			ArraysLists.arrLabel.add(lbl);
			Janela.dtmExec.addRow(new Object[]{"","",lbl,lbl});
		}
	}

	/**
	 *	BuscarRegistradores(String linha)
	 *		Ap�s ser encontrado um Operador, � realizada uma busca
	 *		por todos os registradores encontrados na linha em quest�o;
	 *	 @author insidemybrain
	 **/

	public void BuscarRegistradores(String linha){
		Matcher mtchReg = registrador.matcher(linha);
		while(mtchReg.find()){
			// Adiciona REGISTRADOR ao ArrayList de Registradores Encontrados
			for(int re = 0; re < ArraysLists.registradores.size(); re++){
				if(mtchReg.group().toLowerCase().equals(ArraysLists.registradores.get(re).toString())){
					ArraysLists.regEncontrados.add(ArraysLists.registradores.get(re));
				}
			}
			
		}
	}

	/**
	 *	Verificar$Zero(String linha)
	 *		Verificar se 1� Registrador encontrado � o $zero
	 *		se for verdadeiro, a linha de c�digo � ignorada e � enviada uma mensagem ao usu�rio;
	 *	 @author insidemybrain
	 **/
	public boolean Verificar$Zero(String linha){
		if(ArraysLists.regEncontrados.get(0).equals(Registrador.$zero)){
  			JOptionPane.showMessageDialog(null,
					"<html>"
					+ "Por padr�o o registrador <b style='color:red;'>"+Registrador.$zero+"</b> possui um valor fixo e imut�vel.<br>"
					+ "<i>Linha de c�digo ignorada!</i>"
					+ "</html>",
					"Aten��o!",
					JOptionPane.WARNING_MESSAGE);
  			return true;
  		}
		return false;
	}

	/**
	 *	BuscarEnderecosOperador(String linha)
	 *		Busca o valor inteiro digitado ap�s um operador;
	 *	 @author insidemybrain
	 **/
	public void BuscarEnderecosOperador(String linha){
		Matcher mtchELN = endereco.matcher(linha);
		if(mtchELN.find()){
			enderecoLabel = mtchELN.group().toLowerCase().substring(1).replaceAll("[\\^ ]", "");
			
		}
	}

	/**
	 *	BuscarLabelOperador(String linha)
	 *		Busca a label digitada ap�s um operador;
	 *	 @author insidemybrain
	 **/
	public void BuscarLabelOperador(String linha){
		Matcher mtchLbl = label.matcher(linha);
		if(mtchLbl.find()){
			// Label ap�s REGISTRADORES
			if(mtchLbl.group().charAt(0) == ','){
				enderecoLabel = mtchLbl.group().substring(1).replaceAll("[\\^ ]", "");
			}
			// Label ap�s J/JAL
			else if(mtchLbl.group().charAt(0) == 'j'){
				if(mtchLbl.group().charAt(1) == 'a'){
					enderecoLabel = mtchLbl.group().substring(3).replaceAll("[\\^ ]", "");
				} else {
					enderecoLabel = mtchLbl.group().substring(1).replaceAll("[\\^ ]", "");
				}
			}
			
		}
	}
}
