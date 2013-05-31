package com.example.gastos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class miembros extends Activity {//si en el xml no se declara list view: "@android:id/list" no extiende de listActivity
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.miembros);

		Bundle extras = getIntent().getExtras();
		
		String miembros = null;
		String mails = null;

		if (extras != null) {
			miembros = extras.getString("miembros");
			mails = extras.getString("mails");
		}

		mostrarMiembros(pasarArray(miembros, mails));
	}

	private List<Map<String, String>> pasarArray(String miembros, String mails) {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		String[] result = miembros.split(",");
		String[] mail = mails.split(",");
		int i = 0;
		for (String m : result) {
			Map<String, String> datum = new HashMap<String, String>(2);
			datum.put("miembro", m);
			datum.put("mail", mail[i]);
			data.add(datum);
			i++;
		}

		return data;
	}

	private void mostrarMiembros(List<Map<String, String>> data) {
		Log.d("MIEMBROS", "lista");
		SimpleAdapter adapter = new SimpleAdapter(this, data,
				android.R.layout.simple_list_item_2, new String[] { "miembro",
						"mail" }, new int[] { android.R.id.text1,
						android.R.id.text2 });
		ListView lv = (ListView) this.findViewById(R.id.lista_miembros);
		lv.setAdapter(adapter);

	}
}
