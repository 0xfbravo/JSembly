package com.jsembly.funcoes;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.jsembly.mips.Operador;
import com.jsembly.mips.Registrador;

public class BinaryLogic {
	public static boolean chkOverflowI(String lm, int linhaAtual){
		if(lm.length() > 16){
				JOptionPane.showMessageDialog(null,
								"<html>"
								+ "Underflow na linha: "
								+ "<b style='color:red;'>"+linhaAtual+"</b><br>"
								+ "<i>Número total de bits da instrução digitada é <b>maior</b> que 16 bits.</i><br><br>"
								+ "Número digitado: <b>"+lm+"</b><br>"
								+ "<i>Valor total de bits:</i> <b style='color:red;'>"+lm.length()+"</b>"
								+ "</html>", "Detectado Overflow!", JOptionPane.ERROR_MESSAGE);
				return true;
		}
		return false;
	}
	public static boolean chkUnderflow(String lm, int linhaAtual, Operador op, String valorBinario){
		if(lm.length() > 32){
				JOptionPane.showMessageDialog(null,
								"<html>"
								+ "Underflow na linha: "
								+ "<b style='color:red;'>"+linhaAtual+"</b><br>"
								+ "<i>Número total de bits da instrução digitada é <b>maior</b> que 32 bits.</i><br><br>"
								+ "Operador: <b>"+op+"</b> | Valor Binário: <b>"+op.getValorBits()+"</b><br>"
								+ "Valor Digitado:  <b>"+ConversaoBase.converteBinarioParaDecimal(valorBinario)+"</b> | Valor Binário:  <b>"+valorBinario+"</b>"

								+ "<br><br>"
								+ "Instrução Completa em Binário: <b>"+lm+"</b><br>"
								+ "<i>Valor total de bits:</i> <b style='color:red;'>"+lm.length()+"</b>"
								+ "</html>", "Detectado Underflow!", JOptionPane.ERROR_MESSAGE);
				return true;
		}
		return false;
	}
	public static boolean chkUnderflow(String lm, int linhaAtual, Operador op, ArrayList<Registrador> listaReg, String valorBinario){
		String reg2 = "",reg3 = "";
		if(listaReg.size() > 1){
			reg2 = "Registrador 2: <b>"+listaReg.get(1)+"</b> | Valor Binário: <b>"+listaReg.get(1).getValorBits()+"</b><br>";
		}
		if(listaReg.size() > 2){
			reg3 = "Registrador 3: <b>"+listaReg.get(2)+"</b> | Valor Binário: <b>"+listaReg.get(2).getValorBits()+"</b><br>";
		}
		if(lm.length() < 32){
				JOptionPane.showMessageDialog(null,
						"<html>"
						+ "Underflow na linha: "
						+ "<b style='color:red;'>"+linhaAtual+"</b><br>"
						+ "<i>Número total de bits da instrução digitada é <b>menor</b> que 32 bits.</i><br><br>"
						+ "Operador: <b>"+op+"</b> | Valor Binário: <b>"+op.getValorBits()+"</b><br>"
						+ "Registrador 1: <b>"+listaReg.get(0)+"</b> | Valor Binário: <b>"+listaReg.get(0).getValorBits()+"</b><br>"
						+  reg2
						+  reg3
						+ "Valor Digitado:  <b>"+ConversaoBase.converteBinarioParaDecimal(valorBinario)+"</b> | Valor Binário:  <b>"+valorBinario+"</b>"
						+ "<br><br>"
						+ "Instrução Completa em Binário: <b>"+lm+"</b><br>"
						+ "<i>Valor total de bits:</i> <b style='color:red;'>"+lm.length()+"</b>"
						+ "</html>", "Detectado Underflow!", JOptionPane.ERROR_MESSAGE);
				return true;
		}
		return false;
	}
	public static boolean chkUnderflow(String lm, int linhaAtual, Operador op, ArrayList<Registrador> listaReg){
		String reg2 = "",reg3 = "";
		if(listaReg.size() > 1){
			reg2 = "Registrador 2: <b>"+listaReg.get(1)+"</b> | Valor Binário: <b>"+listaReg.get(1).getValorBits()+"</b><br>";
		}
		if(listaReg.size() > 2){
			reg3 = "Registrador 3: <b>"+listaReg.get(2)+"</b> | Valor Binário: <b>"+listaReg.get(2).getValorBits()+"</b><br>";
		}
		if(lm.length() < 32){
				JOptionPane.showMessageDialog(null,
						"<html>"
								+ "Underflow na linha: "
								+ "<b style='color:red;'>"+linhaAtual+"</b><br>"
								+ "<i>Número total de bits da instrução digitada é <b>menor</b> que 32 bits.</i><br><br>"
								+ "Operador: <b>"+op+"</b> | Valor Binário: <b>"+op.getValorBits()+"</b><br>"
								+ "Registrador 1: <b>"+listaReg.get(0)+"</b> | Valor Binário: <b>"+listaReg.get(0).getValorBits()+"</b><br>"
								+  reg2
								+  reg3
								+ "<br><br>"
								+ "Instrução Completa em Binário: <b>"+lm+"</b><br>"
								+ "<i>Valor total de bits:</i> <b style='color:red;'>"+lm.length()+"</b>"
								+ "</html>", "Detectado Underflow!", JOptionPane.ERROR_MESSAGE);
				return true;
		}
		return false;
	}

	
	public static boolean chkOverflow(String lm, int linhaAtual, Operador op, String valorBinario){
		if(lm.length() > 32){
				JOptionPane.showMessageDialog(null,
								"<html>"
								+ "Overflow na linha: "
								+ "<b style='color:red;'>"+linhaAtual+"</b><br>"
								+ "<i>Número total de bits da instrução digitada é <b>maior</b> que 32 bits.</i><br><br>"
								+ "Operador: <b>"+op+"</b> | Valor Binário: <b>"+op.getValorBits()+"</b><br>"
								+ "Valor Digitado:  <b>"+ConversaoBase.converteBinarioParaDecimal(valorBinario)+"</b> | Valor Binário:  <b>"+valorBinario+"</b>"

								+ "<br><br>"
								+ "Instrução Completa em Binário: <b>"+lm+"</b><br>"
								+ "<i>Valor total de bits:</i> <b style='color:red;'>"+lm.length()+"</b>"
								+ "</html>", "Detectado Overflow!", JOptionPane.ERROR_MESSAGE);
				return true;
		}
		return false;
	}
	public static boolean chkOverflow(String lm, int linhaAtual, Operador op, ArrayList<Registrador> listaReg){
		String reg2 = "",reg3 = "";
		if(listaReg.size() > 1){
			reg2 = "Registrador 2: <b>"+listaReg.get(1)+"</b> | Valor Binário: <b>"+listaReg.get(1).getValorBits()+"</b><br>";
		}
		if(listaReg.size() > 2){
			reg3 = "Registrador 3: <b>"+listaReg.get(2)+"</b> | Valor Binário: <b>"+listaReg.get(2).getValorBits()+"</b><br>";
		}
		if(lm.length() > 32){
				JOptionPane.showMessageDialog(null,
								"<html>"
								+ "Overflow na linha: "
								+ "<b style='color:red;'>"+linhaAtual+"</b><br>"
								+ "<i>Número total de bits da instrução digitada é <b>maior</b> que 32 bits.</i><br><br>"
								+ "Operador: <b>"+op+"</b> | Valor Binário: <b>"+op.getValorBits()+"</b><br>"
								+ "Registrador 1: <b>"+listaReg.get(0)+"</b> | Valor Binário: <b>"+listaReg.get(0).getValorBits()+"</b><br>"
								+  reg2
								+  reg3
								+ "<br><br>"
								+ "Instrução Completa em Binário: <b>"+lm+"</b><br>"
								+ "<i>Valor total de bits:</i> <b style='color:red;'>"+lm.length()+"</b>"
								+ "</html>", "Detectado Overflow!", JOptionPane.ERROR_MESSAGE);
				return true;
		}
		return false;
	}
	public static boolean chkOverflow(String lm, int linhaAtual, Operador op, ArrayList<Registrador> listaReg, String valorBinario){
		String reg2 = "",reg3 = "";
		if(listaReg.size() > 1){
			reg2 = "Registrador 2: <b>"+listaReg.get(1)+"</b> | Valor Binário: <b>"+listaReg.get(1).getValorBits()+"</b><br>";
		}
		if(listaReg.size() > 2){
			reg3 = "Registrador 3: <b>"+listaReg.get(2)+"</b> | Valor Binário: <b>"+listaReg.get(2).getValorBits()+"</b><br>";
		}
		if(lm.length() > 32){
				JOptionPane.showMessageDialog(null,
						"<html>"
						+ "Overflow na linha: "
						+ "<b style='color:red;'>"+linhaAtual+"</b><br>"
						+ "<i>Número total de bits da instrução digitada é <b>menor</b> que 32 bits.</i><br><br>"
						+ "Operador: <b>"+op+"</b> | Valor Binário: <b>"+op.getValorBits()+"</b><br>"
						+ "Registrador 1: <b>"+listaReg.get(0)+"</b> | Valor Binário: <b>"+listaReg.get(0).getValorBits()+"</b><br>"
						+  reg2
						+  reg3
						+ "Valor Digitado:  <b>"+ConversaoBase.converteBinarioParaDecimal(valorBinario)+"</b> | Valor Binário:  <b>"+valorBinario+"</b>"
						+ "<br><br>"
						+ "Instrução Completa em Binário: <b>"+lm+"</b><br>"
						+ "<i>Valor total de bits:</i> <b style='color:red;'>"+lm.length()+"</b>"
						+ "</html>", "Detectado Overflow!", JOptionPane.ERROR_MESSAGE);
				return true;
		}
		return false;
	}	


	
	public static String inverter(String data){
		String binary = new String();
		for(int i = 0; i < data.length(); i++){
			if(data.charAt(i) == '0') binary = binary + '1';
			else binary = binary + '0';
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
			
		}
		while(inverted.charAt(counter) != '0');
		return complement;
	}
	
	public static int unsignedToInteger(String binary){
		int integer = 0;
		for(int i = 0; i < binary.length(); i++){
			if(binary.charAt(i) == '1') integer += (int) Math.pow(2, ((binary.length() - 1) - i));
		}
		return integer;
	}
	
	public static String resizeBinary(String binary, int newSize, boolean tipoI){
		if(binary.length() < newSize){
			if(tipoI){
				while(binary.length() < newSize) { binary = '0' + binary; }
			} else {
				if(binary.charAt(0) == '0'){
					while(binary.length() < newSize){ binary = '0' + binary; }
				} else {
					while(binary.length() < newSize){ binary = '1' + binary; }
				}
			}
		}
		else if(binary.length() > newSize) binary = binary.substring(binary.length() - newSize, binary.length());
		return binary;
	}
	
	public static String and(String firstBinary, String secondBinary){
		String binary = new String();
		if(firstBinary.length() != 32) firstBinary = resizeBinary(firstBinary, 32,false);	
		if(secondBinary.length() != 32) secondBinary = resizeBinary(secondBinary, 32,false);	
		for(int i = 0; i < firstBinary.length(); i++){
			if(firstBinary.charAt(i) == '0' | secondBinary.charAt(i) == '0'){
				binary = binary + '0';
			} else {
				binary = binary + '1';
			}
		}
		return binary;
	}
	
	public static String or(String firstBinary, String secondBinary){
		String binary = new String();
		if(firstBinary.length() != 32) firstBinary = resizeBinary(firstBinary, 32,false);
		if(secondBinary.length() != 32) secondBinary = resizeBinary(secondBinary, 32,false);
		for(int i = 0; i < firstBinary.length(); i++){
			if(firstBinary.charAt(i) == '1' | secondBinary.charAt(i) == '1'){
				binary = binary + '1';
			} else {
				binary = binary + '0';
			}
		}
		return binary;
	}
	
	public static String xor(String firstBinary, String secondBinary){		
		String binary = new String();
		if(firstBinary.length() != 32) firstBinary = resizeBinary(firstBinary, 32,false);		
		if(secondBinary.length() != 32) secondBinary = resizeBinary(secondBinary, 32,false);	
		for(int i = 0; i < firstBinary.length(); i++){
			if(firstBinary.charAt(i) == secondBinary.charAt(i)){
				binary = binary + '0';
			} else {
				binary = binary + '1';
			}
		}
		return binary;
	}
	
	public static String nand(String firstBinary, String secondBinary){
		String binary = and(firstBinary, secondBinary);	
		binary = inverter(binary);
		return binary;
	}
	
	public static String nor(String firstBinary, String secondBinary){
		String binary = or(firstBinary, secondBinary);
		binary = inverter(binary);
		return binary;
	}
	
	public static String xnor(String firstBinary, String secondBinary){	
		String binary = xor(firstBinary, secondBinary);
		binary = inverter(binary);
		return binary;
	}
}