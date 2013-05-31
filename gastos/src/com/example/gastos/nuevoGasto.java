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
	int id;
	String miembros;
	ArrayList mSelectedItems;
	String[] datos;
	// para la foto:
	static final int TOMAR_FOTO = 100;// nos permitira saber el origen al
										// procesar la respuesta del intento
	static final int UBICACION = 3; // en onActivyResult
	Uri fileUri = null;// para guardar el path donde se almacenará la fotografia
	String location = null;// guarda la location: latitude,longitude

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
		adaptador.setDropDownViewResource(android.R.layout.simple_spinner_item);
		cmbOpciones.setAdapter(adaptador);

		EditText e1 = (EditText) findViewById(R.id.fecha);
		EditText e2 = (EditText) findViewById(R.id.hora);
		e1.setText(getFechaActual());
		e2.setText(getHoraActual());
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
			i.putExtra("id", id);// id del grupo a editar en la clase grupo,							// solo se
			i.putExtra("quien_pago",
					e1.getItemAtPosition(e1.getSelectedItemPosition())
							.toString());
			i.putExtra("para_quien", e2.getText().toString());
			i.putExtra("cuanto", Integer.parseInt(e3.getText().toString()));
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
			startActivity(i);

		}
	}

	public void clickUbicacion(View v) {
		Log.d("MIEMBROS", "Ubicacion");
		Log.d("MIEMBROS", new SimpleDateFormat("HH:mm:ss").format(new Date()));
		Log.d("MIEMBROS", new SimpleDateFormat("dd-MM-yyyy").format(new Date()));

		Intent i = new Intent(this, ubicacion.class);
		startActivityForResult(i, UBICACION);

	}

	public void clickParaQuien(View v) {// para quien pago

		mSelectedItems = new ArrayList();

		new AlertDialog.Builder(this)
				.setMultiChoiceItems(datos, null,
						new DialogInterface.OnMultiChoiceClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which, boolean isChecked) {
								// TODO Auto-generated method stub
								Log.d("MIEMBROS", "muliclick on:" + which);
								Log.d("MIEMBROS", "muliclick on: "
										+ mSelectedItems);
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
						Log.d("MIEMBROS", "ACEPT " + mSelectedItems);
						TextView t1 = (TextView) findViewById(R.id.aquien);
						String g = null;
						while (!mSelectedItems.isEmpty()) {
							Log.d("MIEMBROS", "ACEPT " + mSelectedItems.get(0));
							Log.d("MIEMBROS",
									datos[(Integer) mSelectedItems.get(0)]);
							if (g == null)
								g = datos[(Integer) mSelectedItems.get(0)]
										+ ", ";
							else
								g = g + datos[(Integer) mSelectedItems.get(0)]
										+ ", ";

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
		Log.d("MIEMBROS", "clickFoto");
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
				location = data.getStringExtra(ubicacion.LOCATION);
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
