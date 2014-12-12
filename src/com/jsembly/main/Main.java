package com.jsembly.main;

import java.util.Properties;

import javax.swing.UIManager;

import com.jsembly.extras.Utilidades;
import com.jtattoo.plaf.graphite.GraphiteLookAndFeel;

public class Main {
	
	public static void main(String[] args){
        try {
        	Properties props = new Properties();
        	props.put("logoString", "  ");
        	props.put("textAntiAliasing","on");
        	props.put("menuOpaque","20");
        	props.put("windowTitleFont", "Tahoma BOLD 11");
        	props.put("menuTextFont", "Tahoma 11");
        	props.put("systemTextFont", "Tahoma 11");
        	props.put("userTextFont", "Tahoma 11");
        	props.put("textAntiAliasingMode", "default");
        	  
        	  
        	GraphiteLookAndFeel.setCurrentTheme(props);
            UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
            new Janela("JSembly",1300,600);
            Janela.janelaInicial.setIconImage(Utilidades.buscarIcone("img/weather_clouds.png").getImage());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
	}

}
