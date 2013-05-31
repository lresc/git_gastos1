package com.example.gastos;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Toast;

import static android.provider.BaseColumns._ID;
import static com.example.gastos.Constantes.*;

public class gastos extends ListActivity {
	//
	private baseDatos groups;
	private static String[] FROM = { _ID, ID_GRUPO, QUIEN_PAGO, PARA_QUIEN,
			CUANTO, CONCEPTO, FECHA, HORA, FOTO, UBICACION };

	private static int[] TO = { R.id.paga, R.id.cantidad, R.id.aquienitem, };

	String ORDER_BY = FECHA + " DESC";
	Cursor cursor;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resumengastos);
		Log.d("GASTOS", "ONcreate0");
		Bundle extras = getIntent().getExtras();
		Log.d("GASTOS", "ONcreate1");
		// valores q pueden ser null, que sea opcionales introducirlos o no
		groups = new baseDatos(this);
		Log.d("GASTOS", "ONcreate2");
		String concepto = null;
		String location = null;
		String foto = null;
		Log.d("GASTOS", "ONcreate3");
		if (extras != null) {
			Log.d("GASTOS", "ONcreate4");
			int id = extras.getInt("id");
			Log.d("GASTOS", "ONcreate5");
			String quien_pago = extras.getString("quien_pago");
			String para_quien = extras.getString("para_quien");
			Log.d("GASTOS", "ONcreate6");
			int cuanto = extras.getInt("cuanto");
			Log.d("GASTOS", "ONcreate7");
			if (extras.getString("concepto").length() > 0)
				concepto = extras.getString("concepto");
			Log.d("GASTOS", "ONcreate8");
			if (extras.getString("location").length() > 0)
				location = extras.getString("location");
			Log.d("GASTOS", "ONcreate9");
			if (extras.getString("foto").length() > 0)
				foto = extras.getString("foto");

			String fecha = extras.getString("fecha");
			String hora = extras.getString("hora");
			Log.d("GASTOS", "ONcreate5");
			agregarGasto(id, quien_pago, para_quien, cuanto, concepto, fecha,
					hora, foto, location);
		}

		registerForContextMenu(getListView());
		Cursor c = obtenerGastos();
		mostrarGastos(c);

	}

	private Cursor obtenerGastos() {

		SQLiteDatabase db = groups.getReadableDatabase();
		cursor = db.query(TABLA_GASTOS, FROM, null, null, null, null, ORDER_BY);
		startManagingCursor(cursor);
		// db.close();
		return cursor;

	}

	private void mostrarGastos(Cursor cursor) {// muestra los grupos
		Log.d("gastos", "Nmostrar");
		cursor.moveToFirst();
		Log.d("mostrar cursor:",
				cursor.getString(2) + " " + cursor.getString(3));
		cursor.moveToFirst();
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.item_gastos, cursor, new String[] { QUIEN_PAGO,
						CUANTO, PARA_QUIEN, }, TO);// hay otro import
		Log.d("gastos", "Nmostrar2");
		setListAdapter(adapter);

	}

	private void agregarGasto(int id, String quien_pago, String para_quien,
			int cuanto, String concepto, String fecha, String hora,
			String foto, String location) {

		Log.d("gastos", "NEW BD");
		SQLiteDatabase db = groups.getWritableDatabase();
		ContentValues values = new ContentValues();
		Log.d("gastos", "NEW BD1");
		values.put(ID_GRUPO, id);
		values.put(QUIEN_PAGO, quien_pago);
		Log.d("gastos", "NEW BD2");
		values.put(PARA_QUIEN, para_quien);
		Log.d("gastos", "NEW BD3");
		values.put(CUANTO, cuanto);// /deberia ser int
		Log.d("gastos", "NEW BD4");
		values.put(CONCEPTO, concepto);
		values.put(FECHA, fecha);
		values.put(HORA, hora);
		values.put(FOTO, foto);
		values.put(UBICACION, location);
		Log.d("gastos", "NEW BD3");
		db.insertOrThrow(TABLA_GASTOS, null, values);
		Log.d("gastos", "NEW B4");
		// db.close();

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {// crea el menu
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_gastos, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {//el menu al presionar sobre un grupo

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		
		case R.id.menu_verRecibo://FUNCIONA
			cursor.moveToFirst();
			cursor.move(info.position);
			String path=cursor.getString(8);
			Intent i0 = new Intent(this, verFoto.class);
			if(path!=null){
				i0.putExtra("path", path);
				startActivity(i0);
			}else
				Toast.makeText(this, "No tiene foto", Toast.LENGTH_LONG).show();
				

			return true;
		
		case R.id.menu_verUbicacion://FUNCIONA
			
			cursor.moveToFirst();
			cursor.move(info.position);
			String location=cursor.getString(9);
			Intent iU = new Intent(this, ubicacion.class);
			iU.putExtra("ver", true);
			if(location!=null){
				
				String []g=location.split(",");
				iU.putExtra("latitude", Double.parseDouble(g[0]));
				iU.putExtra("longitude", Double.parseDouble(g[1]));
				startActivity(iU);
			}
			else
				Toast.makeText(this, "No tiene ubicación", Toast.LENGTH_LONG).show();
			return true;
			
		case R.id.menu_editarGasto:
		//	Intent i = new Intent(this, gastos.class);
			Log.d("GRUPOS","VERGASTOS");
			Intent i = new Intent(this, nuevoGasto.class);//en realidad es el otro, pero para ir probando..
			cursor.moveToFirst();
			cursor.move(info.position);
			i.putExtra("id", cursor.getInt(0));
			i.putExtra("miembros", cursor.getString(2));
			startActivity(i);
			return true;

		case R.id.menu_borrarGasto:///DUDAS DE SI SE TNEDRIA Q BORRAR...
			
			cursor.moveToFirst();
			cursor.move(info.position);
			borrarGasto(cursor.getInt(0));
			cursor = obtenerGastos();
			mostrarGastos(cursor);
			return true;

		

			

		default:
			return super.onContextItemSelected(item);
		}

	}

	private void borrarGasto(int id) {// borra grupo seleccionado
		SQLiteDatabase db = groups.getWritableDatabase();
		db.delete(TABLA_GASTOS, _ID + "='" + id + "'", null);
	}

}
