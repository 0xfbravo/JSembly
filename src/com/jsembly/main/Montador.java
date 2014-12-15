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
		Janela.Reiniciar(); // Limpa toda a atividade na Janela (Arrays, Memória, Registradores etc)
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
				+ "Prezado usuário, nenhum comando válido foi encontrado.<br>"
				+ "<i>Favor revisar as informações digitadas.</i>"
				+ "</html>",
				"Ué, cadê teu código?",
			JOptionPane.ERROR_MESSAGE);
		}
		
	}

	/**
	 *	BuscarOperador(String linha)
	 *		Procura na linha um operador válido
	 *		e inicia a busca por registradores,labels,endereços e etc.
	 *		Caso não seja encontrado nenhum operador válido,
	 *		umas mensagem é disparada para o usuário;
	 *	@author insidemybrain
	 **/
	public void BuscarOperador(String linha){
		Matcher mtchOp = operador.matcher(linha);
		if(mtchOp.find()){
			for(int op = 0; op < ArraysLists.operadores.size(); op++){
				// O OPERADOR exite?
				// 	Se sim, buscamos todos os registradores, endereços & labels
				//	Caso contrário, uma mensagem é enviada ao Usuário.
				if(mtchOp.group().replaceAll("[\\^ ]", "").toLowerCase().equals(ArraysLists.operadores.get(op).toString())){
					if(ArraysLists.operadores.get(op).getId() >= 999){
		  				JOptionPane.showMessageDialog(
		  						null,
		  						"<html>"
		  						+ "Lamentamos, mas o operador <b style='font-size:9px; color: red;'>"+ArraysLists.operadores.get(op)+"</b>, localizado na linha <b style='font-size:9px; color: red;'>"+linhaAtual+"</b>,<br>"
		  						+ " não poderá ser executado.<br>"
		  						+ "O mesmo ainda não foi completamente habilitado.<br>"
		  						+ "<i>Tente utilizá-lo na próxima revisão do sistema.</i>"
		  						+ "</html>",
		  						"Operador não implementado",
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
	 *		Verifica qual o tipo da Operação encontrada
	 *		e chama o método correto para cada instrução
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
							enderecoLabel,						// Label|Endereço|Valor Encontrado
							linhaAtual);						// Código da Linha
				break;
				default:
					Instrucao tipoI = new Instrucao(
							ArraysLists.operadores.get(op),		// Operador
							ArraysLists.regEncontrados.get(0),	// Primeiro Registrador Encontrado
							ArraysLists.regEncontrados.get(1),	// Segundo Registrador Encontrado
							enderecoLabel,						// Label|Endereço|Valor Encontrado
							linhaAtual);						// Código da Linha
				break;
			}
			break;
			
		// TIPO J
		case 1:
			Instrucao tipoJ = new Instrucao(
					ArraysLists.operadores.get(op),		// Operador
					enderecoLabel,						// Label|Endereço Encontrado
					linhaAtual);						// Código da Linha
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
							linhaAtual);						// Código da Linha
					break;
				// MFHI & MFLO
				case 102:
				case 103:
					Instrucao mfHILO = new Instrucao(
							ArraysLists.operadores.get(op),		// Operador
							ArraysLists.regEncontrados.get(0),	// Primeiro Registrador Encontrado
							linhaAtual);						// Código da Linha
					break;
				// Tipo R (default)
				default:
					Instrucao tipoR = new Instrucao(
							ArraysLists.operadores.get(op),		// Operador
							ArraysLists.regEncontrados.get(0),	// Primeiro Registrador Encontrado
							ArraysLists.regEncontrados.get(1),	// Segundo Registrador Encontrado
							ArraysLists.regEncontrados.get(2),	// Terceiro Registrador Encontrado
							linhaAtual);						// Código da Linha
					break;
			}
			break;
			
		// TIPO I (LOAD/STORE)
		case 3:
			Instrucao tipoI_LoadStore = new Instrucao(
					ArraysLists.operadores.get(op),		// Operador
					ArraysLists.regEncontrados.get(0),	// Primeiro Registrador Encontrado
					enderecoLabel,						// Endereço Encontrado
					ArraysLists.regEncontrados.get(1),	// Segundo Registrador Encontrado
					linhaAtual);						// Código da Linha
			break;
			
		// TIPO R (JUMP/BRANCH)
		case 4:
			Instrucao tipoR_JumpBranch = new Instrucao(
					ArraysLists.operadores.get(op),		// Operador
					ArraysLists.regEncontrados.get(0),	// Primeiro Registrador Encontrado
					linhaAtual);						// Código da Linha
			break;
			
		// TIPO I (JUMP/BRANCH)
		case 5:
			switch(ArraysLists.operadores.get(op).getId()){
				default:
					Instrucao tipoI = new Instrucao(
							ArraysLists.operadores.get(op),		// Operador
							ArraysLists.regEncontrados.get(0),	// Primeiro Registrador Encontrado
							ArraysLists.regEncontrados.get(1),	// Segundo Registrador Encontrado
							enderecoLabel,						// Label|Endereço Encontrado
							linhaAtual);						// Código da Linha
					break;
				}
			break;
	}
	}
	/**
	 *	BuscarLabelDefinida(String linha)
	 *		Procura na linha Labels que foram definidas
	 *		e as adiciona em um ArrayList.
	 *		Verificando também, se as mesmas já foram
	 *		adicionadas anteriormente e alertando o usuário;
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
						+ "Prezado usuário, a label: <b style='color:red;'>"+lbl+"</b> já foi definida anteriormente no sistema.<br>"
						+ "<i>Favor revisar as informações digitadas.</i>"
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
	 *		Após ser encontrado um Operador, é realizada uma busca
	 *		por todos os registradores encontrados na linha em questão;
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
	 *		Verificar se 1ª Registrador encontrado é o $zero
	 *		se for verdadeiro, a linha de código é ignorada e é enviada uma mensagem ao usuário;
	 *	 @author insidemybrain
	 **/
	public boolean Verificar$Zero(String linha){
		if(ArraysLists.regEncontrados.get(0).equals(Registrador.$zero)){
  			JOptionPane.showMessageDialog(null,
					"<html>"
					+ "Por padrão o registrador <b style='color:red;'>"+Registrador.$zero+"</b> possui um valor fixo e imutável.<br>"
					+ "<i>Linha de código ignorada!</i>"
					+ "</html>",
					"Atenção!",
					JOptionPane.WARNING_MESSAGE);
  			return true;
  		}
		return false;
	}

	/**
	 *	BuscarEnderecosOperador(String linha)
	 *		Busca o valor inteiro digitado após um operador;
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
	 *		Busca a label digitada após um operador;
	 *	 @author insidemybrain
	 **/
	public void BuscarLabelOperador(String linha){
		Matcher mtchLbl = label.matcher(linha);
		if(mtchLbl.find()){
			// Label após REGISTRADORES
			if(mtchLbl.group().charAt(0) == ','){
				enderecoLabel = mtchLbl.group().substring(1).replaceAll("[\\^ ]", "");
			}
			// Label após J/JAL
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
