package com.example.gastos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class nuevoGrupo extends Activity {
	List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	
	String miembros = null; //parametros usados para editar el grupo, los traer de la clase grupo.java
	String mails = null;
	String nombre = null;
	String sinc = null;
	boolean editar; //indica si se ha llamado desde editarGrupo (en grupo.java) será true si es q quiere editar grupo
	int id;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nuevogrupo);
	//	Log.d("GRUPOS", "onCreate");
		editar = false;
		
		Bundle extras = getIntent().getExtras();
		
		ListView lv = (ListView) this.findViewById(R.id.members);//itemListener en la listView de miembros del grupo
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				alertaBorrar(position);
			}

		});

		if (extras != null) {
			nombre = extras.getString("nombre");
			miembros = extras.getString("miembros");
			mails = extras.getString("mail");
			sinc = extras.getString("sync");
			id = extras.getInt("id");
			escribir();
		}

		Log.d("GRUPOS", "onCreate1");
	}

	private void alertaBorrar(final int position) {//al seleccionar un item de la listView q muestra los miembros, muestra una alerta: Borrar miembro?
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this).setMessage("Borrar Miembro?")
				.setPositiveButton("Borrar",new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								borrarMiembro(position);

							}
						})
				.setNegativeButton("Cancelar",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								try {
									finalize();
								} catch (Throwable e) {
									e.printStackTrace();
								}
							}
						}).show();
	}

	private void borrarMiembro(int position) {//borrar miembro de ListView: es llamado al seleccionar un miembro de la listView
		int i;
		Log.d("GRUPOS", "miembros era: " + miembros);
		String m[] = miembros.split(",");
		String ma[] = mails.split(",");
		data.removeAll(data);
		miembros = null;
		mails = null;

		if (m.length == 1) {// para el caso de q haya solo un item
			addMembers(null, null);
		}
		for (i = 0; i < position; i++) {
			addMembers(m[i], ma[i]);
		}
		for (i = position; i < m.length - 1; i++) {
			addMembers(m[i + 1], ma[i + 1]);
		}
	}

	private void escribir() {//es llamado cuando se selecciona editarGrupo desde la clase grupo
		int i = 0;
		EditText e1 = (EditText) this.findViewById(R.id.gname);
		Button b1 = (Button) this.findViewById(R.id.sincronizarMiembro);
		editar = true;// ponemos a true

		// nombre del grupo
		e1.setText(nombre);

		// parte miembros y mails
		String[] m = miembros.split(",");
		String[] ma = mails.split(",");

		// ponemos a null las variables..
		miembros = null;
		mails = null;

		for (String t : m) {
			addMembers(t, ma[i]);
			i++;
		}

		// parte sincronizar
		if (sinc.equals("true")) {
			b1.setText(this.getString(R.string.notsync));
		} else {
			b1.setText(this.getString(R.string.sync));
		}

	}

	public void clickMiembro(View v) { //al pulsar añadir miembro, despliega un dialog para introducir nombre y mail

		final Dialog prueba = new Dialog(this);
		prueba.setContentView(R.layout.nuevomiembro);
		prueba.show();

		final EditText e1 = (EditText) prueba.findViewById(R.id.name);
		final EditText e2 = (EditText) prueba.findViewById(R.id.email);
		final Button button = (Button) prueba.findViewById(R.id.acept2);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addMembers(e1.getText().toString(), e2.getText().toString());
				prueba.cancel();
			}
		});
		final Button button1 = (Button) prueba.findViewById(R.id.cancel2);
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("GRUPOS", "CLICKMIEMBRO_CANCEL");
				prueba.cancel();

			}
		});
	}

	public void addMembers(String m, String mail) {//añadir miembros al ListView

		if (m != null) {
			if (m.length() > 0) {
				if (mail.length() == 0)
					mail = "-";

				Map<String, String> datum = new HashMap<String, String>(2);
				if (miembros == null) {
					miembros = m + ",";
					mails = mail + ",";

				} else {
					miembros = miembros + m + ",";
					mails = mails + mail + ",";

				}

				datum.put("miembro", m);
				datum.put("mail", mail);
				data.add(datum);
				Log.d("GRUPOS", "data: "+data);
			}
		}
		SimpleAdapter adapter = new SimpleAdapter(this, data,
				android.R.layout.simple_list_item_2, new String[] { "miembro",
						"mail" }, new int[] { android.R.id.text1,
						android.R.id.text2 });
		ListView lv = (ListView) this.findViewById(R.id.members);
		lv.setAdapter(adapter);

	}

	public void clickSincMiembro(View v) { //cuando se pulsa el boton Sincronizar: cambiar a No sincronizar y viceversa
		Log.d("GRUPOS", "sincMiembro1");
		Button e1 = (Button) this.findViewById(R.id.sincronizarMiembro);
		Log.d("GRUPOS", "sincMiembro2");
		if (e1.getText().toString() == this.getString(R.string.sync))
			e1.setText(this.getString(R.string.notsync));
		else
			e1.setText(this.getString(R.string.sync));
		Log.d("GRUPOS", "sincMiembro3");
	}

	public void clickNuevoGrupo(View v) {//cuanod clicka en aceptar: llama a la clase grupo.java


		EditText e1 = (EditText) this.findViewById(R.id.gname);
		Button b1 = (Button) this.findViewById(R.id.sincronizarMiembro);
		Intent i = new Intent(this, grupos.class);
		i.putExtra("editar", editar);
		i.putExtra("id", id);// id del grupo a editar en la clase grupo, solo se
								// agarrara para el caso en q editar sea true
		i.putExtra("name", e1.getText().toString());
		i.putExtra("members", miembros);
		i.putExtra("mails", mails);

		if (b1.getText().toString() == this.getString(R.string.sync)) {
			// no sincronización
			i.putExtra("sync", "false");
		} else {
			// sincronizacion
			i.putExtra("sync", "true");
		}
		Log.d("GRUPOS", "STARTACTIVITY");
		startActivity(i);
	}
	public void clickCancel(View v){
		finish();
	}
}

