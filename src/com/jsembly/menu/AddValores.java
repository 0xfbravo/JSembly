package com.jsembly.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.jsembly.extras.JTextFieldLimit;
import com.jsembly.extras.Utilidades;
import com.jsembly.funcoes.BinaryLogic;
import com.jsembly.funcoes.ConversaoBase;
import com.jsembly.main.ArraysLists;
import com.jsembly.main.Janela;
import com.jsembly.mips.Registrador;

public class AddValores {
	
	public AddValores(){
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
		
		Janela.valor1 = new JTextField();
		Janela.valor1.setSize(170,20);
		Janela.valor1.setLocation(140, 30);
		Janela.valor1.setDocument(new JTextFieldLimit(5));
		Janela.valor1.setName("Registrador 1");
        jdpi.add(Janela.valor1);
        
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
        
		Janela.valor2 = new JTextField();
		Janela.valor2.setSize(170,20);
		Janela.valor2.setLocation(140, 60);
		Janela.valor2.setDocument(new JTextFieldLimit(5));
		Janela.valor2.setName("Registrador 2");
        jdpi.add(Janela.valor2);
		
      //botao
		JButton resetar = new JButton("Resetar Valores");
		resetar.setSize(150,30);
		resetar.setLocation(20, 90);
		jdpi.add(resetar);
		resetar.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				for(int i = 0; i < ArraysLists.registradores.size(); i ++){
					Janela.dtm.setValueAt(ArraysLists.registradores.get(i).getValorInicial(), i, 2);
					Janela.dtm.setValueAt(BinaryLogic.resizeBinary(ArraysLists.registradores.get(i).getValorBits(),32,true), i, 3);
				}
				JOptionPane.showMessageDialog(null,
						"<html>"
						+ "Todos os valores foram alterados para o padrão com <b style='color: #375828;'>SUCESSO</b>!<br>"
						+ "<i>Acompanhe as modificações pela tabela de Registradores.</i>"
						+ "</html>",
						"Valores alterados com sucesso!",
						JOptionPane.DEFAULT_OPTION);
			}
			
		});
        
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
					JOptionPane.showMessageDialog(null,
							"<html>"
							+ "Por padrão o registrador <b style='color:red;'>"+Registrador.$zero+"</b> possui um valor fixo e imutável.<br>"
							+ "<i>Nenhuma alteração será feita caso este esteja selecionado.</i>"
							+ "</html>",
							"Atenção!",
							JOptionPane.WARNING_MESSAGE);
				} else {
				if(Janela.valor1.getText().equals("") ||
					Janela.valor2.getText().equals("")){
					String v1 = "<b style='color: #375828;'>Ok</b>",
						v2 = "<b style='color: #375828;'>Ok</b>";
					if(Janela.valor1.getText().equals("")){v1 = "<b style='color:red;'>Em Branco</b>";}
					if(Janela.valor2.getText().equals("")){v2 = "<b style='color:red;'>Em Branco</b>";}
						JOptionPane.showMessageDialog(null,
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
				for(int r = 0; r < Janela.dtm.getRowCount(); r++){
		  			if(Janela.dtm.getValueAt(r, 0).toString().equals(registradores1.getSelectedItem().toString())){
		  				valor = Janela.valor1.getText();
		  				Pattern letrasEtc = Pattern.compile("[^\\d]");
		  				Matcher matcher = letrasEtc.matcher(valor);
		  				if(matcher.find()){
		  					JOptionPane.showMessageDialog(null,
									"<html>"
									+ "Nós encontramos caracteres diferentes no valor digitado no campo: <b>"+Janela.valor1.getName()+"</b>.<br>"
									+ "Observe: estamos ignorando todos os valores não-numéricos para processar corretamente.<br>"
									+ "Cuidado na próxima vez!"
									+ "</html>",
									"Atenção!",
									JOptionPane.WARNING_MESSAGE);
		  				}
		  				valor = valor.replaceAll("[^\\d]", "");
		  				//System.out.println("Achei um: "+registradores1.getSelectedItem());
		  				//System.out.println("Valor:"+Integer.parseInt(valor));
		  				Janela.dtm.setValueAt(Integer.parseInt(valor), r, 2);
		  				Janela.dtm.setValueAt(BinaryLogic.resizeBinary(
		  						ConversaoBase.converteDecimalParaBinario(Integer.parseInt(valor))
		  						,32,true), r, 3);
		  			}
		  			else if(Janela.dtm.getValueAt(r, 0).toString().equals(registradores2.getSelectedItem().toString())){
		  				valor = Janela.valor2.getText();
		  				Pattern letrasEtc = Pattern.compile("[^\\d]");
		  				Matcher matcher = letrasEtc.matcher(valor);
		  				if(matcher.find()){
		  					JOptionPane.showMessageDialog(null,
									"<html>"
									+ "Nós encontramos caracteres diferentes no valor digitado no campo: <b>"+Janela.valor2.getName()+"</b>.<br>"
									+ "Observe: estamos ignorando todos os valores não-numéricos para processar corretamente.<br>"
									+ "Cuidado na próxima vez!"
									+ "</html>",
									"Atenção!",
									JOptionPane.WARNING_MESSAGE);
		  				}
		  				valor = valor.replaceAll("[^\\d]", "");
		  				//System.out.println("Achei um: "+registradores2.getSelectedItem());
		  				//System.out.println("Valor:"+Integer.parseInt(valor));
		  				Janela.dtm.setValueAt(Integer.parseInt(valor), r, 2);
		  				Janela.dtm.setValueAt(BinaryLogic.resizeBinary(
		  						ConversaoBase.converteDecimalParaBinario(Integer.parseInt(valor))
		  						,32,true), r, 3);
		  			}
				}
				JOptionPane.showMessageDialog(null,
						"<html>"
						+ "Os registradores selecionados receberam os valores com <b style='color: #375828;'>SUCESSO</b>!<br>"
						+ "<i>Acompanhe as modificações pela tabela de Registradores.</i>"
						+ "</html>",
						"Valores alterados com sucesso!",
						JOptionPane.DEFAULT_OPTION);
		  	}
		}	
	}});
	}
}
