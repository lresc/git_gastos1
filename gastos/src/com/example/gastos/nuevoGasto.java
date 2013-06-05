package com.example.gastos;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
	int id;//id_grupo
	int id_gasto=0;//se inicializa para q no de error
	String miembros;
	String deuda_anterior;
	//para multichoice para_quien
	ArrayList mSelectedItems;
	boolean[] selectedItems;
	
	String[] datos;
	// para la foto:
	static final int TOMAR_FOTO = 100;// nos permitira saber el origen al
										// procesar la respuesta del intento
	static final int UBICACION = 3; // en onActivyResult
	Uri fileUri = null;// para guardar el path donde se almacenará la fotografia
	String location = null;// guarda la location: latitude,longitude
	boolean editar;//indica se si he llamado desde editarGasto (en gasto.java) true si quiere editar
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ingresogasto);

		
		Bundle extras = getIntent().getExtras();
		
		id = extras.getInt("id");
		miembros = extras.getString("miembros");
		
		datos = miembros.split(",");
		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, datos);
		
		Spinner cmbOpciones = (Spinner) findViewById(R.id.quien_pago);
		adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cmbOpciones.setAdapter(adaptador);
		
		editar=extras.getBoolean("editar");
		
		if(editar==true){
			Log.d("nuevoGasto", "en editar");
			escribir(extras);
		}else{
			Log.d("nuevoGasto","en no editar");
			EditText e1 = (EditText) findViewById(R.id.fecha);
			EditText e2 = (EditText) findViewById(R.id.hora);
			e1.setText(getFechaActual());
			e2.setText(getHoraActual());
		}

	}

	private void escribir(Bundle extras) {
		TextView t1 = (TextView) findViewById(R.id.aquien);
		Spinner cmbOpciones = (Spinner) findViewById(R.id.quien_pago);
		Log.d("nuevoGasto","escribir0");
		id_gasto=extras.getInt("id_gasto");
		deuda_anterior=extras.getString("deuda_anterior");
		
		String q=extras.getString("quien_pago");
		Log.d("quien_pago",q);
		String m[]=miembros.split(",");
		int i;
		for(i=0;i< m.length;i++){
			if(m[i].compareTo(q)==0){
				Log.d("quien_pago",m[i]+" = "+q);
				cmbOpciones.setSelection(i);
				i=m.length;
			}
		}
		
		t1.setText(extras.getString("para_quien"));
		//falta!!!! //se deberia hacer un algoritmo para el selectedItem boolean[]
		EditText e3 = (EditText) findViewById(R.id.cuanto);
		e3.setText(Double.toString(extras.getDouble("cuanto")));
		
		
		EditText e4 = (EditText) findViewById(R.id.concepto);// puede ser null
		if(extras.getString("concepto").length()>0)
			e4.setText(extras.getString("concepto"));
		
		EditText e5 = (EditText) findViewById(R.id.fecha);
		e5.setText(extras.getString("fecha"));
		EditText e6 = (EditText) findViewById(R.id.hora);
		e6.setText(extras.getString("hora"));
		
		
		if(extras.getString("foto").length()>0){
			fileUri=Uri.fromFile(new File(extras.getString("foto")));
		}
		if(extras.getString("location").length()>0){
			location=extras.getString("location");
		}
		
		
	}

	public void clickAceptar(View v) {
		Log.d("gastos", "clickAceptar");

		Spinner e1 = (Spinner) findViewById(R.id.quien_pago);
		TextView e2 = (TextView) findViewById(R.id.aquien);
		EditText e3 = (EditText) findViewById(R.id.cuanto);
		EditText e4 = (EditText) findViewById(R.id.concepto);// puede ser null
		EditText e5 = (EditText) findViewById(R.id.fecha);
		EditText e6 = (EditText) findViewById(R.id.hora);

		if (e2.getText().toString().length()<1) {
			Toast.makeText(this, "Rellene a quien paga", Toast.LENGTH_LONG)
					.show();
		} else if (e3.getText().toString().length()<1) {
			Toast.makeText(this, "Rellene cuanto paga", Toast.LENGTH_LONG)
					.show();
		} else {
			Intent i = new Intent(this, gastos.class);
			i.putExtra("quien_pago",
					e1.getItemAtPosition(e1.getSelectedItemPosition())
							.toString());
			i.putExtra("para_quien", e2.getText().toString());
			i.putExtra("cuanto", Double.parseDouble(e3.getText().toString()));
			if (e4.getText().toString().length() > 0) {
				i.putExtra("concepto", e4.getText().toString());
			}
			else
				i.putExtra("concepto", "");
Log.d("gastos", "pasa0");
			if (fileUri != null) {
				i.putExtra("foto", fileUri.getPath());// se guarda el string
Log.d("gastos","pasa1");														// donde

			}else
				i.putExtra("foto", "");
		
			if (location != null) {
				i.putExtra("location", location); // la ubicacion // estaria la
											// foto
			}
			else
				i.putExtra("location","");
			i.putExtra("fecha", e5.getText().toString());
			i.putExtra("hora", e6.getText().toString());
			i.putExtra("editar", editar);//true edita, false agrega
			i.putExtra("listar", false);//agrega o edita// si true: listaria solo 
			i.putExtra("id_gasto", id_gasto);
			i.putExtra("id", id);// id del grupo a editar en la clase grupo	//id del grupo, se siguen intercambiando de un lado para otro
			i.putExtra("miembros", miembros);//lo mismo, miembros se sigue intercambiando de una lado para otro
			i.putExtra("deuda_anterior", deuda_anterior);//solo se leera en gastos.java en el caso q sea editar
			startActivity(i);

		}
	}

	public void clickUbicacion(View v) {
		Log.d("MIEMBROS", "Ubicacion");
		
		if(location==null){
			calcularUbicacion(true);
			}
		else{
			new AlertDialog.Builder(this)
			.setMessage("Ya tiene una ubicacion, desea sustituirla?")
			.setPositiveButton("Sustituir",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0,
								int arg1) {
							calcularUbicacion(true);
						}
					})
			.setNegativeButton("Cancelar",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0,
								int arg1) {
							try {
								Log.d("ubicacion", "cancelar");
								finalize();
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					})
			.setNeutralButton("Ver ubicación",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							// TODO Auto-generated method stub
					
							calcularUbicacion(false);
						}
					}).show();
}

	}

	private void calcularUbicacion(boolean calcular) {
		Intent i = new Intent(this, ubicacion.class);
		if(calcular==false){//se le pasan los parametros, no hay q calcularlos
			String[] g = location.split(",");
			i.putExtra("latitude", Double.parseDouble(g[0]));
			i.putExtra("longitude", Double.parseDouble(g[1]));
			startActivity(i);
		}
		else		
			startActivityForResult(i, UBICACION);//para q se le pase el valor location por parametro, en ver no necesitamos calcular nada
	
	}

	public void clickParaQuien(View v) {// para quien pago

		mSelectedItems = new ArrayList();

		new AlertDialog.Builder(this)
				.setMultiChoiceItems(datos, selectedItems,
						new DialogInterface.OnMultiChoiceClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which, boolean isChecked) {
								// TODO Auto-generated method stub
								if (isChecked) {
									// If the user checked the item, add it to
									// the selected items
									mSelectedItems.add(which);
								} else if (mSelectedItems.contains(which)) {
									// Else, if the item is already in the
									// array, remove it
									mSelectedItems.remove(Integer
											.valueOf(which));
								}
							}
						})
				.setPositiveButton("ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// User clicked OK, so save the mSelectedItems results
						// somewhere
						// or return them to the component that opened the
						// dialog
					
						TextView t1 = (TextView) findViewById(R.id.aquien);
						String g = null;
						while (!mSelectedItems.isEmpty()) {
							if (g == null)
								g = datos[(Integer) mSelectedItems.get(0)]
										+ ",";
							else
								g = g + datos[(Integer) mSelectedItems.get(0)]
										+ ",";

							mSelectedItems.remove(0);
						}
						t1.setText(g);
					}
				})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								Log.d("MIEMBROS", "CANCEL");

							}
						}).show();

	}

	public void clickFoto(View v) {// al pulsar el boton para hacer la foto
		
		if (fileUri == null) {
			hacerFoto();
		} else {
			new AlertDialog.Builder(this)
					.setMessage("Ya ha hecho una foto, desea sustituirla?")
					.setPositiveButton("Sustituir",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									hacerFoto();
								}
							})
					.setNegativeButton("Cancelar",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									try {
										Log.d("GASTOS", "cancelar");
										finalize();
									} catch (Throwable e) {
										e.printStackTrace();
									}
								}
							})
					.setNeutralButton("Ver foto",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									Log.d("GASTOS", "neutral button");
									ver_foto();
								}
							}).show();
		}

	}

	private void hacerFoto() {
		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileUri = obtenerNuevoArchivo();
		i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		startActivityForResult(i, TOMAR_FOTO); // sabremos el resultado
												// escribiendo el método
												// onActivityResult
	}

	private void ver_foto() {
		Intent i = new Intent(this, verFoto.class);
		i.putExtra("path", fileUri.getPath());
		startActivity(i);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { // foto
		// PARA CAMARA
		try {
			if (requestCode == TOMAR_FOTO) {
				if (resultCode == RESULT_OK) {
					Toast.makeText(this, "Imagen guardada en:\n" + fileUri,
							Toast.LENGTH_LONG).show();
				}
			}
		} catch (Exception ex) {
			Log.e("Error al volver de la foto", ex.getMessage());
		}
		
		// PARA UBICACION
		if (requestCode == UBICACION) {
			Log.d("UBICACION", "onActivityResult; ubicacion");
			if (resultCode == RESULT_OK) {
				Log.d("UBICACION", "onActivityResult; resultOK");
				location = data.getStringExtra(ubicacion.LOCATION);//tipo: latitude,longitude
				Log.d("location", "onActivityResult;" + location);
			}
		}
	}

	private Uri obtenerNuevoArchivo() {// foto

		Uri uri = null;

		try {
			File mediaStorageDir = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					"fotosGastos");
			if (!mediaStorageDir.exists()) {
				mediaStorageDir.mkdirs();
			}
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
					.format(new Date());
			File mediaFile = new File(mediaStorageDir.getPath()
					+ File.separator + "IMG_" + timeStamp + ".jpg");
			uri = Uri.fromFile(mediaFile);
		} catch (Exception ex) {
			Log.e("Error al crear archivo", ex.getMessage());
		}

		return uri;
	}

	public static String getFechaActual() {
		return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
	}

	public static String getHoraActual() {
		return new SimpleDateFormat("HH:mm:ss").format(new Date());// hh en
																	// mayus??
	}

	public void clickCancelar(View v) {
		Log.d("gastos", "clickCancelar");
		finish();
	}
}
