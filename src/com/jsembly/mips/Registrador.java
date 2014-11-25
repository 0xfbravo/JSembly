package com.jsembly.mips;

public enum Registrador {
	// Registrador
	//  exemplo: $s0(id,valorBits,tipoRegistrador)
	
	// AINDA FALTAM INFORMAÇÕES
	pc(999,"[ Faltam informações ]","[ Faltam informações ]",false),
	hi(999,"[ Faltam informações ]","[ Faltam informações ]",false),
	lo(999,"[ Faltam informações ]","[ Faltam informações ]",false),
	$k0(26,"11010 [ Faltam informações ]","[ Faltam informações ]",false),
	$k1(27,"11011 [ Faltam informações ]","[ Faltam informações ]",false),
	$at(1,"00001 [ Faltam informações ]","[ Faltam informações ]",false),
	
	// --- Constante Zero (0)
	$zero(0,"00000","Constante Zero",false),
	// --- Resultados e Avaliações de Expressões
	$v0(2,"00010","Resultados e Avaliações de Expressões",false),$v1(3,"00011","Resultados e Avaliações de Expressões",false),
	// --- Argumentos
	$a0(4,"00100","Argumentos",true),$a1(5,"00101","Argumentos",true),$a2(6,"00110","Argumentos",true),$a3(7,"00111","Argumentos",true),
	// --- Temporários part 1
	$t0(8,"01000","Temporário",false),$t1(9,"01001","Temporário",false),$t2(10,"01010","Temporário",false),$t3(11,"01011","Temporário",false),
	$t4(12,"01100","Temporário",false),$t5(13,"01101","Temporário",false),$t6(14,"01110","Temporário",false),$t7(15,"01111","Temporário",false),
	// --- Salvos
	$s0(16,"10000","Salvo",true),$s1(17,"10001","Salvo",true),$s2(18,"10010","Salvo",true),$s3(19,"10011","Salvo",true),
	$s4(20,"10100","Salvo",true),$s5(21,"10101","Salvo",true),$s6(22,"10110","Salvo",true),$s7(23,"10111","Salvo",true),
	// --- Temporários part 2
	$t8(24,"11000","Temporário",false),$t9(25,"11001","Temporário",false),
	// --- Ponteiro Global
	$gp(28,"11100","Ponteiro Global",true),
	// --- Ponteiro para Pilha
	$sp(29,"11101","Ponteiro para Pilha",true),
	// --- Ponteiro para Frame
	$fp(30,"11110","Ponteiro para Frame",true),
	// --- Endereço de Retorno
	$ra(31,"11111","Endereço de Retorno",true);
	
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
