package com.jsembly.main;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.List;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;

import com.jsembly.extras.TextLineNumber;
import com.jsembly.extras.Utilidades;
import com.jsembly.funcoes.Configuracoes;
import com.jsembly.funcoes.ConversaoBase;
import com.jsembly.funcoes.Cores;
import com.jsembly.mips.TipoInstrucao;

import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("serial")
public class Janela extends JFrame{
	ArrayList<HashMap<String, String>> memoria = new ArrayList<HashMap<String, String>>();
	Configuracoes conf = new Configuracoes();
	Timer alphaChanger;
	private String titulo;
	private int altura,largura;
	JFrame janelaInicial = new JFrame();
	JWindow splJanela = new JWindow();
	GridLayout layoutMenu = new GridLayout(0,1);
	GridLayout layoutCentral = new GridLayout(2,1);
	GridLayout layoutBaixo = new GridLayout(1,2);
	JDesktopPane painelMenu = new JDesktopPane();
	JDesktopPane painelJanela = new JDesktopPane();
	
	JTabbedPane painelCima = new JTabbedPane();
		DefaultStyledDocument documento = new DefaultStyledDocument();
			JTextPane linguagemMIPS = new JTextPane(documento);
				StyledDocument doc = linguagemMIPS.getStyledDocument();
			JTextArea linguagemMaquina = new JTextArea();
			JTable abaExecutar = new JTable();
	JDesktopPane painelBaixo = new JDesktopPane();
		JTabbedPane valoresMIPS = new JTabbedPane();
		JTabbedPane tblMemoria = new JTabbedPane();
	
	JDesktopPane splPainel = new JDesktopPane();

	/*
	 * Método para criação da Janela Interna
	 */
	protected JInternalFrame criarJIF(String t, int Altura, int Largura) {
		      JInternalFrame f = new JInternalFrame(t);
		      f.setResizable(false);
		      f.setClosable(true);
		      f.setVisible(true);
		      f.setSize(Largura,Altura);
		      Dimension tamanhoPainelInterno = painelJanela.getSize();
		      Dimension tamanhoJanelaInterna = f.getSize();
		      f.setLocation((tamanhoPainelInterno.width - tamanhoJanelaInterna.width)/2,
		          (tamanhoPainelInterno.height- tamanhoJanelaInterna.height)/2);
		      return f;
	}
	
	public Janela(String titulo,int largura,int altura){
		try{
		File temp = File.createTempFile("temp-file-name", ".tmp");
		System.out.println("Arquivo Temporário : " + temp.getAbsolutePath());
		
		this.setTitulo(titulo);
		this.setAltura(altura);
		this.setLargura(largura);
		UIManager.getDefaults().put("TabbedPane.contentBorderInsets", new Insets(0,0,0,0));
		UIManager.getDefaults().put("TabbedPane.tabsOverlapBorder", true);

		janelaInicial.setTitle(titulo);
		janelaInicial.setSize(largura,altura);
		janelaInicial.setResizable(true);
		janelaInicial.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janelaInicial.setLocationRelativeTo(null);
		janelaInicial.requestFocusInWindow();
		janelaInicial.add(painelJanela, BorderLayout.CENTER);
		painelJanela.setLayout(layoutCentral);
		
		painelJanela.add(painelCima);
		painelBaixo.add(valoresMIPS,BorderLayout.WEST);
		painelBaixo.add(tblMemoria,BorderLayout.EAST);
				linguagemMIPS.addKeyListener(new KeyListener(){
					@Override
					public void keyPressed(KeyEvent arg0) {}
					@Override
					public void keyReleased(KeyEvent arg0) {
						try {
							BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
							switch (arg0.getKeyCode()){					
								default:
									bw.write(linguagemMIPS.getText());
									bw.close();
									Cores.buscarCores(temp.getAbsolutePath(),linguagemMIPS);
								break;
							}
						} catch (IOException e) {
						e.printStackTrace();
						}
					}
					
					@Override
					public void keyTyped(KeyEvent arg0) {
					}
				});
		
		JScrollPane linguagemMIPS_Scroll = new JScrollPane(linguagemMIPS);
		TextLineNumber tln = new TextLineNumber(linguagemMIPS);
		linguagemMIPS_Scroll.setRowHeaderView( tln );
		linguagemMIPS_Scroll.setPreferredSize(painelCima.getMinimumSize());
		painelCima.add(linguagemMIPS_Scroll,conf.getLinguagemMIPS());
		painelCima.setIconAt(0, Utilidades.buscarIcone("img/page_code.png"));
				
		JTextArea painelLinguagemMaquina = new JTextArea();
		painelLinguagemMaquina.setEnabled(false);
		JScrollPane linguagemMaquina = new JScrollPane(painelLinguagemMaquina);
		TextLineNumber tln2 = new TextLineNumber(painelLinguagemMaquina);
		linguagemMaquina.setRowHeaderView( tln2 );
		linguagemMaquina.setPreferredSize(painelCima.getMinimumSize());
		painelCima.add(linguagemMaquina,conf.getLinguagemMaquina());
		painelCima.setIconAt(1, Utilidades.buscarIcone("img/monitor.png"));
		
		DefaultTableModel dtmExec = new DefaultTableModel(0, 0);
		String headerExec[] = new String[] { "Endereço", "Valor Hexadecimal", "Basic", "Código" };
		dtmExec.setColumnIdentifiers(headerExec);
		abaExecutar.setModel(dtmExec);
		abaExecutar.setEnabled(false);
		abaExecutar.getColumnModel().getColumn(0).setHeaderValue("Endereço");
		abaExecutar.getColumnModel().getColumn(1).setHeaderValue("Valor Hexadecimal");
		abaExecutar.getColumnModel().getColumn(2).setHeaderValue("Basic");
		abaExecutar.getColumnModel().getColumn(3).setHeaderValue("Código");

		JScrollPane abaExecutar_Scroll = new JScrollPane(abaExecutar);
		painelCima.add(abaExecutar_Scroll,conf.getExecutar());
		painelCima.setIconAt(2, Utilidades.buscarIcone("img/dashboard.png"));
		
		painelBaixo.setLayout(layoutBaixo);
		painelJanela.add(painelBaixo);

		// -- Tabela de Registradores
		JTable listaMem = new JTable();
		listaMem.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {
            	super.getTableCellRendererComponent(table, value, isSelected,hasFocus, row, column);
            	// Prepare default color         
            	Color color = table.getForeground();
            	if (column == 0) {
            			color = new Color(19,102,97);
            			//new Color(160,43,4)
            	}
            	setForeground(color);
            	return this;
            }                                            
		});
		DefaultTableModel dtmMem = new DefaultTableModel(0, 0);
		String headerMem[] = new String[] { "Endereços", "Dados" };
		dtmMem.setColumnIdentifiers(headerMem);
		listaMem.setModel(dtmMem);
		listaMem.setEnabled(false);
		listaMem.getColumnModel().getColumn(0).setHeaderValue("Endereços");
		listaMem.getColumnModel().getColumn(1).setHeaderValue("Dados");
		
		HashMap<String, String> n = new HashMap<String, String>();
		for(int i = 0; i < 9; i +=4){
			if(i<10){ n.put("00000"+i, "000000");/* System.out.println(memoria.get("00000"+i).toString()+" para o i: "+i);*/}
			else if(100 > i && i >= 10){ n.put("0000"+i, "000000");/* System.out.println(memoria.get("0000"+i).toString()+" para o i: "+i);*/}
			else if(1000 > i && i >= 100){ n.put("000"+i, "000000");/* System.out.println(memoria.get("000"+i).toString()+" para o i: "+i);*/}
			else if(10000 > i && i >= 1000){ n.put("00"+i, "000000");/* System.out.println(memoria.get("00"+i).toString()+" para o i: "+i);*/}
			else if(100000 > i && i >= 10000){ n.put("0"+i, "000000");/* System.out.println(memoria.get("0"+i).toString()+" para o i: "+i);*/ }
			else { n.put(""+i, "000000");/* System.out.println(memoria.get(""+i).toString()+" para o i: "+i);*/}
			memoria.add(n);
		}
		
		for(int i =0; i < memoria.size(); i ++){
			System.out.println(memoria.get(i));
			if(i<10){ dtmMem.addRow(new Object[]{"00000"+i, memoria.get(i)}); }
			else if(100 > i && i >= 10){ dtmMem.addRow(new Object[]{"0000"+i, memoria.get(i)}); }
			else if(1000 > i && i >= 100){ dtmMem.addRow(new Object[]{"000"+i, memoria.get(i)}); }
			else if(10000 > i && i >= 1000){ dtmMem.addRow(new Object[]{"00"+i, memoria.get(i)}); }
			else if(100000 > i && i >= 10000){ dtmMem.addRow(new Object[]{"0"+i, memoria.get(i)}); }
			else { dtmMem.addRow(new Object[]{i, memoria.get(i)}); }
		}
		
		JScrollPane spMem = new JScrollPane(listaMem);
		tblMemoria.add(spMem,conf.getMemoria());
		tblMemoria.setIconAt(0, Utilidades.buscarIcone("img/timeline_marker.png"));
		
		// -- Tabela de Registradores
		JTable listaReg = new JTable();
		listaReg.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {
            	super.getTableCellRendererComponent(table, value, isSelected,hasFocus, row, column);
            	// Prepare default color         
            	Color color = table.getForeground();
            	if (column == 0) {
            			color = new Color(60,38,150);
            			//new Color(160,43,4)
            	}
            	setForeground(color);
            	return this;
            }                                            
		});
		DefaultTableModel dtm = new DefaultTableModel(0, 0);
		String header[] = new String[] { "Registrador", "Número", "Valor Binário" };
		dtm.setColumnIdentifiers(header);
		listaReg.setModel(dtm);
		listaReg.setEnabled(false);
		listaReg.getColumnModel().getColumn(0).setHeaderValue("Registrador");
		listaReg.getColumnModel().getColumn(1).setHeaderValue("Número");
		listaReg.getColumnModel().getColumn(2).setHeaderValue("Valor Binário");

		for(int i = 0; i < ArraysLists.registradores.size(); i ++){
			dtm.addRow(new Object[]{ArraysLists.registradores.get(i).toString(), ArraysLists.registradores.get(i).getId(), ArraysLists.registradores.get(i).getValorBits()});
		}
		JScrollPane spReg = new JScrollPane(listaReg);
		valoresMIPS.add(spReg,conf.getRegistradores());
		valoresMIPS.setIconAt(0, Utilidades.buscarIcone("img/brick.png"));
		
		// -- Tabela de Operadores
		JTable listaOp = new JTable();
		listaOp.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {
            	super.getTableCellRendererComponent(table, value, isSelected,hasFocus, row, column);
            	// Prepare default color         
            	Color color = table.getForeground();
            	if (column == 0) {
            			color = new Color(160,43,4);
            	}
            	setForeground(color);
            	return this;
            }                                            
		});
		DefaultTableModel dtm2 = new DefaultTableModel(0, 0);
		String header2[] = new String[] { "Operador", "Número", "Valor Binário" };
		dtm2.setColumnIdentifiers(header2);
		listaOp.setModel(dtm2);
		listaOp.setEnabled(false);
		listaOp.getColumnModel().getColumn(0).setHeaderValue("Operador");
		listaOp.getColumnModel().getColumn(1).setHeaderValue("Tipo");
		listaOp.getColumnModel().getColumn(2).setHeaderValue("Valor Binário");
		for(int i = 0; i < ArraysLists.operadores.size(); i ++){
			String tipoInstrucao = null;
			switch(ArraysLists.operadores.get(i).getTipoIntrucao()){
				case 0:
					tipoInstrucao = "Tipo I";
					break;
				case 1:
					tipoInstrucao = "Tipo J";
					break;
				case 2:
					tipoInstrucao = "Tipo R";
					break;
			}
			dtm2.addRow(new Object[]{ArraysLists.operadores.get(i).toString(), tipoInstrucao, ArraysLists.operadores.get(i).getValorBits()});
		}
		JScrollPane spOp = new JScrollPane(listaOp);
		valoresMIPS.add(spOp,conf.getOperadores());
		valoresMIPS.setIconAt(1, Utilidades.buscarIcone("img/sockets.png"));
		
		
		janelaInicial.add(painelMenu, BorderLayout.WEST);
		painelMenu.setLayout(layoutMenu);
		painelMenu.setBackground(new Color(29,88,97));
		for(int i =0; i < ItensMenu.values().length; i++){
			SoftJButton itensMenu = new SoftJButton();
			itensMenu.setIcon(Utilidades.buscarIcone(ArraysLists.itensMenuLista.get(i).getCaminhoImg()));
			itensMenu.setToolTipText(ArraysLists.itensMenuLista.get(i).getNomeMenu());
			itensMenu.setBorder(BorderFactory.createEmptyBorder());
			itensMenu.setPreferredSize(new Dimension(90,0));
			itensMenu.setBorderPainted(false);
			itensMenu.setFocusPainted(false);
			itensMenu.setContentAreaFilled(false);
			itensMenu.setOpaque(true);
			itensMenu.setBackground(new Color(29,88,97));

			itensMenu.addMouseListener(new MouseListener(){

				@Override
				public void mouseClicked(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					itensMenu.setBackground(new Color(131,186,194));
					alphaChanger = new Timer(15, new ActionListener() {

			            private float incrementer = -.03f;

			            @Override
			            public void actionPerformed(ActionEvent e) {
			                float newAlpha = itensMenu.getAlpha() + incrementer;
			                if (newAlpha < 0) {
			                    newAlpha = 0;
			                    incrementer = -incrementer;
			                } else if (newAlpha > 1f) {
			                    newAlpha = 1f;
			                    incrementer = -incrementer;
			                }
			                itensMenu.setAlpha(newAlpha);
			            }
			        });
			        alphaChanger.start();
					repaint();
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
					itensMenu.setBackground(new Color(29,88,97));
					alphaChanger.stop();
					itensMenu.setAlpha(1);
					repaint();
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
			painelMenu.add(itensMenu);
			switch(ArraysLists.itensMenuLista.get(i).getId()){
				case 0:
					itensMenu.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							System.out.println(ArraysLists.itensMenuLista.get(0).getNomeMenu());
						}
					});
					break;
				case 1:
					itensMenu.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							System.out.println(ArraysLists.itensMenuLista.get(1).getNomeMenu());
						}
					});
					break;
				case 2:
					itensMenu.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							System.out.println(ArraysLists.itensMenuLista.get(2).getNomeMenu());
						}
					});
					break;
				case 3:
					itensMenu.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							painelLinguagemMaquina.setText("");
							painelCima.setSelectedComponent(linguagemMaquina);
							ArrayList<String> linhasLidas = Utilidades.LerArquivo(temp.getAbsolutePath());
							// Busca por linhas, no arquivo
							for(String linha : linhasLidas){
								Pattern operador = Pattern.compile("\\w+", Pattern.CASE_INSENSITIVE);
								Pattern registrador = Pattern.compile("[$]\\w+", Pattern.CASE_INSENSITIVE);
								Pattern endereco = Pattern.compile(" \\w+|[0-9]|\\,w+|,[0-9]", Pattern.CASE_INSENSITIVE);
								Pattern enderecoTipoI = Pattern.compile(",\\w+|,[0-9]", Pattern.CASE_INSENSITIVE);

								// Busca de Operadores
							    Matcher matcher = operador.matcher(linha);
							    if(matcher.find()) {
								    // Busca de Registradores
								    Matcher matcher2 = registrador.matcher(linha);
								    ArraysLists.regEncontrados.removeAll(ArraysLists.regEncontrados);
								    while(matcher2.find()) {
								      for(int i = 0; i < ArraysLists.registradores.size(); i++){
								    	  if(matcher2.group().toLowerCase().equals(ArraysLists.registradores.get(i).toString())){
								    		  //System.out.println("Achei um registrador: "+registradores.get(i).toString());
								    		  ArraysLists.regEncontrados.add(ArraysLists.registradores.get(i));
								    		  break;
								    	  }
								      }
								    }
							      for(int i = 0; i < ArraysLists.operadores.size(); i++){
							    	  if(matcher.group().toLowerCase().equals(ArraysLists.operadores.get(i).toString())){
							    		 //System.out.println("Achei um operador: "+ArraysLists.operadores.get(i).toString());
						    		  	  String enderecoOuLabel = null;
							    		  switch(ArraysLists.operadores.get(i).getTipoIntrucao()){
							    		  	case 0:
							    		  		//System.out.println("Tipo I");
							    		  		// Busca de Endereços
							    		  		if(ArraysLists.operadores.get(i).toString() == "bne" || ArraysLists.operadores.get(i).toString() == "beq"){
								    		  		Matcher matcher3 = enderecoTipoI.matcher(linha);
													if(matcher3.find()) { enderecoOuLabel = matcher3.group().substring(1); }
								    		  		painelLinguagemMaquina.append(TipoInstrucao.InstrucaoTipoI(ArraysLists.operadores.get(i).getValorBits(),ArraysLists.regEncontrados.get(0).getValorBits(),ArraysLists.regEncontrados.get(1).getValorBits(),enderecoOuLabel)+"\n");
							    		  		}
							    		  		break;
							    		  	case 1:
							    		  		//System.out.println("Tipo J");
							    		  		// Busca de Endereços
							    		  		Matcher matcher4 = endereco.matcher(linha);
												if(matcher4.find()) { enderecoOuLabel = matcher4.group().substring(1); }							    		  		
							    		  		painelLinguagemMaquina.append(TipoInstrucao.InstrucaoTipoJ(ArraysLists.operadores.get(i).getValorBits(),enderecoOuLabel)+"\n");
							    		  		break;
							    		  	case 2:
							    		  		//System.out.println("Tipo R");
							    		  		painelLinguagemMaquina.append(TipoInstrucao.InstrucaoTipoR(ArraysLists.regEncontrados.get(0).getValorBits(),ArraysLists.regEncontrados.get(1).getValorBits(),ArraysLists.regEncontrados.get(2).getValorBits(),"00000",ArraysLists.operadores.get(i).getValorBits())+"\n");
							    		  		dtmExec.addRow(new Object[]{
							    		  				"[Implementar]",
							    		  				"0x"+ConversaoBase.converteBinarioParaHexadecimal(TipoInstrucao.InstrucaoTipoR(ArraysLists.regEncontrados.get(0).getValorBits(),ArraysLists.regEncontrados.get(1).getValorBits(),ArraysLists.regEncontrados.get(2).getValorBits(),"00000",ArraysLists.operadores.get(i).getValorBits())),
							    		  				ArraysLists.operadores.get(i)+" $"+ArraysLists.regEncontrados.get(0).getId()+",$"+ArraysLists.regEncontrados.get(1).getId()+",$"+ArraysLists.regEncontrados.get(2).getId(),
							    		  				i+1+": "+ArraysLists.operadores.get(i)+" "+ArraysLists.regEncontrados.get(0).toString()+","+ArraysLists.regEncontrados.get(1).toString()+","+ArraysLists.regEncontrados.get(2).toString()});
							    		  		break;
							    		  }
							    		  break;
							    	  }
							      }
							    }
							}
							for(int i = 0; i < ArraysLists.arrLabel.size(); i++){
								System.out.println(ArraysLists.arrLabel.get(i));
							}
						}
					});
					break;
					
				// -- Configurações
				case 4:
					itensMenu.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							//System.out.println(ArraysLists.itensMenuLista.get(4).getNomeMenu());
							JFrame janelaConf = new JFrame("Configurações");
							janelaConf.setVisible(true);
							janelaConf.setSize(350, 200);
							janelaConf.setResizable(false);
							janelaConf.setIconImage(Utilidades.buscarIcone("img/page_white_wrench.png").getImage());
							janelaConf.setLocationRelativeTo(null);
							janelaConf.requestFocusInWindow();
							
							JDesktopPane jdp = new JDesktopPane();
							janelaConf.add(jdp);
							
							JLabel lblLinguagem = new JLabel("Linguagem");
							lblLinguagem.setSize(170,20);
							lblLinguagem.setLocation(30, 10);
							jdp.add(lblLinguagem);
							JComboBox<String> linguagem = new JComboBox<String>();
							linguagem.setSize(170, 20);
							linguagem.addItem(Configuracoes.linguagem.pt_br.getNomeLinguagem());
							linguagem.addItem(Configuracoes.linguagem.eng.getNomeLinguagem());
							linguagem.setLocation(30, 30);
							jdp.add(linguagem);
							
							JButton salvar = new JButton("Salvar");
							salvar.setSize(60,30);
							salvar.setLocation(260, 130);
							jdp.add(salvar);
							salvar.addActionListener(new ActionListener(){

								@Override
								public void actionPerformed(ActionEvent arg0) {
									conf.linguagemPrograma(linguagem.getSelectedIndex());
									valoresMIPS.setTitleAt(0, conf.getRegistradores());
									valoresMIPS.setTitleAt(1, conf.getOperadores());
									tblMemoria.setTitleAt(0, conf.getMemoria());
									painelCima.setTitleAt(0, conf.getLinguagemMIPS());
									painelCima.setTitleAt(1, conf.getLinguagemMaquina());
									painelCima.setTitleAt(2, conf.getExecutar());
									repaint();
								}
								
							});
						}
					});
					break;
				case 5:
					itensMenu.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							System.out.println(ArraysLists.itensMenuLista.get(5).getNomeMenu());
						}
					});
					break;
				case 6:
					itensMenu.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							System.out.println(ArraysLists.itensMenuLista.get(6).getNomeMenu());
						}
					});
					break;
			}
		}
		Splash();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void Splash(){
		Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(
				Toolkit.getDefaultToolkit().createImage(""),new Point(splJanela.getX(), splJanela.getY()), "Cursor");
		splJanela.setCursor(c);
		splJanela.setSize(602, 450);
		splJanela.setLocationRelativeTo(null);
		splJanela.setBackground(new Color(0, 0, 0, 0));
		
		splPainel.setBackground(new Color(0, 0, 0, 0));
		
		JLabel lblNewLabel = new JLabel("v0.1b");
		lblNewLabel.setForeground(SystemColor.controlDkShadow);
		splPainel.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel.setBounds(486, 363, 85, 25);
		
		JLabel lblImgLoad = new JLabel("");
		lblImgLoad.setHorizontalAlignment(SwingConstants.CENTER);
		lblImgLoad.setIcon(Utilidades.buscarIcone("img/loader.gif"));
		lblImgLoad.setBounds(367, 288, 50, 48);
		splPainel.add(lblImgLoad);
		
		JLabel splashImg = new JLabel();
		splashImg.setLocation(0, -110);
		splashImg.setHorizontalAlignment(SwingConstants.CENTER);
		splashImg.setIcon(Utilidades.buscarIcone("img/jSembly_LOGO.png"));
		splashImg.setSize(600, 600);
		splPainel.add(splashImg);
		
		splJanela.getContentPane().add(splPainel);
		splJanela.setOpacity(0.05f);
		splJanela.setVisible(true);
		
		for(int i= 1; i <= 50; i++){
			try {
				splJanela.setOpacity(0.02f * i);
				Thread.sleep(40);
				splJanela.repaint();
			} catch (InterruptedException e) {
			    e.printStackTrace();
			}
		}
		try {
			Thread.sleep(1300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		splJanela.setVisible(false);
		janelaInicial.setVisible(true);
	}
    private static class SoftJButton extends JButton {

        private static final JButton lafDeterminer = new JButton();
        private static final long serialVersionUID = 1L;
        private boolean rectangularLAF;
        private float alpha = 1f;

        SoftJButton() {
            this(null, null);
        }

        SoftJButton(String text, Icon icon) {
            super(text, icon);
            setOpaque(false);
            setFocusPainted(false);
        }

        public float getAlpha() {
            return alpha;
        }

        public void setAlpha(float alpha) {
            this.alpha = alpha;
            repaint();
        }

        @Override
        public void paintComponent(java.awt.Graphics g) {
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            if (rectangularLAF && isBackgroundSet()) {
                Color c = getBackground();
                g2.setColor(c);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
            super.paintComponent(g2);
        }

        @Override
        public void updateUI() {
            super.updateUI();
            lafDeterminer.updateUI();
            rectangularLAF = lafDeterminer.isOpaque();
        }
    }
	public int getLargura() {
		return largura;
	}

	public void setLargura(int largura) {
		this.largura = largura;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public int getAltura() {
		return altura;
	}

	public void setAltura(int altura) {
		this.altura = altura;
	}
}
