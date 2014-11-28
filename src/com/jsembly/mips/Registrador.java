package com.jsembly.mips;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.jsembly.main.ArraysLists;

public enum Registrador {
	// Registrador
	//  exemplo: $s0(id,valorBits,tipoRegistrador)
	
	// AINDA FALTAM INFORMAÇÕES
	pc(999,"[ Faltam informações ]","[ Faltam informações ]",false,false),
	hi(999,"[ Faltam informações ]","[ Faltam informações ]",false,false),
	lo(999,"[ Faltam informações ]","[ Faltam informações ]",false,false),
	$k0(26,"11010 [ Faltam informações ]","[ Faltam informações ]",false,false),
	$k1(27,"11011 [ Faltam informações ]","[ Faltam informações ]",false,false),
	$at(1,"00001 [ Faltam informações ]","[ Faltam informações ]",false,false),
	
	// --- Constante Zero (0)
	$zero(0,"00000","Constante Zero",false,false),
	// --- Resultados e Avaliações de Expressões
	$v0(2,"00010","Resultados e Avaliações de Expressões",false,false),$v1(3,"00011","Resultados e Avaliações de Expressões",false,false),
	// --- Argumentos
	$a0(4,"00100","Argumentos",true,false),$a1(5,"00101","Argumentos",true,false),$a2(6,"00110","Argumentos",true,false),$a3(7,"00111","Argumentos",true,false),
	// --- Temporários part 1
	$t0(8,"01000","Temporário",false,false),$t1(9,"01001","Temporário",false,false),$t2(10,"01010","Temporário",false,false),$t3(11,"01011","Temporário",false,false),
	$t4(12,"01100","Temporário",false,false),$t5(13,"01101","Temporário",false,false),$t6(14,"01110","Temporário",false,false),$t7(15,"01111","Temporário",false,false),
	// --- Salvos
	$s0(16,"10000","Salvo",true,false),$s1(17,"10001","Salvo",true,false),$s2(18,"10010","Salvo",true,false),$s3(19,"10011","Salvo",true,false),
	$s4(20,"10100","Salvo",true,false),$s5(21,"10101","Salvo",true,false),$s6(22,"10110","Salvo",true,false),$s7(23,"10111","Salvo",true,false),
	// --- Temporários part 2
	$t8(24,"11000","Temporário",false,false),$t9(25,"11001","Temporário",false,false),
	// --- Ponteiro Global
	$gp(28,"11100","Ponteiro Global",true,false),
	// --- Ponteiro para Pilha
	$sp(29,"11101","Ponteiro para Pilha",true,false),
	// --- Ponteiro para Frame
	$fp(30,"11110","Ponteiro para Frame",true,false),
	// --- Endereço de Retorno
	$ra(31,"11111","Endereço de Retorno",true,false);
	
	private int id;
	private String valorBits;
	private String tipoRegistrador;
	private boolean preservadoChamadas;
	private boolean ativo;
	
	public boolean isPreservadoChamadas() {
		return preservadoChamadas;
	}
	public void setPreservadoChamadas(boolean preservadoChamadas) {
		this.preservadoChamadas = preservadoChamadas;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getValorBits() {
		return valorBits;
	}
	public void setValorBits(String valorBits) {
		this.valorBits = valorBits;
	}
	public String getTipoRegistrador() {
		return tipoRegistrador;
	}
	public void setTipoRegistrador(String tipoRegistrador) {
		this.tipoRegistrador = tipoRegistrador;
	}
	
	
	Registrador(int id, String valorBits, String tipoRegistrador, boolean perservadorEmChamadas, boolean ativo){
		this.setId(id);
		this.setValorBits(valorBits);
		this.setTipoRegistrador(tipoRegistrador);
		this.setPreservadoChamadas(perservadorEmChamadas);
		this.setAtivo(ativo);
	}
	public boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	
	public static void LimparAtividade(DefaultTableModel dtm){
  		for(int d = 0; d < dtm.getRowCount(); d++){
  			//System.out.println("Valor da Linha:"+dtm.getValueAt(d, 0));
  			for(int r = 0; r < ArraysLists.registradores.size(); r ++){
  				//System.out.println("Registrador:"+ ArraysLists.registradores.get(r).toString());
  	  			if(dtm.getValueAt(d, 0).equals(ArraysLists.registradores.get(r).toString())){
  	  				//System.out.println("É Igual!");
  	  				dtm.setValueAt("Inativo", d, 3);
  	  			}
  			}
  		}
  		dtm.fireTableDataChanged();
	}
	public static void AtualizarAtividade(DefaultTableModel dtm){
  		for(int d = 0; d < dtm.getRowCount(); d++){
  			if(dtm.getValueAt(d, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
  				dtm.setValueAt("Ativo", d, 3);
  			}
  		}
  		dtm.fireTableDataChanged();
	}
}
