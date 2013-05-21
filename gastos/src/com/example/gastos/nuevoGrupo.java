package com.example.gastos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class nuevoGrupo extends Activity {
	String miembros = null;
	String mails = null;
	String nombre = null;
	String sinc = null;
	List<Map<String, String>> data=new ArrayList<Map<String, String>>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nuevogrupo);
		Log.d("GRUPOS","onCreate");
	
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			nombre = extras.getString("nombre");
			miembros = extras.getString("miembros");
			mails = extras.getString("mail");
			sinc = extras.getString("sync");
			Log.d("GRUPOS","onCreate2");
			escribir();
		}
		Log.d("GRUPOS","onCreate1");
	}

	private void escribir() {
		Log.d("GRUPOS","escribir");
		EditText e1 = (EditText) this.findViewById(R.id.gname);
		Button b1 = (Button) this.findViewById(R.id.sincronizarMiembro);
		Log.d("GRUPOS","escribir");
		// nombre del grupo
		e1.setText(nombre);
		// parte miembros y mails
		
		Log.d("GRUPOS",miembros);
	
		//Log.d("GRUPOS",mails);
		String[] m = miembros.split(",");
		String[]ma=null;
		if(mails!=null)
			ma=mails.split(",");
		
		int i=0;
		Log.d("GRUPOS","escribir");
		
		//ponemos a null las variables..
		miembros=null;
		mails=null;
		
		for (String t: m) {		
			Log.d("GRUPOS","addmember "+ t);
			addMembers(t,ma[i]);
		//	addMembers(t,null);
			Log.d("GRUPOS","addmember "+i);
			i++;
		}

		// parte sincronizar
		if (sinc.equals("true")) {
			b1.setText(this.getString(R.string.sync));
		} else {
			b1.setText(this.getString(R.string.notsync));
		}

	}

	public void clickMiembro(View v) {

		final Dialog prueba = new Dialog(this);
		prueba.setContentView(R.layout.nuevomiembro);
		prueba.show();

		final EditText e1 = (EditText) prueba.findViewById(R.id.name);
		final EditText e2 = (EditText) prueba.findViewById(R.id.email);
		final Button button = (Button) prueba.findViewById(R.id.acept2);
		Log.d("GRUPOS","CLICKMIEMBRO1");
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("GRUPOS","CLICKMIEMBROADD");
				// TODO Auto-generated method stub
				addMembers(e1.getText().toString(), e2.getText().toString());
				Log.d("GRUPOS","CLICKMIEMBROADDNO");
				prueba.cancel();
			}
		});
		final Button button1 = (Button) prueba.findViewById(R.id.cancel2);
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("GRUPOS","CLICKMIEMBRO_CANCEL");
				prueba.cancel();
				
			}
		});
	}

	public void addMembers(String m, String mail) {
		Log.d("GRUPOS", "addMembers0");
	
		Log.d("GRUPOS", "addMembers1");
		Map<String, String> datum = new HashMap<String, String>(2);
		if (m != null ) {
			if (miembros == null&&mails==null) {
				miembros = m + ",";
				mails = mail + ","; 
				
			} else {
				miembros = miembros + m + ","; 
				mails = mails + mail + ",";
			
			}
					 
			Log.d("GRUPOS", "addMember2");
			datum.put("miembro", m);
			datum.put("mail", mail);
			Log.d("GRUPOS", "addMember3");
			data.add(datum);
			Log.d("GRUPOS", "addMember4");
			SimpleAdapter adapter = new SimpleAdapter(this, data,
					android.R.layout.simple_list_item_2, new String[] { "miembro",
							"mail" }, new int[] { android.R.id.text1,
							android.R.id.text2 });
			Log.d("GRUPOS", "addMember5");
			ListView l1 = (ListView) this.findViewById(R.id.members);
			l1.setAdapter(adapter);
			Log.d("GRUPOS", "addMember6");
		}
		
	}

	public void clickSincMiembro(View v) {
		Log.d("GRUPOS", "sincMiembro1");
		Button e1 = (Button) this.findViewById(R.id.sincronizarMiembro);
		Log.d("GRUPOS", "sincMiembro2");
		if (e1.getText().toString() == this.getString(R.string.sync))
			e1.setText(this.getString(R.string.notsync));
		else
			e1.setText(this.getString(R.string.sync));
		Log.d("GRUPOS", "sincMiembro3");
	}

	public void clickNuevoGrupo(View v) {
		
		EditText e1 = (EditText) this.findViewById(R.id.gname);
	//	Log.d("GRUPOS","CLICK1");
		Button b1 = (Button) this.findViewById(R.id.sincronizarMiembro);
		//Log.d("GRUPOS","CLICK1");
		Intent i = new Intent(this, grupos.class);
		Log.d("GRUPOS","CLICK1");
		i.putExtra("name", e1.getText().toString());
		Log.d("GRUPOS","CLICK1 "+miembros);
		i.putExtra("members", miembros);
		Log.d("GRUPOS","CLICKmail "+mails);
		i.putExtra("mails", mails);
		Log.d("GRUPOS","CLICK1");
		
		if (b1.getText().toString() == this.getString(R.string.sync)) {
			// no sincronización
			Log.d("GRUPOS", "es false");
			i.putExtra("sync", "false");
		} else {
			// sincronizacion
			Log.d("GRUPOS", "es true");
			i.putExtra("sync", "true");
		}
		startActivity(i);
	}
}
