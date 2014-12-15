package com.jsembly.main;


public enum ItensMenu{
	Novo(0,"Novo","img/add118_hover.png","img/add118.png"),
	Abrir(1,"Abrir","img/file18_hover.png","img/file18.png"),
	Salvar(2,"Salvar","img/save27_hover.png","img/save27.png"),
	Montar(3,"Montar","img/data45_hover.png","img/data45.png"),
	Executar(4,"Executar","img/media23_hover.png","img/media23.png"),
	Configuracoes(5,"Configurações","img/screwdriver3_hover.png","img/screwdriver3.png"),
	Valores(6,"Valor Registrador","img/losing_hover.png","img/losing.png"),
	Dicas(7,"Dicas","img/lightbulb-1_hover.png","img/lightbulb-1.png");

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
	ItensMenu(int id,String nomeMenu, String caminho, String caminhoHover){
		this.setId(id);
		this.setCaminhoImg(caminho);
		this.setNomeMenu(nomeMenu);
		this.setCamingoImgHover(caminhoHover);
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
