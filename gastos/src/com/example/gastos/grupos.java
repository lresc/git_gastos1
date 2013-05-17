package com.example.gastos;

import java.security.acl.Group;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import static com.example.gastos.Constantes.*;

public class grupos extends Activity {
	
	//ESTA ACTIVITY MOSTRARA UNA LISTA DE LOS GRUPOS
	private static String[] FROM = { _ID, NOMBRE_GRUPO, MIEMBROS, SINCRONIZACION};
	String ORDER_BY= NOMBRE_GRUPO + " DESC";
	
	private baseDatos groups= new baseDatos(this);//se añade esto.... respecto a la guia...
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grupos);
		
		Bundle extras = getIntent().getExtras();
		String nombre = extras.getString("name");
		String miembros=extras.getString("members");
		String sinc=extras.getString("sync");
		Log.d("GRUPOS","1");
	//	if(nombre !=null && miembros!=null && sinc!=null){
			//agregarGrupo(nombre,miembros,sinc);
		agregarGrupo("H","holas","Sincronizar");
		Log.d("GRUPOS","Salio de agregar grupo");
			Cursor c=obtenerGrupos();
			
			mostrarGrupos(c);
		//}
		Log.d("GRUPOS","2");
		Log.i("G","2"); 
		
		}
	
	private void agregarGrupo(String nameg,String memb, String synq){
		//TRAS nuevoGrupo al aceptar
		
		//based datos no esta en el manifest
		
		
		Log.d("GRUPOS","NEW BD");
		SQLiteDatabase db=groups.getWritableDatabase();
		Log.d("GRUPOS","DESPUES DE writaDBLE");
		ContentValues values=new ContentValues();
		
		values.put(NOMBRE_GRUPO, nameg);
		values.put(MIEMBROS, memb);
		values.put(SINCRONIZACION, synq);
		
		db.insertOrThrow(TABLA_GRUPOS, null, values);
		db.close();
	}
	
	private Cursor obtenerGrupos(){
		
		Log.d("GRUPOS","antes Readable");
		SQLiteDatabase db= groups.getReadableDatabase();
		Log.d("GRUPOS","hace Readbale");
		
		Cursor cursor =db.query(TABLA_GRUPOS, FROM, null,null, null, null, ORDER_BY);
		Log.d("GRUPOS","hace query");
		startManagingCursor(cursor);
		Log.d("GRUPOS","la funcion q no funciona");
		
		
		return cursor;
		
	}
	
	private void mostrarGrupos(Cursor cursor){
		StringBuilder builder=new StringBuilder("Eventos desde la base de datos:\n");
		Log.d("GRUPOS","builder construido");
		while(cursor.moveToNext()){
			long id=cursor.getLong(0);
			String nombre=cursor.getString(1);
			String miembros=cursor.getString(2);
			String sinc=cursor.getString(3);
			builder.append(id).append(": ");
			builder.append(nombre).append(": ");
			builder.append(miembros).append(": ");
			builder.append(sinc).append("\n");

		}
		TextView t=(TextView) findViewById(R.id.grupos);
		t.setText(builder);
	}
}
