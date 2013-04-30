package com.example.gastos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class nuevoGrupo extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nuevogrupo);
		}
	
	public void clickMiembro(View v){
		Intent i = new Intent(this, nuevoMiembro.class);
		startActivity(i);
	}

}
