package com.jsembly.mips;

public enum Registrador {
	// Registrador
	//  exemplo: $s0(id,valorBits,tipoRegistrador)
	
	// AINDA FALTAM INFORMA��ES
	pc(999,"[ Faltam informa��es ]","[ Faltam informa��es ]",false),
	hi(999,"[ Faltam informa��es ]","[ Faltam informa��es ]",false),
	lo(999,"[ Faltam informa��es ]","[ Faltam informa��es ]",false),
	$k0(26,"11010 [ Faltam informa��es ]","[ Faltam informa��es ]",false),
	$k1(27,"11011 [ Faltam informa��es ]","[ Faltam informa��es ]",false),
	$at(1,"00001 [ Faltam informa��es ]","[ Faltam informa��es ]",false),
	
	// --- Constante Zero (0)
	$zero(0,"00000","Constante Zero",false),
	// --- Resultados e Avalia��es de Express�es
	$v0(2,"00010","Resultados e Avalia��es de Express�es",false),$v1(3,"00011","Resultados e Avalia��es de Express�es",false),
	// --- Argumentos
	$a0(4,"00100","Argumentos",true),$a1(5,"00101","Argumentos",true),$a2(6,"00110","Argumentos",true),$a3(7,"00111","Argumentos",true),
	// --- Tempor�rios part 1
	$t0(8,"01000","Tempor�rio",false),$t1(9,"01001","Tempor�rio",false),$t2(10,"01010","Tempor�rio",false),$t3(11,"01011","Tempor�rio",false),
	$t4(12,"01100","Tempor�rio",false),$t5(13,"01101","Tempor�rio",false),$t6(14,"01110","Tempor�rio",false),$t7(15,"01111","Tempor�rio",false),
	// --- Salvos
	$s0(16,"10000","Salvo",true),$s1(17,"10001","Salvo",true),$s2(18,"10010","Salvo",true),$s3(19,"10011","Salvo",true),
	$s4(20,"10100","Salvo",true),$s5(21,"10101","Salvo",true),$s6(22,"10110","Salvo",true),$s7(23,"10111","Salvo",true),
	// --- Tempor�rios part 2
	$t8(24,"11000","Tempor�rio",false),$t9(25,"11001","Tempor�rio",false),
	// --- Ponteiro Global
	$gp(28,"11100","Ponteiro Global",true),
	// --- Ponteiro para Pilha
	$sp(29,"11101","Ponteiro para Pilha",true),
	// --- Ponteiro para Frame
	$fp(30,"11110","Ponteiro para Frame",true),
	// --- Endere�o de Retorno
	$ra(31,"11111","Endere�o de Retorno",true);
	
	private int id;
	private String valorBits;
	private String tipoRegistrador;
	private boolean preservadoChamadas;
	
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
	
	
	Registrador(int id, String valorBits, String tipoRegistrador, boolean perservadorEmChamadas){
		this.setId(id);
		this.setValorBits(valorBits);
		this.setTipoRegistrador(tipoRegistrador);
		this.setPreservadoChamadas(perservadorEmChamadas);
	}
}
