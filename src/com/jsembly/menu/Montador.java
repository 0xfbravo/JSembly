package com.jsembly.menu;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import com.jsembly.extras.Utilidades;
import com.jsembly.funcoes.ArithmeticLogicUnit;
import com.jsembly.funcoes.BinaryLogic;
import com.jsembly.funcoes.ConversaoBase;
import com.jsembly.main.ArraysLists;
import com.jsembly.main.Janela;
import com.jsembly.main.Memoria;
import com.jsembly.mips.Registrador;
import com.jsembly.mips.TipoInstrucao;

public class Montador {
	public static String lblAddress = new String();
	public static String novoEndLbl = new String();
	public static String valorReg = new String();
	public static String binario = new String();
	public static String lm = "Sem Linguagem de Máquina";
	public static String enderecoOuLabel = new String();
	public static String endLbl = new String();
	public static String q = new String();
	public static String valorReg2 = new String();
	public static String valorReg3 = new String();
	public static boolean erro = false;
	public static boolean resultado = false;
	public static int codigoErro = 0;
	public static int linhaAtual = 0;
	public static int regSalvar = 0;
	public static int decimal;
	public static int p;
	public static int valorRegInt = 0;

	public static ArrayList<String> linhasLidas = Utilidades.LerArquivo(Janela.temp.getAbsolutePath());
	public static Pattern operador = Pattern.compile("\\w+", Pattern.CASE_INSENSITIVE);
	public static Pattern registrador = Pattern.compile("[$]\\w+", Pattern.CASE_INSENSITIVE);
	public static Pattern endereco = Pattern.compile(" \\w+|[0-9]|\\,w+|,[0-9]", Pattern.CASE_INSENSITIVE);
	public static Pattern enderecoTipoI = Pattern.compile("[,][ ]{0,9999}\\w+", Pattern.CASE_INSENSITIVE);
	public static Pattern enderecoTipoR = Pattern.compile("[$]\\w+", Pattern.CASE_INSENSITIVE);
	public static Pattern label = Pattern.compile("\\b\\w{1,9999}[:]", Pattern.CASE_INSENSITIVE);
	
	public Montador(){
		ArraysLists.arrLabel.clear();
		Registrador.LimparAtividade(Janela.dtm);
		Janela.dtmExec.setRowCount(0);
		Memoria.LimparMemoria(Janela.dtmMem);
		Janela.dtmMem.fireTableDataChanged();
		Janela.painelLinguagemMaquina.setText("");
		
		// Primeiro busca todas as labels do arquivo
		//  e adiciona elas ao ArrayList & à Memória
		for(String labelLida : linhasLidas){
			Matcher matcherLbl = label.matcher(labelLida);
			if(matcherLbl.find() && erro != true){
				String lbl = matcherLbl.group().substring(matcherLbl.start(),matcherLbl.end()-1);
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
						erro = true;
						codigoErro = 1;
					}
				}
				ArraysLists.arrLabel.add(lbl);
			}
		}
		// Após encontrar todas a labels definidas
		//  inicia uma busca por operadores,registradores, etc
		if(erro){
			JOptionPane.showMessageDialog(null,
					"<html>"
					+ "Prezado usuário, o sistema não poderá continuar a execução do código,<br>"
					+ "pois o mesmo apresenta um erro.<br><br>"
					+ "<b style='color:red;'>Código: "+codigoErro+"</b><br><br>"
					+ "<i>Favor revisar as informações digitadas ou informar o código ao desenvolvedor.</i>"
					+ "</html>",
					"Erro na Compilação",
					JOptionPane.ERROR_MESSAGE);
		} else {
		for(String linha : linhasLidas){
			linhaAtual++;
			ArraysLists.regEncontrados.clear();
			
			// Busca de Operadores
		    Matcher matcher = operador.matcher(linha);
		    if(matcher.find()) {
			    // Busca de Registradores
			    Matcher matcher2 = registrador.matcher(linha);
			    while(matcher2.find()) {
			      for(int i = 0; i < ArraysLists.registradores.size(); i++){
			    	  if(matcher2.group().toLowerCase().equals(ArraysLists.registradores.get(i).toString())){
			    		  ArraysLists.regEncontrados.add(ArraysLists.registradores.get(i));
			    		  break;
			    	  }
			      }
			    }
		      for(int i = 0; i < ArraysLists.operadores.size(); i++){
		    	  if(matcher.group().toLowerCase().equals(ArraysLists.operadores.get(i).toString())){
		    		  
	    		  	  if(ArraysLists.operadores.get(i).getId() >= 999){
    		  				JOptionPane.showMessageDialog(
    		  						null,
    		  						"<html>"
    		  						+ "Lamentamos, mas o operador <b style='font-size:9px; color: red;'>"+ArraysLists.operadores.get(i)+"</b>, localizado na linha <b style='font-size:9px; color: red;'>"+linhaAtual+"</b>,<br>"
    		  						+ " não poderá ser executado.<br>"
    		  						+ "O mesmo ainda não foi completamente habilitado.<br>"
    		  						+ "<i>Tente utilizá-lo na próxima revisão do sistema.</i>"
    		  						+ "</html>",
    		  						"Operador não implementado",
    		  						JOptionPane.ERROR_MESSAGE);
    		  				break;
	    		  		}
	    		  	if(ArraysLists.regEncontrados.get(0).equals(Registrador.$zero)){
    		  			JOptionPane.showMessageDialog(null,
    							"<html>"
    							+ "Por padrão o registrador <b style='color:red;'>"+Registrador.$zero+"</b> possui um valor fixo e imutável.<br>"
    							+ "<i>Linha de código ignorada!</i>"
    							+ "</html>",
    							"Atenção!",
    							JOptionPane.WARNING_MESSAGE);
    		  			break;
    		  		}
		    		  switch(ArraysLists.operadores.get(i).getTipoIntrucao()){
		    		  
		    		  	// -- TIPO I
		    		  	case 0:
		    		  			Matcher matcher3 = enderecoTipoI.matcher(linha);
		    		  				if(matcher3.find()) { 
		    		  					enderecoOuLabel = matcher3.group().substring(1);
		    		  					enderecoOuLabel = enderecoOuLabel.replaceAll("[\\^ ]", "");
									}
		    		  				switch(ArraysLists.operadores.get(i).getId()){
		    		  				// ADDI
		    		  				case 2:
		    		  					decimal = Integer.parseInt(enderecoOuLabel, 10);
				    		  			binario = Integer.toBinaryString(decimal);
				    		  			binario = BinaryLogic.resizeBinary(binario, 16,true);
				    		  			lm = TipoInstrucao.InstrucaoTipoI(ArraysLists.operadores.get(i).getValorBits(),ArraysLists.regEncontrados.get(0).getValorBits(),ArraysLists.regEncontrados.get(1).getValorBits(),binario);
				    		  			if(BinaryLogic.chkOverflow(lm, linhaAtual,ArraysLists.operadores.get(i),ArraysLists.regEncontrados,binario)) break;
		    		  					if(BinaryLogic.chkUnderflow(lm, linhaAtual,ArraysLists.operadores.get(i),ArraysLists.regEncontrados,binario)) break;
		    		  					Memoria.AlocarMemoria(lm, Janela.dtmMem);
		    		  					Janela.painelLinguagemMaquina.append(lm+"\n");
		    		  					Janela.dtmExec.addRow(new Object[]{
		    		  							Memoria.BuscarEndereco(lm.substring(0, 8), Janela.dtmMem),
		    		  							"0x"+ConversaoBase.converteBinarioParaHexadecimal(lm),
		    		  							ArraysLists.operadores.get(i)+" $"+ArraysLists.regEncontrados.get(0).getId()+",$"+ArraysLists.regEncontrados.get(1).getId()+","+enderecoOuLabel,
		    		  							linhaAtual+": "+ArraysLists.operadores.get(i)+" "+ArraysLists.regEncontrados.get(0).toString()+","+ArraysLists.regEncontrados.get(1).toString()+","+enderecoOuLabel});
		    		  				break;
		    		  				// LUI
		    		  				case 9:
		    		  					decimal = Integer.parseInt(enderecoOuLabel, 10);
				    		  			binario = Integer.toBinaryString(decimal);
		    		  					binario = BinaryLogic.resizeBinary(binario, 26,true);
		    		  					lm = TipoInstrucao.InstrucaoTipoI(ArraysLists.operadores.get(i).getValorBits(),binario);
		    		  					if(BinaryLogic.chkOverflow(lm, linhaAtual,ArraysLists.operadores.get(i),ArraysLists.regEncontrados,binario)) break;
		    		  					if(BinaryLogic.chkUnderflow(lm, linhaAtual,ArraysLists.operadores.get(i),ArraysLists.regEncontrados,binario)) break;
		    		  					Memoria.AlocarMemoria(lm, Janela.dtmMem);
		    		  					Janela.painelLinguagemMaquina.append(lm+"\n");
		    		  					Janela.dtmExec.addRow(new Object[]{
		    		  							Memoria.BuscarEndereco(lm.substring(0, 8), Janela.dtmMem),
		    		  							"0x"+ConversaoBase.converteBinarioParaHexadecimal(lm),
		    		  							ArraysLists.operadores.get(i)+" $"+ArraysLists.regEncontrados.get(0).getId()+","+enderecoOuLabel,
		    		  							linhaAtual+": "+ArraysLists.operadores.get(i)+" "+ArraysLists.regEncontrados.get(0).toString()+","+enderecoOuLabel});
		    		  				break;
		    		  				//ANDI
			    		  			case 13:
			    		  				decimal = Integer.parseInt(enderecoOuLabel, 10);
				    		  			binario = Integer.toBinaryString(decimal);
				    		  			while(binario.length()<16){
				    		  				binario = "0" + binario ;
				    		  				lm = TipoInstrucao.InstrucaoTipoI(ArraysLists.operadores.get(i).getValorBits(),ArraysLists.regEncontrados.get(0).getValorBits(),ArraysLists.regEncontrados.get(1).getValorBits(),binario);
				    		  			}
				    		  			if(BinaryLogic.chkOverflow(lm, linhaAtual,ArraysLists.operadores.get(i),ArraysLists.regEncontrados,binario)) break;
		    		  					if(BinaryLogic.chkUnderflow(lm, linhaAtual,ArraysLists.operadores.get(i),ArraysLists.regEncontrados,binario)) break;
		    		  					Memoria.AlocarMemoria(lm, Janela.dtmMem);
		    		  					Janela.painelLinguagemMaquina.append(lm+"\n");
		    		  					Janela.dtmExec.addRow(new Object[]{
		    		  							Memoria.BuscarEndereco(lm.substring(0, 8), Janela.dtmMem),
		    		  							"0x"+ConversaoBase.converteBinarioParaHexadecimal(lm),
		    		  							ArraysLists.operadores.get(i)+" $"+ArraysLists.regEncontrados.get(0).getId()+",$"+ArraysLists.regEncontrados.get(1).getId()+","+enderecoOuLabel,
		    		  							linhaAtual+": "+ArraysLists.operadores.get(i)+" "+ArraysLists.regEncontrados.get(0).toString()+","+ArraysLists.regEncontrados.get(1).toString()+","+enderecoOuLabel});
			    		  			break;
		    		  				//ANDI
			    		  			case 14:
			    		  				decimal = Integer.parseInt(enderecoOuLabel, 10);
				    		  			binario = Integer.toBinaryString(decimal);
				    		  			while(binario.length()<16){
				    		  				binario = "0" + binario ;
				    		  				lm = TipoInstrucao.InstrucaoTipoI(ArraysLists.operadores.get(i).getValorBits(),ArraysLists.regEncontrados.get(0).getValorBits(),ArraysLists.regEncontrados.get(1).getValorBits(),binario);
				    		  			}
				    		  			if(BinaryLogic.chkOverflow(lm, linhaAtual,ArraysLists.operadores.get(i),ArraysLists.regEncontrados,binario)) break;
		    		  					if(BinaryLogic.chkUnderflow(lm, linhaAtual,ArraysLists.operadores.get(i),ArraysLists.regEncontrados,binario)) break;
		    		  					Memoria.AlocarMemoria(lm, Janela.dtmMem);
		    		  					Janela.painelLinguagemMaquina.append(lm+"\n");
		    		  					Janela.dtmExec.addRow(new Object[]{
		    		  							Memoria.BuscarEndereco(lm.substring(0, 8), Janela.dtmMem),
		    		  							"0x"+ConversaoBase.converteBinarioParaHexadecimal(lm),
		    		  							ArraysLists.operadores.get(i)+" $"+ArraysLists.regEncontrados.get(0).getId()+",$"+ArraysLists.regEncontrados.get(1).getId()+","+enderecoOuLabel,
		    		  							linhaAtual+": "+ArraysLists.operadores.get(i)+" "+ArraysLists.regEncontrados.get(0).toString()+","+ArraysLists.regEncontrados.get(1).toString()+","+enderecoOuLabel});
			    		  			break;
		    		  				// SLTI
		    		  				case 20:
		    		  					decimal = Integer.parseInt(enderecoOuLabel, 10);
				    		  			binario = Integer.toBinaryString(decimal);
				    		  			while(binario.length()<16){
				    		  				binario = "0" + binario ;
				    		  				lm = TipoInstrucao.InstrucaoTipoI(ArraysLists.operadores.get(i).getValorBits(),ArraysLists.regEncontrados.get(0).getValorBits(),ArraysLists.regEncontrados.get(1).getValorBits(),binario);
				    		  			}
				    		  			if(BinaryLogic.chkOverflow(lm, linhaAtual,ArraysLists.operadores.get(i),ArraysLists.regEncontrados,binario)) break;
		    		  					if(BinaryLogic.chkUnderflow(lm, linhaAtual,ArraysLists.operadores.get(i),ArraysLists.regEncontrados,binario)) break;
		    		  					Memoria.AlocarMemoria(lm, Janela.dtmMem);
		    		  					Janela.painelLinguagemMaquina.append(lm+"\n");
		    		  					Janela.dtmExec.addRow(new Object[]{
		    		  							Memoria.BuscarEndereco(lm.substring(0, 8), Janela.dtmMem),
		    		  							"0x"+ConversaoBase.converteBinarioParaHexadecimal(lm),
		    		  							ArraysLists.operadores.get(i)+" $"+ArraysLists.regEncontrados.get(0).getId()+",$"+ArraysLists.regEncontrados.get(1).getId()+","+enderecoOuLabel,
		    		  							linhaAtual+": "+ArraysLists.operadores.get(i)+" "+ArraysLists.regEncontrados.get(0).toString()+","+ArraysLists.regEncontrados.get(1).toString()+","+enderecoOuLabel});
		    		  				break;
		    		  				
		    		  				//ADDIU
	    		  					case 26:
	    		  						decimal = Integer.parseInt(enderecoOuLabel, 10);
				    		  			binario = Integer.toBinaryString(decimal);
				    		  			while(binario.length()<16){
				    		  				binario = "0" + binario ;
				    		  				lm = TipoInstrucao.InstrucaoTipoI(ArraysLists.operadores.get(i).getValorBits(),ArraysLists.regEncontrados.get(0).getValorBits(),ArraysLists.regEncontrados.get(1).getValorBits(),binario);
				    		  			}
				    		  				Memoria.AlocarMemoria(lm, Janela.dtmMem);
			    		  					Janela.painelLinguagemMaquina.append(lm+"\n");
			    		  					Janela.dtmExec.addRow(new Object[]{
			    		  							Memoria.BuscarEndereco(lm.substring(0, 8), Janela.dtmMem),
			    		  							"0x"+ConversaoBase.converteBinarioParaHexadecimal(lm),
			    		  							ArraysLists.operadores.get(i)+" $"+ArraysLists.regEncontrados.get(0).getId()+",$"+ArraysLists.regEncontrados.get(1).getId()+","+enderecoOuLabel,
			    		  							linhaAtual+": "+ArraysLists.operadores.get(i)+" "+ArraysLists.regEncontrados.get(0).toString()+","+ArraysLists.regEncontrados.get(1).toString()+","+enderecoOuLabel});
			    		  				break;
		    		  				
		    		  				default:
			    		  				JOptionPane.showMessageDialog(
			    		  						null,
			    		  						"<html>"
			    		  						+ "Lamentamos, mas o operador <b style='font-size:9px; color: red;'>"+ArraysLists.operadores.get(i)+"</b>, localizado na linha <b style='font-size:9px; color: red;'>"+linhaAtual+"</b>,<br>"
			    		  						+ " não poderá ser executado.<br>"
			    		  						+ "O mesmo ainda não foi completamente habilitado.<br>"
			    		  						+ "<i>Tente utilizá-lo na próxima revisão do sistema.</i>"
			    		  						+ "</html>",
			    		  						"Operador não implementado",
			    		  						JOptionPane.ERROR_MESSAGE);
			    		  			break;
		    		  			}
		    		  		break;
		    		  	
		    		  		
		    		  	// -- TIPO J
		    		  	case 1:
		    		  		Matcher matcher4 = endereco.matcher(linha);
							if(matcher4.find()) { enderecoOuLabel = matcher4.group().substring(1); }
							for(int l = 0; l < ArraysLists.arrLabel.size(); l++){
								if(ArraysLists.arrLabel.get(l).equals(enderecoOuLabel)){
									lblAddress = ArraysLists.labelAddress.get(l).toString();
									novoEndLbl = BinaryLogic.resizeBinary(ArraysLists.labelAddress.get(l).toString(),26,true);
								}
							}
							lm = TipoInstrucao.InstrucaoTipoJ(ArraysLists.operadores.get(i).getValorBits(),novoEndLbl);
							if(BinaryLogic.chkOverflow(lm, linhaAtual,ArraysLists.operadores.get(i),binario)) break;
		  					if(BinaryLogic.chkUnderflow(lm, linhaAtual,ArraysLists.operadores.get(i),binario)) break;
							Memoria.AlocarMemoria(lm, Janela.dtmMem);
							Janela.painelLinguagemMaquina.append(lm+"\n");
							Janela.dtmExec.addRow(new Object[]{
		    		  				Memoria.BuscarEndereco(lm.substring(0, 8), Janela.dtmMem),
		    		  				"0x"+ConversaoBase.converteBinarioParaHexadecimal(lm),
		    		  				ArraysLists.operadores.get(i)+" "+lblAddress,
		    		  				linhaAtual+": "+ArraysLists.operadores.get(i)+" "+enderecoOuLabel});
							Janela.dtm.setValueAt(lblAddress, 0, 2);
							Janela.dtm.setValueAt(BinaryLogic.resizeBinary(lblAddress,32,true),
													0,
													3);
		    		  		break;
		    		  		
		    		  		
		    		  	// -- TIPO R
		    		  	case 2:
		    		  		int p;
    		  				String q = null;
    		  				Matcher matcherR = enderecoTipoI.matcher(linha);
    		  				if(matcherR.find()) { 
    		  					enderecoOuLabel = matcherR.group().substring(1);
    		  					enderecoOuLabel = enderecoOuLabel.replaceAll("[\\^ ]", "");
    		  					p = Integer.parseInt(enderecoOuLabel);
    		  					q = BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario(p), 5, true);
    		  					lm = TipoInstrucao.InstrucaoTipoR("00000",ArraysLists.regEncontrados.get(1).getValorBits(),"00000",q,ArraysLists.operadores.get(i).getValorBits());
    		    		  		if(BinaryLogic.chkOverflow(lm, linhaAtual,ArraysLists.operadores.get(i),ArraysLists.regEncontrados)) break;
    		  					if(BinaryLogic.chkUnderflow(lm, linhaAtual,ArraysLists.operadores.get(i),ArraysLists.regEncontrados)) break;
    		    		  		Memoria.AlocarMemoria(lm, Janela.dtmMem);
    		    		  		Janela.painelLinguagemMaquina.append(lm+"\n");
    		    		  		Janela.dtmExec.addRow(new Object[]{
    		    		  				Memoria.BuscarEndereco(lm.substring(0, 8), Janela.dtmMem),
    		    		  				"0x"+ConversaoBase.converteBinarioParaHexadecimal(lm),
    		    		  				ArraysLists.operadores.get(i)+" $"+ArraysLists.regEncontrados.get(0).getId()+",$"+ArraysLists.regEncontrados.get(1).getId()+","+enderecoOuLabel,
    		    		  				linhaAtual+": "+ArraysLists.operadores.get(i)+" "+ArraysLists.regEncontrados.get(0).toString()+","+ArraysLists.regEncontrados.get(1).toString()+","+enderecoOuLabel});
    		  				} else {
								lm = TipoInstrucao.InstrucaoTipoR(ArraysLists.regEncontrados.get(0).getValorBits(),ArraysLists.regEncontrados.get(1).getValorBits(),ArraysLists.regEncontrados.get(2).getValorBits(),"00000",ArraysLists.operadores.get(i).getValorBits());
			    		  		if(BinaryLogic.chkOverflow(lm, linhaAtual,ArraysLists.operadores.get(i),ArraysLists.regEncontrados)) break;
			  					if(BinaryLogic.chkUnderflow(lm, linhaAtual,ArraysLists.operadores.get(i),ArraysLists.regEncontrados)) break;
			    		  		Memoria.AlocarMemoria(lm, Janela.dtmMem);
			    		  		Janela.painelLinguagemMaquina.append(lm+"\n");
								Janela.dtmExec.addRow(new Object[]{
			    		  				Memoria.BuscarEndereco(lm.substring(0, 8), Janela.dtmMem),
			    		  				"0x"+ConversaoBase.converteBinarioParaHexadecimal(lm),
			    		  				ArraysLists.operadores.get(i)+" $"+ArraysLists.regEncontrados.get(0).getId()+",$"+ArraysLists.regEncontrados.get(1).getId()+",$"+ArraysLists.regEncontrados.get(2).getId(),
			    		  				linhaAtual+": "+ArraysLists.operadores.get(i)+" "+ArraysLists.regEncontrados.get(0).toString()+","+ArraysLists.regEncontrados.get(1).toString()+","+ArraysLists.regEncontrados.get(2).toString()});
    		  				}

		    		  		break;
		    		  		
		    		  		
		    		  	// -- TIPO I (LOAD/STORE)
		    		  	case 3:
		    		  		switch(ArraysLists.operadores.get(i).getId()){
		    		  			//MTHI
		    		  			case 100:
		    		  				System.out.println("achei um MTHI");
		    		  				Matcher matcher6 = enderecoTipoR.matcher(linha);
									if(matcher6.find()) { enderecoOuLabel = matcher6.group(); }
									for(int y = 0; y < Janela.dtm.getRowCount(); y++){
										if(Janela.dtm.getValueAt(y, 0).equals(matcher6.group())){
											lblAddress = Janela.dtm.getValueAt(y,2).toString();
										}
									}
									//novoEndLbl = BinaryLogic.resizeBinary(ArraysLists.labelAddress.get(l).toString(),26,true);
									Janela.dtm.setValueAt("Ativo", 1, 4);
									Janela.dtm.setValueAt(lblAddress, 1, 2);
									Janela.dtm.setValueAt(BinaryLogic.resizeBinary(
														ConversaoBase.converteDecimalParaBinario(
														Integer.parseInt(lblAddress)),32,true),
															1,
															3);
									lm = TipoInstrucao.InstrucaoTipoR(BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario(Integer.parseInt(lblAddress)),15,true),"00000",ArraysLists.operadores.get(i).getValorBits());
				    		  		if(BinaryLogic.chkOverflow(lm, linhaAtual,ArraysLists.operadores.get(i),ArraysLists.regEncontrados)) break;
				  					if(BinaryLogic.chkUnderflow(lm, linhaAtual,ArraysLists.operadores.get(i),ArraysLists.regEncontrados)) break;
				    		  		Memoria.AlocarMemoria(lm, Janela.dtmMem);
				    		  		Janela.painelLinguagemMaquina.append(lm+"\n");
									Janela.dtmExec.addRow(new Object[]{
				    		  				Memoria.BuscarEndereco(lm.substring(0, 8), Janela.dtmMem),
				    		  				"0x"+ConversaoBase.converteBinarioParaHexadecimal(lm),
				    		  				ArraysLists.operadores.get(i)+" $"+ArraysLists.regEncontrados.get(0).getId(),
				    		  				linhaAtual+": "+ArraysLists.operadores.get(i)+" "+ArraysLists.regEncontrados.get(0).toString()});
				    		  		break;
		    		  			//MTLO
		    		  			case 101:
		    		  				Matcher matcher7 = enderecoTipoR.matcher(linha);
									if(matcher7.find()) { enderecoOuLabel = matcher7.group(); }
									for(int y = 0; y < Janela.dtm.getRowCount(); y++){
										if(Janela.dtm.getValueAt(y, 0).equals(matcher7.group())){
											lblAddress = Janela.dtm.getValueAt(y,2).toString();
										}
									}
									//novoEndLbl = BinaryLogic.resizeBinary(ArraysLists.labelAddress.get(l).toString(),26,true);
									Janela.dtm.setValueAt("Ativo", 2, 4);
									Janela.dtm.setValueAt(lblAddress, 2, 2);
									Janela.dtm.setValueAt(BinaryLogic.resizeBinary(
											ConversaoBase.converteDecimalParaBinario(
											Integer.parseInt(lblAddress)),32,true),
												2,
												3);
									lm = TipoInstrucao.InstrucaoTipoR(BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario(Integer.parseInt(lblAddress)),15,true),"00000",ArraysLists.operadores.get(i).getValorBits());
				    		  		if(BinaryLogic.chkOverflow(lm, linhaAtual,ArraysLists.operadores.get(i),ArraysLists.regEncontrados)) break;
				  					if(BinaryLogic.chkUnderflow(lm, linhaAtual,ArraysLists.operadores.get(i),ArraysLists.regEncontrados)) break;
				    		  		Memoria.AlocarMemoria(lm, Janela.dtmMem);
				    		  		Janela.painelLinguagemMaquina.append(lm+"\n");
									Janela.dtmExec.addRow(new Object[]{
				    		  				Memoria.BuscarEndereco(lm.substring(0, 8), Janela.dtmMem),
				    		  				"0x"+ConversaoBase.converteBinarioParaHexadecimal(lm),
				    		  				ArraysLists.operadores.get(i)+" $"+ArraysLists.regEncontrados.get(0).getId(),
				    		  				linhaAtual+": "+ArraysLists.operadores.get(i)+" "+ArraysLists.regEncontrados.get(0).toString()});
				    		  		break;
		    		  		
		    		  		}
		    		  		break;
		    		  		
		    		  		
		    		  	// -- TIPO R (JUMP/BRANCH)
		    		  	case 4:
		    		  		Matcher matcher6 = enderecoTipoR.matcher(linha);
							if(matcher6.find()) { enderecoOuLabel = matcher6.group(); }
							for(int y = 0; y < Janela.dtm.getRowCount(); y++){
								if(Janela.dtm.getValueAt(y, 0).equals(matcher6.group())){
									lblAddress = Janela.dtm.getValueAt(y,2).toString();
								}
							}
							//novoEndLbl = BinaryLogic.resizeBinary(ArraysLists.labelAddress.get(l).toString(),26,true);
							Janela.dtm.setValueAt("Ativo", 0, 4);
							Janela.dtm.setValueAt(lblAddress, 0, 2);
							Janela.dtm.setValueAt(BinaryLogic.resizeBinary(
									ConversaoBase.converteDecimalParaBinario(
									Integer.parseInt(lblAddress)),32,true),
										0,
										3);
							lm = TipoInstrucao.InstrucaoTipoR(BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario(Integer.parseInt(lblAddress)),15,true),"00000",ArraysLists.operadores.get(i).getValorBits());
		    		  		if(BinaryLogic.chkOverflow(lm, linhaAtual,ArraysLists.operadores.get(i),ArraysLists.regEncontrados)) break;
		  					if(BinaryLogic.chkUnderflow(lm, linhaAtual,ArraysLists.operadores.get(i),ArraysLists.regEncontrados)) break;
		    		  		Memoria.AlocarMemoria(lm, Janela.dtmMem);
		    		  		Janela.painelLinguagemMaquina.append(lm+"\n");
							Janela.dtmExec.addRow(new Object[]{
		    		  				Memoria.BuscarEndereco(lm.substring(0, 8), Janela.dtmMem),
		    		  				"0x"+ConversaoBase.converteBinarioParaHexadecimal(lm),
		    		  				ArraysLists.operadores.get(i)+" $"+ArraysLists.regEncontrados.get(0).getId(),
		    		  				linhaAtual+": "+ArraysLists.operadores.get(i)+" "+ArraysLists.regEncontrados.get(0).toString()});
		    		  		break;
		    		  		
		    		  		
		    		  	// -- TIPO I (JUMP/BRANCH)
		    		  	case 5:
		    		  		Matcher matcher5 = enderecoTipoI.matcher(linha);
							if(matcher5.find()) {
								enderecoOuLabel = matcher5.group().substring(1);
								enderecoOuLabel = enderecoOuLabel.replaceAll("[\\^ ]", "");
							}
							if(ArraysLists.arrLabel.size() != 0){
							for(int l = 0; l < ArraysLists.arrLabel.size(); l++){
								if(ArraysLists.arrLabel.get(l).equals(enderecoOuLabel)){
									lblAddress = ArraysLists.labelAddress.get(l).toString();
									novoEndLbl = BinaryLogic.resizeBinary(ArraysLists.labelAddress.get(l).toString(),26,true);
									lm = TipoInstrucao.InstrucaoTipoI(ArraysLists.operadores.get(i).getValorBits(),ArraysLists.regEncontrados.get(0).getValorBits(),ArraysLists.regEncontrados.get(1).getValorBits(),novoEndLbl);
									Memoria.AlocarMemoria(lm, Janela.dtmMem);
									Janela.painelLinguagemMaquina.append(lm+"\n");
									Janela.dtmExec.addRow(new Object[]{
				    		  				Memoria.BuscarEndereco(lm.substring(0, 8), Janela.dtmMem),
				    		  				"0x"+ConversaoBase.converteBinarioParaHexadecimal(lm),
				    		  				ArraysLists.operadores.get(i)+" $"+ArraysLists.regEncontrados.get(0).getId()+",$"+ArraysLists.regEncontrados.get(1).getId()+","+lblAddress,
				    		  				linhaAtual+": "+ArraysLists.operadores.get(i)+" "+ArraysLists.regEncontrados.get(0).toString()+","+ArraysLists.regEncontrados.get(1).toString()+","+enderecoOuLabel});
									Janela.dtm.setValueAt(lblAddress, 0, 2);
									Janela.dtm.setValueAt(BinaryLogic.resizeBinary(lblAddress,32,true),
															0,
															3);
								
								} else {
									JOptionPane.showMessageDialog(null,
											"<html>"
											+ "Prezado usuário, o sistema não poderá continuar a execução do código,<br>"
											+ "pois não reconhecemos a label:<br><br>"
											+ "<b style='color:red;'>"+enderecoOuLabel+"</b><br>"
											+ "Utilizada na linha: <b style='color:red;'>"+linhaAtual+"</b><br>"
											+ "<i>Verifique se a mesma foi adicionada corretamente.</i>"
											+ "</html>",
											"Erro na Compilação",
											JOptionPane.ERROR_MESSAGE);
								}
							}
							} else {
								JOptionPane.showMessageDialog(null,
										"<html>"
										+ "Prezado usuário, o sistema não poderá continuar a execução do código,<br>"
										+ "pois não reconhecemos a label:<br><br>"
										+ "<b style='color:red;'>"+enderecoOuLabel+"</b><br>"
										+ "Utilizada na linha: <b style='color:red;'>"+linhaAtual+"</b><br>"
										+ "<i>Verifique se a mesma foi adicionada corretamente.</i>"
										+ "</html>",
										"Erro na Compilação",
										JOptionPane.ERROR_MESSAGE);
							}
		    		  		break;
		    		  }
		  			if(linhaAtual > 1){
						//System.out.println(linhasLidas.get(linhaAtual-2));
						Matcher matcherLbl = label.matcher(linhasLidas.get(linhaAtual-2));
						if(matcherLbl.find()){
							ArraysLists.labelAddress.add(Memoria.BuscarEndereco(lm.substring(0, 8), Janela.dtmMem));
						}
		  			}
		  				Executar(linha);
		    		  Janela.painelCima.setSelectedComponent(Janela.abaExecutar_Scroll);
		    		  break;
		    	  }
		      }
		    }
		}
	}
}
	
	public static void Executar(String linha){
		// Busca de Operadores
	    Matcher matcher = operador.matcher(linha);
	    if(matcher.find()) {
		    // Busca de Registradores
		    Matcher matcher2 = registrador.matcher(linha);
		    while(matcher2.find()) {
		      for(int i = 0; i < ArraysLists.registradores.size(); i++){
		    	  if(matcher2.group().toLowerCase().equals(ArraysLists.registradores.get(i).toString())){
		    		  ArraysLists.regEncontrados.add(ArraysLists.registradores.get(i));
		    		  break;
		    	  }
		      }
		    }
	      for(int i = 0; i < ArraysLists.operadores.size(); i++){
	    	  if(matcher.group().toLowerCase().equals(ArraysLists.operadores.get(i).toString())){
	    		  
    		  	  if(ArraysLists.operadores.get(i).getId() >= 999){
		  				JOptionPane.showMessageDialog(
		  						null,
		  						"<html>"
		  						+ "Lamentamos, mas o operador <b style='font-size:9px; color: red;'>"+ArraysLists.operadores.get(i)+"</b>, localizado na linha <b style='font-size:9px; color: red;'>"+linhaAtual+"</b>,<br>"
		  						+ " não poderá ser executado.<br>"
		  						+ "O mesmo ainda não foi completamente habilitado.<br>"
		  						+ "<i>Tente utilizá-lo na próxima revisão do sistema.</i>"
		  						+ "</html>",
		  						"Operador não implementado",
		  						JOptionPane.ERROR_MESSAGE);
		  				break;
    		  		}
	    		  switch(ArraysLists.operadores.get(i).getTipoIntrucao()){
	    		  
	    		  	// -- TIPO I
	    		  	case 0:
	    		  		if(ArraysLists.regEncontrados.get(0).equals(Registrador.$zero)){
	    		  			JOptionPane.showMessageDialog(null,
	    							"<html>"
	    							+ "Por padrão o registrador <b style='color:red;'>"+Registrador.$zero+"</b> possui um valor fixo e imutável.<br>"
	    							+ "<i>Linha de código ignorada!</i>"
	    							+ "</html>",
	    							"Atenção!",
	    							JOptionPane.WARNING_MESSAGE);
	    		  			break;
	    		  		}
	    		  		ArraysLists.regEncontrados.get(0).setAtivo(true);
	    		  		Registrador.AtualizarAtividade(Janela.dtm);
	    		  			Matcher matcher3 = enderecoTipoI.matcher(linha);
	    		  				if(matcher3.find()) { 
	    		  					enderecoOuLabel = matcher3.group().substring(1);
	    		  					enderecoOuLabel = enderecoOuLabel.replaceAll("[\\^ ]", "");
								}
	    		  				switch(ArraysLists.operadores.get(i).getId()){
	    		  				// ADDI
	    		  				case 2:
	    		  					decimal = Integer.parseInt(enderecoOuLabel, 10);
			    		  			binario = Integer.toBinaryString(decimal);
			    		  			binario = BinaryLogic.resizeBinary(binario, 16,true);
	    		  					for(int r = 0; r < Janela.dtm.getRowCount(); r++){
				    		  			if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(1).toString())){
				    		  				valorReg = Janela.dtm.getValueAt(r, 2).toString();
				    		  			}
				    		  		}
				    		  		regSalvar = ArithmeticLogicUnit.addi(Integer.parseInt(valorReg,10), decimal);
		    		  				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
		    		  					if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
		    		  						Janela.dtm.setValueAt(regSalvar, r, 2);
		    		  						Janela.dtm.setValueAt( BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario(regSalvar), 32, true), r, 3);
		    		  					}
		    		  				}
	    		  				break;
	    		  				// LUI
	    		  				case 9:
	    		  					decimal = Integer.parseInt(enderecoOuLabel, 10);
			    		  			binario = Integer.toBinaryString(decimal);
	    		  					binario = BinaryLogic.resizeBinary(binario, 26,true);
		    		  				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
		    		  					if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
		    		  						Janela.dtm.setValueAt(ConversaoBase.converteBinarioParaDecimal(binario.substring(16, 26)), r, 2);
		    		  						Janela.dtm.setValueAt( BinaryLogic.resizeBinary(binario, 32, true), r, 3);
		    		  					}
		    		  				}
	    		  				break;
	    		  				//ANDI
		    		  			case 13:
	    		  					for(int r = 0; r < Janela.dtm.getRowCount(); r++){
				    		  			if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(1).toString())){
				    		  				valorReg = Janela.dtm.getValueAt(r, 2).toString();
				    		  			}
				    		  		}
	    		  					for(int r = 0; r < Janela.dtm.getRowCount(); r++){
		    		  					if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
		    		  						Janela.dtm.setValueAt(
		    		  								ConversaoBase.converteBinarioParaDecimal(BinaryLogic.and(valorReg, enderecoOuLabel)),
		    		  								r,
		    		  								2);
		    		  						Janela.dtm.setValueAt(
		    		  								BinaryLogic.resizeBinary(BinaryLogic.and(valorReg, enderecoOuLabel),32,true),
		    		  								r,
		    		  								3);
		    		  					}
		    		  				}
		    		  			break;
	    		  				//ORI
		    		  			case 14:
		    		  				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
				    		  			if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(1).toString())){
				    		  				valorReg = Janela.dtm.getValueAt(r, 2).toString();
				    		  			}
				    		  		}
	    		  					for(int r = 0; r < Janela.dtm.getRowCount(); r++){
		    		  					if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
		    		  						Janela.dtm.setValueAt(
		    		  								ConversaoBase.converteBinarioParaDecimal(BinaryLogic.or(valorReg, enderecoOuLabel)),
		    		  								r,
		    		  								2);
		    		  						Janela.dtm.setValueAt(
		    		  								BinaryLogic.resizeBinary(BinaryLogic.or(valorReg, enderecoOuLabel),32,true),
		    		  								r,
		    		  								3);
		    		  					}
		    		  				}
		    		  			break;
	    		  				// SLTI
	    		  				case 20:
	    		  					decimal = Integer.parseInt(enderecoOuLabel, 10);
			    		  			for(int r = 0; r < Janela.dtm.getRowCount(); r++){
				    		  			if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(1).toString())){
				    		  				valorReg = Janela.dtm.getValueAt(r, 2).toString();
				    		  			}
				    		  		}
	    		  					boolean b = ArithmeticLogicUnit.slti(Integer.parseInt(valorReg,10), decimal);
		    		  				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
		    		  					if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
		    		  						if(b) {
		    		  							Janela.dtm.setValueAt(1, r, 2);
		    		  							Janela.dtm.setValueAt( BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario(1), 32, true), r, 3);
		    		  							}
		    		  						else {
		    		  							Janela.dtm.setValueAt(0, r, 2);
		    		  							Janela.dtm.setValueAt( BinaryLogic.resizeBinary("0", 32, true), r, 3);
		    		  							}
		    		  					}

		    		  				}
	    		  				break;
	    		  				
	    		  				//ADDIU
    		  					case 26:
    		  						decimal = Integer.parseInt(enderecoOuLabel, 10);
			    		  			for(int r = 0; r < Janela.dtm.getRowCount(); r++){
					    		  		if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(1).toString())){
					    		  			valorReg = Janela.dtm.getValueAt(r, 2).toString();
					    		  		}
			    		  			}
			    		  			regSalvar = ArithmeticLogicUnit.addiu(Integer.parseInt(valorReg,10), decimal);
			    		  			for(int r = 0; r < Janela.dtm.getRowCount(); r++){
			    		  				if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
			    		  					Janela.dtm.setValueAt(regSalvar, r, 2);
			    		  					Janela.dtm.setValueAt( BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario(regSalvar), 32, true), r, 3);
			    		  				}
			    		  			}
		    		  				break;
	    		  				
	    		  				default:
		    		  				JOptionPane.showMessageDialog(
		    		  						null,
		    		  						"<html>"
		    		  						+ "Lamentamos, mas o operador <b style='font-size:9px; color: red;'>"+ArraysLists.operadores.get(i)+"</b>, localizado na linha <b style='font-size:9px; color: red;'>"+linhaAtual+"</b>,<br>"
		    		  						+ " não poderá ser executado.<br>"
		    		  						+ "O mesmo ainda não foi completamente habilitado.<br>"
		    		  						+ "<i>Tente utilizá-lo na próxima revisão do sistema.</i>"
		    		  						+ "</html>",
		    		  						"Operador não implementado",
		    		  						JOptionPane.ERROR_MESSAGE);
		    		  			break;
	    		  			}
	    		  		break;
	    		  	
	    		  		
	    		  	// -- TIPO J
	    		  	case 1:
	    		  		Matcher matcher4 = endereco.matcher(linha);
						if(matcher4.find()) { enderecoOuLabel = matcher4.group().substring(1); }
						for(int l = 0; l < ArraysLists.arrLabel.size(); l++){
							if(ArraysLists.arrLabel.get(l).equals(enderecoOuLabel)){
								lblAddress = ArraysLists.labelAddress.get(l).toString();
								novoEndLbl = BinaryLogic.resizeBinary(ArraysLists.labelAddress.get(l).toString(),26,true);
							}
						}
						Janela.dtm.setValueAt("Ativo", 0, 4);
						Janela.dtm.setValueAt(lblAddress, 0, 2);
						Janela.dtm.setValueAt(BinaryLogic.resizeBinary(lblAddress,32,true),
												0,
												3);
						for(int l = 0; l < linhasLidas.size(); l++){
							if(linhasLidas.get(l).equals(enderecoOuLabel+":")){
								//System.out.println("Achei a label na posição: "+l);
								for(int m = l+1; m < linhasLidas.size(); m++){
									//System.out.println(linhasLidas.get(m));
									if(linhasLidas.get(m).contains("j")||linhasLidas.get(m).contains("jal")){
										//System.out.println("Achei na linha: "+ linhasLidas.get(m));
										m = linhasLidas.size();
									} else {
										Executar(linhasLidas.get(m));
									}
								}
							}
						}
	    		  		break;
	    		  		
	    		  		
	    		  	// -- TIPO R
	    		  	case 2:
		  				Matcher matcherR = enderecoTipoI.matcher(linha);
		  				if(matcherR.find()) { 
		  					enderecoOuLabel = matcherR.group().substring(1);
		  					enderecoOuLabel = enderecoOuLabel.replaceAll("[\\^ ]", "");
		  					p = Integer.parseInt(enderecoOuLabel);
		  					q = BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario(p), 5, true);
		  					ArraysLists.regEncontrados.get(0).setAtivo(true);
		    		  		Registrador.AtualizarAtividade(Janela.dtm);
		    		  		for(int r = 0; r < Janela.dtm.getRowCount(); r++){
		    		  			if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(1).toString())){
		    		  				valorRegInt = Integer.parseInt(Janela.dtm.getValueAt(r, 2).toString());
		    		  			}
		    		  		}
		  				} else {
							ArraysLists.regEncontrados.get(0).setAtivo(true);
		    		  		Registrador.AtualizarAtividade(Janela.dtm);
		    		  		for(int r = 0; r < Janela.dtm.getRowCount(); r++){
		    		  			if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(1).toString())){
		    		  				valorReg2 = Janela.dtm.getValueAt(r, 2).toString();
		    		  			}
		    		  			if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(2).toString())){
		    		  				valorReg3 = Janela.dtm.getValueAt(r, 2).toString();
		    		  			}
		    		  		}
		  				}

	    		  		switch(ArraysLists.operadores.get(i).getId()){
	    		  			// ADD
	    		  			case 0:
	    		  				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
	    		  					if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
	    		  						Janela.dtm.setValueAt(
	    		  								ArithmeticLogicUnit.add(Integer.parseInt(valorReg2,10), Integer.parseInt(valorReg3,10)),
	    		  								r,
	    		  								2);
	    		  						Janela.dtm.setValueAt(
	    		  								BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario(
	    		  										ArithmeticLogicUnit.add(Integer.parseInt(valorReg2,10), Integer.parseInt(valorReg3,10))
	    		  										),32,true),
	    		  								r,
	    		  								3);
	    		  					}
	    		  				}
	    		  			break;
	    		  			// SUB
	    		  			case 1:
	    		  				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
	    		  					if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
	    		  						Janela.dtm.setValueAt(
	    		  								ArithmeticLogicUnit.sub(Integer.parseInt(valorReg2,10), Integer.parseInt(valorReg3,10)),
	    		  								r,
	    		  								2);
	    		  						Janela.dtm.setValueAt(
	    		  								BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario(
	    		  										ArithmeticLogicUnit.sub(Integer.parseInt(valorReg2,10), Integer.parseInt(valorReg3,10))
	    		  										),32,true),
	    		  								r,
	    		  								3);
	    		  					}
	    		  				}
	    		  			break;
	    		  			// AND
	    		  			case 10:
	    		  				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
	    		  					if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
	    		  						Janela.dtm.setValueAt(
	    		  								ConversaoBase.converteBinarioParaDecimal(BinaryLogic.and(valorReg2,valorReg3)),
	    		  								r,
	    		  								2);
	    		  						Janela.dtm.setValueAt(
	    		  								BinaryLogic.resizeBinary(BinaryLogic.and(valorReg2,valorReg3),32,true),
	    		  								r,
	    		  								3);
	    		  					}
	    		  				}
	    		  			break;
	    		  			// OR
	    		  			case 11:
	    		  				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
	    		  					if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
	    		  						Janela.dtm.setValueAt(
	    		  								ConversaoBase.converteBinarioParaDecimal(BinaryLogic.or(valorReg2,valorReg3)),
	    		  								r,
	    		  								2);
	    		  						Janela.dtm.setValueAt(
	    		  								BinaryLogic.resizeBinary(BinaryLogic.or(valorReg2,valorReg3),32,true),
	    		  										r,
	    		  										3);
	    		  					}
	    		  				}
	    		  			break;
	    		  			// NAND
	    		  			case 13:
	    		  				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
	    		  					if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
	    		  						Janela.dtm.setValueAt(
	    		  								ConversaoBase.converteBinarioParaDecimal(BinaryLogic.nand(valorReg2,valorReg3)),
	    		  								r,
	    		  								2);
	    		  						Janela.dtm.setValueAt(
	    		  								BinaryLogic.resizeBinary(BinaryLogic.nand(valorReg2,valorReg3),32,true),
	    		  								r,
	    		  								3);
	    		  					}
	    		  				}
	    		  			break;
	    		  			// XNOR
	    		  			case 14:
	    		  				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
	    		  					if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
	    		  						Janela.dtm.setValueAt(
	    		  								ConversaoBase.converteBinarioParaDecimal(BinaryLogic.xnor(valorReg2,valorReg3)),
	    		  								r,
	    		  								2);
	    		  						Janela.dtm.setValueAt(
	    		  								BinaryLogic.resizeBinary(BinaryLogic.xnor(valorReg2,valorReg3),32,true),
	    		  								r,
	    		  								3);
	    		  					}
	    		  				}
	    		  			break;
	    		  			// SLL
	    		  			case 15:
	    		  				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
	    		  					if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
	    		  						Janela.dtm.setValueAt(
	    		  								ConversaoBase.converteBinarioParaDecimal(
	    		  										ArithmeticLogicUnit.sll(
	    		  												valorRegInt,ConversaoBase.converteBinarioParaDecimal(q)
	    		  												)
	    		  										),
	    		  								r,
	    		  								2);
	    		  						Janela.dtm.setValueAt(
	    		  								BinaryLogic.resizeBinary(ArithmeticLogicUnit.sll(
	    		  										valorRegInt,
	    		  										ConversaoBase.converteBinarioParaDecimal(q)),32,true),
	    		  								r,
	    		  								3);
	    		  					}
	    		  				}
	    		  			break;
	    		  			// SRL
	    		  			case 16:
	    		  				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
	    		  					if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
	    		  						Janela.dtm.setValueAt(
	    		  								ConversaoBase.converteBinarioParaDecimal(
	    		  										ArithmeticLogicUnit.srl(
	    		  												valorRegInt,ConversaoBase.converteBinarioParaDecimal(q)
	    		  												)
	    		  										),
	    		  								r,
	    		  								2);
	    		  						Janela.dtm.setValueAt(
	    		  								BinaryLogic.resizeBinary(ArithmeticLogicUnit.srl(
	    		  										valorRegInt,
	    		  										ConversaoBase.converteBinarioParaDecimal(q)),32,true),
	    		  								r,
	    		  								3);
	    		  					}
	    		  				}
	    		  			break;
	    		  			// SLT
    		  				case 19:
		    		  			for(int r = 0; r < Janela.dtm.getRowCount(); r++){
			    		  			if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(1).toString())){
			    		  				valorReg = Janela.dtm.getValueAt(r, 2).toString();
			    		  			}
			    		  			if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(2).toString())){
			    		  				valorReg2 = Janela.dtm.getValueAt(r, 2).toString();
			    		  			}
			    		  		}
    		  					//Memoria.AlocarMemoria(lm, Janela.dtmMem);
			    		  		boolean b = ArithmeticLogicUnit.slti(Integer.parseInt(valorReg,10), Integer.parseInt(valorReg2,10));
	    		  				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
	    		  					if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
	    		  						if(b){
	    		  							Janela.dtm.setValueAt(1, r, 2);
	    		  							Janela.dtm.setValueAt(BinaryLogic.resizeBinary("1", 32, true), r, 3);
	    		  						} else {
	    		  							Janela.dtm.setValueAt(0, r, 2);
	    		  							Janela.dtm.setValueAt(BinaryLogic.resizeBinary("0", 32, true), r, 3);
	    		  						}
	    		  					}
	    		  				}
    		  				break;
	    		  			// MULT
	    		  			case 24:
	    		  				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
	    		  					if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
	    		  						int resultadoMult = 0;
	    		  						if(!BinaryLogic.chkOverflowMult(Integer.parseInt(valorReg2,10), Integer.parseInt(valorReg3,10),
	    		  								ArraysLists.operadores.get(i), linhaAtual))
	    		  						resultadoMult = ArithmeticLogicUnit.mult(Integer.parseInt(valorReg2,10), Integer.parseInt(valorReg3,10));
	    		  						// -- Valor REGISTRADOR
	    		  						Janela.dtm.setValueAt(
	    		  								resultadoMult,
	    		  								r,
	    		  								2);
	    		  						Janela.dtm.setValueAt(
	    		  								BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario(resultadoMult),32,true),
	    		  								r,
	    		  								3);
	    		  						
	    		  						// -- Valor Binário HI & LO
	    		  						// ~ HI
	    		  						String hi = BinaryLogic.resizeBinary(
	    		  								ConversaoBase.converteDecimalParaBinario(resultadoMult),32,true).substring(16, 32);
	    		  						while(hi.length() < 32){
	    		  							hi = hi+"0";
	    		  						}
	    		  						Janela.dtm.setValueAt(
	    		  								hi,
	    		  								1,
	    		  								3);
	    		  						// ~ LO
	    		  						String lo = BinaryLogic.resizeBinary(
	    		  								ConversaoBase.converteDecimalParaBinario(resultadoMult),32,true).substring(16, 32);
	    		  						while(lo.length() < 32){
	    		  							lo = "0"+lo;
	    		  						}
	    		  						Janela.dtm.setValueAt(
	    		  								lo,
	    		  								2,
	    		  								3);
	    		  						
	    		  						// -- Valor Decimal HI & LO
	    		  						Janela.dtm.setValueAt(
	    		  								ConversaoBase.converteBinarioParaDecimal(
	    		  										BinaryLogic.resizeBinary(
	    		  												ConversaoBase.converteDecimalParaBinario(resultadoMult),32,true).substring(0, 16)),
	    		  								1,
	    		  								2);
	    		  						Janela.dtm.setValueAt(
	    		  								ConversaoBase.converteBinarioParaDecimal(
	    		  										BinaryLogic.resizeBinary(
	    		  												ConversaoBase.converteDecimalParaBinario(resultadoMult),32,true).substring(16, 32)),
	    		  								2,
	    		  								2);
	    		  					}
	    		  				}
	    		  			break;
	    		  			// MULTU
	    		  			case 25:
	    		  				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
	    		  					if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
	    		  						int resultadoMult = ArithmeticLogicUnit.mult(Integer.parseInt(valorReg2,10), Integer.parseInt(valorReg3,10));
	    		  						// -- Valor REGISTRADOR
	    		  						Janela.dtm.setValueAt(
	    		  								resultadoMult,
	    		  								r,
	    		  								2);
	    		  						Janela.dtm.setValueAt(
	    		  								BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario(resultadoMult),32,true),
	    		  								r,
	    		  								3);
	    		  						
	    		  						// -- Valor Binário HI & LO
	    		  						// ~ HI
	    		  						String hi = BinaryLogic.resizeBinary(
	    		  								ConversaoBase.converteDecimalParaBinario(resultadoMult),32,true).substring(16, 32);
	    		  						while(hi.length() < 32){
	    		  							hi = hi+"0";
	    		  						}
	    		  						Janela.dtm.setValueAt(
	    		  								hi,
	    		  								1,
	    		  								3);
	    		  						// ~ LO
	    		  						String lo = BinaryLogic.resizeBinary(
	    		  								ConversaoBase.converteDecimalParaBinario(resultadoMult),32,true).substring(16, 32);
	    		  						while(lo.length() < 32){
	    		  							lo = "0"+lo;
	    		  						}
	    		  						Janela.dtm.setValueAt(
	    		  								lo,
	    		  								2,
	    		  								3);
	    		  						
	    		  						// -- Valor Decimal HI & LO
	    		  						Janela.dtm.setValueAt(
	    		  								ConversaoBase.converteBinarioParaDecimal(
	    		  										BinaryLogic.resizeBinary(
	    		  												ConversaoBase.converteDecimalParaBinario(resultadoMult),32,true).substring(0, 16)),
	    		  								1,
	    		  								2);
	    		  						Janela.dtm.setValueAt(
	    		  								ConversaoBase.converteBinarioParaDecimal(
	    		  										BinaryLogic.resizeBinary(
	    		  												ConversaoBase.converteDecimalParaBinario(resultadoMult),32,true).substring(16, 32)),
	    		  								2,
	    		  								2);
	    		  					}
	    		  				}
	    		  			break;
	    		  			//DIV
	    		  			case 28:
	    		  				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
	    		  					if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
	    		  						Janela.dtm.setValueAt(
	    		  								ArithmeticLogicUnit.div(Integer.parseInt(valorReg2,10), Integer.parseInt(valorReg3,10)),
	    		  								1,
	    		  								2);
	    		  						Janela.dtm.setValueAt(
	    		  								Integer.parseInt(valorReg2,10)%Integer.parseInt(valorReg3,10),
	    		  								2,
	    		  								2);
	    		  						Janela.dtm.setValueAt(
	    		  								ArithmeticLogicUnit.div(Integer.parseInt(valorReg2,10), Integer.parseInt(valorReg3,10)),
	    		  								r,
	    		  								2);
	    		  						Janela.dtm.setValueAt(
	    		  								BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario(
	    		  										ArithmeticLogicUnit.add(Integer.parseInt(valorReg2,10), Integer.parseInt(valorReg3,10))
	    		  										),32,true),
	    		  								r,
	    		  								3);
	    		  					}
	    		  				}
	    		  			break;
	    		  			//DIVU
	    		  			case 29:
	    		  				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
	    		  					if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
	    		  						Janela.dtm.setValueAt(
	    		  								ArithmeticLogicUnit.div(Integer.parseInt(valorReg2,10), Integer.parseInt(valorReg3,10)),
	    		  								1,
	    		  								2);
	    		  						Janela.dtm.setValueAt(
	    		  								Integer.parseInt(valorReg2,10)%Integer.parseInt(valorReg3,10),
	    		  								2,
	    		  								2);
	    		  						Janela.dtm.setValueAt(
	    		  								ArithmeticLogicUnit.div(Integer.parseInt(valorReg2,10), Integer.parseInt(valorReg3,10)),
	    		  								r,
	    		  								2);
	    		  						Janela.dtm.setValueAt(
	    		  								BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario(
	    		  										ArithmeticLogicUnit.add(Integer.parseInt(valorReg2,10), Integer.parseInt(valorReg3,10))
	    		  										),32,true),
	    		  								r,
	    		  								3);
	    		  					}
	    		  				}
	    		  			break;
	    		  			//ADDU
	    		  			case 30:
	    		  				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
	    		  					if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
	    		  						Janela.dtm.setValueAt(
	    		  								ArithmeticLogicUnit.addu(Integer.parseInt(valorReg2,10), Integer.parseInt(valorReg3,10)),
	    		  								r,
	    		  								2);
	    		  						Janela.dtm.setValueAt(
	    		  								BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario(
	    		  										ArithmeticLogicUnit.add(Integer.parseInt(valorReg2,10), Integer.parseInt(valorReg3,10))
	    		  										),32,true),
	    		  								r,
	    		  								3);
	    		  					}
	    		  				}
	    		  			break;
	    		  			// XOR
	    		  			case 38:
	    		  				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
	    		  					if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
	    		  						Janela.dtm.setValueAt(
	    		  								ConversaoBase.converteBinarioParaDecimal(BinaryLogic.xor(valorReg2,valorReg3)),
	    		  								r,
	    		  								2);
	    		  						Janela.dtm.setValueAt(
	    		  								BinaryLogic.resizeBinary(BinaryLogic.xor(valorReg2,valorReg3),32,true),
	    		  								r,
	    		  								3);
	    		  					}
	    		  				}
	    		  			break;
	    		  			// NOR
	    		  			case 39:
	    		  				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
	    		  					if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
	    		  						Janela.dtm.setValueAt(
	    		  								ConversaoBase.converteBinarioParaDecimal(BinaryLogic.nor(valorReg2,valorReg3)),
	    		  								r,
	    		  								2);
	    		  						Janela.dtm.setValueAt(
	    		  								BinaryLogic.resizeBinary(BinaryLogic.nor(valorReg2,valorReg3),32,true),
	    		  								r,
	    		  								3);
	    		  					}
	    		  				}
	    		  			break;
	    		  				
    		  				default:
	    		  				JOptionPane.showMessageDialog(
	    		  						null,
	    		  						"<html>"
	    		  						+ "Lamentamos, mas o operador <b style='font-size:9px; color: red;'>"+ArraysLists.operadores.get(i)+"</b>, localizado na linha <b style='font-size:9px; color: red;'>"+linhaAtual+"</b>,<br>"
	    		  						+ " não poderá ser executado.<br>"
	    		  						+ "O mesmo ainda não foi completamente habilitado.<br>"
	    		  						+ "<i>Tente utilizá-lo na próxima revisão do sistema.</i>"
	    		  						+ "</html>",
	    		  						"Operador não implementado",
	    		  						JOptionPane.ERROR_MESSAGE);
	    		  			break;
	    		  		}
	    		  		break;
	    		  		
	    		  		
	    		  	// -- TIPO I (LOAD/STORE)
	    		  	case 3:
	    		  		break;
	    		  		
	    		  		
	    		  	// -- TIPO R (JUMP/BRANCH)
	    		  	case 4:
	    		  		
	    		  		break;
	    		  		
	    		  		
	    		  	// -- TIPO I (JUMP/BRANCH)
	    		  	case 5:
	    		  		for(int r = 0; r < Janela.dtm.getRowCount(); r++){
	    		  			if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
	    		  				valorReg = Janela.dtm.getValueAt(r, 2).toString();
	    		  			}
	    		  			if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(1).toString())){
	    		  				valorReg2 = Janela.dtm.getValueAt(r, 2).toString();
	    		  			}
	    		  		}
	    		  		switch(ArraysLists.operadores.get(i).getId()){
	    		  			// BEQ
	    		  			case 17:
	    		  				resultado = ArithmeticLogicUnit.beq(Integer.parseInt(valorReg),Integer.parseInt(valorReg2));
	    		  				//System.out.println("beq: "+resultado);
	    		  				break;
	    		  			// BNE	
	    		  			case 18:
	    		  				resultado = ArithmeticLogicUnit.bne(Integer.parseInt(valorReg),Integer.parseInt(valorReg2));
	    		  				//System.out.println("bne: "+resultado);
	    		  				break;
	    		  		}
	    		  		Matcher matcher5 = enderecoTipoI.matcher(linha);
						if(matcher5.find()) {
							enderecoOuLabel = matcher5.group().substring(1);
							enderecoOuLabel = enderecoOuLabel.replaceAll("[\\^ ]", "");
						}
						if(ArraysLists.arrLabel.size() != 0){
						for(int l = 0; l < ArraysLists.arrLabel.size(); l++){
							if(ArraysLists.arrLabel.get(l).equals(enderecoOuLabel)){
								for(int x = 0; x < linhasLidas.size(); x++){
									if(linhasLidas.get(x).equals(enderecoOuLabel+":")){
										//System.out.println("Achei a label na posição: "+l);
										for(int m = x+1; m < linhasLidas.size(); m++){
											//System.out.println(linhasLidas.get(m));
											if(linhasLidas.get(m).contains(ArraysLists.operadores.get(i).toString()) &&
													ArraysLists.operadores.get(i).toString().equals("bne")){
													resultado = ArithmeticLogicUnit.bne(Integer.parseInt(valorReg),Integer.parseInt(valorReg2));
											}
											if(linhasLidas.get(m).contains(ArraysLists.operadores.get(i).toString()) &&
													ArraysLists.operadores.get(i).toString().equals("beq")){
													resultado = ArithmeticLogicUnit.beq(Integer.parseInt(valorReg),Integer.parseInt(valorReg2));
											}
											if(resultado) Executar(linhasLidas.get(m));
										}
									}
								}
							} else {
								JOptionPane.showMessageDialog(null,
										"<html>"
										+ "Prezado usuário, o sistema não poderá continuar a execução do código,<br>"
										+ "pois não reconhecemos a label:<br><br>"
										+ "<b style='color:red;'>"+enderecoOuLabel+"</b><br>"
										+ "Utilizada na linha: <b style='color:red;'>"+linhaAtual+"</b><br>"
										+ "<i>Verifique se a mesma foi adicionada corretamente.</i>"
										+ "</html>",
										"Erro na Compilação",
										JOptionPane.ERROR_MESSAGE);
							}
						}
						Janela.dtm.setValueAt("Ativo", 0, 4);
						Janela.dtm.setValueAt(lblAddress, 0, 2);
						Janela.dtm.setValueAt(BinaryLogic.resizeBinary(lblAddress,32,true),
												0,
												3);
						} else {
							JOptionPane.showMessageDialog(null,
									"<html>"
									+ "Prezado usuário, o sistema não poderá continuar a execução do código,<br>"
									+ "pois não reconhecemos a label:<br><br>"
									+ "<b style='color:red;'>"+enderecoOuLabel+"</b><br>"
									+ "Utilizada na linha: <b style='color:red;'>"+linhaAtual+"</b><br>"
									+ "<i>Verifique se a mesma foi adicionada corretamente.</i>"
									+ "</html>",
									"Erro na Compilação",
									JOptionPane.ERROR_MESSAGE);
						}
	    		  		break;
	    		  }
	    	  }
	      }
	    }
	}
	public void Reexecutar(String endLabel){
		String instrucao = null;
		for(int i = 0; i < Janela.dtmMem.getRowCount(); i++){
			if(Janela.dtmMem.getValueAt(i, 0).equals(endLabel)){
				instrucao = Janela.dtmMem.getValueAt(i,1).toString();
				instrucao += Janela.dtmMem.getValueAt(i+1,1).toString();
				instrucao += Janela.dtmMem.getValueAt(i+2,1).toString();
				instrucao += Janela.dtmMem.getValueAt(i+3,1).toString();
				System.out.println(instrucao);
				for(int j = 0; j < ArraysLists.operadores.size(); j++){
					switch(ArraysLists.operadores.get(j).getTipoIntrucao()){
					// -- TIPO I
					case 0:
						if(ArraysLists.operadores.get(j).getValorBits().equals(instrucao.substring(0, 6))){
						System.out.println("Instrução tipo I");
						System.out.println("Achei um "+ArraysLists.operadores.get(j));
						}
					j = ArraysLists.operadores.size();
					break;
					// -- TIPO J
					case 1:
						if(ArraysLists.operadores.get(j).getValorBits().equals(instrucao.substring(0, 6))){
						System.out.println("Instrução tipo J");
						System.out.println("Achei um "+ArraysLists.operadores.get(j));
						}
					j = ArraysLists.operadores.size();
					break;
					// -- TIPO R
					case 2:
						if(ArraysLists.operadores.get(j).getValorBits().equals(instrucao.substring(0, 6))){
						System.out.println("Instrução tipo R");
						System.out.println("Achei um "+ArraysLists.operadores.get(j));
						}
					j = ArraysLists.operadores.size();
					break;
					// -- TIPO I (LOAD/STORE)
					case 3:
						if(ArraysLists.operadores.get(j).getValorBits().equals(instrucao.substring(0, 6))){
						System.out.println("Instrução tipo I (LOAD/STORE)");
						System.out.println("Achei um "+ArraysLists.operadores.get(j));
						}
					j = ArraysLists.operadores.size();
					break;
					// -- TIPO R (JUMP/BRANCH)
					case 4:
						if(ArraysLists.operadores.get(j).getValorBits().equals(instrucao.substring(0, 6))){
						System.out.println("Instrução tipo R (JUMP/BRANCH)");
						System.out.println("Achei um "+ArraysLists.operadores.get(j));
						}
					j = ArraysLists.operadores.size();
					break;
					// -- TIPO I (JUMP/BRANCH)
					case 5:
						if(ArraysLists.operadores.get(j).getValorBits().equals(instrucao.substring(0, 6))){
						System.out.println("Instrução tipo I (JUMP/BRANCH)");
						System.out.println("Achei um "+ArraysLists.operadores.get(j));
						}
					j = ArraysLists.operadores.size();
					break;
					}
				}
			}
		}
	}
}
