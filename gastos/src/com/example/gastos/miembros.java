package com.example.gastos;


import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class miembros extends ListActivity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.miembros);
		
		Bundle extras=getIntent().getExtras();
		Log.d("MIEMBROS","EXTRAS");
		String miembros=null;
			if(extras!=null){
				miembros=extras.getString("miembros");
				Log.d("MIEMBROS",extras.getString("miembros"));
			}
			
			mostrarMiembros(pasarArray(miembros));
}

	private String[] pasarArray(String miembros) {
		// TODO Auto-generated method stub

		Log.d("MIEMBROS","antes de split");
		String[] result  = miembros.split( "," );
	
		//Separate the original text by two characters ("," and ";")
		
		
		return result;
	}

	private void mostrarMiembros(String[] e) {
		// MODIFICAR: PONER IGUAL QUE EN nuevoGrupo.java
		final ListView lv1 =(ListView)findViewById(android.R.id.list);		
		Log.d("MIEMBROS","lista");
		
		ArrayAdapter <String> adapter = new ArrayAdapter<String>(this,R.layout.item_miembros,R.id.mnombre,e );
	//	SimpleCursorAdapter adapter=new SimpleCursorAdapter(this,android.R.layout.simple_item2, cursor, FROM,TO);//hay otro import
		Log.d("MIEMBROS","lista2");
		setListAdapter(adapter);
		
	}
}
