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
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;

import com.jsembly.extras.JTextFieldLimit;
import com.jsembly.extras.TextLineNumber;
import com.jsembly.extras.Utilidades;
import com.jsembly.funcoes.ArithmeticLogicUnit;
import com.jsembly.funcoes.BinaryArithmetic;
import com.jsembly.funcoes.BinaryLogic;
import com.jsembly.funcoes.Configuracoes;
import com.jsembly.funcoes.ConversaoBase;
import com.jsembly.funcoes.Cores;
import com.jsembly.mips.Registrador;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("serial")
public class Janela extends JFrame{
	BinaryArithmetic binAri = new BinaryArithmetic();
	String lblOito;
	File temp;
	ArrayList<Color> cores = new ArrayList<Color>();
	Configuracoes conf = new Configuracoes();
	int linhaAtualStep = 0;
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
	FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivo Assembly (*.asm)", "asm", "assembly");
	
	JTextField valor1 = null;
	JTextField valor2 = null;
	JTextField valor3 = null;
	JTextField valor4 = null;

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
		for(int i = 0; i < 100; i ++){
			cores.add(Cores.gerarCores()); // Cor Aleatória
		}
		try{
		temp = File.createTempFile("temp-file-name", ".asm");
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
						} catch (IOException e) { e.printStackTrace(); }
					}
					
					@Override
					public void keyTyped(KeyEvent arg0) {}
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

		// -- Tabela da Memória
		JTable listaMem = new JTable(){
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
			{
				Component c = super.prepareRenderer(renderer, row, column);

				//  Color row based on a cell value

				if (!isRowSelected(row))
				{
					c.setBackground(getBackground());
					int modelRow = convertRowIndexToModel(row);
					int instrucao = 4;
					String type = (String)getModel().getValueAt(modelRow, 1);
					if ((!"".equals(type)) && modelRow < instrucao){ c.setBackground(cores.get(0)); c.setForeground(Color.WHITE);}
					for(int i = 1; i < 100; i++){
						if ((!"".equals(type)) && (modelRow >= instrucao*i && modelRow < instrucao*(i+1))){ c.setBackground(cores.get(i)); c.setForeground(Color.WHITE); }
					}
				}

				return c;
			}
		};
		listaMem.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {
            	super.getTableCellRendererComponent(table, value, isSelected,hasFocus, row, column);      
            	Color color = table.getForeground();
            	if (column == 0) {
            		setFont(getFont().deriveFont(Font.BOLD));
            		color = new Color(67,116,181);
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
		
		Memoria memoria = new Memoria(10240,dtmMem);
		
		JScrollPane spMem = new JScrollPane(listaMem);
		tblMemoria.add(spMem,conf.getMemoria());
		tblMemoria.setIconAt(0, Utilidades.buscarIcone("img/timeline_marker.png"));
		
		// -- Tabela de Registradores
		JTable listaReg = new JTable(){
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
			{
				Component c = super.prepareRenderer(renderer, row, column);

				//  Color row based on a cell value

				if (!isRowSelected(row))
				{
					c.setBackground(getBackground());
					int modelRow = convertRowIndexToModel(row);
					String type = (String)getModel().getValueAt(modelRow, 4);
					if(type.equals("Ativo")){
						c.setBackground(new Color(226,226,226));
						c.setForeground(new Color(97,134,26));
						DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
						centro.setHorizontalAlignment(SwingConstants.CENTER);
	            		c.setFont(getFont().deriveFont(Font.BOLD));
	            		centro.setIcon(Utilidades.buscarIcone("img/connect.png"));
	            		getColumnModel().getColumn(4).setCellRenderer(centro);
					} else {
						c.setBackground(new Color(247,247,247));
						DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
						centro.setHorizontalAlignment(SwingConstants.CENTER);
	            		centro.setForeground(new Color(167,167,167));
	            		centro.setIcon(Utilidades.buscarIcone("img/disconnect.png"));
	            		getColumnModel().getColumn(4).setCellRenderer(centro);
					}
				}

				return c;
			}
		};
		listaReg.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {
            	super.getTableCellRendererComponent(table, value, isSelected,hasFocus, row, column);
            	if (column == 0) {
            		setFont( getFont().deriveFont(Font.BOLD) );
            		setForeground(new Color(55,55,55));
            	}
            	if (column == 1) { setForeground(new Color(28,89,75)); }
            	if (column == 2) { setForeground(new Color(206,159,31)); }
            	if (column == 3) { setForeground(new Color(118,146,153)); }
            	setBackground(Color.WHITE);
            	return this;
            }                                            
		});
		DefaultTableModel dtm = new DefaultTableModel(0, 0);
		String header[] = new String[] { "Registrador", "ID", "Valor", "Valor Binário", "Atividade" };
		dtm.setColumnIdentifiers(header);
		listaReg.setModel(dtm);
		listaReg.setEnabled(false);
		listaReg.getColumnModel().getColumn(0).setHeaderValue("Registrador");
		listaReg.getColumnModel().getColumn(0).setMaxWidth(63);
		listaReg.getColumnModel().getColumn(1).setHeaderValue("ID");
		listaReg.getColumnModel().getColumn(1).setMaxWidth(23);
		listaReg.getColumnModel().getColumn(2).setHeaderValue("Valor");
		listaReg.getColumnModel().getColumn(2).setMaxWidth(46);
		listaReg.getColumnModel().getColumn(3).setHeaderValue("Valor Binário");
		listaReg.getColumnModel().getColumn(4).setHeaderValue("Atividade");

		for(int i = 0; i < ArraysLists.registradores.size(); i ++){
			String atividade = "Inativo";
			if(ArraysLists.registradores.get(i).isAtivo()){atividade = "Ativo";}
			dtm.addRow(new Object[]{
					ArraysLists.registradores.get(i).toString(),
					ArraysLists.registradores.get(i).getId(),
					ArraysLists.registradores.get(i).getValorInicial(),
					ArraysLists.registradores.get(i).getValorBits(),
					atividade});
		}
		JScrollPane spReg = new JScrollPane(listaReg);
		valoresMIPS.add(spReg,conf.getRegistradores());
		valoresMIPS.setIconAt(0, Utilidades.buscarIcone("img/brick.png"));
		
		// -- Tabela de Operadores
		JTable listaOp = new JTable(){
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
			{
				Component c = super.prepareRenderer(renderer, row, column);

				if (!isRowSelected(row))
				{
					c.setBackground(getBackground());
					int modelRow = convertRowIndexToModel(row);
					String type = (String)getModel().getValueAt(modelRow, 1);
					if("Tipo R".equals(type)||"Tipo R (Jump/Branch)".equals(type)){
						c.setBackground(new Color(247,247,247));
					}
					else if("Tipo I".equals(type)||"Tipo I (Load/Store)".equals(type)||"Tipo I (Jump/Branch)".equals(type)){
						c.setBackground(new Color(247,247,247));
					}
					else if("Tipo J".equals(type)){
						c.setBackground(new Color(247,247,247));
					}
				}

				return c;
			}
		};
		listaOp.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {
            	super.getTableCellRendererComponent(table, value, isSelected,hasFocus, row, column);
            	if (column == 0) {
            		setFont( getFont().deriveFont(Font.BOLD) );
            		setForeground(new Color(55,55,55));
            	}
            	if (column == 1) {
            		setFont( getFont().deriveFont(Font.BOLD) );
            		setForeground(new Color(28,89,75));
            	}
            	if (column == 2) { setForeground(new Color(181,30,40)); }
            	setBackground(Color.WHITE);
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
				case 0: tipoInstrucao = "Tipo I"; break;
				case 1: tipoInstrucao = "Tipo J"; break;
				case 2: tipoInstrucao = "Tipo R"; break;
				case 3: tipoInstrucao = "Tipo I (Load/Store)"; break;
				case 4: tipoInstrucao = "Tipo R (Jump/Branch)"; break;
				case 5: tipoInstrucao = "Tipo I (Jump/Branch)"; break;
			}
			dtm2.addRow(new Object[]{ArraysLists.operadores.get(i).toString(), tipoInstrucao, ArraysLists.operadores.get(i).getValorBits()});
		}
		JScrollPane spOp = new JScrollPane(listaOp);
		valoresMIPS.add(spOp,conf.getOperadores());
		valoresMIPS.setIconAt(1, Utilidades.buscarIcone("img/sockets.png"));
		
		
		janelaInicial.add(painelMenu, BorderLayout.WEST);
		painelMenu.setLayout(layoutMenu);
		painelMenu.setBackground(new Color(63,63,63));
		painelMenu.setPreferredSize(new Dimension(120,0));
		for(int i =0; i < ItensMenu.values().length; i++){
			
			SoftJButton itensMenu = new SoftJButton();
			itensMenu.setIcon(Utilidades.buscarIcone(ArraysLists.itensMenuLista.get(i).getCaminhoImg()));
			itensMenu.setToolTipText(ArraysLists.itensMenuLista.get(i).getNomeMenu());
			itensMenu.setText(ArraysLists.itensMenuLista.get(i).getNomeMenu());
			itensMenu.setBorder(BorderFactory.createEmptyBorder());
			itensMenu.setPreferredSize(new Dimension(90,0));
			itensMenu.setBorderPainted(false);
			itensMenu.setFocusPainted(false);
			itensMenu.setHorizontalTextPosition(JButton.CENTER);
			itensMenu.setVerticalTextPosition(JButton.BOTTOM);
			itensMenu.setContentAreaFilled(false);
			itensMenu.setOpaque(true);
			itensMenu.setBackground(new Color(63,63,63));
			itensMenu.setForeground(Color.WHITE);
			int id = ArraysLists.itensMenuLista.get(i).getId();

			itensMenu.addMouseListener(new MouseListener(){

				@Override
				public void mouseClicked(MouseEvent arg0) {}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					itensMenu.setForeground(Color.BLACK);
					itensMenu.setBackground(new Color(209,209,209));
					itensMenu.setIcon(Utilidades.buscarIcone(ArraysLists.itensMenuLista.get(id).getCamingoImgHover()));
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
					itensMenu.setForeground(Color.WHITE);
					itensMenu.setBackground(new Color(63,63,63));
					itensMenu.setIcon(Utilidades.buscarIcone(ArraysLists.itensMenuLista.get(id).getCaminhoImg()));
					alphaChanger.stop();
					itensMenu.setAlpha(1);
					repaint();
				}

				@Override
				public void mousePressed(MouseEvent arg0) {}

				@Override
				public void mouseReleased(MouseEvent arg0) {}
				
			});
			painelMenu.add(itensMenu);
			switch(ArraysLists.itensMenuLista.get(i).getId()){
				// -- Novo Arquivo
				case 0:
					itensMenu.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							int resposta = JOptionPane.showConfirmDialog(Janela.this,
									"<html>"
									+ "Deseja fechar o arquivo atual e abrir um novo?<br>"
									+ "<i>Aconselhamos que o salve a fim de não perder dados importantes.</i>"
									+ "</html>",
									"Abrir arquivo novo?",
									JOptionPane.YES_NO_OPTION);
							if(resposta == JOptionPane.YES_OPTION){
								try {
									temp = File.createTempFile("temp-file-name", ".asm");
									linguagemMIPS.setText("");
									Registrador.LimparAtividade(dtm);
									dtmExec.setRowCount(0);
									memoria.LimparMemoria(dtmMem);
									dtmMem.fireTableDataChanged();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							} else {
								JOptionPane.showMessageDialog(Janela.this,
										"<html>"
										+ "Salve o arquivo <b>antes</b> de iniciar um novo, a fim de não perder dados importantes."
										+ "</html>",
										"Dicas jSembly",
										JOptionPane.WARNING_MESSAGE);
							}

						}
					});
					break;
				// -- Abrir Arquivo
					case 1:
					itensMenu.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							JFileChooser abrirArquivo = new JFileChooser(); 
							abrirArquivo.setCurrentDirectory(new java.io.File("."));
							abrirArquivo.setDialogTitle(ArraysLists.itensMenuLista.get(1).getNomeMenu());
							abrirArquivo.setVisible(true);
							abrirArquivo.setFileFilter(filter);
							int retorno = abrirArquivo.showSaveDialog(null);
							if (retorno==JFileChooser.APPROVE_OPTION){
								temp = new File(abrirArquivo.getSelectedFile().getAbsolutePath());
								linguagemMIPS.setText("");
								Registrador.LimparAtividade(dtm);
								dtmExec.setRowCount(0);
								memoria.LimparMemoria(dtmMem);
								dtmMem.fireTableDataChanged();
								ArrayList<String> linhasLidas = Utilidades.LerArquivo(temp.getAbsolutePath());
								for(String linha : linhasLidas){
									try {
										doc.insertString(doc.getLength(), linha+"\n", null);
									} catch (BadLocationException e1) {
										e1.printStackTrace();
									}
								}
							}
						}
					});
					break;
				// -- Salvar Arquivo
				case 2:
					itensMenu.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							JFileChooser salvarArquivo = new JFileChooser(); 
							salvarArquivo.setCurrentDirectory(temp.getAbsoluteFile());
							salvarArquivo.setDialogTitle(ArraysLists.itensMenuLista.get(2).getNomeMenu());
							salvarArquivo.setVisible(true);
							salvarArquivo.setSelectedFile(temp.getAbsoluteFile());
							salvarArquivo.setFileFilter(filter);
							int retorno = salvarArquivo.showSaveDialog(null);
							if (retorno==JFileChooser.APPROVE_OPTION){
								linguagemMIPS.setText("");
								File arqRenomeado;
								if(temp.getAbsolutePath().contains(".")){
									arqRenomeado = new File(salvarArquivo.getSelectedFile().getAbsolutePath());
								} else {
									arqRenomeado = new File(salvarArquivo.getSelectedFile().getAbsolutePath()+".asm");
								}
								temp.renameTo(arqRenomeado);
								JOptionPane.showMessageDialog(Janela.this,
										"<html>"
										+ "O arquivo foi salvo com <b style='color: #375828;'>SUCESSO</b>!<br>"
										+ "<i>Nenhuma alteração será perdida.</i>"
										+ "</html>",
										"Arquivo salvo com sucesso!",
										JOptionPane.DEFAULT_OPTION);
								linguagemMIPS.setText("");
								Registrador.LimparAtividade(dtm);
								dtmExec.setRowCount(0);
								memoria.LimparMemoria(dtmMem);
								dtmMem.fireTableDataChanged();
								ArrayList<String> linhasLidas = Utilidades.LerArquivo(arqRenomeado.getAbsolutePath());
								for(String linha : linhasLidas){
									try {
										doc.insertString(doc.getLength(), linha+"\n", null);
									} catch (BadLocationException e1) {
										e1.printStackTrace();
									}
								}
							}
							else if (retorno==JFileChooser.CANCEL_OPTION){
								JOptionPane.showMessageDialog(Janela.this,
										"<html>"
										+ "O arquivo <b>não</b> foi salvo.<br>"
										+ "<i>Caso feche o programa suas alterações serão perdidas.</i>"
										+ "</html>",
										"Arquivo não salvo",
										JOptionPane.ERROR_MESSAGE);
							}
							else {
								JOptionPane.showMessageDialog(Janela.this,
										"<html>"
										+ "O caminho do arquivo não foi encontrado.<br>"
										+ "<i>Favor selecionar novamente.</i>"
										+ "</html>",
										"Caminho do Arquivo não selecionado",
										JOptionPane.ERROR_MESSAGE);
							}
						}
					});
					break;
				// -- Compilar
				case 3:
					itensMenu.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							boolean erro = false;
							int codigoErro = 0;
							int linhaAtual = 0;
							ArraysLists.arrLabel.clear();
							String lm = "Sem Linguagem de Máquina";
							Registrador.LimparAtividade(dtm);
							dtmExec.setRowCount(0);
							memoria.LimparMemoria(dtmMem);
							dtmMem.fireTableDataChanged();
							painelLinguagemMaquina.setText("");
							
							ArrayList<String> linhasLidas = Utilidades.LerArquivo(temp.getAbsolutePath());
							Pattern operador = Pattern.compile("\\w+", Pattern.CASE_INSENSITIVE);
							Pattern registrador = Pattern.compile("[$]\\w+", Pattern.CASE_INSENSITIVE);
							Pattern endereco = Pattern.compile(" \\w+|[0-9]|\\,w+|,[0-9]", Pattern.CASE_INSENSITIVE);
							Pattern enderecoTipoI = Pattern.compile(",\\w+|,[0-9]", Pattern.CASE_INSENSITIVE);
							Pattern label = Pattern.compile("\\b\\w{1,8}[:]", Pattern.CASE_INSENSITIVE);
							
							// Primeiro busca todas as labels do arquivo
							//  e adiciona elas ao ArrayLIst & à Memória
							for(String labelLida : linhasLidas){
								Matcher matcherLbl = label.matcher(labelLida);
								if(matcherLbl.find() && erro != true){
									String lbl = matcherLbl.group().substring(matcherLbl.start(),matcherLbl.end()-1);
									for(int i = 0; i < ArraysLists.arrLabel.size(); i ++){
										if(ArraysLists.arrLabel.get(i).equals(lbl)){
											JOptionPane.showMessageDialog(Janela.this,
												"<html>"
												+ "Prezado usuário, a label: <b style='color:red;'>"+lbl+"</b> já foi definida anteriormente no sistema.<br>"
												+ "<i>Favor revisar as informações digitadas.</i>"
												+ "</html>",
												"Label duplicada",
												JOptionPane.ERROR_MESSAGE);
											ArraysLists.arrLabel.remove(lbl);
											erro = true;
											codigoErro = 1;
										}
									}
									ArraysLists.arrLabel.add(lbl);
								}
							}
							// Após encontrar todas a labels definidas
							//  inicia uma busca por operadores,registradores, etc
							if(erro){
								JOptionPane.showMessageDialog(Janela.this,
										"<html>"
										+ "Prezado usuário, o sistema não poderá continuar a execução do código,<br>"
										+ "pois o mesmo apresenta um erro.<br><br>"
										+ "<b style='color:red;'>Código: "+codigoErro+"</b><br><br>"
										+ "<i>Favor revisar as informações digitadas ou informar o código ao desenvolvedor.</i>"
										+ "</html>",
										"Erro na Compilação",
										JOptionPane.ERROR_MESSAGE);
							} else {
							for(String linha : linhasLidas){
								linhaAtual++;
								ArraysLists.regEncontrados.clear();
								
								Matcher matcherLbl = label.matcher(linha);
								if(matcherLbl.find()){
									String lbl = matcherLbl.group().substring(matcherLbl.start(),matcherLbl.end()-1);
									while(lbl.length() < 8){
										lbl += " ";
									}
									lblOito = lbl;
									while(lbl.length() < 32){
										lbl += "0";
									}
									memoria.AlocarMemoria(lbl,dtmMem);
								}
								// Busca de Operadores
							    Matcher matcher = operador.matcher(linha);
							    if(matcher.find()) {
								    // Busca de Registradores
								    Matcher matcher2 = registrador.matcher(linha);
								    while(matcher2.find()) {
								      for(int i = 0; i < ArraysLists.registradores.size(); i++){
								    	  if(matcher2.group().toLowerCase().equals(ArraysLists.registradores.get(i).toString())){
								    		  ArraysLists.regEncontrados.add(ArraysLists.registradores.get(i));
								    		  break;
								    	  }
								      }
								    }
							      for(int i = 0; i < ArraysLists.operadores.size(); i++){
							    	  if(matcher.group().toLowerCase().equals(ArraysLists.operadores.get(i).toString())){
							    		  
						    		  	  String enderecoOuLabel = null;
						    		  	  String endLbl = null;
						    		  	  int end = 0;
							    		  switch(ArraysLists.operadores.get(i).getTipoIntrucao()){
							    		  
							    		  	// -- TIPO I
							    		  	case 0:
							    		  		ArraysLists.regEncontrados.get(0).setAtivo(true);
							    		  		Registrador.AtualizarAtividade(dtm);
							    		  			Matcher matcher3 = enderecoTipoI.matcher(linha);
													if(matcher3.find()) { enderecoOuLabel = matcher3.group().substring(1); }
							    		  			int decimal = Integer.parseInt(enderecoOuLabel, 10);
							    		  			String binario = Integer.toBinaryString(decimal);
							    		  			while(binario.length()<16){
							    		  				binario = "0" + binario ;
							    		  			}
							    		  			lm = TipoInstrucao.InstrucaoTipoI(ArraysLists.operadores.get(i).getValorBits(),ArraysLists.regEncontrados.get(0).getValorBits(),ArraysLists.regEncontrados.get(1).getValorBits(),binario);
							    		  			if(lm.length() > 32){
							    		  				JOptionPane.showMessageDialog(Janela.this,
							    		  						"<html>"
							    		  						+ "Overflow na linha: "
							    		  						+ "<b style='color:red;'>"+linhaAtual+"</b>"
							    		  						+ "</html>", "Detectado overflow!", JOptionPane.ERROR_MESSAGE);
							    		  			} else {
							    		  			memoria.AlocarMemoria(lm, dtmMem);
							    		  			painelLinguagemMaquina.append(lm+"\n");
							    		  			dtmExec.addRow(new Object[]{
								    		  				memoria.BuscarEndereco(lm.substring(0, 8), dtmMem),
								    		  				"0x"+ConversaoBase.converteBinarioParaHexadecimal(lm),
								    		  				ArraysLists.operadores.get(i)+" $"+ArraysLists.regEncontrados.get(0).getId()+",$"+ArraysLists.regEncontrados.get(1).getId()+","+enderecoOuLabel,
								    		  				linhaAtual+": "+ArraysLists.operadores.get(i)+" "+ArraysLists.regEncontrados.get(0).toString()+","+ArraysLists.regEncontrados.get(1).toString()+","+enderecoOuLabel});
							    		  			}
							    		  		break;
							    		  	
							    		  		
							    		  	// -- TIPO J
							    		  	case 1:
							    		  		Matcher matcher4 = endereco.matcher(linha);
												if(matcher4.find()) { enderecoOuLabel = matcher4.group().substring(1); }
												for(int l = 0; l < ArraysLists.arrLabel.size(); l++){
													if(ArraysLists.arrLabel.get(l).equals(enderecoOuLabel)){
														while(enderecoOuLabel.length() < 8){
															enderecoOuLabel += " ";
														}
														lblOito = enderecoOuLabel;
														System.out.println(lblOito);
													}
												}
												if(memoria.memoria.containsValue(lblOito)){
													end = Integer.parseInt(memoria.BuscarEndereco(lblOito, dtmMem), 10)+16;
													endLbl = ConversaoBase.converteDecimalParaBinario(end);
												}
												while(endLbl.length()<26){
						    		  				endLbl = "0" + endLbl ;
						    		  			}
												String novoEndLbl = ""+end;
												while(novoEndLbl.length() < 6){
													novoEndLbl = "0"+novoEndLbl;
												}
												lm = TipoInstrucao.InstrucaoTipoJ(ArraysLists.operadores.get(i).getValorBits(),endLbl);
												memoria.AlocarMemoria(lm, dtmMem);
												painelLinguagemMaquina.append(lm+"\n");
												dtmExec.addRow(new Object[]{
							    		  				memoria.BuscarEndereco(lm.substring(0, 8), dtmMem),
							    		  				"0x"+ConversaoBase.converteBinarioParaHexadecimal(lm),
							    		  				ArraysLists.operadores.get(i)+" "+novoEndLbl,
							    		  				linhaAtual+": "+ArraysLists.operadores.get(i)+" "+enderecoOuLabel});
							    		  		break;
							    		  		
							    		  		
							    		  	// -- TIPO R
							    		  	case 2:
							    		  		ArraysLists.regEncontrados.get(0).setAtivo(true);
							    		  		Registrador.AtualizarAtividade(dtm);
							    		  		lm = TipoInstrucao.InstrucaoTipoR(ArraysLists.regEncontrados.get(0).getValorBits(),ArraysLists.regEncontrados.get(1).getValorBits(),ArraysLists.regEncontrados.get(2).getValorBits(),"00000",ArraysLists.operadores.get(i).getValorBits());
							    		  		memoria.AlocarMemoria(lm, dtmMem);
							    		  		painelLinguagemMaquina.append(lm+"\n");
							    		  		dtmExec.addRow(new Object[]{
							    		  				memoria.BuscarEndereco(lm.substring(0, 8), dtmMem),
							    		  				"0x"+ConversaoBase.converteBinarioParaHexadecimal(lm),
							    		  				ArraysLists.operadores.get(i)+" $"+ArraysLists.regEncontrados.get(0).getId()+",$"+ArraysLists.regEncontrados.get(1).getId()+",$"+ArraysLists.regEncontrados.get(2).getId(),
							    		  				linhaAtual+": "+ArraysLists.operadores.get(i)+" "+ArraysLists.regEncontrados.get(0).toString()+","+ArraysLists.regEncontrados.get(1).toString()+","+ArraysLists.regEncontrados.get(2).toString()});

							    		  		String valorReg2 = null;
							    		  		String valorReg3 = null;
							    		  		for(int r = 0; r < dtm.getRowCount(); r++){
							    		  			if(dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(1).toString())){
							    		  				valorReg2 = dtm.getValueAt(r, 2).toString();
							    		  			}
							    		  			else if(dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(2).toString())){
							    		  				valorReg3 = dtm.getValueAt(r, 2).toString();
							    		  			}
							    		  		}
							    		  		int regSalvar = 0;
							    		  		switch(ArraysLists.operadores.get(i).getId()){
							    		  			// ADD
							    		  			case 0:
							    		  				regSalvar = ArithmeticLogicUnit.add(Integer.parseInt(valorReg2,10), Integer.parseInt(valorReg3,10));
							    		  				for(int r = 0; r < dtm.getRowCount(); r++){
							    		  					if(dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
							    		  						dtm.setValueAt(regSalvar, r, 2);
							    		  					}
							    		  				}
							    		  			break;
							    		  			// SUB
							    		  			case 1:
							    		  				regSalvar = ArithmeticLogicUnit.sub(Integer.parseInt(valorReg2,10), Integer.parseInt(valorReg3,10));
							    		  				for(int r = 0; r < dtm.getRowCount(); r++){
							    		  					if(dtm.getValueAt(r, 0).equals(ArraysLists.regEncontrados.get(0).toString())){
							    		  						dtm.setValueAt(regSalvar, r, 2);
							    		  					}
							    		  				}
							    		  			break;
							    		  			default:
							    		  				JOptionPane.showMessageDialog(
							    		  						Janela.this,
							    		  						"<html>"
							    		  						+ "Lamentamos, mas o operador da linha <b style='font-size:9px; color: red;'>"+linhaAtual+"</b> não poderá ser executado.<br>"
							    		  						+ "O mesmo ainda não foi completamente habilitado.<br>"
							    		  						+ "<i>Tente utilizá-lo na próxima revisão do sistema.</i>"
							    		  						+ "</html>",
							    		  						"Operador não implementado",
							    		  						JOptionPane.ERROR_MESSAGE);
							    		  			break;
							    		  		}
							    		  		break;
							    		  		
							    		  		
							    		  	// -- TIPO I (JUMP/BRANCH)
							    		  	case 3:
							    		  		break;
							    		  		
							    		  		
							    		  	// -- TIPO I (JUMP/BRANCH)
							    		  	case 4:
							    		  		break;
							    		  		
							    		  		
							    		  	// -- TIPO I (JUMP/BRANCH)
							    		  	case 5:
							    		  		Matcher matcher5 = enderecoTipoI.matcher(linha);
												if(matcher5.find()) { enderecoOuLabel = matcher5.group().substring(1); }
												for(int l = 0; l < ArraysLists.arrLabel.size(); l++){
													if(ArraysLists.arrLabel.get(l).equals(enderecoOuLabel)){
														while(enderecoOuLabel.length() < 8){
															enderecoOuLabel += " ";
														}
														lblOito = enderecoOuLabel;
														System.out.println(lblOito);
													}
												}

												endLbl = null;
												end = 0;
												if(memoria.memoria.containsValue(lblOito)){
													end = Integer.parseInt(memoria.BuscarEndereco(lblOito, dtmMem), 10)+16;
													endLbl = ConversaoBase.converteDecimalParaBinario(end);
												}
												while(endLbl.length()<16){
						    		  				endLbl = "0" + endLbl ;
						    		  			}
												novoEndLbl = ""+end;
												while(novoEndLbl.length() < 6){
													novoEndLbl = "0"+novoEndLbl;
												}
												lm = TipoInstrucao.InstrucaoTipoI(ArraysLists.operadores.get(i).getValorBits(),ArraysLists.regEncontrados.get(0).getValorBits(),ArraysLists.regEncontrados.get(1).getValorBits(),endLbl);
												memoria.AlocarMemoria(lm, dtmMem);
												painelLinguagemMaquina.append(lm+"\n");
												dtmExec.addRow(new Object[]{
							    		  				memoria.BuscarEndereco(lm.substring(0, 8), dtmMem),
							    		  				"0x"+ConversaoBase.converteBinarioParaHexadecimal(lm),
							    		  				ArraysLists.operadores.get(i)+" $"+ArraysLists.regEncontrados.get(0).getId()+",$"+ArraysLists.regEncontrados.get(1).getId()+","+novoEndLbl,
							    		  				linhaAtual+": "+ArraysLists.operadores.get(i)+" "+ArraysLists.regEncontrados.get(0).toString()+","+ArraysLists.regEncontrados.get(1).toString()+","+enderecoOuLabel});
							    		  		break;
							    		  }
							    		  painelCima.setSelectedComponent(linguagemMaquina);
							    		  break;
							    	  }
							      }
							    }
							}
						}
					}
				});
					break;
				// -- Compilar por Step
				case 4:
					itensMenu.setEnabled(false);
					itensMenu.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							String lm = "Sem Linguagem de Máquina";
							painelCima.setSelectedComponent(linguagemMaquina);
							ArrayList<String> linhasLidas = Utilidades.LerArquivo(temp.getAbsolutePath());
							// Busca por linhas, no arquivo
							for(String linha : linhasLidas){
								linhaAtualStep++;
								ArraysLists.regEncontrados.clear();
								Pattern operador = Pattern.compile("\\w+", Pattern.CASE_INSENSITIVE);
								Pattern registrador = Pattern.compile("[$]\\w+", Pattern.CASE_INSENSITIVE);
								Pattern endereco = Pattern.compile(" \\w+|[0-9]|\\,w+|,[0-9]", Pattern.CASE_INSENSITIVE);
								Pattern enderecoTipoI = Pattern.compile(",\\w+|,[0-9]", Pattern.CASE_INSENSITIVE);
								Pattern label = Pattern.compile("\\b\\w{1,8}[:]", Pattern.CASE_INSENSITIVE);
								
								Matcher matcherLbl = label.matcher(linha);
								if(matcherLbl.find()){
									String lbl = matcherLbl.group().substring(matcherLbl.start(),matcherLbl.end()-1);
									memoria.AtualizarMemoria(memoria.BuscarMemoria(dtmMem),lbl,dtmMem);
									if(memoria.memoria.containsValue(lbl)){
										System.out.println(memoria.BuscarEndereco(lbl, dtmMem));
									}
								}

								// Busca de Operadores
							    Matcher matcher = operador.matcher(linha);
							    if(matcher.find()) {
								    // Busca de Registradores
								    Matcher matcher2 = registrador.matcher(linha);
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
							    		  		ArraysLists.regEncontrados.get(0).setAtivo(true);
							    		  		Registrador.AtualizarAtividade(dtm);
							    		  		if(ArraysLists.operadores.get(i).toString() == "bne" || ArraysLists.operadores.get(i).toString() == "beq"){
								    		  		Matcher matcher3 = enderecoTipoI.matcher(linha);
													if(matcher3.find()) { enderecoOuLabel = matcher3.group().substring(1); }
													lm = TipoInstrucao.InstrucaoTipoI(ArraysLists.operadores.get(i).getValorBits(),ArraysLists.regEncontrados.get(0).getValorBits(),ArraysLists.regEncontrados.get(1).getValorBits(),enderecoOuLabel);
								    		  		painelLinguagemMaquina.append(lm+"\n");
							    		  		} else {
							    		  			Matcher matcher3 = enderecoTipoI.matcher(linha);
													if(matcher3.find()) { enderecoOuLabel = matcher3.group().substring(1); }
							    		  			int decimal = Integer.parseInt(enderecoOuLabel, 10);
							    		  			String binario = Integer.toBinaryString(decimal);
							    		  			while(binario.length()<16){
							    		  				binario = "0" + binario ;
							    		  			}
							    		  			lm = TipoInstrucao.InstrucaoTipoI(ArraysLists.operadores.get(i).getValorBits(),ArraysLists.regEncontrados.get(0).getValorBits(),ArraysLists.regEncontrados.get(1).getValorBits(),binario);
							    		  			memoria.AlocarMemoria(lm, dtmMem);
							    		  			painelLinguagemMaquina.append(lm+"\n");
							    		  			dtmExec.addRow(new Object[]{
								    		  				memoria.BuscarEndereco(lm.substring(0, 8), dtmMem),
								    		  				"0x"+ConversaoBase.converteBinarioParaHexadecimal(lm),
								    		  				ArraysLists.operadores.get(i)+" $"+ArraysLists.regEncontrados.get(0).getId()+",$"+ArraysLists.regEncontrados.get(1).getId()+","+enderecoOuLabel,
								    		  				linhaAtualStep+": "+ArraysLists.operadores.get(i)+" "+ArraysLists.regEncontrados.get(0).toString()+","+ArraysLists.regEncontrados.get(1).toString()+","+enderecoOuLabel});
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
							    		  		ArraysLists.regEncontrados.get(0).setAtivo(true);
							    		  		Registrador.AtualizarAtividade(dtm);
							    		  		lm = TipoInstrucao.InstrucaoTipoR(ArraysLists.regEncontrados.get(0).getValorBits(),ArraysLists.regEncontrados.get(1).getValorBits(),ArraysLists.regEncontrados.get(2).getValorBits(),"00000",ArraysLists.operadores.get(i).getValorBits());
							    		  		memoria.AlocarMemoria(lm, dtmMem);
							    		  		painelLinguagemMaquina.append(lm+"\n");
							    		  		dtmExec.addRow(new Object[]{
							    		  				memoria.BuscarEndereco(lm.substring(0, 8), dtmMem),
							    		  				"0x"+ConversaoBase.converteBinarioParaHexadecimal(lm),
							    		  				ArraysLists.operadores.get(i)+" $"+ArraysLists.regEncontrados.get(0).getId()+",$"+ArraysLists.regEncontrados.get(1).getId()+",$"+ArraysLists.regEncontrados.get(2).getId(),
							    		  				linhaAtualStep+": "+ArraysLists.operadores.get(i)+" "+ArraysLists.regEncontrados.get(0).toString()+","+ArraysLists.regEncontrados.get(1).toString()+","+ArraysLists.regEncontrados.get(2).toString()});
							    		  		break;
							    		  }
							    		  break;
							    	  }
							      }
							    }
							}
						}
					});
					break;
					
				// -- Configurações
				case 5:
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
				// -- Adicionar Valores
				case 6:
					itensMenu.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							JFrame addValor = new JFrame("Adicionar Valor ao Registrador");
							addValor.setVisible(true);
							addValor.setSize(350, 160);
							addValor.setResizable(false);
							addValor.setIconImage(Utilidades.buscarIcone("img/sort_number.png").getImage());
							addValor.setLocationRelativeTo(null);
							addValor.requestFocusInWindow();
							
							JDesktopPane jdpi = new JDesktopPane();
							addValor.add(jdpi);
							
							JLabel lblAdicionar = new JLabel("Registrador");
							lblAdicionar.setSize(170,20);
							lblAdicionar.setLocation(30, 10);
							jdpi.add(lblAdicionar);
							JComboBox<Registrador> registradores1 = new JComboBox<Registrador>();
							registradores1.setSize(80, 20);
							for(int i = 0; i < ArraysLists.registradores.size(); i++){
								registradores1.addItem(ArraysLists.registradores.get(i));
							}
							registradores1.setLocation(30, 30);
							jdpi.add(registradores1);
							
							valor1 = new JTextField();
							valor1.setSize(170,20);
							valor1.setLocation(140, 30);
							valor1.setDocument(new JTextFieldLimit(5));
				            jdpi.add(valor1);
				            
				            JLabel lblValor = new JLabel("Valor");
				            lblValor.setSize(170, 20);
				            lblValor.setLocation(140, 10);
				            jdpi.add(lblValor);
							
				            //caixa 2
				            JComboBox<Registrador> registradores2 = new JComboBox<Registrador>();
							registradores2.setSize(80, 20);
							for(int i = 0; i < ArraysLists.registradores.size(); i++){
								registradores2.addItem(ArraysLists.registradores.get(i));
							}
							registradores2.setLocation(30, 60);
							jdpi.add(registradores2);
				            
							valor2 = new JTextField();
							valor2.setSize(170,20);
							valor2.setLocation(140, 60);
							valor2.setDocument(new JTextFieldLimit(5));
				            jdpi.add(valor2);
							
				            //botao
							JButton salve = new JButton("Salvar");
							salve.setSize(60,30);
							salve.setLocation(250, 90);
							jdpi.add(salve);
							salve.addActionListener(new ActionListener(){

								@Override
								public void actionPerformed(ActionEvent e) {
									String valor;
									if((registradores1.getSelectedItem() == Registrador.$zero) ||
										(registradores2.getSelectedItem() == Registrador.$zero)){
										JOptionPane.showMessageDialog(Janela.this,
												"<html>"
												+ "Por padrão o registrador <b style='color:red;'>"+Registrador.$zero+"</b> possui um valor fixo e imutável.<br>"
												+ "<i>Nenhuma alteração será feita caso este esteja selecionado.</i>"
												+ "</html>",
												"Atenção!",
												JOptionPane.WARNING_MESSAGE);
									} else {
									if(valor1.getText().equals("") ||
										valor2.getText().equals("")){
										String v1 = "<b style='color: #375828;'>Ok</b>",
											v2 = "<b style='color: #375828;'>Ok</b>";
										if(valor1.getText().equals("")){v1 = "<b style='color:red;'>Em Branco</b>";}
										if(valor2.getText().equals("")){v2 = "<b style='color:red;'>Em Branco</b>";}
											JOptionPane.showMessageDialog(Janela.this,
													"<html>"
													+ "Prezado usuário, o sistema não poderá continuar com adição de valores<br>"
													+ "aos registradores, pois alguns encontram-se em branco:<br><br>"
													+ "Registrador 1: "+v1+"<br>"
													+ "Registrador 2: "+v2+"<br><br>"
													+ "<i>Favor revisar as informações digitadas.</i>"
													+ "</html>",
													"Erro na Adição de Valores",
													JOptionPane.ERROR_MESSAGE);
									} else {
									for(int r = 0; r < dtm.getRowCount(); r++){
				    		  			if(dtm.getValueAt(r, 0).toString().equals(registradores1.getSelectedItem().toString())){
				    		  				valor = valor1.getText();
				    		  				Pattern letrasEtc = Pattern.compile("[^\\d]");
				    		  				Matcher matcher = letrasEtc.matcher(valor);
				    		  				if(matcher.find()){
				    		  					JOptionPane.showMessageDialog(Janela.this,
														"<html>"
														+ "Nós encontramos caracteres diferentes no valor digitado no campo: <b>"+valor1.getName()+"</b>.<br>"
														+ "Observe: estamos ignorando todos os valores não-numéricos para processar corretamente.<br>"
														+ "Cuidado na próxima vez!"
														+ "</html>",
														"Atenção!",
														JOptionPane.WARNING_MESSAGE);
				    		  				}
				    		  				valor = valor.replaceAll("[^\\d]", "");
				    		  				//System.out.println("Achei um: "+registradores1.getSelectedItem());
				    		  				//System.out.println("Valor:"+Integer.parseInt(valor));
				    		  				dtm.setValueAt(Integer.parseInt(valor), r, 2);
				    		  			}
				    		  			else if(dtm.getValueAt(r, 0).toString().equals(registradores2.getSelectedItem().toString())){
				    		  				valor = valor2.getText();
				    		  				Pattern letrasEtc = Pattern.compile("[^\\d]");
				    		  				Matcher matcher = letrasEtc.matcher(valor);
				    		  				if(matcher.find()){
				    		  					JOptionPane.showMessageDialog(Janela.this,
														"<html>"
														+ "Nós encontramos caracteres diferentes no valor digitado no campo: <b>"+valor2.getName()+"</b>.<br>"
														+ "Observe: estamos ignorando todos os valores não-numéricos para processar corretamente.<br>"
														+ "Cuidado na próxima vez!"
														+ "</html>",
														"Atenção!",
														JOptionPane.WARNING_MESSAGE);
				    		  				}
				    		  				valor = valor.replaceAll("[^\\d]", "");
				    		  				//System.out.println("Achei um: "+registradores2.getSelectedItem());
				    		  				//System.out.println("Valor:"+Integer.parseInt(valor));
				    		  				dtm.setValueAt(Integer.parseInt(valor), r, 2);
				    		  			}
									}
				    		  	}
							}	
						}});
						}
					});
					break;
				// -- Dicas
				case 7:
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
