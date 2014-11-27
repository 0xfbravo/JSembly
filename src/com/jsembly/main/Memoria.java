package com.jsembly.main;

import java.util.HashMap;

import javax.swing.table.DefaultTableModel;

public class Memoria {

	HashMap<String,String> memoria = new HashMap<String,String>();
	public Memoria(int memMax, DefaultTableModel dtm){
		/*
		 * Valores em HEXA
		 *
		 
		for(int i = 0; i < memMax; i +=4){
			if(i<10){ this.AtualizarMemoria("00000"+i, "0x"+ConversaoBase.converteBinarioParaHexadecimal("00000000"), dtm); }
			else if(100 > i && i >= 10){ this.AtualizarMemoria("0000"+i, "0x"+ConversaoBase.converteBinarioParaHexadecimal("00000000"), dtm); }
			else if(1000 > i && i >= 100){ this.AtualizarMemoria("000"+i, "0x"+ConversaoBase.converteBinarioParaHexadecimal("00000000"), dtm); }
			else if(10000 > i && i >= 1000){ this.AtualizarMemoria("00"+i, "0x"+ConversaoBase.converteBinarioParaHexadecimal("00000000"), dtm);  }
			else if(100000 > i && i >= 10000){ this.AtualizarMemoria("0"+i, "0x"+ConversaoBase.converteBinarioParaHexadecimal("00000000"), dtm); }
			else { this.AtualizarMemoria(""+i, "0x"+ConversaoBase.converteBinarioParaHexadecimal("00000000"), dtm);  }
		}*/
		for(int i = 0; i < memMax; i +=4){
			if(i<10){ this.AtualizarMemoria("00000"+i, "00000000", dtm); }
			else if(100 > i && i >= 10){ this.AtualizarMemoria("0000"+i, "00000000", dtm); }
			else if(1000 > i && i >= 100){ this.AtualizarMemoria("000"+i, "00000000", dtm); }
			else if(10000 > i && i >= 1000){ this.AtualizarMemoria("00"+i, "00000000", dtm);  }
			else if(100000 > i && i >= 10000){ this.AtualizarMemoria("0"+i, "00000000", dtm); }
			else { this.AtualizarMemoria(""+i, "00000000", dtm);  }
		}
	}
	public void AtualizarMemoria(String key, String valor,DefaultTableModel dtm){
		if(memoria.containsKey(key)){
			//System.out.println("Key: "+ key);
			//System.out.println("Valor Hashmap: "+ memoria.get(key));
			memoria.put(key, valor);
			//System.out.println("Novo Valor Hashmap: "+ memoria.get(key));
			for(int i = 0; i < dtm.getRowCount(); i++){
				if(dtm.getValueAt(i, 0).equals(key)){
					//System.out.println("Endereço Tabela: "+dtm.getValueAt(i, 0));
					//System.out.println("Valor Tabela: "+dtm.getValueAt(i, 1));
					dtm.setValueAt(memoria.get(key), i, 1);
					//System.out.println("Novo Valor Tabela: "+dtm.getValueAt(i, 1));
				}
			}
			dtm.fireTableDataChanged();
		} else {
			memoria.put(key, valor);
			dtm.addRow(new Object[]{key, memoria.get(key)});
			//System.out.println(key+","+valor);
		}
	}
	public void LimparMemoria(DefaultTableModel dtm){
		for(int i = 0; i < dtm.getRowCount(); i++){
			if(dtm.getValueAt(i, 1).equals("00000000") == false){
				memoria.put(dtm.getValueAt(i,0).toString(), "00000000");
				//System.out.println(dtm.getValueAt(i, 1).toString());
				dtm.setValueAt(memoria.get(dtm.getValueAt(i,0).toString()), i, 1);
			}
		}
	}
	public String BuscarMemoria(DefaultTableModel dtm){
		String key = "Sem Memória";
		int i = 0;
		while(i < dtm.getRowCount()){
			if(dtm.getValueAt(i, 1).equals("00000000")){
				//System.out.println("Achei Memória livre!");
				key = dtm.getValueAt(i, 0).toString();
				break;
			}
			i++;
		}
		return key;
	}
	public String BuscarEndereco(String valor,DefaultTableModel dtm){
		String key = "Sem Endereço";
		int i = 0;
		while(i < dtm.getRowCount()){
			if(dtm.getValueAt(i, 1).equals(valor)){
				//System.out.println("Achei Memória livre!");
				key = dtm.getValueAt(i, 0).toString();
			}
			i+=4;
		}
		return key;
	}
	
	public void AlocarMemoria(String lm,DefaultTableModel dtm){
		//System.out.println(lm.substring(0, 8));
		//System.out.println(lm.substring(8, 16));
		//System.out.println(lm.substring(16, 24));
		//System.out.println(lm.substring(24, 32));
		AtualizarMemoria(BuscarMemoria(dtm),lm.substring(0, 8),dtm);
		AtualizarMemoria(BuscarMemoria(dtm),lm.substring(8, 16),dtm);
		AtualizarMemoria(BuscarMemoria(dtm),lm.substring(16, 24),dtm);
		AtualizarMemoria(BuscarMemoria(dtm),lm.substring(24, 32),dtm);
	}
}
