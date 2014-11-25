package com.jsembly.funcoes;

public class ConversaoBase {
	/**
	* Converte bin�rio para hexadecimal. A regra � converter grupos de 4 bits - da direita para a esquerda - em um caractere hexadecimal. Para facilitar,
	* converto o grupo de 4 caracteres em bin�rio para decimal e o valor resultante ser� a posi��o do array de valores hexadecimais, cujo caractere ser�
	* retornado. Obs.: assume que o valor passado � diferente de null e tem pelo menos um bit. Exemplo: 11011011
	* Grupo 1: 1011 = 11 em decimal -> posi��o 11 do array de hexadecimais: 'b' -> Resultado: b
	* Grupo 2: 1101 = 13 em decimal -> posi��o 13 do array de hexadecimais: 'd' -> Resultado: db
	*
	* Resultado: db
	*
	* @param binario valor bin�rio a ser convertido
	* @return valor em hexadecimal
	*/
	public static String converteBinarioParaHexadecimal(String binario) {
	   char[] hexa = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	   StringBuilder sb = new StringBuilder();
	   int posicaoInicial = 0;
	   int posicaoFinal = 0;
	   char caractereEncontrado = '-';
	 
	   // come�a a pegar grupos de 4 d�gitos da direita para a esquerda, por isso posicaoInicial e posicaoFinal ficam na posi��o de fim da String
	   posicaoInicial = binario.length();
	   posicaoFinal = posicaoInicial;
	 
	   while (posicaoInicial > 0) {
	      // pega de 4 em 4 caracteres da direita para a esquerda. Se o �ltimo grupo � esquerda tiver menos de 4 caracteres, pega os restantes (1, 2 ou 3)
	      posicaoInicial = ((posicaoInicial - 4) >= 0) ? posicaoInicial - 4 : 0;
	 
	      /*
	      *  Transforma o grupo de 4 caracteres em um d�gito hexadecimal. Primeiro converte o grupo de 4 (ou menos) caracteres em decimal e depois pega
	      *  o caractere correspondente no array de hexadecimais. Utilize o m�todo converteBinarioParaDecimal(String) mais acima.
	      */
	      caractereEncontrado = hexa[converteBinarioParaDecimal(binario.substring(posicaoInicial, posicaoFinal))];
	      // insere no in�cio da String de retorno
	      sb.insert(0, caractereEncontrado);
	 
	      posicaoFinal = posicaoInicial;
	   }
	 
	   return sb.toString();
	}
	/**
	* Converte decimal para bin�rio. A regra � ficar dividindo o valor por 2, pegar o resto de cada divis�o e inserir o valor da direita para a esquerda na String de
	* retorno. O algoritmo � executado at� que o valor que foi sucessivamente dividido se torne 0. Obs.: assume que o valor passado � inteiro positivo.
	* Exemplo: 13
	* 13/2 = 6 -> resto 1 -> Resultado: 1
	* 6/2 = 3  -> resto 0 -> Resultado: 01
	* 3/2 = 1  -> resto 1 -> Resultado: 101
	* 1/2 = 0  -> resto 1 -> Resultado: 1101
	*
	* Resultado: 1101
	*
	* @param valor n�mero decimal a ser convertido
	* @return String contendo o valor em bin�rio
	*/
	public static String converteDecimalParaBinario(int valor) {
	   int resto = -1;
	   StringBuilder sb = new StringBuilder();

	   if (valor == 0) {
	      return "0";
	   }

	   // enquanto o resultado da divis�o por 2 for maior que 0 adiciona o resto ao in�cio da String de retorno
	   while (valor > 0) {
	      resto = valor % 2;
	      valor = valor / 2;
	      sb.insert(0, resto);
	   }

	   return sb.toString();
	}
	/**
	* Converte decimal para hexadecimal. A regra � ficar dividindo o valor por 16, pegar o resto de cada divis�o e inserir o valor da direita para a esquerda na String * de retorno. Se o resultado da divis�o for maior que 15, chamo o m�todo atrav�s de recurs�o. O algoritmo � executado at� que o valor que foi sucessivamente
	* dividido se torne 0. Obs.: assume que o valor passado � inteiro positivo.
	* Exemplo: 1279
	* 1279/16 = 79 -> resto 15 -> Resultado: F
	* 79/16 = 4    -> resto 15 -> Resultado: F
	* 4/16 = 0     -> resto 4  -> Resultado: 4
	*
	* Resultado: 4FF
	*
	* @param valor n�mero decimal a ser convertido
	* @return String contendo o valor em hexadecimal
	*/
	public static String converteDecimalParaHexadecimal(int valor) {
	   char[] hexa = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	   int resto = -1;
	   StringBuilder sb = new StringBuilder();

	   if (valor == 0) {
	      return "0";
	   }

	   // enquanto o resultado da divis�o por 16 for maior que 0 adiciona o resto ao in�cio da String de retorno
	   while (valor > 0) {
	      resto = valor % 16;
	      valor = valor / 16;
	      sb.insert(0, hexa[resto]);
	   }

	   return sb.toString();
	}
	/**
	* Converte bin�rio para decimal. A regra � ficar multiplicando, da direita para a esquerda, o valor bin�rio por 2 elevado a um �ndice (come�a em 0).
	* Obs.: assume que o valor passado � diferente de null e tem pelo menos um bit. Exemplo: '1101'
	* 1*2^0 + 0*2^1 + 1*2^2 + 1*2^3 = 13.
	*
	* Resultado: 13
	*
	* @param valorBinario String contendo o valor em bin�rio
	* @return valor convertido em decimal
	*/
	public static int converteBinarioParaDecimal(String valorBinario) {
	   int valor = 0;

	   // soma ao valor final o d�gito bin�rio da posi��o * 2 elevado ao contador da posi��o (come�a em 0)
	   for (int i=valorBinario.length(); i>0; i--) {
	      valor += Integer.parseInt(valorBinario.charAt(i-1)+"")*Math.pow(2, (valorBinario.length()-i));
	   }

	   return valor;
	}

}
