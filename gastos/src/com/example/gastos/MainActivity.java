package com.example.gastos;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_main);
	
	
	}
	
	public void clickNuevoGrupo(View v) {//cuando se clicka

		Intent i = new Intent(this, nuevoGrupo.class);
		startActivity(i);
		//modificar
	}

	
	public void clickVerGrupos(View v){
		
		Intent i = new Intent(this, grupos.class);
		startActivity(i);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
