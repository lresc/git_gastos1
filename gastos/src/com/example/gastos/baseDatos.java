package com.example.gastos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import static com.example.gastos.Constantes.*;
import static android.provider.BaseColumns._ID;



public class baseDatos extends SQLiteOpenHelper {

	//public static String DB_PATH="/data/data/com.example.gastos/databases/";
	private final static String DB_NAME="db_gastos";
	private final static  int VERSION_BASE=1;
	
	
	private final Context myContext;
	
	public baseDatos(Context context) {
		super(context, DB_NAME, null, VERSION_BASE);
		Log.d("BD","1");
		this.myContext=context;
	}
	

	
	//--------------LOCAL---------------///
	String sqlCreate="CREATE TABLE "+ TABLA_GRUPOS +" ("+ _ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " + NOMBRE_GRUPO+
			" TEXT, "+ MIEMBROS+" TEXT, "+EMAILS+" TEXT, "+ SINCRONIZACION+" TEXT)"; 
	
	String sqlCreate2="CREATE TABLE "+TABLA_GASTOS +"("+_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+ID_GRUPO+" INTEGER NOT NULL, " + 
	QUIEN_PAGO +" TEXT NOT NULL, "+ PARA_QUIEN+" TEXT NOT NULL,"+CUANTO+ 
	" INTEGER NOT NULL,"+ CONCEPTO+" STRING,"+FECHA+" TEXT NOT NULL, "+HORA+" TEXT NOT NULL,"+FOTO+" TEXT, "+ UBICACION+" TEXT)";
		

	
	String sqlUpdate="DROP TABLE IF EXISTS "+ TABLA_GRUPOS; //PARA ACTUALZIAZ
	String sqlUpdate1="DROP TABLE IF EXISTS "+ TABLA_GASTOS;		
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("BD","2");
		if(db.isReadOnly()){
			db=getWritableDatabase();
		}
		
		Log.d("BD","3");	
		db.execSQL(sqlCreate);
		Log.d("BD","4");
		db.execSQL(sqlCreate2);
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d("BD","6");

			Log.d("BD","7");
			db.execSQL(sqlUpdate);
			db.execSQL(sqlUpdate1);
			onCreate(db);
		
	}
	//SELECT usuario,email FROM Usuarios WHERE usuario='usu1' "
	

}
