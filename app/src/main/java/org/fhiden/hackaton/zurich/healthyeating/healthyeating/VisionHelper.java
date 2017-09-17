package org.fhiden.hackaton.zurich.healthyeating.healthyeating;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.IOUtils;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.common.io.Resources;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.api.services.vision.v1.Vision.*;

/**
 * Created by fhiden on 2017-09-16.
 */

/*public class VisionHelper {
    Vision.Builder visionBuilder;
    public VisionHelper(){
        visionBuilder = new Vision.Builder(
                new NetHttpTransport(),
                new AndroidJsonFactory(),
                null);

        visionBuilder.setVisionRequestInitializer(
                new VisionRequestInitializer("AIzaSyAyY8TylsMl0zbTFMVkrdMZsUpLq8qy-9k"));
    }
    public void makeRequest(Context context,final Bitmap image){
        final Vision vision = visionBuilder.build();
        NeededThings neededThings = new NeededThings(context, vision, image);
        visionCaller caller = new visionCaller();
        caller.execute(neededThings);
    }
    private class NeededThings{
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
    private class visionCaller extends AsyncTask<NeededThings, Void, List<String>>{
        Context context;
        @Override
        protected List<String> doInBackground(NeededThings... neededThingses) {
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

           Intent i = new Intent(context.getApplicationContext(), ResponseActivity.class);
           i.putExtra("TheList", items.toArray());
            for (String e: items) {
                Log.v("item", e);
            }

        }
    }
}*/
