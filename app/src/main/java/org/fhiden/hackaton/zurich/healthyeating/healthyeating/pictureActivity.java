package org.fhiden.hackaton.zurich.healthyeating.healthyeating;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class pictureActivity extends AppCompatActivity {

    ImageView imageViewer;
    Button takePicButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        takePicButton = (Button)findViewById(R.id.imgButton);
        takePicButton.setOnClickListener(new onclickiness());

        imageViewer = (ImageView) findViewById(R.id.imageViewer);
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageViewer.setImageBitmap(imageBitmap);

            VisionHelper helper = new VisionHelper();
            helper.makeRequest(getApplicationContext(), imageBitmap);
        }
    }
    private class  onclickiness implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            dispatchTakePictureIntent();
        }
        private void dispatchTakePictureIntent() {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

        }
    }
}
