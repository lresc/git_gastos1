package com.example.gastos;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class ubicacion extends FragmentActivity {
	private LocationManager mgr;
	private String mejor;
	public final static String LOCATION = "com.example.gastos.LOCATION";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ubicacion);
		Log.d("UBICACION", "ubi 0");
		Bundle extras = getIntent().getExtras();
		Double latitude;
		Double longitude;
		if (extras != null) {
			latitude = extras.getDouble("latitude");
			longitude = extras.getDouble("longitude");

		} else {
			mgr = (LocationManager) getSystemService(LOCATION_SERVICE);

			Criteria criteria = new Criteria();
			mejor = mgr.getBestProvider(criteria, true);// el mejor proveedor
			Log.d("UBICACION", "ubi 1");
			Location location = mgr.getLastKnownLocation(mejor);
			String location_string = location.getLatitude() + ","
					+ location.getLongitude();
			Log.d("UBICACION", "ubi 2");
			// //para leer los datos desde la activity anterior (nuevo gasto)
			Intent intentRetornoDatos = new Intent();
			intentRetornoDatos.putExtra(LOCATION, location_string);
			Log.d("UBICACION", "ubi 3");
			setResult(Activity.RESULT_OK, intentRetornoDatos);
			latitude=location.getLatitude();
			longitude=location.getLongitude();
		}
		Log.d("UBICACION", "ubi 4");
		if (getVersionFromPackageManager(this) < 2) {
			Log.d("UBICACION", "ubi 5");
			mostrarUbicacion(latitude, longitude);
		} else {
			Log.d("UBICACION", "tiener mapa!");
			mostrarMapa(latitude, longitude);
		}

	}

	private void mostrarMapa(Double latitude, Double longitude) {
		// TODO Auto-generated method stub
		Log.d("UBICACION", "mostrar mapa 0");

		GoogleMap mapa = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		Log.d("UBICACION", "1");
		mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL); // se puede elegir satelite,
													// etc..
		Log.d("UBICACION", "2");
		CameraUpdate camUpd1 = CameraUpdateFactory.newLatLng(new LatLng(
				latitude, longitude));
		Log.d("UBICACION", "ubi 3");
		mapa.moveCamera(camUpd1);
		CameraUpdate camUpd2 = CameraUpdateFactory.zoomTo(14);
		mapa.animateCamera(camUpd2);
		Log.d("UBICACION", "fin mostrar mapa");
		mapa.addMarker(new MarkerOptions().position(new LatLng(latitude,
				longitude)));
	}

	private void mostrarUbicacion(Double latitude, Double longitude) {
		// para el caso de tener un openGL< 2.0. al no poder mostrar mapas
		// saldra un alertDialog con la ubicacion
		String lat = Double.toString(latitude);
		String lon = Double.toString(longitude);

		new AlertDialog.Builder(this)
				.setMessage(
						"Su dispositivo utiliza una v"
								+ getVersionFromPackageManager(this)
								+ " de OpenGL, es necesario un version mayor de 2.0 para mostrar mapas. Su ubición es: ["
								+ lat + ", " + lon + "]")
				.setNeutralButton("Aceptar",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								finish();
							}
						}).show();

	}

	// ////version openGL para mostrar mapas con hay q tener minimo la 2.0
	private static int getVersionFromPackageManager(Context context) {
		PackageManager packageManager = context.getPackageManager();
		FeatureInfo[] featureInfos = packageManager
				.getSystemAvailableFeatures();
		if (featureInfos != null && featureInfos.length > 0) {
			for (FeatureInfo featureInfo : featureInfos) {
				// Null feature name means this feature is the open gl es
				// version feature.
				if (featureInfo.name == null) {
					if (featureInfo.reqGlEsVersion != FeatureInfo.GL_ES_VERSION_UNDEFINED) {
						return getMajorVersion(featureInfo.reqGlEsVersion);
					} else {
						return 1; // Lack of property means OpenGL ES version 1
					}
				}
			}
		}
		return 1;
	}

	/** @see FeatureInfo#getGlEsVersion() */
	private static int getMajorVersion(int glEsVersion) {
		return ((glEsVersion & 0xffff0000) >> 16);
	}

}
