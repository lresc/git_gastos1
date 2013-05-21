package com.example.gastos;


//FALTA: cuando se pulse atras, que vaya a pagina principal.xml


import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
//import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.SimpleCursorAdapter;
import static com.example.gastos.Constantes.*;

public class grupos extends ListActivity {
	//ESTA ACTIVITY MOSTRARA UNA LISTA DE LOS GRUPOS
	private static String[] FROM = {_ID,NOMBRE_GRUPO,MIEMBROS,EMAILS,SINCRONIZACION};// {_ID, NOMBRE_GRUPO, MIEMBROS, SINCRONIZACION}
	private static int[] TO={R.id.gnombre, R.id.gmiembros};//ampliar a sincronización...
	String ORDER_BY= NOMBRE_GRUPO + " ASC";
	Cursor cursor;
	
	private baseDatos groups= new baseDatos(this);
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grupos);
		
		Bundle extras=getIntent().getExtras();
		if(extras!=null){
			String nombre = extras.getString("name");
			String miembros=extras.getString("members");
			String sinc=extras.getString("sync");
			String mails=extras.getString("mails");
			if(nombre !=null && miembros!=null && sinc!=null){
				agregarGrupo(nombre,miembros,mails,sinc);
				Log.d("GRUPOS","sinSalio de agregar grupoces sYnc "+sinc);
			}
			else{
				//falta algun campo por poner..
			}
		}
		registerForContextMenu(getListView());		
		Cursor c=obtenerGrupos();
		mostrarGrupos(c);
		}
	
	private void agregarGrupo(String nameg,String memb,String mail, String synq){
		//TRAS nuevoGrupo al aceptar	

		Log.d("GRUPOS","NEW BD");
		SQLiteDatabase db=groups.getWritableDatabase();
//		Log.d("GRUPOS","DESPUES DE writaDBLE");
		ContentValues values=new ContentValues();
		
		values.put(NOMBRE_GRUPO, nameg);
		values.put(MIEMBROS, memb);
		values.put(EMAILS, mail);
		values.put(SINCRONIZACION, synq);
		Log.d("GRUPOS",nameg+" "+memb+" "+mail+" "+synq);
		db.insertOrThrow(TABLA_GRUPOS, null, values);
		db.close();
	}
	
	private Cursor obtenerGrupos(){
		
		SQLiteDatabase db= groups.getReadableDatabase();
		cursor =db.query(TABLA_GRUPOS, FROM, null,null, null, null, ORDER_BY);
		Log.d("GRUPOS","QUERY ");
		startManagingCursor(cursor);	

		return cursor;
		
	}
	
	private void mostrarGrupos(Cursor cursor){		
		cursor.moveToFirst();
		SimpleCursorAdapter adapter=new SimpleCursorAdapter(this,R.layout.item, cursor,new String[]{NOMBRE_GRUPO,MIEMBROS},TO);//hay otro import
		setListAdapter(adapter);
		        
	}
	
	
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_grupos, menu);
	}
	
	    
	@Override
	public boolean onContextItemSelected(MenuItem item) {

	Log.d("GRUPOS","SELECTitem");	
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch(item.getItemId()) {
			case R.id.menu_verMiembros:///mejor sin esto... => quitar miembro.java y item_miembros.xml
				String e=cursor.getString(2);
				Log.d("GRUPOS","lo que se envia "+e);	
				Intent i0=new Intent(this, miembros.class);
				i0.putExtra("miembros",cursor.getString(2));
				
				startActivity(i0);				
		
			return true;
			case R.id.menu_verGastos:
				Intent i = new Intent(this, gastos.class);
				//poner de extra el id de grupo
				startActivity(i);
				return true; 
				
			case R.id.menu_editarGrupo:
				Intent i1 = new Intent(this, nuevoGrupo.class);
				cursor.moveToFirst();
				cursor.move(info.position);
				
				i1.putExtra("nombre", cursor.getString(1));
				i1.putExtra("miembros", cursor.getString(2));
				i1.putExtra("mail", cursor.getString(3));
				i1.putExtra("sync", cursor.getString(4));
				cursor.moveToFirst();
				cursor.move(info.position);
				startActivity(i1);
				return true;
				
			case R.id.menu_borrarGrupo:

				cursor.moveToFirst();
				cursor.move(info.position);
				borrarGrupo(cursor.getInt(0));
				cursor=obtenerGrupos();
				mostrarGrupos(cursor);
				return true;
				
			default:
				return super.onContextItemSelected(item);
		}
				
	}
	
	private int borrarGrupo(int id){
		SQLiteDatabase db=groups.getWritableDatabase();
		//Log.d("GRUPOS", id);
		return db.delete(TABLA_GRUPOS, _ID +"='"+id+"'", null);
	}


}



