package org.fhiden.hackaton.zurich.healthyeating.healthyeating;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.txusballesteros.widgets.FitChart;
import com.txusballesteros.widgets.FitChartValue;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CheckResponseActivity extends AppCompatActivity {
    String[] itemlist;
    List<String> improvedList = new ArrayList<>();

    TextView text, procentageView;
    double totalScore;
    final double colorMax  = 255;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);


        text = (TextView) findViewById(R.id.tipBox);
        final FitChart fitChart = (FitChart)findViewById(R.id.fitChart);
        procentageView = (TextView) findViewById(org.fhiden.hackaton.zurich.healthyeating.healthyeating.R.id.procentageView);


        Collection<FitChartValue> myvalues = new ArrayList<>();
        fitChart.setMinValue(0f);
        fitChart.setMaxValue(100f);


        JSONreader nreader = new JSONreader(this);
        Intent i = getIntent();
        String[] list = i.getExtras().getStringArray("TheList");
        itemlist = list;
        int score=0, size = 0;
        try {

            for (String item: list){
                int temp= nreader.findFood(item);
                Log.v("temp", temp+"");
                if (temp != -1){
                    improvedList.add(item);
                    score += temp;
                    size++;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(size > 0) {
            totalScore = score / size;
        }
        double red= colorMax, green = colorMax;
        if(totalScore >50){
            red = ((100- totalScore)/50) *colorMax;
            green = colorMax;
        }else if(totalScore <50) {
            red = colorMax;
            green = (totalScore /50)*colorMax;
        }

        final int color = (int) Long.parseLong((String.format("%02X", (int)colorMax)+String.format("%02X", (int)(red))+String.format("%02X", (int)(green))+"00").toLowerCase(), 16);

        myvalues.add(new FitChartValue((int) totalScore, color));
        fitChart.setValues(myvalues);

        procentageView.setText((int) totalScore +"%");

    }

    @Override
    protected void onResume() {
        super.onResume();
        StringBuilder stringBuilder = new StringBuilder();
        for (String item: improvedList){
            stringBuilder.append(item);
            stringBuilder.append(" \n");
        }
        text.setText(stringBuilder.toString());
    }
}
