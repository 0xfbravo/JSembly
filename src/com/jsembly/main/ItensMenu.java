package com.jsembly.main;


public enum ItensMenu{
	Novo(0,"Novo","img/write13.png"),
	Abrir(1,"Abrir Arquivo","img/folder5.png"),
	Salvar(2,"Salvar Arquivo","img/save23.png"),
	Compilar(3,"Compilar","img/data45.png"),
	Configuracoes(4,"Configurações","img/tools6.png"),
	Valores(5,"Adicionar Valores","img/losing.png"),
	Dicas(6,"Dicas","img/light7.png");

	private int id;
	private String caminhoImg;
	private String nomeMenu;

	public String getCaminhoImg() {
		return caminhoImg;
	}

	public void setCaminhoImg(String caminhoImg) {
		this.caminhoImg = caminhoImg;
	}
	public String getNomeMenu() {
		return nomeMenu;
	}

	public void setNomeMenu(String nomeMenu) {
		this.nomeMenu = nomeMenu;
	}
	ItensMenu(int id,String nomeMenu, String caminhoImg){
		this.setId(id);
		this.setCaminhoImg(caminhoImg);
		this.setNomeMenu(nomeMenu);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
