package com.example.gastos;




import android.app.ListActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
//import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.widget.AdapterView;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import static com.example.gastos.Constantes.*;

public class grupos extends ListActivity {
	
	//ESTA ACTIVITY MOSTRARA UNA LISTA DE LOS GRUPOS
	private static String[] FROM = { _ID, NOMBRE_GRUPO};// {_ID, NOMBRE_GRUPO, MIEMBROS, SINCRONIZACION}
	private static int[] TO={R.id.gid, R.id.gnombre};//ampliar a sincronización...
	String ORDER_BY= NOMBRE_GRUPO + " DESC";
	
	private baseDatos groups= new baseDatos(this);//se añade esto.... respecto a la guia...
	//based datos no esta en el manifest
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grupos);
		//Log.d("GRUPOS","preextras");
		
		Bundle extras=getIntent().getExtras();
	//	registerForContextMenu(getListView());//para relacionar el listView con el menu
	//	Log.d("GRUPOS","registerForContextMenu" + getListView());		
		if(extras!=null){
			String nombre = extras.getString("name");
			String miembros=extras.getString("members");
			String sinc=extras.getString("sync");
			if(nombre !=null && miembros!=null && sinc!=null){
				agregarGrupo(nombre,miembros,sinc);
			//	Log.d("GRUPOS","Salio de agregar grupo");
			}
			else{
				//falta algun campo por poner..
			}
		}
		
//		Log.d("GRUPOS","2");		
		Cursor c=obtenerGrupos();
		mostrarGrupos(c);
		}
	
	private void agregarGrupo(String nameg,String memb, String synq){
		//TRAS nuevoGrupo al aceptar	
		
	//	Log.d("GRUPOS","NEW BD");
		SQLiteDatabase db=groups.getWritableDatabase();
//		Log.d("GRUPOS","DESPUES DE writaDBLE");
		ContentValues values=new ContentValues();
		
		values.put(NOMBRE_GRUPO, nameg);
		values.put(MIEMBROS, memb);
		values.put(SINCRONIZACION, synq);
		
		db.insertOrThrow(TABLA_GRUPOS, null, values);
		db.close();
	}
	
	private Cursor obtenerGrupos(){
		
	//	Log.d("GRUPOS","antes Readable");
		SQLiteDatabase db= groups.getReadableDatabase();
//		Log.d("GRUPOS","hace Readbale");
		
		Cursor cursor =db.query(TABLA_GRUPOS, FROM, null,null, null, null, ORDER_BY);
//		Log.d("GRUPOS","hace query");
		startManagingCursor(cursor);
//		Log.d("GRUPOS","la funcion q no funciona");
		
		
		return cursor;
		
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		Log.d("GRUPOS","creaContextMenu1");		
		MenuInflater inflater = getMenuInflater();
		Log.d("GRUPOS","creaContextMenu2");		
		inflater.inflate(R.menu.menu_grupos, menu);
		Log.d("GRUPOS","creaContextMenu3");		
	}
	
	    
	
	public boolean onContextItemSelected(MenuItem item) {

	Log.d("GRUPOS","SELECTitem");	
	/*	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch(item.getItemId()) {
			case R.id.menu_verMiembros:
				//Toast.makeText(this, "You have chosen the " + getResources().getString(R.string.edit) +	" context menu option for " + names[(int)info.id],Toast.LENGTH_SHORT).show();
		
			return true;
			case R.id.menu_verGastos:
				return true; 
				
			case R.id.menu_editarGrupo:
				return true;
				
			case R.id.menu_borrarGrupo:
				return true;
				
			default:
				return false;
		}*/
				return super.onContextItemSelected(item);
	}

	private void mostrarGrupos(Cursor cursor){
		
	//	final ListView lv1 =(ListView)findViewById(android.R.id.list);
		//ArrayAdapter <String> adapter = new ArrayAdapter<String>(this,R.layout.item, FROM);
		Log.d("GRUPOS","adapter prelist");
		SimpleCursorAdapter adapter=new SimpleCursorAdapter(this,R.layout.item, cursor, FROM,TO);//hay otro import		
		setListAdapter(adapter);
		
        registerForContextMenu(getListView());
		//lv1.setAdapter(adapter);
		Log.d("GRUPOS","registerForContextMenu");
		
	}
}



