package com.example.gastos;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.content.Intent;
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
	Uri fileUri = null;// para guardar el path donde se almacenará la fotografia

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ingresogasto);

		Bundle extras = getIntent().getExtras();
		id = extras.getInt("id");
		miembros = extras.getString("miembros");
		datos = miembros.split(",");

		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, datos);

		Spinner cmbOpciones;

		cmbOpciones = (Spinner) findViewById(R.id.quien_pago);

		adaptador
				.setDropDownViewResource(android.R.layout.simple_list_item_multiple_choice);

		cmbOpciones.setAdapter(adaptador);

		/*
		 * ArrayAdapter<String> adaptador2 = new ArrayAdapter<String>(this,
		 * android.R.layout.simple_list_item_multiple_choice, datos);
		 * 
		 * cmbOpciones2.setAdapter(adaptador2);
		 */
	}

	public void clickAceptar(View v) {
		Log.d("gastos", "clickAceptar");

		EditText e1 = (EditText) findViewById(R.id.quien_pago);
		EditText e2 = (EditText) findViewById(R.id.para_quien);
		EditText e3 = (EditText) findViewById(R.id.cuanto);
		EditText e4 = (EditText) findViewById(R.id.concepto);// puede ser null
		// la fecha se introduce automaticamente con ms getms?
		Intent i = new Intent(this, grupos.class);
		i.putExtra("id", id);// id del grupo a editar en la clase grupo, solo se
		i.putExtra("quien_pago", e1.getText().toString());
		i.putExtra("para_quien", e2.getText().toString());
		i.putExtra("cuanto", e3.getText());
		i.putExtra("concepto", e4.getText().toString());
		i.putExtra("fecha", getFechaActual());
		i.putExtra("hora", getHoraActual());
		startActivity(i);
	}

	public void clickUbicacion(View v) {
		Log.d("MIEMBROS", "Ubicacion");
	}

	public void clickParaQuien(View v) {

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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
	}

	private Uri obtenerNuevoArchivo() {

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
		return new SimpleDateFormat("hh:mm:ss").format(new Date());// hh en
																	// mayus??
	}

	public void clickCancelar(View v) {
		Log.d("gastos", "clickCancelar");
	}
}
