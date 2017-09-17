package org.fhiden.hackaton.zurich.healthyeating.healthyeating;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            takePicButton.setEnabled(false);
            VisionHelper helper = new VisionHelper();
            helper.makeRequest(this, imageBitmap);
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

    public class VisionHelper {
        Vision.Builder visionBuilder;

        class NeededThings{
            private Bitmap bitmap;
            private Vision vision;
            private Context context;

            public NeededThings(Context context, Vision vision, Bitmap bitmap){
                this.bitmap = bitmap;
                this.vision = vision;

            }
            public Vision getVision() {
                return vision;
            }

            public Bitmap getBitmap() {
                return bitmap;
            }

            public Context getContext() {
                return context;
            }
        }

        public VisionHelper(){
            visionBuilder = new Vision.Builder(
                    new NetHttpTransport(),
                    new AndroidJsonFactory(),
                    null);

            visionBuilder.setVisionRequestInitializer(
                    new VisionRequestInitializer("AIzaSyAyY8TylsMl0zbTFMVkrdMZsUpLq8qy-9k"));
        }
        public void makeRequest(Context context, final Bitmap image){
            final Vision vision = visionBuilder.build();
            NeededThings neededThings = new NeededThings(context, vision, image);
            visionCaller caller = new visionCaller();
            caller.execute(neededThings);
        }

    }

    class visionCaller extends AsyncTask<VisionHelper.NeededThings, Void, List<String>> {
        Context context;
        @Override
        protected List<String> doInBackground(VisionHelper.NeededThings... neededThingses) {
            byte[] photoData;
            this.context = neededThingses[0].getContext();
            InputStream inputStream = null;
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                neededThingses[0].getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, stream);
                inputStream = new ByteArrayInputStream(stream.toByteArray());

                photoData = org.apache.commons.io.IOUtils.toByteArray(inputStream);
                inputStream.close();
                Image image1 = new Image();
                image1.encodeContent(photoData);

                Feature feature = new Feature();
                feature.setType("LABEL_DETECTION");

                AnnotateImageRequest request = new AnnotateImageRequest();
                request.setImage(image1);
                request.setFeatures(Arrays.asList(feature));

                BatchAnnotateImagesRequest batchrequest = new BatchAnnotateImagesRequest();
                batchrequest.setRequests(Arrays.asList(request));

                BatchAnnotateImagesResponse batchAnnotateImagesResponse = neededThingses[0].getVision().images().annotate(batchrequest).execute();

                List<AnnotateImageResponse> responses = batchAnnotateImagesResponse.getResponses();
                Log.v("SENDING...", "REQUEST");
                List<String> items = new ArrayList<>();
                for (AnnotateImageResponse response : responses){
                    Log.v("RESPONSE", response.getLabelAnnotations().get(0).getDescription());
                    for (EntityAnnotation entity : response.getLabelAnnotations()){
                        items.add(entity.getDescription());
                    }
                }
                return items;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> items) {
            super.onPostExecute(items);

            Intent i = new Intent(getApplicationContext(), ResponseActivity.class);
            i.putExtra("TheList", items.toArray());
            startActivity(i);
            for (String e: items) {
                Log.v("item", e);
            }

        }
    }

}
