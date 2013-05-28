package com.example.gastos;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class nuevoGasto extends Activity {
	int id;
	String miembros;
	ArrayList  mSelectedItems;
	String [] datos;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ingresogasto);

		Bundle extras = getIntent().getExtras();
		id=extras.getInt("id");
		miembros=extras.getString("miembros");
		datos = miembros.split(","); 
		  
		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, 
		            android.R.layout.simple_spinner_item, datos); 
		
		Spinner cmbOpciones; 

		  
		cmbOpciones = (Spinner)findViewById(R.id.quien_pago); 
		  
		adaptador.setDropDownViewResource( 
		        android.R.layout.simple_list_item_multiple_choice); 
		  
		cmbOpciones.setAdapter(adaptador); 
		
	 
		/*ArrayAdapter<String> adaptador2 = new ArrayAdapter<String>(this, 
	            android.R.layout.simple_list_item_multiple_choice, datos); 
		
		cmbOpciones2.setAdapter(adaptador2);
		
		*/
	}
	
	public void clickAceptar(View v){
		Log.d("gastos", "clickAceptar");
		
		EditText e1=(EditText)findViewById(R.id.quien_pago);
		EditText e2=(EditText)findViewById(R.id.para_quien);
		EditText e3=(EditText)findViewById(R.id.cuanto);
		EditText e4=(EditText)findViewById(R.id.concepto);//puede ser null
//la fecha se introduce automaticamente con ms getms?
		Intent i = new Intent(this, grupos.class);
		i.putExtra("id", id);// id del grupo a editar en la clase grupo, solo se
		i.putExtra("quien_pago", e1.getText().toString());
		i.putExtra("para_quien", e2.getText().toString());	
		i.putExtra("cuanto", e3.getText());
		i.putExtra("concepto", e4.getText().toString());
		i.putExtra("fecha", getFechaActual());
		i.putExtra("hora",getHoraActual());
		startActivity(i);
	}
	
	public void clickUbicacion(View v){
		Log.d("MIEMBROS","Ubicacion");
	}
	
	public void clickParaQuien(View v){
		
		 mSelectedItems = new ArrayList();
		
		new AlertDialog.Builder(this).setMultiChoiceItems(datos, null, new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				// TODO Auto-generated method stub
				Log.d("MIEMBROS","muliclick on:"+which);
				Log.d("MIEMBROS","muliclick on: "+mSelectedItems);
				if (isChecked) {
                    // If the user checked the item, add it to the selected items
                    mSelectedItems.add(which);
                } else if (mSelectedItems.contains(which)) {
                    // Else, if the item is already in the array, remove it 
                    mSelectedItems.remove(Integer.valueOf(which));
			}
			}
		}).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK, so save the mSelectedItems results somewhere
                // or return them to the component that opened the dialog
                Log.d("MIEMBROS", "ACEPT "+mSelectedItems);
                TextView t1=(TextView) findViewById(R.id.aquien);
                String g=null;
                while(!mSelectedItems.isEmpty()){
                	Log.d("MIEMBROS", "ACEPT "+mSelectedItems.get(0));
                	Log.d("MIEMBROS",datos[(Integer)mSelectedItems.get(0)]);
                	if(g==null)
                		g=datos[(Integer)mSelectedItems.get(0)]+", ";
                	else
                		g=g+datos[(Integer)mSelectedItems.get(0)]+", ";
                	
                	mSelectedItems.remove(0);
                }
                t1.setText(g);
            }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            	Log.d("MIEMBROS", "CANCEL");

            }
        }).show();
		
		}
	public void clickFoto(View v){
		Log.d("MIEMBROS","clickFoto");
	}
	//Metodo usado para obtener la fecha actual
    //@return Retorna un <b>STRING</b> con la fecha actual formato "dd-MM-yyyy"
    public static String getFechaActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
        return formateador.format(ahora);
    }

    //Metodo usado para obtener la hora actual del sistema
    //@return Retorna un <b>STRING</b> con la hora actual formato "hh:mm:ss"
    public static String getHoraActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("hh:mm:ss");
        return formateador.format(ahora);
    }
	public void clickCancelar(View v){
		Log.d("gastos", "clickCancelar");
	}
}

	
