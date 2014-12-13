package com.jsembly.main;

public class Configuracoes {
	private String linguagemMIPS = "Linguagem MIPS",
			linguagemMaquina = "Linguagem Máquina",
			registradores = "Registradores",
			operadores = "Operadores",
			memoria = "Memória",
	        executar = "Executar";
	
	public enum memoria{
		decimal("Decimal"),Hexadecimal("Hexadecimal"),binario("Binário");
		private String nomeMemoria;
		public String getnomeMemoria() {
			return nomeMemoria;
		}

		public void setnomeMemoria(String nomeMemoria) {
			this.nomeMemoria = nomeMemoria;
		}
		memoria(String nomeMemoria){
			this.setnomeMemoria(nomeMemoria);
		}
		
	}
	public enum linguagem{
		pt_br("Português - Brasil"),eng("English - International");
		
		private String nomeLinguagem;
		
		public String getNomeLinguagem() {
			return nomeLinguagem;
		}

		public void setNomeLinguagem(String nomeLinguagem) {
			this.nomeLinguagem = nomeLinguagem;
		}
		linguagem(String nomeLinguagem){
			this.setNomeLinguagem(nomeLinguagem);
		}
	}

	public void linguagemPrograma(int i){
		switch(i){
			default:
			case 0:
				setLinguagemMIPS("Linguagem MIPS");
				setLinguagemMaquina("Linguagagem Máquina");
				setRegistradores("Registradres");
				setOperadores("Operadores");
				setMemoria("Memória");
				setExecutar("Executar");
				break;
			case 1:
				setLinguagemMIPS("MIPS Language");
				setLinguagemMaquina("Machine Language");
				setRegistradores("Registers");
				setOperadores("Instructions");
				setMemoria("Memory");
				setExecutar("Execute");
				break;
		}
	}

	public String getLinguagemMIPS() {
		return linguagemMIPS;
	}

	public void setLinguagemMIPS(String linguagemMIPS) {
		this.linguagemMIPS = linguagemMIPS;
	}

	public String getLinguagemMaquina() {
		return linguagemMaquina;
	}

	public void setLinguagemMaquina(String linguagemMaquina) {
		this.linguagemMaquina = linguagemMaquina;
	}

	public String getRegistradores() {
		return registradores;
	}

	public void setRegistradores(String registradores) {
		this.registradores = registradores;
	}

	public String getMemoria() {
		return memoria;
	}

	public void setMemoria(String memoria) {
		this.memoria = memoria;
	}

	public String getOperadores() {
		return operadores;
	}

	public void setOperadores(String operadores) {
		this.operadores = operadores;
	}

	public String getExecutar() {
		return executar;
	}

	public void setExecutar(String executar) {
		this.executar = executar;
	}
}
