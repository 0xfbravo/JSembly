package com.jsembly.main;


public enum ItensMenu{
	Novo(0,"Novo","img/write13.png",""),
	Abrir(1,"Abrir Arquivo","img/folder5.png","img/folder5_hover.png"),
	Salvar(2,"Salvar Arquivo","img/save23.png",""),
	Compilar(3,"Compilar","img/data45.png",""),
	CompilarStep(4,"Compilar por Step","img/data45_step.png",""),
	Configuracoes(5,"Configurações","img/tools6.png",""),
	Valores(6,"Adicionar Valores","img/losing.png",""),
	Dicas(7,"Dicas","img/light7.png","");

	private int id;
	private String caminhoImg;
	private String camingoImgHover;
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
	ItensMenu(int id,String nomeMenu, String caminhoImg, String caminhoImgHover){
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

	public String getCamingoImgHover() {
		return camingoImgHover;
	}

	public void setCamingoImgHover(String camingoImgHover) {
		this.camingoImgHover = camingoImgHover;
	}
}
