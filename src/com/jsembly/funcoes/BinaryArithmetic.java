package com.jsembly.funcoes;

public class BinaryArithmetic {
	
	public static String sum(String operatorA, String operatorB){
		
		String sumValue = new String();
		int value;
		
		value = BinaryLogic.binaryToInteger(operatorA) + BinaryLogic.binaryToInteger(operatorB);
		
		sumValue = BinaryLogic.integerToBinary(value);
		
		return sumValue;
	}
	
	public static String sumUnsigned(String operatorA, String operatorB){
		
		String sumValue = new String();
		int value;
		
		value = BinaryLogic.unsignedToInteger(operatorA) + BinaryLogic.unsignedToInteger(operatorB);
		
		sumValue = BinaryLogic.integerToBinary(value);
		
		return sumValue;
	}
	
	public static String subtract(String operatorA, String operatorB){
		
		String subValue = new String();
		int value;
		
		value = BinaryLogic.binaryToInteger(operatorA) - BinaryLogic.binaryToInteger(operatorB);
		
		subValue = BinaryLogic.integerToBinary(value);
		
		return subValue;
	}
	
	public static String subtractUnsigned(String operatorA, String operatorB){
		
		String sumValue = new String();
		int value;
		
		value = BinaryLogic.unsignedToInteger(operatorA) - BinaryLogic.unsignedToInteger(operatorB);
		
		sumValue = BinaryLogic.integerToBinary(value);
		
		return sumValue;
	}
	
	public static String multiply(String operatorA, String operatorB){
		
		String multValue = new String();
		int value;
		
		value = BinaryLogic.binaryToInteger(operatorA) * BinaryLogic.binaryToInteger(operatorB);
		
		multValue = BinaryLogic.integerToBinary(value);
		
		return multValue;
	}
	
	public static String divide(String operatorA, String operatorB){
		
		String divValue = new String();
		int value;
		
		value = BinaryLogic.binaryToInteger(operatorA) / BinaryLogic.binaryToInteger(operatorB);
		
		divValue = BinaryLogic.integerToBinary(value);
		
		return divValue;
	}
}
