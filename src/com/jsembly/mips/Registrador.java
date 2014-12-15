package com.jsembly.mips;

import javax.swing.table.DefaultTableModel;

import com.jsembly.main.ArraysLists;

public enum Registrador {
	// Registrador
	//  exemplo: $s0(id,valorInicial,valorBits,tipoRegistrador,preservadoChamadas,ativo)
	
	// AINDA FALTAM INFORMA��ES
	pc(999,0,"","",false,false),
	hi(999,0,"","",false,false),
	lo(999,0,"","",false,false),
	
	// --- Constante Zero (0)
	$zero(0,0,"00000","Constante Zero",false,false),
	// -- Indefinido
	$at(1,0,"00001","Assembly Temporary",false,false),
	// --- Resultados e Avalia��es de Express�es
	$v0(2,0,"00010","Resultados e Avalia��es de Express�es",false,false),$v1(3,0,"00011","Resultados e Avalia��es de Express�es",false,false),
	// --- Argumentos
	$a0(4,0,"00100","Argumentos",true,false),$a1(5,0,"00101","Argumentos",true,false),$a2(6,0,"00110","Argumentos",true,false),$a3(7,0,"00111","Argumentos",true,false),
	// --- Tempor�rios part 1
	$t0(8,0,"01000","Tempor�rio",false,false),$t1(9,0,"01001","Tempor�rio",false,false),$t2(10,0,"01010","Tempor�rio",false,false),$t3(11,0,"01011","Tempor�rio",false,false),
	$t4(12,0,"01100","Tempor�rio",false,false),$t5(13,0,"01101","Tempor�rio",false,false),$t6(14,0,"01110","Tempor�rio",false,false),$t7(15,0,"01111","Tempor�rio",false,false),
	// --- Salvos
	$s0(16,0,"10000","Salvo",true,false),$s1(17,0,"10001","Salvo",true,false),$s2(18,0,"10010","Salvo",true,false),$s3(19,0,"10011","Salvo",true,false),
	$s4(20,0,"10100","Salvo",true,false),$s5(21,0,"10101","Salvo",true,false),$s6(22,0,"10110","Salvo",true,false),$s7(23,0,"10111","Salvo",true,false),
	// --- Tempor�rios part 2
	$t8(24,0,"11000","Tempor�rio",false,false),$t9(25,0,"11001","Tempor�rio",false,false),
	// -- Indefinido
	$k0(26,0,"11010","Reservado para Interrup��es",false,false),
	$k1(27,0,"11011","Reservado para Interrup��es",false,false),
	// --- Ponteiro Global
	$gp(28,0,"11100","Ponteiro Global",true,false),
	// --- Ponteiro para Pilha
	$sp(29,0,"11101","Ponteiro para Pilha",true,false),
	// --- Ponteiro para Frame
	$fp(30,0,"11110","Ponteiro para Frame",true,false),
	// --- Endere�o de Retorno
	$ra(31,0,"11111","Endere�o de Retorno",true,false);
	
	private int id;
	private int valorInicial;
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
	
	
	Registrador(int id,int valorInicial, String valorBits, String tipoRegistrador, boolean perservadorEmChamadas, boolean ativo){
		this.setId(id);
		this.setValorBits(valorBits);
		this.setTipoRegistrador(tipoRegistrador);
		this.setPreservadoChamadas(perservadorEmChamadas);
		this.setAtivo(ativo);
		this.setValorInicial(valorInicial);
	}
	public boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	
	public static void LimparAtividade(DefaultTableModel dtm){
  		for(int d = 0; d < dtm.getRowCount(); d++){
  			for(int r = 0; r < ArraysLists.registradores.size(); r ++){
  	  			if(dtm.getValueAt(d, 0).equals(ArraysLists.registradores.get(r).toString())){
  	  				dtm.setValueAt("Inativo", d, 4);
  	  			}
  			}
  		}
  		dtm.fireTableDataChanged();
	}
	public static void AtualizarAtividade(DefaultTableModel dtm){
  		for(int d = 0; d < dtm.getRowCount(); d++){
  			if(dtm.getValueAt(d, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
  				dtm.setValueAt("Ativo", d, 4);
  			}
  		}
  		dtm.fireTableDataChanged();
	}
	public int getValorInicial() {
		return valorInicial;
	}
	public void setValorInicial(int valorInicial) {
		this.valorInicial = valorInicial;
	}
}
