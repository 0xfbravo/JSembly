package com.jsembly.funcoes;

public class BinaryLogic {
	
	public static String inverter(String data){
		
		String binary = new String();
		
		for(int i = 0; i < data.length(); i++){
			
			if(data.charAt(i) == '0')
				binary = binary + '1';
			else
				binary = binary + '0';
		}
		
		return binary;
	}
	
	public static String twoComplements(String binary){
		
		String inverted = new String();
		String complement = new String();
		int counter = (binary.length() - 1);
		
		inverted = inverter(binary);
		
		do{
			
			if(inverted.charAt(counter) == '1'){
				
				complement = '0' + complement;
				counter --;
			}
			
			if(inverted.charAt(counter) == '0'){
				
				complement = inverted.substring(0, (counter)) + '1' + complement;
			}
			
		}while(inverted.charAt(counter) != '0');
		
		return complement;
	}
	
	public static String integerToBinary(int integer){
		
		String binary = new String();
		
		if(integer >= 0){
			
			binary = Integer.toBinaryString(integer);
			
			while(binary.length() < 32){
				
				binary = '0' + binary;
			}
		}
		else
			binary = Integer.toBinaryString(integer);
		
		return binary;
	}
	
	public static String integerToBinary(int integer, int size){
		
		String binary = new String();
		
		binary = integerToBinary(integer);
		binary = resizeBinary(binary, size);
		
		return binary;
	}

	public static int binaryToInteger(String binary){
		
		int integer = 0;
		boolean isNegative;
		
		if(binary.charAt(0) == '1'){
			
			isNegative = true;
			binary = twoComplements(binary);
		}
		else
			isNegative = false;
		
		for(int i = 0; i < binary.length(); i++)
			if(binary.charAt(i) == '1')
				integer += (int) Math.pow(2, ((binary.length() - 1) - i));
		
		if(isNegative)
			integer *= -1;
		
		return integer;
	}
	
	public static int unsignedToInteger(String binary){
		
		int integer = 0;
		
		for(int i = 0; i < binary.length(); i++)
			if(binary.charAt(i) == '1')
				integer += (int) Math.pow(2, ((binary.length() - 1) - i));
		
		return integer;
	}
	
	public static String resizeBinary(String binary, int newSize){
		
		String data = binary;
		
		if(data.length() < newSize)
			if(binary.charAt(0) == '0')
				while(data.length() < newSize){
					
					data = '0' + data;
				}
			else
				while(data.length() < newSize){
					
					data = '1' + data;
				}
		
		else if(data.length() > newSize)
			data = data.substring(data.length() - newSize, data.length());
		
		return data;
	}
	
	public static String and(String firstBinary, String secondBinary){
		
		String binary = new String();
		
		if(firstBinary.length() != 32)
			firstBinary = resizeBinary(firstBinary, 32);
		
		if(secondBinary.length() != 32)
			secondBinary = resizeBinary(secondBinary, 32);
			
		for(int i = 0; i < firstBinary.length(); i++){
			
			if(firstBinary.charAt(i) == '0' | secondBinary.charAt(i) == '0')
				binary = binary + '0';
			else
				binary = binary + '1';
		}
			
		
		return binary;
	}
	
	public static String or(String firstBinary, String secondBinary){
		
		String binary = new String();
		
		if(firstBinary.length() != 32)
			firstBinary = resizeBinary(firstBinary, 32);
		
		if(secondBinary.length() != 32)
			secondBinary = resizeBinary(secondBinary, 32);
			
		for(int i = 0; i < firstBinary.length(); i++){
			
			if(firstBinary.charAt(i) == '1' | secondBinary.charAt(i) == '1')
				binary = binary + '1';
			else
				binary = binary + '0';
		}
			
		return binary;
	}
	
	public static String xor(String firstBinary, String secondBinary){
		
		String binary = new String();
		
		if(firstBinary.length() != 32)
			firstBinary = resizeBinary(firstBinary, 32);
		
		if(secondBinary.length() != 32)
			secondBinary = resizeBinary(secondBinary, 32);
		
		for(int i = 0; i < firstBinary.length(); i++){
			
			if(firstBinary.charAt(i) == secondBinary.charAt(i))
				binary = binary + '0';
			else
				binary = binary + '1';
		}
		
		return binary;
	}
	
	public static String nand(String firstBinary, String secondBinary){
		
		String binary = new String();
		
		binary = and(firstBinary, secondBinary);
			
		binary = inverter(binary);
		
		return binary;
	}
	
	public static String nor(String firstBinary, String secondBinary){
		
		String binary = new String();
		
		binary = or(firstBinary, secondBinary);
		
		binary = inverter(binary);
		
		return binary;
	}
	
	public static String xnor(String firstBinary, String secondBinary){
		
		String binary = new String();
		
		binary = xor(firstBinary, secondBinary);
		
		binary = inverter(binary);
		
		return binary;
	}
}