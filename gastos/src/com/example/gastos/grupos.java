package com.example.gastos;

//FALTA: cuando se pulse atras, que vaya a pagina principal.xml

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
	// ESTA ACTIVITY MOSTRARA UNA LISTA DE LOS GRUPOS
	private static String[] FROM = { _ID, NOMBRE_GRUPO, MIEMBROS, EMAILS,
			SINCRONIZACION };
	private static int[] TO = { R.id.gnombre, R.id.gmiembros };// los campos q
																// nos interesan
																// de item.xml

	String ORDER_BY = NOMBRE_GRUPO + " DESC";
	Cursor cursor;

	private baseDatos groups;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grupos);
		Log.d("grupos", "tras layout grupos =)");
		Bundle extras = getIntent().getExtras();
		groups = new baseDatos(this);
		if (extras != null) {

			String nombre = extras.getString("name");
			String miembros = extras.getString("members");
			String sinc = extras.getString("sync");
			String mails = extras.getString("mails");
			boolean editar = extras.getBoolean("editar");

			if (nombre.length() > 0 && miembros != null) { // ni el nombre y los
															// miembros son null
				Log.d("GRUPOS", "NOMBRE NO ES NULL Y MIEMBROS TAMPOCO");
				if (editar) // se edita un grupo
					editarGrupo(extras.getInt("id"), nombre, miembros, mails,
							sinc);
				else
					// se añade un grupo nuevo
					agregarGrupo(nombre, miembros, mails, sinc);
			} else
				Log.d("GRUPOS", "NOMBRE o miembros ES NULL");

		}

		registerForContextMenu(getListView());
		Cursor c = obtenerGrupos();
		mostrarGrupos(c);
	}

	private void editarGrupo(int id, String nameg, String memb, String mail,
			String synq) {// actualiza los datos en la BBDD tras haber editado
							// un grupo
		SQLiteDatabase db = groups.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(NOMBRE_GRUPO, nameg);
		values.put(MIEMBROS, memb);
		values.put(EMAILS, mail);
		values.put(SINCRONIZACION, synq);

		db.update(TABLA_GRUPOS, values, _ID + "=" + id, null);
	}

	private void agregarGrupo(String nameg, String memb, String mail,
			String synq) { // TRAS nuevoGrupo.java al aceptar, escribe en la
							// base de datos el grupo

		Log.d("GRUPOS", "NEW BD");
		SQLiteDatabase db = groups.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(NOMBRE_GRUPO, nameg);
		values.put(MIEMBROS, memb);
		values.put(EMAILS, mail);
		values.put(SINCRONIZACION, synq);
		db.insertOrThrow(TABLA_GRUPOS, null, values);
		// db.close();
	}

	private Cursor obtenerGrupos() {// lectura de la BBDD a los grupos

		SQLiteDatabase db = groups.getReadableDatabase();
		cursor = db.query(TABLA_GRUPOS, FROM, null, null, null, null, ORDER_BY);
		startManagingCursor(cursor);

		return cursor;

	}

	private void mostrarGrupos(Cursor cursor) {// muestra los grupos
		cursor.moveToFirst();
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.item, cursor, new String[] { NOMBRE_GRUPO, MIEMBROS },
				TO);// hay otro import
		setListAdapter(adapter);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {// crea el menu
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_grupos, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {// el menu al presionar
															// sobre un grupo

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.menu_verMiembros:
			Intent i0 = new Intent(this, miembros.class);
			cursor.moveToFirst();
			cursor.move(info.position);
			i0.putExtra("miembros", cursor.getString(2));
			i0.putExtra("mails", cursor.getString(3));
			startActivity(i0);

			return true;

		case R.id.menu_verGastos:
			// Intent i = new Intent(this, gastos.class);
			Log.d("GRUPOS", "VERGASTOS");
			Intent i = new Intent(this, gastos.class);// en realidad es el
															// otro, pero para
															// ir probando..
			cursor.moveToFirst();
			cursor.move(info.position);
			i.putExtra("listar", true);// cuando se llame a nuevoGasto para
										// agregar grupo tendran que ir estos 3
										// extras
			i.putExtra("id", cursor.getInt(0));
			i.putExtra("miembros", cursor.getString(2));
			startActivity(i);
			return true;

		case R.id.menu_editarGrupo:
			Intent i1 = new Intent(this, nuevoGrupo.class);
			cursor.moveToFirst();
			cursor.move(info.position);
			i1.putExtra("id", cursor.getInt(0));
			i1.putExtra("nombre", cursor.getString(1));
			i1.putExtra("miembros", cursor.getString(2));
			i1.putExtra("mail", cursor.getString(3));
			i1.putExtra("sync", cursor.getString(4));
			startActivity(i1);
			return true;

		case R.id.menu_borrarGrupo:

			cursor.moveToFirst();
			cursor.move(info.position);
			borrarGrupo(cursor.getInt(0));
			cursor = obtenerGrupos();
			mostrarGrupos(cursor);
			return true;

		default:
			return super.onContextItemSelected(item);
		}

	}

	private int borrarGrupo(int id) {// borra grupo seleccionado
		SQLiteDatabase db = groups.getWritableDatabase();
		return db.delete(TABLA_GRUPOS, _ID + "='" + id + "'", null);
	}

}
