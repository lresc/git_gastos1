package com.example.gastos;

import android.provider.BaseColumns;

public interface Constantes extends BaseColumns{
	
	public static final String TABLA_GRUPOS="grupos";
	public static final String TABLA_GASTOS="gastos";
	public static final String TABLA_DEUDAS="deudas"; //Todavia sin desarrollar
	
	
	//Columnas en la tabla grupos
	public static final String NOMBRE_GRUPO="nombre";
	public static final String MIEMBROS="miembros";
	public static final String SINCRONIZACION="sincronizacion";
	
	//Columnas en la tabla gastos
	
	public static final String QUIEN_PAGO="quien_pago";
	public static final String PARA_QUIEN="para_quien";
	public static final String CUANTO="cuanto";
	public static final String CONCEPTO="concepto";
	public static final String FECHA="fecha";
	public static final String HORA="hora";
	public static final String FOTO="foto";
	public static final String UBICACION="ubicacion";
	
	//Columnas en la tabla deudas
	
	//public static final String
	//Se pone tb la id_gastos_anterior? o con _.ID?¿
	//como escribir el campo 
	
	
	
}
