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

import com.jsembly.extras.TextLineNumber;
import com.jsembly.extras.Utilidades;
import com.jsembly.funcoes.BinaryArithmetic;
import com.jsembly.funcoes.BinaryLogic;
import com.jsembly.funcoes.ConversaoBase;
import com.jsembly.funcoes.Cores;
import com.jsembly.menu.AddValores;
import com.jsembly.menu.Montar;
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
	public static BinaryArithmetic binAri = new BinaryArithmetic();
	public static String lblOito;
	public static File temp;
	public static ArrayList<Color> cores = new ArrayList<Color>();
	public static Configuracoes conf = new Configuracoes();
	public static int linhaAtualStep = 0;
	Timer alphaChanger;
	private String titulo;
	private int altura,largura;
	
	public static JFrame janelaInicial = new JFrame();
	public static JWindow splJanela = new JWindow();
	public static GridLayout layoutMenu = new GridLayout(0,1);
	public static GridLayout layoutCentral = new GridLayout(2,1);
	public static GridLayout layoutBaixo = new GridLayout(1,2);
	public static JDesktopPane painelMenu = new JDesktopPane();
	public static JDesktopPane painelJanela = new JDesktopPane();
	
	public static JTabbedPane painelCima = new JTabbedPane();
	public static 	DefaultStyledDocument documento = new DefaultStyledDocument();
	public static 		JTextPane linguagemMIPS = new JTextPane(documento);
	public static 			StyledDocument doc = linguagemMIPS.getStyledDocument();
	public static 		JTable abaExecutar = new JTable();
	public static JDesktopPane painelBaixo = new JDesktopPane();
	public static 	JTabbedPane valoresMIPS = new JTabbedPane();
	public static 	JTabbedPane tblMemoria = new JTabbedPane();
	
	public static JDesktopPane splPainel = new JDesktopPane();
	public static FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivo Assembly (*.asm)", "asm", "assembly");
	
	public static JTextField valor1 = null;
	public static JTextField valor2 = null;
	public static JTextField valor3 = null;
	public static DefaultTableModel dtmExec = new DefaultTableModel(0, 0);
	public static DefaultTableModel dtmMem = new DefaultTableModel(0, 0);
	public static DefaultTableModel dtm = new DefaultTableModel(0, 0);
	
	public static JTextArea painelLinguagemMaquina = new JTextArea();
	public static JScrollPane linguagemMIPS_Scroll = new JScrollPane(linguagemMIPS);
	public static JScrollPane linguagemMaquina = new JScrollPane(painelLinguagemMaquina);

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
							case KeyEvent.VK_ESCAPE:
							case KeyEvent.VK_CAPS_LOCK:
							case KeyEvent.VK_CONTROL:
							case KeyEvent.VK_TAB:
							case KeyEvent.VK_ALT:	
							break;
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
		
		TextLineNumber tln = new TextLineNumber(linguagemMIPS);
		linguagemMIPS_Scroll.setRowHeaderView( tln );
		linguagemMIPS_Scroll.setPreferredSize(painelCima.getMinimumSize());
		painelCima.add(linguagemMIPS_Scroll,conf.getLinguagemMIPS());
		painelCima.setIconAt(0, Utilidades.buscarIcone("img/page_code.png"));
				
		painelLinguagemMaquina.setEnabled(false);
		TextLineNumber tln2 = new TextLineNumber(painelLinguagemMaquina);
		linguagemMaquina.setRowHeaderView( tln2 );
		linguagemMaquina.setPreferredSize(painelCima.getMinimumSize());
		painelCima.add(linguagemMaquina,conf.getLinguagemMaquina());
		painelCima.setIconAt(1, Utilidades.buscarIcone("img/monitor.png"));
		
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

		String headerMem[] = new String[] { "Endereços", "Dados" };
		dtmMem.setColumnIdentifiers(headerMem);
		listaMem.setModel(dtmMem);
		listaMem.setEnabled(false);
		listaMem.getColumnModel().getColumn(0).setHeaderValue("Endereços");
		listaMem.getColumnModel().getColumn(1).setHeaderValue("Dados");
		new Memoria(10240,dtmMem);
		JScrollPane spMem = new JScrollPane(listaMem);
		tblMemoria.add(spMem,conf.getMemoria());
		tblMemoria.setIconAt(0, Utilidades.buscarIcone("img/timeline_marker.png"));
		
		// -- Tabela de Registradores
		JTable listaReg = new JTable(){
			@Override
			public boolean isCellEditable(int row, int col) {
			     switch (col) {
			         case 2:
			        	 switch(row){
			        	 	case 0:
			        	 	case 1:
			        	 	case 2:
			        	 	case 3:
			        	 	return false;
			        	 }
			             return true;
			         default:
			             return false;
			      }
			}
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
		
		String header[] = new String[] { "Registrador", "ID", "Valor", "Valor Binário", "Atividade" };
		dtm.setColumnIdentifiers(header);
		listaReg.setModel(dtm);
		listaReg.getColumnModel().getColumn(0).setHeaderValue("Registrador");
		listaReg.getColumnModel().getColumn(0).setMaxWidth(63);
		listaReg.getColumnModel().getColumn(1).setHeaderValue("ID");
		listaReg.getColumnModel().getColumn(1).setMaxWidth(23);
		listaReg.getColumnModel().getColumn(2).setHeaderValue("Valor");
		listaReg.getColumnModel().getColumn(2).setMaxWidth(90);
		listaReg.getColumnModel().getColumn(3).setHeaderValue("Valor Binário (32bits)");
		listaReg.getColumnModel().getColumn(3).setMaxWidth(230);
		listaReg.getColumnModel().getColumn(3).setMinWidth(230);
		listaReg.getColumnModel().getColumn(4).setHeaderValue("Atividade");

		for(int i = 0; i < ArraysLists.registradores.size(); i ++){
			String atividade = "Inativo";
			if(ArraysLists.registradores.get(i).isAtivo()){atividade = "Ativo";}
			String bin32 = BinaryLogic.resizeBinary(
					ConversaoBase.converteDecimalParaBinario(
							ArraysLists.registradores.get(i).getValorInicial()),32,true);
			dtm.addRow(new Object[]{
					ArraysLists.registradores.get(i).toString(),
					ArraysLists.registradores.get(i).getId(),
					ArraysLists.registradores.get(i).getValorInicial(),
					bin32,
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
									Memoria.LimparMemoria(dtmMem);
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
								Memoria.LimparMemoria(dtmMem);
								dtmMem.fireTableDataChanged();
								ArrayList<String> linhasLidas = Utilidades.LerArquivo(temp.getAbsolutePath());
								for(String linha : linhasLidas){
									try {
										doc.insertString(doc.getLength(), linha+"\n", null);
									} catch (BadLocationException e1) {
										e1.printStackTrace();
									}
								}
								Cores.buscarCores(temp.getAbsolutePath(),linguagemMIPS);
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
								Memoria.LimparMemoria(dtmMem);
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
						public void actionPerformed(ActionEvent e){ new Montar(); }
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
									Memoria.AtualizarMemoria(Memoria.BuscarMemoria(dtmMem),lbl,dtmMem);
									if(Memoria.memoria.containsValue(lbl)){
										System.out.println(Memoria.BuscarEndereco(lbl, dtmMem));
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
							    		  			Memoria.AlocarMemoria(lm, dtmMem);
							    		  			painelLinguagemMaquina.append(lm+"\n");
							    		  			dtmExec.addRow(new Object[]{
								    		  				Memoria.BuscarEndereco(lm.substring(0, 8), dtmMem),
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
							    		  		Memoria.AlocarMemoria(lm, dtmMem);
							    		  		painelLinguagemMaquina.append(lm+"\n");
							    		  		dtmExec.addRow(new Object[]{
							    		  				Memoria.BuscarEndereco(lm.substring(0, 8), dtmMem),
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
						public void actionPerformed(ActionEvent e){ new AddValores(); }
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
