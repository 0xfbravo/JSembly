package com.jsembly.main;

import java.util.ArrayList;
import java.util.EnumSet;

import com.jsembly.mips.Operador;
import com.jsembly.mips.Registrador;

public class ArraysLists {
	
    public static ArrayList<Operador> operadores = new ArrayList<Operador>(EnumSet.allOf(Operador.class));	
    public static ArrayList<Registrador> registradores = new ArrayList<Registrador>(EnumSet.allOf(Registrador.class));
    public static ArrayList<Registrador> regEncontrados = new ArrayList<Registrador>();
    public static ArrayList<Registrador> regAtivos = new ArrayList<Registrador>();
    public static ArrayList<String> arrLabel  = new ArrayList<String>();
    public static ArrayList<String> labelAddress  = new ArrayList<String>();
    
    public static ArrayList<ItensMenu> itensMenuLista = new ArrayList<ItensMenu>(EnumSet.allOf(ItensMenu.class));
}
