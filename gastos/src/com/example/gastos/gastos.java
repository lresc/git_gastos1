package com.example.gastos;

import static com.example.gastos.Constantes.*;

import android.app.ListActivity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

public class gastos extends ListActivity {
	private baseDatos groups = new baseDatos(this);

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resumengastos);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			int id = extras.getInt("id");
			String quien_pago = extras.getString("quien_pago");
			String para_quien = extras.getString("para_quien");
			int cuanto = extras.getInt("cuanto");
			String concepto = extras.getString("concepto");
			String fecha = extras.getString("fecha");
			String hora = extras.getString("hora");
			agregarGasto(id, quien_pago, para_quien, cuanto, concepto, fecha,
					hora);
		}

	}

	private void agregarGasto(int id, String quien_pago, String para_quien,
			int cuanto, String concepto, String fecha, String hora) {
	
	Log.d("gastos", "NEW BD");
	SQLiteDatabase db = groups.getWritableDatabase();
	ContentValues values = new ContentValues();

	values.put("id_grupo", id);
	values.put(QUIEN_PAGO, quien_pago);
	values.put(PARA_QUIEN, para_quien);
	values.put(CUANTO, cuanto);///deberia ser int
	values.put(CONCEPTO, concepto);
	values.put(FECHA, fecha);
	//values.put(FOTO, "foto");
	//values.put(UBICACION, value)
	db.insertOrThrow(TABLA_GASTOS, null, values);

		
	}
}
