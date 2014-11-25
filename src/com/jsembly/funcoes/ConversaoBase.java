package com.jsembly.funcoes;

public class ConversaoBase {
	/**
	* Converte binário para hexadecimal. A regra é converter grupos de 4 bits - da direita para a esquerda - em um caractere hexadecimal. Para facilitar,
	* converto o grupo de 4 caracteres em binário para decimal e o valor resultante será a posição do array de valores hexadecimais, cujo caractere será
	* retornado. Obs.: assume que o valor passado é diferente de null e tem pelo menos um bit. Exemplo: 11011011
	* Grupo 1: 1011 = 11 em decimal -> posição 11 do array de hexadecimais: 'b' -> Resultado: b
	* Grupo 2: 1101 = 13 em decimal -> posição 13 do array de hexadecimais: 'd' -> Resultado: db
	*
	* Resultado: db
	*
	* @param binario valor binário a ser convertido
	* @return valor em hexadecimal
	*/
	public static String converteBinarioParaHexadecimal(String binario) {
	   char[] hexa = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	   StringBuilder sb = new StringBuilder();
	   int posicaoInicial = 0;
	   int posicaoFinal = 0;
	   char caractereEncontrado = '-';
	 
	   // começa a pegar grupos de 4 dígitos da direita para a esquerda, por isso posicaoInicial e posicaoFinal ficam na posição de fim da String
	   posicaoInicial = binario.length();
	   posicaoFinal = posicaoInicial;
	 
	   while (posicaoInicial > 0) {
	      // pega de 4 em 4 caracteres da direita para a esquerda. Se o último grupo à esquerda tiver menos de 4 caracteres, pega os restantes (1, 2 ou 3)
	      posicaoInicial = ((posicaoInicial - 4) >= 0) ? posicaoInicial - 4 : 0;
	 
	      /*
	      *  Transforma o grupo de 4 caracteres em um dígito hexadecimal. Primeiro converte o grupo de 4 (ou menos) caracteres em decimal e depois pega
	      *  o caractere correspondente no array de hexadecimais. Utilize o método converteBinarioParaDecimal(String) mais acima.
	      */
	      caractereEncontrado = hexa[converteBinarioParaDecimal(binario.substring(posicaoInicial, posicaoFinal))];
	      // insere no início da String de retorno
	      sb.insert(0, caractereEncontrado);
	 
	      posicaoFinal = posicaoInicial;
	   }
	 
	   return sb.toString();
	}
	/**
	* Converte decimal para binário. A regra é ficar dividindo o valor por 2, pegar o resto de cada divisão e inserir o valor da direita para a esquerda na String de
	* retorno. O algoritmo é executado até que o valor que foi sucessivamente dividido se torne 0. Obs.: assume que o valor passado é inteiro positivo.
	* Exemplo: 13
	* 13/2 = 6 -> resto 1 -> Resultado: 1
	* 6/2 = 3  -> resto 0 -> Resultado: 01
	* 3/2 = 1  -> resto 1 -> Resultado: 101
	* 1/2 = 0  -> resto 1 -> Resultado: 1101
	*
	* Resultado: 1101
	*
	* @param valor número decimal a ser convertido
	* @return String contendo o valor em binário
	*/
	public static String converteDecimalParaBinario(int valor) {
	   int resto = -1;
	   StringBuilder sb = new StringBuilder();

	   if (valor == 0) {
	      return "0";
	   }

	   // enquanto o resultado da divisão por 2 for maior que 0 adiciona o resto ao início da String de retorno
	   while (valor > 0) {
	      resto = valor % 2;
	      valor = valor / 2;
	      sb.insert(0, resto);
	   }

	   return sb.toString();
	}
	/**
	* Converte decimal para hexadecimal. A regra é ficar dividindo o valor por 16, pegar o resto de cada divisão e inserir o valor da direita para a esquerda na String * de retorno. Se o resultado da divisão for maior que 15, chamo o método através de recursão. O algoritmo é executado até que o valor que foi sucessivamente
	* dividido se torne 0. Obs.: assume que o valor passado é inteiro positivo.
	* Exemplo: 1279
	* 1279/16 = 79 -> resto 15 -> Resultado: F
	* 79/16 = 4    -> resto 15 -> Resultado: F
	* 4/16 = 0     -> resto 4  -> Resultado: 4
	*
	* Resultado: 4FF
	*
	* @param valor número decimal a ser convertido
	* @return String contendo o valor em hexadecimal
	*/
	public static String converteDecimalParaHexadecimal(int valor) {
	   char[] hexa = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	   int resto = -1;
	   StringBuilder sb = new StringBuilder();

	   if (valor == 0) {
	      return "0";
	   }

	   // enquanto o resultado da divisão por 16 for maior que 0 adiciona o resto ao início da String de retorno
	   while (valor > 0) {
	      resto = valor % 16;
	      valor = valor / 16;
	      sb.insert(0, hexa[resto]);
	   }

	   return sb.toString();
	}
	/**
	* Converte binário para decimal. A regra é ficar multiplicando, da direita para a esquerda, o valor binário por 2 elevado a um índice (começa em 0).
	* Obs.: assume que o valor passado é diferente de null e tem pelo menos um bit. Exemplo: '1101'
	* 1*2^0 + 0*2^1 + 1*2^2 + 1*2^3 = 13.
	*
	* Resultado: 13
	*
	* @param valorBinario String contendo o valor em binário
	* @return valor convertido em decimal
	*/
	public static int converteBinarioParaDecimal(String valorBinario) {
	   int valor = 0;

	   // soma ao valor final o dígito binário da posição * 2 elevado ao contador da posição (começa em 0)
	   for (int i=valorBinario.length(); i>0; i--) {
	      valor += Integer.parseInt(valorBinario.charAt(i-1)+"")*Math.pow(2, (valorBinario.length()-i));
	   }

	   return valor;
	}

}
