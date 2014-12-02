package com.jsembly.mips;

public class TipoInstrucao {
	static String formatacao;
	
	public static String InstrucaoTipoI(String instrucao, String registrador1, String registrador2,String endereco){
		formatacao = instrucao+registrador1+registrador2+endereco;
		return formatacao;
	}
	public static String InstrucaoTipoI(String instrucao, String registrador1,String endereco){
		formatacao = instrucao+registrador1+endereco;
		return formatacao;
	}
	public static String InstrucaoTipoI(String instrucao,String endereco){
		formatacao = instrucao+endereco;
		return formatacao;
	}
	public static String InstrucaoTipoJ(String instrucao,String endereco){
		formatacao = instrucao+endereco;
		return formatacao;
	}
	public static String InstrucaoTipoR(String registrador1, String registrador2, String registrador3, String shamt, String funcao){
		formatacao = "000000"+registrador2+registrador3+registrador1+shamt+funcao;
		return formatacao;
	}
	public static String InstrucaoTipoR(String registrador1, String registrador2, String shamt, String funcao){
		formatacao = "000000"+registrador2+registrador1+shamt+funcao;
		return formatacao;
	}
	public static String InstrucaoTipoR(String registrador1, String shamt, String funcao){
		formatacao = "000000"+registrador1+shamt+funcao;
		return formatacao;
	}
}
