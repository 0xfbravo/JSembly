package com.jsembly.mips;

public enum Operador {
	// Operadores
	//  exemplo: add(id,valorBits,nomeInstrucao,tipoInstrucao)
	// Tipos Instrucao:
	// 		0 - TIPO I
	//		1 - TIPO J
	//		2 - TIPO R
	//		3 - TIPO I (LOAD/STORE)
	//		4 - TIPO R (JUMP/BRANCH)
	//		5 - TIPO I (JUMP/BRANCH)
	
	// -- Tipo I
	addi(2,"001000","Add Immediate",0),
	addiu(000,"001001[ Faltam informações ]","Add Immediate Unsigned",0),
	lui(9,"001111","Load Upper Immediate",0),
	andi(13,"001100","And Immediate",0),
	ori(14,"001101","Or Immediate",0),
	xori(0,"001110","Exclusive Or Immediate",0),
	slti(20,"001010","Set On Less Than Immediate",0),
	sltiu(000,"001011[ Faltam informações ]","Set On Less Than Immediate Unsigned",0),
	li(000,"[ Faltam informações ]","Load Immediate",0),
	blt(1,"000001","Branch Less Than",0),
	ble(6,"000110","Branch Less Than or Equal",0),
	bgt(000,"[ Faltam informações ]","Branch Greater Than",0),
	bge(000,"[ Faltam informações ]","Branch Greater Than or Equal",0),
	// -- Tipo I (LOAD/STORE)
	lw(3,"100011","Load Word",3),
	sw(4,"101011","Store Word",3),
	lh(5,"100001","Load Half",3),
	sh(6,"101001","Store Half",3),
	lb(7,"100000","Load Byte",3),
	sb(8,"101000","Store Byte",3),
	lbu(0,"100100","Load Byte Unsigned",3),
	lhu(0,"100101","Load Halfword Unsigned",3),
	mthi(000,"010001[ Faltam informações ]","Move To High",3),
	mtlo(000,"010011[ Faltam informações ]","Move To Low",3),
	// -- Tipo I (JUMP/BRANCH)
	beq(17,"000100","Branch on Equal",5),
	bne(18,"000101","Branch on Not Equal",5),
	blez(000,"000110[ Faltam informações ]","Branch Less Than or Equal Zero",5),
	bgtz(000,"000111[ Faltam informações ]","Branch Greater Than Zero",5),
	bltz(000,"000001[ Faltam informações ]","Branch Less Than Zero",5),
	bgez(000,"000001[ Faltam informações ]","Branch Greater Than or Equal Zero",5),
	
	// -- Tipo J
	j(21,"000010","Jump",1),
	jal(23,"000011","Jump and Link",1),
	
	// -- Tipo R
	mfhi(000,"010000[ Faltam informações ]","Move From High",2),
	mflo(000,"010010[ Faltam informações ]","Move From Low",2),
	add(0,"100000","Add",2),
	sub(1,"100010","Substract",2),
	and(10,"100100","And",2),
	or(11,"100101","Or",2),
	xor(000,"100110[ Faltam informações ]","Exclusive Or",2),
	nor(12,"100111","Nor",2),
	sll(15,"000000","Shift Left Logical",2),
	srl(16,"000010","Shift Right Logical",2),
	sra(000,"000011[ Faltam informações ]","Shift Right Arithmetic",2),
	sllv(000,"000100[ Faltam informações ]","[ Faltam informações ]",2),
	srlv(000,"000110[ Faltam informações ]","[ Faltam informações ]",2),
	srav(000,"000111[ Faltam informações ]","[ Faltam informações ]",2),
	addu(000,"100001[ Faltam informações ]","Add Unsigned",2),
	subu(000,"100011[ Faltam informações ]","Substract Unsigned",2),
	slt(19,"001010","Set On Less Than",2),
	sltu(000,"101011[ Faltam informações ]","Set On Less Than Unsigned",2),
	move(000,"[ Faltam informações ]","Move",2),
	mult(24,"011000","Multiply",2),
	multu(25,"011001","Multiply Unsigned",2),
	div(000,"011010[ Faltam informações ]","Divide",2),
	divu(000,"011011[ Faltam informações ]","Divide Unsigned",2),
	// -- Tipo R (JUMP/BRANCH)
	jr(22,"001000","Jump Register",4),
	jalr(000,"001001[ Faltam informações ]","Jump and Link Register",4);
	
	
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
