package org.fhiden.hackaton.zurich.healthyeating.healthyeating;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


/**
 * Created by fhiden on 2017-09-16.
 */

public class JSONreader {
    private JSONObject theObject, theFoodObject;

    public JSONreader( final Context context){
        this.theObject = loadJSONFromAsset(context);
        this.theFoodObject = loadFoodFromAsset(context);
    }

    public String getRandomComment(final int score) throws JSONException {
        final int field = score/25;
        final Random r = new Random();

        final JSONArray viableComments = theObject.getJSONArray("scoreArea");
        final JSONArray nextCommentplz = viableComments.getJSONObject(field).getJSONArray("area"+field);

        final int length = nextCommentplz.length();
        final int randomIndex = r.nextInt(length);
        final JSONObject finalObejct = nextCommentplz.getJSONObject(randomIndex);

        return new String(finalObejct.getString("tip"));
    }

    public JSONObject loadJSONFromAsset(final Context context) {
        String json = null;
        try {
            final InputStream is = context.getAssets().open("data.json");
            final int size = is.available();
            final byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            return new JSONObject(json);

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject loadFoodFromAsset(final Context context) {
        String json = null;
        try {
            final InputStream is = context.getAssets().open("foodData.json");
            final int size = is.available();
            final byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            return new JSONObject(json);

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public int findFood(final String item) throws JSONException {

        final JSONArray viableItems = theFoodObject.getJSONArray("scores");
        for (int i = 0; i<viableItems.length(); i++){
            JSONObject obj = (JSONObject) viableItems.get(i);
            if (obj.get("label").equals(item)){
                return  obj.getInt("score");
            }
        }
        return -1;
    }
}
