package com.jsembly.mips;

import com.jsembly.funcoes.ArithmeticLogicUnit;
import com.jsembly.funcoes.BinaryLogic;
import com.jsembly.funcoes.ConversaoBase;
import com.jsembly.main.ArraysLists;
import com.jsembly.main.Janela;
import com.jsembly.main.Memoria;

public class Instrucao {
	
	private Operador op;
		private int tipoOp;
		private int linhaOp;
	
	private Registrador r1;
		private int valorR1;
	private Registrador r2;
		protected int valorR2;
	private Registrador r3;
		protected int valorR3;
	
	private String shamt;
	private String enderecoPC;
	private String enderecoLabel;
	private String label;
	
	private String linguagemMaquina;
	
	/* Instrução TIPO R */
	public Instrucao(Operador op, Registrador r1, Registrador r2, Registrador r3, int linhaOP){
		this.setLinhaOp(linhaOP);
		this.setOp(op);
		this.setTipoOp(op.getTipoIntrucao());
		this.setR1(r1);
		this.setR2(r2);
		this.setR3(r3);	
		this.setLinguagemMaquina(
				"000000"+
				this.getR2().getValorBits()+
				this.getR3().getValorBits()+
				this.getR1().getValorBits()+
				"00000"+
				this.getOp().getValorBits()
				);
		this.Montar();
		this.BuscarValorReg(r2, r3);
	}
	
	public Instrucao(Operador op, Registrador r1, String enderecoLabel, Registrador r2, int linhaOP){
		this.setLinhaOp(linhaOP);
		this.setOp(op);
		switch(this.getOp().getId()){
			default:
				this.setR1(r1);
				this.setR2(r2);
				this.setEnderecoLabel(enderecoLabel);
				this.setTipoOp(op.getTipoIntrucao());
				this.setLinguagemMaquina(
						this.getOp().getValorBits()+
						this.getR1().getValorBits()+
						this.getR2().getValorBits()+
						BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario(Integer.parseInt(this.getEnderecoLabel())),16,true));
				this.Montar();
				this.BuscarValorReg(r1, r2);
				break;
		}
	}
	
	public Instrucao(Operador op, Registrador r1, Registrador r2, String enderecoLabel, int linhaOP){
		this.setLinhaOp(linhaOP);
		this.setOp(op);
		switch(this.getOp().getId()){
			// BEQ & BNE
			case 17:
			case 18:
				for(int i = 0; i < Janela.dtmExec.getRowCount(); i++){
					if(Janela.dtmExec.getValueAt(i, 3).equals(enderecoLabel)){
						this.setEnderecoPC(Janela.dtmExec.getValueAt(i+1, 0).toString());
					}
				}
				this.setR1(r1);
				this.setR2(r2);
				this.setEnderecoLabel(enderecoLabel);
				this.setTipoOp(op.getTipoIntrucao());
				this.setLinguagemMaquina(
						this.getOp().getValorBits()+
						this.getR1().getValorBits()+
						this.getR2().getValorBits()+
						BinaryLogic.resizeBinary(this.getEnderecoPC(),16,true));
				this.BuscarValorReg(r1, r2);
				break;
		
			default:
				this.setR1(r1);
				this.setR2(r2);
				this.setEnderecoLabel(enderecoLabel);
				this.setTipoOp(op.getTipoIntrucao());
				this.setLinguagemMaquina(
						this.getOp().getValorBits()+
						this.getR1().getValorBits()+
						this.getR2().getValorBits()+
						BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario(Integer.parseInt(this.getEnderecoLabel())),16,true));
				this.Montar();
				this.BuscarValorReg(r1, r2);
				break;
		}
	}
	
	/* Instrução TIPO I */
	public Instrucao(Operador op, Registrador r1, String enderecoLabel, int linhaOP){
		this.setOp(op);
		this.setLinhaOp(linhaOP);
		this.setR1(r1);
		this.setR2(r2);
		this.setEnderecoLabel(enderecoLabel);
		this.setTipoOp(op.getTipoIntrucao());
		this.setLinguagemMaquina(
				this.getOp().getValorBits()+
				this.getR1().getValorBits()+
				"00000"+
				BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario(Integer.parseInt(this.getEnderecoLabel())),16,true));
		this.Montar();
		this.BuscarValorReg(r1);
	}
	
	/* Instrução TIPO J */
	public Instrucao(Operador op, String enderecoLabel, int linhaOP){
		String endPC = BinaryLogic.resizeBinary(
				ConversaoBase.converteDecimalParaBinario(Integer.parseInt(Janela.dtm.getValueAt(0,2).toString())),32,true).substring(28, 32);
		
		this.setLinhaOp(linhaOP);
		this.setOp(op);
		for(int i = 0; i < Janela.dtmExec.getRowCount(); i++){
			if(Janela.dtmExec.getValueAt(i, 3).equals(enderecoLabel)){
				this.setEnderecoPC(Janela.dtmExec.getValueAt(i+1, 0).toString());
			}
		}
		this.setR1(r1);
		this.setR2(r2);
		this.setEnderecoLabel(enderecoLabel);
		this.setTipoOp(op.getTipoIntrucao());
		this.setLinguagemMaquina(
				endPC+
				BinaryLogic.resizeBinary(this.getEnderecoPC(),26,true)+"00");
		this.Montar();
	}
	
	/* Instrução TIPO R (JUMP/BRANCH) */
	public Instrucao(Operador op, Registrador r1, int linhaOP){
		this.setLinhaOp(linhaOP);
		this.setOp(op);
		this.setTipoOp(op.getTipoIntrucao());
		this.setR1(r1);
		this.setLinguagemMaquina(
				"000000"+
				BinaryLogic.resizeBinary(this.getR1().getValorBits(),15,true)+
				"00000"+
				this.getOp().getValorBits());
		this.Montar();
		this.BuscarValorReg(r1);
	}
	
	

	public void Montar(){
		switch(this.getTipoOp()){
		/**
		 * 	== TIPO DE INSTRUÇÕES
		 *	0 - TIPO I
		 *	1 - TIPO J
		 *	2 - TIPO R
		 *	3 - TIPO I (LOAD/STORE)
		 *	4 - TIPO R (JUMP/BRANCH)
		 *	5 - TIPO I (JUMP/BRANCH)
		 *	==
		 **/
		case 0:
			switch(this.getOp().getId()){
			// LUI
			case 9:
				ArraysLists.instrucoes.add(this);
				Memoria.AlocarMemoria(this.getLinguagemMaquina(), Janela.dtmMem);
				Janela.painelLinguagemMaquina.append(this.getLinguagemMaquina()+"\n");
				Janela.dtmExec.addRow(new Object[]{
						Memoria.BuscarEndereco(this.getLinguagemMaquina().substring(0, 8), Janela.dtmMem),
						"0x"+ConversaoBase.converteBinarioParaHexadecimal(this.getLinguagemMaquina()),
						this.getOp()+" $"+this.getR1().getId()+","+this.getEnderecoLabel(),
						this.linhaOp+": "+this.getOp()+" "+this.getR1()+","+this.getEnderecoLabel()});
			break;
			
			default:
				ArraysLists.instrucoes.add(this);
				Memoria.AlocarMemoria(this.getLinguagemMaquina(), Janela.dtmMem);
				Janela.painelLinguagemMaquina.append(this.getLinguagemMaquina()+"\n");
				Janela.dtmExec.addRow(new Object[]{
						Memoria.BuscarEndereco(this.getLinguagemMaquina().substring(0, 8), Janela.dtmMem),
						"0x"+ConversaoBase.converteBinarioParaHexadecimal(this.getLinguagemMaquina()),
						this.getOp()+" $"+this.getR1().getId()+",$"+this.getR2().getId()+","+this.getEnderecoLabel(),
						this.linhaOp+": "+this.getOp()+" "+this.getR1()+","+this.getR2()+","+this.getEnderecoLabel()});
			break;
			}
			break;
		
		case 1:
			ArraysLists.instrucoes.add(this);
			Memoria.AlocarMemoria(this.getLinguagemMaquina(), Janela.dtmMem);
			Janela.painelLinguagemMaquina.append(this.getLinguagemMaquina()+"\n");
			Janela.dtmExec.addRow(new Object[]{
	  				Memoria.BuscarEndereco(this.getLinguagemMaquina().substring(0, 8), Janela.dtmMem),
	  				"0x"+ConversaoBase.converteBinarioParaHexadecimal(this.getLinguagemMaquina()),
	  				this.getOp()+" "+this.getEnderecoPC(),
	  				this.linhaOp+": "+this.getOp()+" "+this.getEnderecoLabel()});
			break;
		
		case 2:
			switch(this.getOp().getId()){
				// SLL & SRL
				case 15:
				case 16:
					ArraysLists.instrucoes.add(this);
					Memoria.AlocarMemoria(this.getLinguagemMaquina(), Janela.dtmMem);
					Janela.painelLinguagemMaquina.append(this.getLinguagemMaquina()+"\n");
					Janela.dtmExec.addRow(new Object[]{
							Memoria.BuscarEndereco(this.getLinguagemMaquina().substring(0, 8), Janela.dtmMem),
							"0x"+ConversaoBase.converteBinarioParaHexadecimal(this.getLinguagemMaquina()),
							this.getOp()+" $"+this.getR1().getId()+",$"+this.getR2().getId()+","+this.getEnderecoLabel(),
							this.linhaOp+": "+this.getOp()+" "+this.getR1()+","+this.getR2()+","+this.getEnderecoLabel()});
					break;
				// MFHI & MFLO
				case 102:
				case 103:
					ArraysLists.instrucoes.add(this);
					Memoria.AlocarMemoria(this.getLinguagemMaquina(), Janela.dtmMem);
					Janela.painelLinguagemMaquina.append(this.getLinguagemMaquina()+"\n");
					Janela.dtmExec.addRow(new Object[]{
							Memoria.BuscarEndereco(this.getLinguagemMaquina().substring(0, 8), Janela.dtmMem),
							"0x"+ConversaoBase.converteBinarioParaHexadecimal(this.getLinguagemMaquina()),
							this.getOp()+" $"+this.getR1().getId(),
							this.linhaOp+": "+this.getOp()+" "+this.getR1()});
					break;
				// Tipo R (default)
				default:
					ArraysLists.instrucoes.add(this);
					Memoria.AlocarMemoria(this.getLinguagemMaquina(), Janela.dtmMem);
					Janela.painelLinguagemMaquina.append(this.getLinguagemMaquina()+"\n");
					Janela.dtmExec.addRow(new Object[]{
							Memoria.BuscarEndereco(this.getLinguagemMaquina().substring(0, 8), Janela.dtmMem),
							"0x"+ConversaoBase.converteBinarioParaHexadecimal(this.getLinguagemMaquina()),
							this.getOp()+" $"+this.getR1().getId()+",$"+this.getR2().getId()+",$"+this.getR3().getId(),
							this.linhaOp+": "+this.getOp()+" "+this.getR1()+","+this.getR2()+","+this.getR3()});
				break;
			}
			break;
		
		case 3:
			ArraysLists.instrucoes.add(this);
			Memoria.AlocarMemoria(this.getLinguagemMaquina(), Janela.dtmMem);
			Janela.painelLinguagemMaquina.append(this.getLinguagemMaquina()+"\n");
			Janela.dtmExec.addRow(new Object[]{
					Memoria.BuscarEndereco(this.getLinguagemMaquina().substring(0, 8), Janela.dtmMem),
					"0x"+ConversaoBase.converteBinarioParaHexadecimal(this.getLinguagemMaquina()),
					this.getOp()+" $"+this.getR1().getId()+","+this.getEnderecoLabel()+"($"+this.getR2().getId()+")",
					this.linhaOp+": "+this.getOp()+" "+this.getR1()+","+this.getEnderecoLabel()+"("+this.getR2()+")"});
			break;
		
		case 4:
			ArraysLists.instrucoes.add(this);
			Memoria.AlocarMemoria(this.getLinguagemMaquina(), Janela.dtmMem);
			Janela.painelLinguagemMaquina.append(this.getLinguagemMaquina()+"\n");
			Janela.dtmExec.addRow(new Object[]{
	  				Memoria.BuscarEndereco(this.getLinguagemMaquina().substring(0, 8), Janela.dtmMem),
	  				"0x"+ConversaoBase.converteBinarioParaHexadecimal(this.getLinguagemMaquina()),
	  				this.getOp()+" $"+this.getR1().getId(),
	  				this.linhaOp+": "+this.getOp()+" "+this.getR1()});
			break;
		
		case 5:
			ArraysLists.instrucoes.add(this);
			Memoria.AlocarMemoria(this.getLinguagemMaquina(), Janela.dtmMem);
			Janela.painelLinguagemMaquina.append(this.getLinguagemMaquina()+"\n");
			Janela.dtmExec.addRow(new Object[]{
						Memoria.BuscarEndereco(this.getLinguagemMaquina().substring(0, 8), Janela.dtmMem),
						"0x"+ConversaoBase.converteBinarioParaHexadecimal(this.getLinguagemMaquina()),
						this.getOp()+" $"+this.getR1().getId()+",$"+this.getR2().getId()+","+this.getEnderecoPC(),
						this.linhaOp+": "+this.getOp()+" "+this.getR1()+","+this.getR2()+","+this.getEnderecoLabel()});
			break;
		}
		
	}

	public void BuscarValorReg(Registrador r2){
		for(int r = 0; r < Janela.dtm.getRowCount(); r++){
			if(Janela.dtm.getValueAt(r, 0).equals(r2.toString())){
				this.setValorR2(Integer.parseInt(Janela.dtm.getValueAt(r, 2).toString(),10));
			}
		}
	}
	public void BuscarValorReg(Registrador r2, Registrador r3){
		for(int r = 0; r < Janela.dtm.getRowCount(); r++){
			if(Janela.dtm.getValueAt(r, 0).equals(r2.toString())){
				this.setValorR2(Integer.parseInt(Janela.dtm.getValueAt(r, 2).toString(),10));
			}
			else if(Janela.dtm.getValueAt(r, 0).equals(r3.toString())){
				this.setValorR3(Integer.parseInt(Janela.dtm.getValueAt(r, 2).toString(),10));
			}
		}
	}
	
	
	// -------------------------------
	// TODO: Executar instrução TIPO R
	// -------------------------------
	public void Executar(Operador op, Registrador r1, Registrador r2, Registrador r3, int linhaOP){
		r1.setAtivo(true);
  		//Registrador.AtualizarAtividade(Janela.dtm);
		switch(op.getId()){
			// ADD
			case 0:
				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
					if(Janela.dtm.getValueAt(r, 0).equals(r1.toString())){
					Janela.dtm.setValueAt(ArithmeticLogicUnit.add(getValorR2(), getValorR3()),r,2);
					Janela.dtm.setValueAt(
						BinaryLogic.resizeBinary(
							ConversaoBase.converteDecimalParaBinario(
								ArithmeticLogicUnit.add(valorR2, valorR3)),32,true),r,3);
					}
				}
			break;
			// SUB
			case 1:
				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
					if(Janela.dtm.getValueAt(r, 0).equals(r1.toString())){
						Janela.dtm.setValueAt(
								ArithmeticLogicUnit.sub(getValorR2(), getValorR3()),
								r,
								2);
						Janela.dtm.setValueAt(
								BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario(
										ArithmeticLogicUnit.sub(getValorR2(), getValorR3())
										),32,true),
								r,
								3);
					}
				}
			break;
			// AND
			case 10:
				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
					if(Janela.dtm.getValueAt(r, 0).equals(r1.toString())){
						Janela.dtm.setValueAt(
								ConversaoBase.converteBinarioParaDecimal(BinaryLogic.and(r2.getValorBits(),r3.getValorBits())),
								r,
								2);
						Janela.dtm.setValueAt(
								BinaryLogic.resizeBinary(BinaryLogic.and(r2.getValorBits(),r3.getValorBits()),32,true),
								r,
								3);
					}
				}
			break;
			// OR
			case 11:
				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
					if(Janela.dtm.getValueAt(r, 0).equals(r1.toString())){
						Janela.dtm.setValueAt(
								ConversaoBase.converteBinarioParaDecimal(BinaryLogic.or(r2.getValorBits(),r3.getValorBits())),
								r,
								2);
						Janela.dtm.setValueAt(
								BinaryLogic.resizeBinary(BinaryLogic.or(r2.getValorBits(),r3.getValorBits()),32,true),
										r,
										3);
					}
				}
			break;
			// NAND
			case 13:
				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
					if(Janela.dtm.getValueAt(r, 0).equals(r1.toString())){
						Janela.dtm.setValueAt(
								ConversaoBase.converteBinarioParaDecimal(BinaryLogic.nand(r2.getValorBits(),r3.getValorBits())),
								r,
								2);
						Janela.dtm.setValueAt(
								BinaryLogic.resizeBinary(BinaryLogic.nand(r2.getValorBits(),r3.getValorBits()),32,true),
								r,
								3);
					}
				}
			break;
			// XNOR
			case 14:
				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
					if(Janela.dtm.getValueAt(r, 0).equals(r1.toString())){
						Janela.dtm.setValueAt(
								ConversaoBase.converteBinarioParaDecimal(BinaryLogic.xnor(r2.getValorBits(),r3.getValorBits())),
								r,
								2);
						Janela.dtm.setValueAt(
								BinaryLogic.resizeBinary(BinaryLogic.xnor(r2.getValorBits(),r3.getValorBits()),32,true),
								r,
								3);
					}
				}
			break;
			// SLT
			case 19:
	  		boolean b = ArithmeticLogicUnit.slti(getValorR3(), getValorR2());
				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
					if(Janela.dtm.getValueAt(r, 0).equals(r1.toString())){
						if(b){
							Janela.dtm.setValueAt(1, r, 2);
							Janela.dtm.setValueAt(BinaryLogic.resizeBinary("1", 32, true), r, 3);
						} else {
							Janela.dtm.setValueAt(0, r, 2);
							Janela.dtm.setValueAt(BinaryLogic.resizeBinary("0", 32, true), r, 3);
						}
					}
				}
			break;
			// MULT
			case 24:
				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
					if(Janela.dtm.getValueAt(r, 0).equals(r1.toString())){
						int resultadoMult = ArithmeticLogicUnit.mult(getValorR2(), getValorR3());
						// -- Valor REGISTRADOR
						Janela.dtm.setValueAt(
								resultadoMult,
								r,
								2);
						Janela.dtm.setValueAt(
								BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario(resultadoMult),32,true),
								r,
								3);
						
						// -- Valor Binário HI & LO
						// ~ HI
						String hi = BinaryLogic.resizeBinary(
								ConversaoBase.converteDecimalParaBinario(resultadoMult),32,true).substring(16, 32);
						while(hi.length() < 32){
							hi = hi+"0";
						}
						Janela.dtm.setValueAt(
								hi,
								1,
								3);
						// ~ LO
						String lo = BinaryLogic.resizeBinary(
								ConversaoBase.converteDecimalParaBinario(resultadoMult),32,true).substring(16, 32);
						while(lo.length() < 32){
							lo = "0"+lo;
						}
						Janela.dtm.setValueAt(
								lo,
								2,
								3);
						
						// -- Valor Decimal HI & LO
						Janela.dtm.setValueAt(
								ConversaoBase.converteBinarioParaDecimal(
										BinaryLogic.resizeBinary(
												ConversaoBase.converteDecimalParaBinario(resultadoMult),32,true).substring(0, 16)),
								1,
								2);
						Janela.dtm.setValueAt(
								ConversaoBase.converteBinarioParaDecimal(
										BinaryLogic.resizeBinary(
												ConversaoBase.converteDecimalParaBinario(resultadoMult),32,true).substring(16, 32)),
								2,
								2);
					}
				}
			break;
			// MULTU
			case 25:
				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
					if(Janela.dtm.getValueAt(r, 0).equals(r1.toString())){
						int resultadoMult = ArithmeticLogicUnit.mult(getValorR2(), getValorR3());
						// -- Valor REGISTRADOR
						Janela.dtm.setValueAt(
								resultadoMult,
								r,
								2);
						Janela.dtm.setValueAt(
								BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario(resultadoMult),32,true),
								r,
								3);
						
						// -- Valor Binário HI & LO
						// ~ HI
						String hi = BinaryLogic.resizeBinary(
								ConversaoBase.converteDecimalParaBinario(resultadoMult),32,true).substring(16, 32);
						while(hi.length() < 32){
							hi = hi+"0";
						}
						Janela.dtm.setValueAt(
								hi,
								1,
								3);
						// ~ LO
						String lo = BinaryLogic.resizeBinary(
								ConversaoBase.converteDecimalParaBinario(resultadoMult),32,true).substring(16, 32);
						while(lo.length() < 32){
							lo = "0"+lo;
						}
						Janela.dtm.setValueAt(
								lo,
								2,
								3);
						
						// -- Valor Decimal HI & LO
						Janela.dtm.setValueAt(
								ConversaoBase.converteBinarioParaDecimal(
										BinaryLogic.resizeBinary(
												ConversaoBase.converteDecimalParaBinario(resultadoMult),32,true).substring(0, 16)),
								1,
								2);
						Janela.dtm.setValueAt(
								ConversaoBase.converteBinarioParaDecimal(
										BinaryLogic.resizeBinary(
												ConversaoBase.converteDecimalParaBinario(resultadoMult),32,true).substring(16, 32)),
								2,
								2);
					}
				}
			break;
			//DIV
			case 28:
				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
					if(Janela.dtm.getValueAt(r, 0).equals(r1.toString())){
						Janela.dtm.setValueAt(
								ArithmeticLogicUnit.div(getValorR2(), getValorR3()),
								1,
								2);
						Janela.dtm.setValueAt(
								getValorR2()%getValorR3(),
								2,
								2);
						Janela.dtm.setValueAt(
								ArithmeticLogicUnit.div(getValorR2(), getValorR3()),
								r,
								2);
						Janela.dtm.setValueAt(
								BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario(
										ArithmeticLogicUnit.add(getValorR2(), getValorR3())
										),32,true),
								r,
								3);
					}
				}
			break;
			//DIVU
			case 29:
				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
					if(Janela.dtm.getValueAt(r, 0).equals(r1.toString())){
						Janela.dtm.setValueAt(
								ArithmeticLogicUnit.div(getValorR2(), getValorR3()),
								1,
								2);
						Janela.dtm.setValueAt(
								getValorR2()%getValorR3(),
								2,
								2);
						Janela.dtm.setValueAt(
								ArithmeticLogicUnit.div(getValorR2(), getValorR3()),
								r,
								2);
						Janela.dtm.setValueAt(
								BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario(
										ArithmeticLogicUnit.add(getValorR2(), getValorR3())
										),32,true),
								r,
								3);
					}
				}
			break;
			//ADDU
			case 30:
				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
					if(Janela.dtm.getValueAt(r, 0).equals(r1.toString())){
						Janela.dtm.setValueAt(
								ArithmeticLogicUnit.addu(getValorR2(), getValorR3()),
								r,
								2);
						Janela.dtm.setValueAt(
								BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario(
										ArithmeticLogicUnit.add(getValorR2(), getValorR3())
										),32,true),
								r,
								3);
					}
				}
			break;
			// XOR
			case 38:
				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
					if(Janela.dtm.getValueAt(r, 0).equals(r1.toString())){
						Janela.dtm.setValueAt(
								ConversaoBase.converteBinarioParaDecimal(BinaryLogic.xor(r2.getValorBits(),r3.getValorBits())),
								r,
								2);
						Janela.dtm.setValueAt(
								BinaryLogic.resizeBinary(BinaryLogic.xor(r2.getValorBits(),r3.getValorBits()),32,true),
								r,
								3);
					}
				}
			break;
			// NOR
			case 39:
				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
					if(Janela.dtm.getValueAt(r, 0).equals(r1.toString())){
						Janela.dtm.setValueAt(
								ConversaoBase.converteBinarioParaDecimal(BinaryLogic.nor(r2.getValorBits(),r3.getValorBits())),
								r,
								2);
						Janela.dtm.setValueAt(
								BinaryLogic.resizeBinary(BinaryLogic.nor(r2.getValorBits(),r3.getValorBits()),32,true),
								r,
								3);
					}
				}
		}
	}
	
	// -------------------------------
	// TODO: Executar instrução TIPO I
	// -------------------------------
	public void Executar(Operador op, Registrador r1, Registrador r2, String enderecoLabel, int linhaOP){
		r1.setAtivo(true);
  		Registrador.AtualizarAtividade(Janela.dtm);
		switch(op.getId()){
			// ADDI
			case 2:
				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
					if(Janela.dtm.getValueAt(r, 0).equals(r1.toString())){
						Janela.dtm.setValueAt(ArithmeticLogicUnit.addi(valorR2, Integer.parseInt(enderecoLabel)), r, 2);
						Janela.dtm.setValueAt(
							BinaryLogic.resizeBinary(
								ConversaoBase.converteDecimalParaBinario(
									ArithmeticLogicUnit.addi(valorR2, Integer.parseInt(enderecoLabel))), 32, true), r, 3);
					}
				}
			break;
			// LUI
			case 9:
				String bin = BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario(
									Integer.parseInt(enderecoLabel)), 32, true);
				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
					if(Janela.dtm.getValueAt(r, 0).equals(r1.toString())){
						Janela.dtm.setValueAt(ConversaoBase.converteBinarioParaDecimal(bin.substring(0, 16)), r, 2);
  						Janela.dtm.setValueAt(
  							BinaryLogic.resizeBinary(
  								ConversaoBase.converteDecimalParaBinario(
  									Integer.parseInt(enderecoLabel)), 32, true), r, 3);
					}
				}
			break;
			//ANDI
			case 13:
				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
					if(Janela.dtm.getValueAt(r, 0).equals(r1.toString())){
						Janela.dtm.setValueAt(
							ConversaoBase.converteBinarioParaDecimal(
								BinaryLogic.and(
									ConversaoBase.converteDecimalParaBinario(valorR2),
									ConversaoBase.converteDecimalParaBinario(Integer.parseInt(enderecoLabel)))),r,2);
						Janela.dtm.setValueAt(
							BinaryLogic.resizeBinary(
								BinaryLogic.and(
									ConversaoBase.converteDecimalParaBinario(valorR2),
									ConversaoBase.converteDecimalParaBinario(Integer.parseInt(enderecoLabel))),32,true),r,2);
					}
				}
			break;
			//ORI
			case 14:
				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
					if(Janela.dtm.getValueAt(r, 0).equals(r1.toString())){
						Janela.dtm.setValueAt(
							ConversaoBase.converteBinarioParaDecimal(
								BinaryLogic.or(
									ConversaoBase.converteDecimalParaBinario(valorR2),
									ConversaoBase.converteDecimalParaBinario(Integer.parseInt(enderecoLabel)))),r,2);
						Janela.dtm.setValueAt(
							BinaryLogic.resizeBinary(
								BinaryLogic.or(
									ConversaoBase.converteDecimalParaBinario(valorR2),
									ConversaoBase.converteDecimalParaBinario(Integer.parseInt(enderecoLabel))),32,true),r,2);
					}
				}
			break;
			// SLTI
			case 20:
				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
					if(Janela.dtm.getValueAt(r, 0).equals(r1.toString())){
						if(ArithmeticLogicUnit.slti(valorR3, Integer.parseInt(enderecoLabel))) {
							Janela.dtm.setValueAt(1, r, 2);
							Janela.dtm.setValueAt( BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario(1), 32, true), r, 3);
						} else {
							Janela.dtm.setValueAt(0, r, 2);
							Janela.dtm.setValueAt( BinaryLogic.resizeBinary("0", 32, true), r, 3);
						}
					}
				}
			break;		
			//ADDIU
			case 26:
				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
					if(Janela.dtm.getValueAt(r, 0).equals(r1.toString())){
						Janela.dtm.setValueAt(ArithmeticLogicUnit.addiu(valorR2, Integer.parseInt(enderecoLabel)), r, 2);
						Janela.dtm.setValueAt(
							BinaryLogic.resizeBinary(
								ConversaoBase.converteDecimalParaBinario(
									ArithmeticLogicUnit.addiu(valorR2, Integer.parseInt(enderecoLabel))), 32, true), r, 3);
					}
				}
				break;
			// SLL - TIPO R (Porém com formato parecido com TIPO I)
			case 15:
				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
					if(Janela.dtm.getValueAt(r, 0).equals(r1.toString())){
						Janela.dtm.setValueAt(
							ConversaoBase.converteBinarioParaDecimal(
								ArithmeticLogicUnit.sll(valorR2,Integer.parseInt(enderecoLabel))),r,2);
						Janela.dtm.setValueAt(
							BinaryLogic.resizeBinary(
								ArithmeticLogicUnit.sll(valorR2,Integer.parseInt(enderecoLabel)),32,true),r,3);
					}
				}
			break;
			// SRL - TIPO R (Porém com formato parecido com TIPO I)
			case 16:
				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
					if(Janela.dtm.getValueAt(r, 0).equals(r1.toString())){
						Janela.dtm.setValueAt(
							ConversaoBase.converteBinarioParaDecimal(
									ArithmeticLogicUnit.srl(valorR2,Integer.parseInt(enderecoLabel))),r,2);
						Janela.dtm.setValueAt(
							BinaryLogic.resizeBinary(
								ArithmeticLogicUnit.srl(valorR2,Integer.parseInt(enderecoLabel)),32,true),r,3);
					}
				}
			break;
		}
	}
	
	// --------------------------------------------
	// TODO: Executar instrução TIPO I (LOAD/STORE)
	// --------------------------------------------
	public void Executar(Operador op, Registrador r1, String enderecoLabel, Registrador r2, int linhaOP){
		r1.setAtivo(true);
  		Registrador.AtualizarAtividade(Janela.dtm);
  		switch(op.getId()){
  			// LW
  			case 3:
  				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
  					if(Janela.dtm.getValueAt(r, 0).equals(r1.toString())){
  						Janela.dtm.setValueAt((valorR3+Integer.parseInt(enderecoLabel)), r, 2);
  						String endereco = Janela.dtm.getValueAt(r, 2).toString();
  						int tamanhoEndereco = Janela.dtm.getValueAt(r, 2).toString().length();
  						if(tamanhoEndereco<10){ endereco = "00000"+endereco; }
  						else if(100 > tamanhoEndereco && tamanhoEndereco >= 10){ endereco = "0000"+endereco; }
  						else if(1000 > tamanhoEndereco && tamanhoEndereco >= 100){ endereco = "000"+endereco; }
  						else if(10000 > tamanhoEndereco && tamanhoEndereco >= 1000){ endereco = "00"+endereco;  }
  						else if(100000 > tamanhoEndereco && tamanhoEndereco >= 10000){ endereco = "0"+endereco; }
  						for(int m = 0; m < Janela.dtmMem.getRowCount(); m++){
  							if(Janela.dtmMem.getValueAt(m, 0).toString().equals(endereco)){
  								
  								Janela.dtm.setValueAt(
  										ConversaoBase.converteBinarioParaDecimal(
  										Janela.dtmMem.getValueAt(m, 1).toString()+
  										Janela.dtmMem.getValueAt(m+1, 1).toString()+
  										Janela.dtmMem.getValueAt(m+2, 1).toString()+
  										Janela.dtmMem.getValueAt(m+3, 1).toString()), r, 2);
  								
  								Janela.dtm.setValueAt(
  										Janela.dtmMem.getValueAt(m, 1).toString()+
  										Janela.dtmMem.getValueAt(m+1, 1).toString()+
  										Janela.dtmMem.getValueAt(m+2, 1).toString()+
  										Janela.dtmMem.getValueAt(m+3, 1).toString(), r, 3);
  							}
  						}
  					}
  				}
  			break;
  			
  			// SW
  			case 4:
  				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
  					if(Janela.dtm.getValueAt(r, 0).equals(r1.toString())){
  						Janela.dtm.setValueAt((valorR3+Integer.parseInt(enderecoLabel)), r, 2);
  						Janela.dtm.setValueAt(
  								BinaryLogic.resizeBinary(ConversaoBase.converteDecimalParaBinario((valorR3+Integer.parseInt(enderecoLabel))),32,true), r, 3);
  						String endereco = Janela.dtm.getValueAt(r, 2).toString();
  						int tamanhoEndereco = Janela.dtm.getValueAt(r, 2).toString().length();
  						if(tamanhoEndereco<10){ endereco = "00000"+endereco; }
  						else if(100 > tamanhoEndereco && tamanhoEndereco >= 10){ endereco = "0000"+endereco; }
  						else if(1000 > tamanhoEndereco && tamanhoEndereco >= 100){ endereco = "000"+endereco; }
  						else if(10000 > tamanhoEndereco && tamanhoEndereco >= 1000){ endereco = "00"+endereco;  }
  						else if(100000 > tamanhoEndereco && tamanhoEndereco >= 10000){ endereco = "0"+endereco; }
  						for(int m = 0; m < Janela.dtm.getRowCount(); m++){
  							if(Janela.dtm.getValueAt(m, 0).toString().equals(r1.toString())){
  								Memoria.AlocarMemoria(Janela.dtm.getValueAt(m, 3).toString(), Janela.dtmMem);
  								m = Janela.dtm.getRowCount();
  							}
  						}
  					}
  				}
  			break;
  		}
	}
	
	// -------------------------------
	// TODO: Executar instrução TIPO J
	// -------------------------------
	public void Executar(Operador op, String enderecoLabel, int linhaOP){
		Janela.dtm.setValueAt("Ativo", 0, 4);
		Janela.dtm.setValueAt(this.getEnderecoPC(), 0, 2);
		Janela.dtm.setValueAt(BinaryLogic.resizeBinary(this.getEnderecoPC(),32,true),0,3);
	}
	
	// -------------------------------
	// TODO: Executar instrução TIPO R (JUMP/BRANCH)
	// -------------------------------
	public void Executar(Operador op, Registrador r1, int linhaOP){
	}
	
	
	public Registrador getR1() {
		return r1;
	}
	public void setR1(Registrador r1) {
		this.r1 = r1;
	}
	public Registrador getR2() {
		return r2;
	}
	public void setR2(Registrador r2) {
		this.r2 = r2;
	}
	public Registrador getR3() {
		return r3;
	}
	public void setR3(Registrador r3) {
		this.r3 = r3;
	}
	public String getShamt() {
		return shamt;
	}
	public void setShamt(String shamt) {
		this.shamt = shamt;
	}
	public String getEnderecoLabel() {
		return enderecoLabel;
	}
	public void setEnderecoLabel(String enderecoLabel) {
		this.enderecoLabel = enderecoLabel;
	}
	public Operador getOp() {
		return op;
	}
	public void setOp(Operador op) {
		this.op = op;
	}
	public String getLinguagemMaquina() {
		return linguagemMaquina;
	}
	public void setLinguagemMaquina(String linguagemMaquina) {
		this.linguagemMaquina = linguagemMaquina;
	}
	public int getTipoOp() {
		return tipoOp;
	}
	public void setTipoOp(int tipoOp) {
		this.tipoOp = tipoOp;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getEnderecoPC() {
		return enderecoPC;
	}
	public void setEnderecoPC(String enderecoPC) {
		this.enderecoPC = enderecoPC;
	}
	public int getLinhaOp() {
		return linhaOp;
	}
	public void setLinhaOp(int linhaOp) {
		this.linhaOp = linhaOp;
	}
	public int getValorR1() {
		return valorR1;
	}
	public void setValorR1(int valorR1) {
		this.valorR1 = valorR1;
	}
	public int getValorR2() {
		return valorR2;
	}
	public void setValorR2(int valorR2) {
		this.valorR2 = valorR2;
	}
	public int getValorR3() {
		return valorR3;
	}
	public void setValorR3(int valorR3) {
		this.valorR3 = valorR3;
	}
}
