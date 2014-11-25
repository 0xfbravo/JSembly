package com.jsembly.mips;

public enum Operador {
	// Operadores
	//  exemplo: add(id,valorBits,nomeInstrucao,tipoInstrucao)
	// Tipos Instrucao:
	// 		0 - TIPO I
	//		1 - TIPO J
	//		2 - TIPO R
	
	// -- Tipo R
	add(0,"100000","Add",2),
	sub(1,"100010","Substract",2),
	slt(19,"001010","Set On Less Than",2),
	slti(20,"001011","Set On Less Than Immediate",2),
	jr(22,"001000","Jump Register",2),
	move(000,"[ Faltam informações ]","[ Faltam informações ]",2),
	mult(000,"[ Faltam informações ]","[ Faltam informações ]",2),
	
	// -- Tipo I
	addi(2,"001000","Add Immediate",0),
	lw(3,"100011","Load Word",0),
	sw(4,"101011","Store Word",0),
	lb(7,"100000","Load Byte",0),
	sb(8,"101000","Store Byte",0),
	lui(9,"001111","Load Upper Immediate",0),
	beq(17,"000100","Branch on equal",0),
	bne(18,"000101","Branch on not equal",0),
	multi(000,"[ Faltam informações ]","[ Faltam informações ]",0),
	li(000,"[ Faltam informações ]","[ Faltam informações ]",0),
	blt(000,"[ Faltam informações ]","[ Faltam informações ]",0),
	ble(000,"[ Faltam informações ]","[ Faltam informações ]",0),
	bgt(000,"[ Faltam informações ]","[ Faltam informações ]",0),
	bge(000,"[ Faltam informações ]","[ Faltam informações ]",0),
	
	// -- Tipo J
	j(21,"000010","Jump",1),
	jal(23,"000011","Jump and Link",1),

	// -- Ainda não Organizados
	lh(5,"100001","Load Half",0),
	sh(6,"101001","Store Half",0),
	and(10,"100100","And",0),
	or(11,"100101","Or",0),
	nor(12,"100111","Nor",0),
	andi(13,"001100","And Immediate",0),
	ori(14,"001101","Or Immediate",0),
	sll(15,"000000","Shift Left Logical",0),
	srl(16,"000010","Shift Right Logical",0);
	
	
	private String categoria;
	private String nomeInstrucao;
	private int tipoIntrucao;
	private String exemplo;
	private String significado;
	private String comentarios;
	private String valorBits;
	private int id;
	
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getNomeInstrucao() {
		return nomeInstrucao;
	}
	public void setNomeInstrucao(String nomeInstrucao) {
		this.nomeInstrucao = nomeInstrucao;
	}
	public String getExemplo() {
		return exemplo;
	}
	public void setExemplo(String exemplo) {
		this.exemplo = exemplo;
	}
	public String getSignificado() {
		return significado;
	}
	public void setSignificado(String significado) {
		this.significado = significado;
	}
	public String getComentarios() {
		return comentarios;
	}
	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}
	public String getValorBits() {
		return valorBits;
	}
	public void setValorBits(String valorBits) {
		this.valorBits = valorBits;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	Operador(int id, String valorBits, String nomeInstrucao, int tipoIntrucao){
		this.setId(id);
		this.setValorBits(valorBits);
		this.setNomeInstrucao(nomeInstrucao);
		this.setTipoIntrucao(tipoIntrucao);
	}
	public int getTipoIntrucao() {
		return tipoIntrucao;
	}
	public void setTipoIntrucao(int tipoIntrucao) {
		this.tipoIntrucao = tipoIntrucao;
	}
}
