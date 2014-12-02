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
	addi(2,"001000","Add Immediate",0), // !_IMPLEMENTADO & FUNCIONAL_!
	addiu(999,"001001","Add Immediate Unsigned",0), /* Falta Implementar */
	lui(9,"001111","Load Upper Immediate",0), // !_IMPLEMENTADO & FUNCIONAL_!
	andi(13,"001100","And Immediate",0),
	ori(14,"001101","Or Immediate",0),
	xori(999,"001110","Exclusive Or Immediate",0), /* Falta Implementar */
	slti(20,"001010","Set On Less Than Immediate",0),
	sltiu(999,"001011","Set On Less Than Immediate Unsigned",0), /* Falta Implementar */
	// -- Tipo I (LOAD/STORE)
	lw(3,"100011","Load Word",3),
	sw(4,"101011","Store Word",3),
	lh(5,"100001","Load Half",3),
	sh(6,"101001","Store Half",3),
	lb(7,"100000","Load Byte",3),
	sb(8,"101000","Store Byte",3),
	lbu(999,"100100","Load Byte Unsigned",3), /* Falta Implementar */
	lhu(999,"100101","Load Halfword Unsigned",3), /* Falta Implementar */
	lhi(999,"","Load to High",0), /* Falta Implementar -- ID Aleatória */
	llo(999,"","Load to Low",0), /* Falta Implementar -- ID Aleatória */
	mthi(999,"010001","Move To High",3), /* Falta Implementar -- ID Aleatória */
	mtlo(999,"010011","Move To Low",3), /* Falta Implementar -- ID Aleatória */
	// -- Tipo I (JUMP/BRANCH)
	blt(1,"000001","Branch Less Than",5),
	ble(6,"000110","Branch Less Than or Equal",5),
	bgt(999,"","Branch Greater Than",5), /* Falta Implementar */
	bge(999,"","Branch Greater Than or Equal",5), /* Falta Implementar */
	beq(17,"000100","Branch on Equal",5),
	bne(18,"000101","Branch on Not Equal",5),
	blez(999,"000110","Branch Less Than or Equal Zero",5), /* Falta Implementar */
	bgtz(999,"000111","Branch Greater Than Zero",5), /* Falta Implementar */
	bltz(999,"000001","Branch Less Than Zero",5), /* Falta Implementar */
	bgez(999,"000001","Branch Greater Than or Equal Zero",5), /* Falta Implementar */
	
	// -- Tipo J
	j(21,"000010","Jump",1),
	jal(23,"000011","Jump and Link",1),
	
	// -- Tipo R
	mfhi(999,"010000","Move From High",2), /* Falta Implementar -- ID Aleatória*/
	mflo(999,"010010","Move From Low",2), /* Falta Implementar  -- ID Aleatória*/
	add(0,"100000","Add",2), // !_IMPLEMENTADO & FUNCIONAL_!
	sub(1,"100010","Substract",2), // !_IMPLEMENTADO & FUNCIONAL_!
	and(10,"100100","And",2), // !_IMPLEMENTADO & FUNCIONAL_!
	or(11,"100101","Or",2), // !_IMPLEMENTADO & FUNCIONAL_!
	xor(999,"100110","Exclusive Or",2), /* Falta Implementar */
	nor(12,"100111","Nor",2), // !_IMPLEMENTADO & FUNCIONAL_!
	sll(15,"000000","Shift Left Logical",2), // !_IMPLEMENTADO & FUNCIONAL_!
	srl(16,"000010","Shift Right Logical",2), // !_IMPLEMENTADO & FUNCIONAL_!
	sra(999,"000011","Shift Right Arithmetic",2), /* Falta Implementar */
	sllv(999,"000100","",2), /* Falta Implementar */
	srlv(999,"000110","",2), /* Falta Implementar */
	srav(999,"000111","",2), /* Falta Implementar */
	addu(999,"100001","Add Unsigned",2), /* Falta Implementar */
	subu(999,"100011","Substract Unsigned",2), /* Falta Implementar */
	slt(19,"001010","Set On Less Than",2),
	sltu(999,"101011","Set On Less Than Unsigned",2), /* Falta Implementar */
	move(999,"","Move",2), /* Falta Implementar */
	mult(24,"011000","Multiply",2),
	multu(25,"011001","Multiply Unsigned",2),
	div(999,"011010","Divide",2), /* Falta Implementar */
	divu(999,"011011","Divide Unsigned",2), /* Falta Implementar */
	// -- Tipo R (JUMP/BRANCH)
	jr(22,"001000","Jump Register",4),
	jalr(999,"001001","Jump and Link Register",4); /* Falta Implementar */
	
	
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
