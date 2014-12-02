package com.jsembly.funcoes;

public class ArithmeticLogicUnit {

	public static int add(int rs, int rt){ int rd = BinaryArithmetic.sum(rs, rt); return rd; }
	
	public static int addi(int rs, int rt){ int rd = BinaryArithmetic.sum(rs, rt); return rd; }
	
	public static boolean slti(int rs, int C){ boolean rd; if(rs < C){ rd = true;} else { rd = false; } return rd;}
	
	public static int sub(int rs, int rt){ int rd = BinaryArithmetic.subtract(rs, rt); return rd; }
	
	public static String or(String rs, String rt){ String rd = BinaryLogic.or(rs, rt); return rd; }
	
	public static String and(String rs, String rt){ String rd = BinaryLogic.and(rs, rt); return rd; }
	
	public static String nor(String rs, String rt){ String rd = BinaryLogic.nor(rs, rt); return rd; }
	
	public static String xor(String rs, String rt){ String rd = BinaryLogic.xor(rs, rt); return rd; }
	
	public static String sll(int rt, int shamtValue){
		String teste = ConversaoBase.converteDecimalParaBinario(rt);
		teste = BinaryLogic.resizeBinary(teste, 32, true);
		String base = teste.substring(shamtValue);
		while(shamtValue > 0){			 
			base = base + '0'; 
			shamtValue--;
		}	
		return base;
	}
	
	public static String srl(int rt, int shamtValue){		
		String teste = ConversaoBase.converteDecimalParaBinario(rt);
		teste = BinaryLogic.resizeBinary(teste, 32, true);
		String base = teste.substring(shamtValue,teste.length()-shamtValue);
		while(shamtValue > 0){			 
			base = '0'+ base; 
			shamtValue--;
		}	
		return base;
	}
	
	public static int addu(int rs, int rt){ int rd = BinaryArithmetic.sumUnsigned(rs, rt); return rd; }
	
	public static int subu(int rs, int rt){ int rd = BinaryArithmetic.subtractUnsigned(rs, rt); return rd; }

}
