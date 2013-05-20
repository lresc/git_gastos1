package com.example.gastos;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


public class nuevoGrupo extends Activity {
	String miembros=null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nuevogrupo);	
	}
	
	public void clickMiembro(View v){
		
		final TextView l1=(TextView) this.findViewById(R.id.members);
			
		
		final Dialog prueba=new Dialog(this);
		prueba.setContentView(R.layout.nuevomiembro);
		prueba.show();
		
		final EditText e1=(EditText) prueba.findViewById(R.id.name);
		final EditText e2=(EditText) prueba.findViewById(R.id.email);
		final Button button = (Button) prueba.findViewById(R.id.acept2);
        button.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			addMembers(e1.getText().toString(),e2.getText().toString());
			prueba.cancel();
		}});
        final Button button1 = (Button) prueba.findViewById(R.id.cancel2);
        button1.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			prueba.cancel();
		}});
		
	}

	public void addMembers(String m,String mail){
		 TextView l1=(TextView) this.findViewById(R.id.members);
		if(miembros==null){
			miembros=m;
			miembros=miembros +", " + mail +";";
			l1.setText(m);
			
		}
		else{
			miembros=miembros+  m + ", "+mail +",";
			l1.setText(l1.getText().toString()+"\n"+m);
		}
	}
	
	
	public void clickSincMiembro(View v){
		Button e1=(Button) this.findViewById(R.id.sincronizarMiembro);
		
		if(e1.getText().toString() == this.getString(R.string.sync) )				
			e1.setText(this.getString(R.string.notsync));
		else
			e1.setText(this.getString(R.string.sync));
		
		
	}
	public void clickNuevoGrupo(View v){
		
		EditText e1=(EditText) this.findViewById(R.id.gname);
		TextView t1=(TextView) this.findViewById(R.id.members);
		Button b1=(Button) this.findViewById(R.id.sincronizarMiembro);
		
		if(b1.getText().toString() == this.getString(R.string.sync)){				
			//no sincronización
		//faltaria error de campos null! q salga un alertDialog
			
		//	grupos g=new grupos();
		
			Intent i =new Intent(this, grupos.class);
			i.putExtra("name", e1.getText().toString());
			i.putExtra("members", miembros);
			i.putExtra("sync", b1.getText().toString());
			
			startActivity(i);
			
		}
		else{
			//sincronizacion
		}
			
		
		//String members=	
		
	}
}
