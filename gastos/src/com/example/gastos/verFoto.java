package com.example.gastos;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class verFoto extends Activity {
	Bitmap myBitmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.verfoto);
		Log.d("foto","1");
		Bundle extras = getIntent().getExtras();
		Log.d("foto","2");
		String path = extras.getString("path");
		File imgFile = new File(path);
		Log.d("foto","3");
		if (imgFile.exists()) {
			myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			Log.d("foto","4");
			ImageView myImage = (ImageView) findViewById(R.id.imagen);
			Log.d("foto","5");
			myImage.setImageBitmap(myBitmap);
		}

	}
/////cuando giras la pantalla da error, no cabe el bitmap: supuestamente podria ser esta la solucion..
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d("foto","6");
		// guardamos la informacion relevante
		outState.putParcelable("IMAGEN", myBitmap);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		Log.d("foto","7");
		myBitmap = savedInstanceState.getParcelable("IMAGEN");
	}

}
