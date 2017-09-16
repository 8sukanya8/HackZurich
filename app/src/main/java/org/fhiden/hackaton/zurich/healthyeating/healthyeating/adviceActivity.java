package org.fhiden.hackaton.zurich.healthyeating.healthyeating;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.txusballesteros.widgets.FitChart;
import com.txusballesteros.widgets.FitChartValue;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;

public class adviceActivity extends AppCompatActivity {

    TextView theTip, procentageView;
    double perceivedHealthiness;
    final double colorMax  = 255;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);

        perceivedHealthiness = getIntent().getExtras().getInt("healthScore");
        theTip = (TextView) findViewById(R.id.tipBox);

        final FitChart fitChart = (FitChart)findViewById(R.id.fitChart);

        fitChart.setMinValue(0f);
        fitChart.setMaxValue(100f);

        Collection<FitChartValue> myvalues = new ArrayList<>();
        double red= colorMax, green = colorMax;
        if(perceivedHealthiness>50){
            red = ((100-perceivedHealthiness)/50) *colorMax;
            green = colorMax;
        }else if(perceivedHealthiness<50) {
            red = colorMax;
            green = (perceivedHealthiness/50)*colorMax;
        }

        final int color = (int) Long.parseLong((String.format("%02X", (int)colorMax)+String.format("%02X", (int)(red))+String.format("%02X", (int)(green))+"00").toLowerCase(), 16);

        Log.v("HEX", red + "   "+ green + "   "+ Integer.toHexString((int)red));
        Log.v("HEX", ""+ color);

        myvalues.add(new FitChartValue((int)perceivedHealthiness, color));
        fitChart.setValues(myvalues);

        procentageView = (TextView) findViewById(org.fhiden.hackaton.zurich.healthyeating.healthyeating.R.id.procentageView);
        procentageView.setText((int)perceivedHealthiness+"%");

    }

    @Override
    protected void onResume() {
        super.onResume();
        JSONreader jsrdr = new JSONreader(getApplicationContext());
        try {
            theTip.setText(jsrdr.getRandomComment((int)perceivedHealthiness));
        } catch (JSONException e) {
            e.printStackTrace();
            theTip.setText(getAdviceInCaseOfEmergency((int)perceivedHealthiness));
        }
    }

    private String getAdviceInCaseOfEmergency(final int score){
        if (score<25) {
            return "An apple a day keeps the doctor away, you need to eat a lot more appels...";
        }
        else if (score<50) {
            return "Eating healthy will both increase your energy, go eat an apple";
        }
        else if (score<75) {
            return "You're already eating healthy food but to increase your score you should decrease on your guilty pleasures.";
        }
        else if(score<100){
            return "You're really eating healthy food! Try to keep it up!";
        }
        else if (score==100) {
            return "You're unrealistic healthy with what you eat, keep it up!";
        }
        else {
            return "We didn't get a score, try again later";
        }
    }
}
