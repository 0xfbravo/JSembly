package com.jsembly.funcoes;

public class ArithmeticLogicUnit {

	public static int add(int rs, int rt){ int rd = BinaryArithmetic.sum(rs, rt); return rd; }
	
	public static int sub(int rs, int rt){ int rd = BinaryArithmetic.subtract(rs, rt); return rd; }
	
	public static String or(String rs, String rt){ String rd = BinaryLogic.or(rs, rt); return rd; }
	
	public static String and(String rs, String rt){ String rd = BinaryLogic.and(rs, rt); return rd; }
	
	public static String nor(String rs, String rt){ String rd = BinaryLogic.nor(rs, rt); return rd; }
	
	public static String xor(String rs, String rt){ String rd = BinaryLogic.xor(rs, rt); return rd; }
	
	public static String sll(String rt, String shamt){	
		int shamtValue = BinaryLogic.unsignedToInteger(shamt);
		String base = new String();		
		base = rt.substring(shamtValue, (rt.length()));		 
		while(shamtValue > 0){			 
			base = base + '0'; 
			shamtValue--;
		}		
		String rd = base;
		return rd;
	}
	
	public static String srl(String rt, String shamt){		
		int shamtValue = BinaryLogic.unsignedToInteger(shamt);
		String base = new String();		
		base = rt.substring(0, (rt.length() - shamtValue));	
		while(shamtValue > 0){			 
			base = '0' + base; 
			shamtValue--;
		}		
		String rd = base;
		return rd;
	}
	
	public static int addu(int rs, int rt){ int rd = BinaryArithmetic.sumUnsigned(rs, rt); return rd; }
	
	public static int subu(int rs, int rt){ int rd = BinaryArithmetic.subtractUnsigned(rs, rt); return rd; }

}
