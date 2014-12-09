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

public class Montar {

	public Montar(){
		String lblAddress = "";
		String novoEndLbl = "";
		boolean erro = false;
		int codigoErro = 0;
		int linhaAtual = 0;
		int regSalvar = 0;
		String valorReg = null;
		int decimal;
		String binario = null;
		ArraysLists.arrLabel.clear();
		String lm = "Sem Linguagem de Máquina";
		Registrador.LimparAtividade(Janela.dtm);
		Janela.dtmExec.setRowCount(0);
		Memoria.LimparMemoria(Janela.dtmMem);
		Janela.dtmMem.fireTableDataChanged();
		Janela.painelLinguagemMaquina.setText("");
		
		ArrayList<String> linhasLidas = Utilidades.LerArquivo(Janela.temp.getAbsolutePath());
		Pattern operador = Pattern.compile("\\w+", Pattern.CASE_INSENSITIVE);
		Pattern registrador = Pattern.compile("[$]\\w+", Pattern.CASE_INSENSITIVE);
		Pattern endereco = Pattern.compile(" \\w+|[0-9]|\\,w+|,[0-9]", Pattern.CASE_INSENSITIVE);
		Pattern enderecoTipoI = Pattern.compile("[,][ ]{0,9999}\\w+", Pattern.CASE_INSENSITIVE);
		Pattern label = Pattern.compile("\\b\\w{1,8}[:]", Pattern.CASE_INSENSITIVE);
		
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
		    		  
	    		  	  String enderecoOuLabel = null;
	    		  	  String endLbl = null;
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
				    		  			lm = TipoInstrucao.InstrucaoTipoI(ArraysLists.operadores.get(i).getValorBits(),ArraysLists.regEncontrados.get(0).getValorBits(),ArraysLists.regEncontrados.get(1).getValorBits(),binario);
				    		  			if(BinaryLogic.chkOverflow(lm, linhaAtual,ArraysLists.operadores.get(i),ArraysLists.regEncontrados,binario)) break;
		    		  					if(BinaryLogic.chkUnderflow(lm, linhaAtual,ArraysLists.operadores.get(i),ArraysLists.regEncontrados,binario)) break;
		    		  					for(int r = 0; r < Janela.dtm.getRowCount(); r++){
					    		  			if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(1).toString())){
					    		  				valorReg = Janela.dtm.getValueAt(r, 2).toString();
					    		  			}
					    		  		}
		    		  					Memoria.AlocarMemoria(lm, Janela.dtmMem);
		    		  					Janela.painelLinguagemMaquina.append(lm+"\n");
		    		  					Janela.dtmExec.addRow(new Object[]{
		    		  							Memoria.BuscarEndereco(lm.substring(0, 8), Janela.dtmMem),
		    		  							"0x"+ConversaoBase.converteBinarioParaHexadecimal(lm),
		    		  							ArraysLists.operadores.get(i)+" $"+ArraysLists.regEncontrados.get(0).getId()+",$"+ArraysLists.regEncontrados.get(1).getId()+","+enderecoOuLabel,
		    		  							linhaAtual+": "+ArraysLists.operadores.get(i)+" "+ArraysLists.regEncontrados.get(0).toString()+","+ArraysLists.regEncontrados.get(1).toString()+","+enderecoOuLabel});
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
			    		  				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
			    		  					if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
			    		  						Janela.dtm.setValueAt(ConversaoBase.converteBinarioParaDecimal(binario.substring(16, 26)), r, 2);
			    		  						Janela.dtm.setValueAt( BinaryLogic.resizeBinary(binario, 32, true), r, 3);
			    		  					}
			    		  				}
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
		    		  					for(int r = 0; r < Janela.dtm.getRowCount(); r++){
					    		  			if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(1).toString())){
					    		  				valorReg = Janela.dtm.getValueAt(r, 2).toString();
					    		  			}
					    		  		}
		    		  					Memoria.AlocarMemoria(lm, Janela.dtmMem);
		    		  					Janela.painelLinguagemMaquina.append(lm+"\n");
		    		  					Janela.dtmExec.addRow(new Object[]{
		    		  							Memoria.BuscarEndereco(lm.substring(0, 8), Janela.dtmMem),
		    		  							"0x"+ConversaoBase.converteBinarioParaHexadecimal(lm),
		    		  							ArraysLists.operadores.get(i)+" $"+ArraysLists.regEncontrados.get(0).getId()+",$"+ArraysLists.regEncontrados.get(1).getId()+","+enderecoOuLabel,
		    		  							linhaAtual+": "+ArraysLists.operadores.get(i)+" "+ArraysLists.regEncontrados.get(0).toString()+","+ArraysLists.regEncontrados.get(1).toString()+","+enderecoOuLabel});
					    		  		boolean b = ArithmeticLogicUnit.slti(Integer.parseInt(valorReg,10), decimal);
			    		  				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
			    		  					if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
			    		  						if(b) {
			    		  							Janela.dtm.setValueAt(Integer.parseInt(valorReg,10), r, 2);
			    		  							Janela.dtm.setValueAt( BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario(Integer.parseInt(valorReg,10)), 32, true), r, 3);
			    		  							}
			    		  						else {
			    		  							Janela.dtm.setValueAt(0, r, 2);
			    		  							Janela.dtm.setValueAt( BinaryLogic.resizeBinary("0", 32, true), r, 3);
			    		  							}
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
							lm = TipoInstrucao.InstrucaoTipoJ(ArraysLists.operadores.get(i).getValorBits(),novoEndLbl);
							System.out.println(lm);
							if(BinaryLogic.chkOverflow(lm, linhaAtual,ArraysLists.operadores.get(i),binario)) break;
		  					if(BinaryLogic.chkUnderflow(lm, linhaAtual,ArraysLists.operadores.get(i),binario)) break;
							Memoria.AlocarMemoria(lm, Janela.dtmMem);
							Janela.painelLinguagemMaquina.append(lm+"\n");
							Janela.dtmExec.addRow(new Object[]{
		    		  				Memoria.BuscarEndereco(lm.substring(0, 8), Janela.dtmMem),
		    		  				"0x"+ConversaoBase.converteBinarioParaHexadecimal(lm),
		    		  				ArraysLists.operadores.get(i)+" "+lblAddress,
		    		  				linhaAtual+": "+ArraysLists.operadores.get(i)+" "+enderecoOuLabel});
		    		  		break;
		    		  		
		    		  		
		    		  	// -- TIPO R
		    		  	case 2:
		    		  		int p;
    		  				String q = null;
    		  				int valorRegInt = 0;
    		  				String valorReg2 = null;
		    		  		String valorReg3 = null;
    		  				Matcher matcherR = enderecoTipoI.matcher(linha);
    		  				if(matcherR.find()) { 
    		  					enderecoOuLabel = matcherR.group().substring(1);
    		  					enderecoOuLabel = enderecoOuLabel.replaceAll("[\\^ ]", "");
    		  					p = Integer.parseInt(enderecoOuLabel);
    		  					q = BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario(p), 5, true);
    		  					lm = TipoInstrucao.InstrucaoTipoR("00000",ArraysLists.regEncontrados.get(1).getValorBits(),"00000",q,ArraysLists.operadores.get(i).getValorBits());
    		  	 		  		ArraysLists.regEncontrados.get(0).setAtivo(true);
    		    		  		Registrador.AtualizarAtividade(Janela.dtm);
    		    		  		if(BinaryLogic.chkOverflow(lm, linhaAtual,ArraysLists.operadores.get(i),ArraysLists.regEncontrados)) break;
    		  					if(BinaryLogic.chkUnderflow(lm, linhaAtual,ArraysLists.operadores.get(i),ArraysLists.regEncontrados)) break;
    		    		  		Memoria.AlocarMemoria(lm, Janela.dtmMem);
    		    		  		Janela.painelLinguagemMaquina.append(lm+"\n");
    		    		  		Janela.dtmExec.addRow(new Object[]{
    		    		  				Memoria.BuscarEndereco(lm.substring(0, 8), Janela.dtmMem),
    		    		  				"0x"+ConversaoBase.converteBinarioParaHexadecimal(lm),
    		    		  				ArraysLists.operadores.get(i)+" $"+ArraysLists.regEncontrados.get(0).getId()+",$"+ArraysLists.regEncontrados.get(1).getId()+","+enderecoOuLabel,
    		    		  				linhaAtual+": "+ArraysLists.operadores.get(i)+" "+ArraysLists.regEncontrados.get(0).toString()+","+ArraysLists.regEncontrados.get(1).toString()+","+enderecoOuLabel});
    		    		  		for(int r = 0; r < Janela.dtm.getRowCount(); r++){
			    		  			if(Janela.dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(1).toString())){
			    		  				valorRegInt = Integer.parseInt(Janela.dtm.getValueAt(r, 2).toString());
			    		  			}
			    		  		}
    		  				} else {
								lm = TipoInstrucao.InstrucaoTipoR(ArraysLists.regEncontrados.get(0).getValorBits(),ArraysLists.regEncontrados.get(1).getValorBits(),ArraysLists.regEncontrados.get(2).getValorBits(),"00000",ArraysLists.operadores.get(i).getValorBits());
				 		  		ArraysLists.regEncontrados.get(0).setAtivo(true);
			    		  		Registrador.AtualizarAtividade(Janela.dtm);
			    		  		if(BinaryLogic.chkOverflow(lm, linhaAtual,ArraysLists.operadores.get(i),ArraysLists.regEncontrados)) break;
			  					if(BinaryLogic.chkUnderflow(lm, linhaAtual,ArraysLists.operadores.get(i),ArraysLists.regEncontrados)) break;
			    		  		Memoria.AlocarMemoria(lm, Janela.dtmMem);
			    		  		Janela.painelLinguagemMaquina.append(lm+"\n");
								Janela.dtmExec.addRow(new Object[]{
			    		  				Memoria.BuscarEndereco(lm.substring(0, 8), Janela.dtmMem),
			    		  				"0x"+ConversaoBase.converteBinarioParaHexadecimal(lm),
			    		  				ArraysLists.operadores.get(i)+" $"+ArraysLists.regEncontrados.get(0).getId()+",$"+ArraysLists.regEncontrados.get(1).getId()+",$"+ArraysLists.regEncontrados.get(2).getId(),
			    		  				linhaAtual+": "+ArraysLists.operadores.get(i)+" "+ArraysLists.regEncontrados.get(0).toString()+","+ArraysLists.regEncontrados.get(1).toString()+","+ArraysLists.regEncontrados.get(2).toString()});
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
		    		  			// NOR
		    		  			case 12:
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
		    		  			// SLL
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
		    		  		
		    		  		
		    		  	// -- TIPO I (JUMP/BRANCH)
		    		  	case 3:
		    		  		break;
		    		  		
		    		  		
		    		  	// -- TIPO I (JUMP/BRANCH)
		    		  	case 4:
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
									novoEndLbl = ArraysLists.labelAddress.get(l).toString();
									lm = TipoInstrucao.InstrucaoTipoI(ArraysLists.operadores.get(i).getValorBits(),ArraysLists.regEncontrados.get(0).getValorBits(),ArraysLists.regEncontrados.get(1).getValorBits(),endLbl);
									Memoria.AlocarMemoria(lm, Janela.dtmMem);
									Janela.painelLinguagemMaquina.append(lm+"\n");
									Janela.dtmExec.addRow(new Object[]{
				    		  				Memoria.BuscarEndereco(lm.substring(0, 8), Janela.dtmMem),
				    		  				"0x"+ConversaoBase.converteBinarioParaHexadecimal(lm),
				    		  				ArraysLists.operadores.get(i)+" $"+ArraysLists.regEncontrados.get(0).getId()+",$"+ArraysLists.regEncontrados.get(1).getId()+","+novoEndLbl,
				    		  				linhaAtual+": "+ArraysLists.operadores.get(i)+" "+ArraysLists.regEncontrados.get(0).toString()+","+ArraysLists.regEncontrados.get(1).toString()+","+enderecoOuLabel});
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
		    		  Janela.painelCima.setSelectedComponent(Janela.linguagemMaquina);
		    		  break;
		    	  }
		      }
		    }
		}
	}
	}
}
