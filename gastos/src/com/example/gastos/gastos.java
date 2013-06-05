package com.example.gastos;

import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;

import static android.provider.BaseColumns._ID;
import static com.example.gastos.Constantes.*;

public class gastos extends ListActivity {
	//
	private baseDatos groups;
	private static String[] FROM = { _ID, ID_GRUPO, QUIEN_PAGO, PARA_QUIEN,
			CUANTO, DEUDA_ACTUAL, CONCEPTO, FECHA, HORA, FOTO, UBICACION };
	private static String[] FROMD = { _ID, ID_GRUPO, DEUDA_TOTAL };

	private static int[] TO = { R.id.paga, R.id.cantidad, R.id.aquienitem, };

	String ORDER_BY = FECHA + " DESC";
	Cursor cursor;
	int id_grupo;
	String miembros;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resumengastos);

		// //---------TABS-----------//////////
		Resources res = getResources();

		TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);
		tabs.setup();

		TabHost.TabSpec spec = tabs.newTabSpec("gastos");
		spec.setContent(R.id.tab1);
		spec.setIndicator("GASTOS",
				res.getDrawable(android.R.drawable.ic_delete));
		tabs.addTab(spec);

		spec = tabs.newTabSpec("deudas");
		spec.setContent(R.id.tab2);
		spec.setIndicator("DEUDAS",
				res.getDrawable(android.R.drawable.btn_star));
		tabs.addTab(spec);

		tabs.setCurrentTab(0);

		tabs.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				Log.d("AndroidTabsDemo", "Pulsada pestaña: " + tabId);
				if (tabId == "gastos") {
					Log.d("gastos", "2gastosTab");
					Cursor c = obtenerGastos();
					mostrarGastos(c);

				}
				if (tabId == "deudas") {
					Log.d("gastos", "2deudasTab");
					Cursor c1 = obtenerDeudas();
					mostrarDeudas(c1);// se calcularan las deudas despues de
										// añadir un

				}
			}
		});

		// ////////------------RESTO------------///////

		Bundle extras = getIntent().getExtras();

		// id del grupo y miembros del grupo siempre se pasaran al llamar a
		// nuevoGasto, y tambien listar q indica si se llama la funcion desde
		// nuevoGasto o no. nuevoGasto -> listar=false
		id_grupo = extras.getInt("id");
		miembros = extras.getString("miembros");
		// valores q pueden ser null, que sea opcionales introducirlos o no
		groups = new baseDatos(this);

		String concepto = null;
		String location = null;
		String foto = null;

		if (extras != null) {
			if (extras.getBoolean("listar") == false) {// agregara o editara, si
														// true, mostrara la
														// lista y ya

				String quien_pago = extras.getString("quien_pago");
				String para_quien = extras.getString("para_quien");
				double cuanto = extras.getDouble("cuanto");
				if (extras.getString("concepto").length() > 0)
					concepto = extras.getString("concepto");
				if (extras.getString("location").length() > 0)
					location = extras.getString("location");

				if (extras.getString("foto").length() > 0)
					foto = extras.getString("foto");

				String fecha = extras.getString("fecha");
				String hora = extras.getString("hora");

				Boolean editar = extras.getBoolean("editar");
				if (editar == true) {
					int id_gasto = extras.getInt("id_gasto");
					String deuda_anterior = extras.getString("deuda_anterior");

					editarGasto(
							id_gasto,
							obtenerContent(id_grupo, quien_pago, para_quien,
									cuanto, concepto, fecha, hora, foto,
									location, deuda_anterior));
				} else
					agregarGasto(obtenerContent(id_grupo, quien_pago,
							para_quien, cuanto, concepto, fecha, hora, foto,
							location, null));

			}
		}

		registerForContextMenu(getListView());
		Cursor c = obtenerGastos();
		mostrarGastos(c);

	}

	private Cursor obtenerDeudas() {
		SQLiteDatabase db = groups.getReadableDatabase();
		cursor = db.query(TABLA_DEUDAS, FROMD, ID_GRUPO + "=" + id_grupo, null,
				null, null, null);
		startManagingCursor(cursor);

		return cursor;

	}

	private void mostrarDeudas(Cursor cursor) {
		// TODO Auto-generated method stub

	
		Log.d("gastos", "Nmostrar DEUDAS");
		String FROMDe[] = new String[] { DEUDA_TOTAL };
		int TD[] = new int[] { R.id.dnombre };
		cursor.moveToFirst();
		
		
		if (cursor.getCount() == 0) {
			TextView t1 = (TextView) this.findViewById(R.id.vacio);
			t1.setText(getString(R.string.nodeudas));
		}else{
			List<Map<String, String>> data = resumenDeudas(cursor);
		
		/*
		 * Log.d("deudas","es: "+cursor.getString(2)); cursor.moveToFirst();
		 * SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
		 * R.layout.item_deudas, cursor, FROMDe, TD);// hay otro import
		 * Log.d("gastos", "Nmostrar dEUDADAS");
		 * 
		 * setListAdapter(adapter);
		 */

		 SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.item_deudas, cursor, FROMDe, TD);//
		// hay otro import

		//SimpleAdapter adapter = new SimpleAdapter(this, data,		R.layout.item_deudas, FROMDe, TD);

		 ListView lv = (ListView) this.findViewById(R.id.lista_deudas);
			lv.setAdapter(adapter);
		
		}
	}

	private List<Map<String, String>> resumenDeudas(Cursor cursor2) {

		String cursor[] = cursor2.getString(2).split(",");
		String miembro[] = miembros.split(",");
		double deuda_actual[] = new double[cursor.length];
		double deuda_total[] = new double[cursor.length];
		int i;

		for (i = 0; i < cursor.length; i++) {
			deuda_total[i] = Double.parseDouble(cursor[i]);
			deuda_actual[i] = 0;
		}
		Log.d("resumen0","antes del while");
		String resumen = "";
		
	//	while (!(deuda_total[0]==0 &&deuda_total[1]==0&&deuda_total[3]==0)) {
			
			for (i = 0; i < cursor.length; i++) {
				for(int s=0;s<cursor.length;s++){
					Log.d("deuda", deuda_total[s]+",");	
				}
				if (deuda_total[i] < 0) {
					for (int j = 0; j < cursor.length; j++) {
						if (deuda_total[j] > 0) {
							double resta = deuda_total[j] + deuda_total[i];
							if (resta >= 0) {// el valor negativo estaría
												// saldado, y el positivo (lo q
												// le deben todavia) seria el
												// resultado de la resta
								resumen += miembro[i] + "," + miembro[j] + ","
										+ Double.toString(-deuda_total[i]) + ";"; // tal a tal
																	// le debe
																	// tanto
								deuda_total[i] = 0;
								deuda_total[j] = resta;
								// falta q quede a este le ha dado tal
								
							} else {
								resumen += miembro[i] + "," + miembro[j] + ","
										+ Double.toString(deuda_total[i]) + ";"; // tal a tal
																	// le debe
																	// tanto
								deuda_total[i] = resta;
								deuda_total[j] = 0;
								// falta q queda a este le ha dado tal
								
							}
							Log.d("deudas", "resumen =" + resumen);

						}
					}
				}
		//	}
				
		}
			for(i=0;i<deuda_total.length;i++){
				Log.d("deuda_fin", deuda_total[i]+",");	
			}
			
		return null;
	}

	private void nuevaDeuda(ContentValues values) {
		Log.d("gastos", "NEW BD");
		// solo habra que hacerlo una vez, cuando se agregue un gasto por
		// primera vez

		Log.d("deudaBD", "creando nueva fila");
		SQLiteDatabase db = groups.getWritableDatabase();
		db.insertOrThrow(TABLA_DEUDAS, null, values);
		Log.d("gastos", "NEW B4");
	}

	private void editarDeudas(String deu) {
		// /siempre se editar, ya q solo habra una entrada. Se crea la entrada
		// al crear el grupo.
		String resultado;
		Cursor c = obtenerDeudas();
		if (c.getCount() > 0) {
			c.moveToFirst();
			Log.d("deuda", "tiene ya la lineacreada");
			String deuda[] = c.getString(2).split(",");
			String deud[] = deu.split(",");

			double deuda_actual[] = new double[deuda.length];
			double deuda_total[] = new double[deuda.length];
			int i = 0;

			for (i = 0; i < deuda.length; i++) {
				deuda_total[i] = Double.parseDouble(deuda[i]);
				deuda_actual[i] = Double.parseDouble(deud[i]);
				deuda_total[i] += deuda_actual[i];
			}

			// // ahora se pasa a string
			resultado = Double.toString(deuda_total[0]) + ",";

			for (i = 1; i < deuda_total.length; i++) {
				resultado += deuda_total[i] + ",";
			}
			Log.d("deudas", "deuda_total=" + deuda_total);
		} else
			resultado = deu;
		Log.d("editar", "deuda_total = " + resultado);
		SQLiteDatabase db = groups.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(ID_GRUPO, id_grupo);
		values.put(DEUDA_TOTAL, resultado);
		if (c.getCount() > 0)
			db.update(TABLA_DEUDAS, values, ID_GRUPO + "=" + id_grupo, null);
		else
			nuevaDeuda(values);
		Log.d("deudas", "end");

	}

	private Cursor obtenerGastos() {

		SQLiteDatabase db = groups.getReadableDatabase();
		cursor = db.query(TABLA_GASTOS, FROM, ID_GRUPO + "=" + id_grupo, null,
				null, null, ORDER_BY);
		startManagingCursor(cursor);

		// db.close();
		return cursor;

	}

	private void mostrarGastos(Cursor cursor) {// muestra los grupos
		Log.d("gastos", "Nmostrar");
		cursor.moveToFirst();
		// Log.d("mostrar cursor:",
		// cursor.getString(2) + " " + cursor.getString(3));
		cursor.moveToFirst();
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.item_gastos, cursor, new String[] { QUIEN_PAGO,
						CUANTO, PARA_QUIEN, }, TO);// hay otro import
		Log.d("gastos", "Nmostrar2");
		setListAdapter(adapter);

	}

	private void agregarGasto(ContentValues value) {

		Log.d("gastos", "NEW BD");
		SQLiteDatabase db = groups.getWritableDatabase();
		db.insertOrThrow(TABLA_GASTOS, null, value);
		Log.d("gastos", "NEW B4");

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {// crea el menu
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_gastos, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {// el menu al presionar
															// sobre un grupo

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {

		case R.id.menu_verRecibo:// FUNCIONA
			cursor.moveToFirst();
			cursor.move(info.position);
			String path = cursor.getString(9);

			if (path != null) {
				Intent i0 = new Intent(this, verFoto.class);
				i0.putExtra("path", path);
				startActivity(i0);
			} else
				Toast.makeText(this, "No tiene foto", Toast.LENGTH_LONG).show();

			return true;

		case R.id.menu_verUbicacion:// FUNCIONA

			cursor.moveToFirst();
			cursor.move(info.position);
			String location = cursor.getString(10);

			if (location != null) {
				Intent iU = new Intent(this, ubicacion.class);
				String[] g = location.split(",");
				iU.putExtra("latitude", Double.parseDouble(g[0]));
				iU.putExtra("longitude", Double.parseDouble(g[1]));
				startActivity(iU);
			} else
				Toast.makeText(this, "No tiene ubicación", Toast.LENGTH_LONG)
						.show();
			return true;

		case R.id.menu_editarGasto:
			// Intent i = new Intent(this, gastos.class);
			Log.d("GRUPOS", "editarGASTO");
			String s;
			Intent i = new Intent(this, nuevoGasto.class);
			cursor.moveToFirst();
			cursor.move(info.position);
			i.putExtra("editar", true);
			i.putExtra("miembros", miembros);
			i.putExtra("id_gasto", cursor.getInt(0));// id del gasto a editar en
														// la
			// clase grupo, // solo se

			i.putExtra("id", cursor.getInt(1));
			i.putExtra("quien_pago", cursor.getString(2));
			i.putExtra("para_quien", cursor.getString(3));
			i.putExtra("cuanto", cursor.getDouble(4));
			i.putExtra("deuda_anterior", cursor.getString(5));
			s = cursor.getString(6);
			if (s != null)
				i.putExtra("concepto", s);
			else
				i.putExtra("concepto", "");
			i.putExtra("fecha", cursor.getString(7));
			i.putExtra("hora", cursor.getString(8));
			Log.d("gastos", "pasa0");
			s = cursor.getString(9);
			if (s != null)
				i.putExtra("foto", s);
			else
				i.putExtra("foto", "");
			Log.d("gastos", "pasa1"); // donde

			s = cursor.getString(10);
			if (s != null)
				i.putExtra("location", s); // la ubicacion // estaria la
			else
				i.putExtra("location", "");
			s = "";
			Log.d("prueba", Integer.toString(s.length()));

			Log.d("gastos", "pasa2"); // donde
			startActivity(i);
			return true;

		case R.id.menu_borrarGasto:// /DUDAS DE SI SE TNEDRIA Q BORRAR...

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

	private void editarGasto(int id, ContentValues values) {// actualiza los
															// datos en la BBDD
															// tras haber
															// editado un grupo
		SQLiteDatabase db = groups.getWritableDatabase();

		db.update(TABLA_GASTOS, values, _ID + "=" + id, null);

	}

	private String calcularDeuda(String quien_pago, String para_quien,
			double cuanto) {
		String deuda;
		String p_quien[] = para_quien.split(",");// si salen ordenados mas
													// facil...
		String member[] = miembros.split(",");
		double deuda_actual[] = new double[member.length];

		for (int i = 0; i < member.length; i++) {// tendra el mismo orden q
													// miembros
			deuda_actual[i] = 0;
			if (member[i].equals(quien_pago))
				deuda_actual[i] = cuanto;
		}

		for (int i = 0; i < p_quien.length; i++) {
			for (int j = 0; j < member.length; j++) {
				if (p_quien[i].equals(member[j])) {
					deuda_actual[j] -= cuanto / (p_quien.length);
				}
			}
		}

		deuda = Double.toString(deuda_actual[0]) + ",";

		for (int i = 1; i < deuda_actual.length; i++) {
			deuda += deuda_actual[i] + ",";
		}
		Log.d("deudas", "va : " + deuda);
		return deuda;
	}

	private String calcularDeuda2(String deuda_anterior, String deuda_actual) {
		String deuda;
		Log.d("calcular2", "anterior: " + deuda_anterior);
		Log.d("calcular2", "actual: " + deuda_actual);

		String d_anterior[] = deuda_anterior.split(",");
		String d_actual[] = deuda_actual.split(",");
		double dd_actual[] = new double[miembros.split(",").length];
		double dd_anterior[] = new double[miembros.split(",").length];
		for (int i = 0; i < miembros.split(",").length; i++) {
			dd_anterior[i] = Double.parseDouble(d_anterior[i]);
			dd_actual[i] = Double.parseDouble(d_actual[i]);
			dd_actual[i] = dd_actual[i] - dd_anterior[i];
		}
		deuda = Double.toString(dd_actual[0]) + ",";

		for (int i = 1; i < dd_actual.length; i++) {
			deuda += dd_actual[i] + ",";
		}

		Log.d("deudas", "cuando se edita: " + deuda);
		return deuda;
	}

	private ContentValues obtenerContent(int id, String quien_pago,
			String para_quien, double cuanto, String concepto, String fecha,
			String hora, String foto, String location, String deuda_anterior) {
		ContentValues values = new ContentValues();
		values.put(ID_GRUPO, id);
		values.put(QUIEN_PAGO, quien_pago);
		values.put(PARA_QUIEN, para_quien);
		Log.d("gastos", "NEW BD3");
		values.put(CUANTO, cuanto);// /deberia ser double

		String deuda = calcularDeuda(quien_pago, para_quien, cuanto);// si hay q
																		// editar
																		// el
																		// gasto
																		// se
																		// hara
																		// conforme
																		// a la
																		// deuda
		// anterior
		if (deuda_anterior != null) {
			editarDeudas(calcularDeuda2(deuda_anterior, deuda));
		} else
			editarDeudas(deuda);// para editar la fila

		values.put(DEUDA_ACTUAL, deuda);
		Log.d("gastos", "NEW BD4");
		values.put(CONCEPTO, concepto);
		values.put(FECHA, fecha);
		values.put(HORA, hora);
		values.put(FOTO, foto);
		values.put(UBICACION, location);
		Log.d("gastos", "NEW BD3");

		return values;
	}

	private void borrarGasto(int id) {// borra grupo seleccionado
		SQLiteDatabase db = groups.getWritableDatabase();
		db.delete(TABLA_GASTOS, _ID + "='" + id + "'", null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addGasto:
			Log.d("ActionBar", "Nuevo!");
			Intent i = new Intent(this, nuevoGasto.class);

			i.putExtra("editar", false);// editar es falso, se crea una nueva
										// fila en la columna
			i.putExtra("id", id_grupo);
			i.putExtra("miembros", miembros);
			startActivity(i);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
