package net.lnmcc.cameraExample;

import java.io.FileNotFoundException;

import net.lnmcc.cameraExample.R;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity  {

	static final int CAMERA_REQUEST = 1; 
	
	Uri imageFileUri;
	ImageView imgView;
	Button takePictureBT;
	Button saveDataBT;
	TextView titleTV;
	TextView descriptionTV;
	EditText titleET;
	EditText descriptionET;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		imgView = (ImageView)findViewById(R.id.imgView);
		takePictureBT = (Button)findViewById(R.id.TakePictureBT);
		saveDataBT = (Button)findViewById(R.id.SaveDataBT);
		titleTV = (TextView)findViewById(R.id.TitleTV);
		descriptionTV  = (TextView)findViewById(R.id.DescriptionTV);
		titleET = (EditText)findViewById(R.id.TitleET);
		descriptionET = (EditText)findViewById(R.id.DescriptionET);
		
		imgView.setVisibility(View.GONE);
		saveDataBT.setVisibility(View.GONE);
		titleTV.setVisibility(View.GONE);
		descriptionTV.setVisibility(View.GONE);
		titleET.setVisibility(View.GONE);
		descriptionET.setVisibility(View.GONE);
		
		takePictureBT.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				imageFileUri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, new ContentValues());
				Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
				startActivityForResult(i, CAMERA_REQUEST);
			}
		});
		
		saveDataBT.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ContentValues contentValues = new ContentValues(3);
				contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, titleET.getText().toString());
				contentValues.put(MediaStore.Images.Media.DESCRIPTION, descriptionET.getText().toString());
				int upRows = getContentResolver().update(imageFileUri, contentValues, null, null);
				Toast.makeText(MainActivity.this, "Record Updated" + Integer.valueOf(upRows).toString(), Toast.LENGTH_SHORT).show();
				
				takePictureBT.setVisibility(View.VISIBLE);
				imgView.setVisibility(View.GONE);
				saveDataBT.setVisibility(View.GONE);
				titleTV.setVisibility(View.GONE);
				descriptionTV.setVisibility(View.GONE);
				titleET.setVisibility(View.GONE);
				descriptionET.setVisibility(View.GONE);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
			takePictureBT.setVisibility(View.GONE);
			saveDataBT.setVisibility(View.VISIBLE);
			imgView.setVisibility(View.VISIBLE);
			titleTV.setVisibility(View.VISIBLE);
	
			descriptionTV.setVisibility(View.VISIBLE);
			descriptionET.setVisibility(View.VISIBLE);
			titleET.setVisibility(View.VISIBLE);
			
			int dw = 200;
			int dh = 200;
			
			try {
				BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
				bmpFactoryOptions.inJustDecodeBounds = true;
				Bitmap bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageFileUri),
						null, bmpFactoryOptions);
				int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight / (float)dh);
				int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth / (float)dw);
				
				if(heightRatio > 1 && widthRatio > 1) {
					if(heightRatio > widthRatio) {
						bmpFactoryOptions.inSampleSize = heightRatio;
					} else {
						bmpFactoryOptions.inSampleSize = widthRatio;
					}
				}
				bmpFactoryOptions.inJustDecodeBounds = false;
				bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageFileUri),
						null, bmpFactoryOptions);
				imgView.setImageBitmap(bmp);
			} catch(FileNotFoundException e) {
				Log.v("ERROR", e.toString());
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
