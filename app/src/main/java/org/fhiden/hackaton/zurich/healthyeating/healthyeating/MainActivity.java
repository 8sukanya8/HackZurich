package org.fhiden.hackaton.zurich.healthyeating.healthyeating;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        number = (EditText) findViewById(R.id.numberText);
        final Button go = (Button) findViewById(R.id.goButton);
        final Button pic = (Button) findViewById(R.id.picButton);
        pic.setOnClickListener(new onpicyclickiness());

        go.setOnClickListener(new onclickiness());
    }
    class onclickiness implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent i = new Intent( getApplicationContext(), adviceActivity.class);
            i.putExtra("healthScore", Integer.parseInt(number.getText().toString()));
            startActivity(i);

        }
    }
    class onpicyclickiness implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent i = new Intent( getApplicationContext(), pictureActivity.class);
            startActivity(i);

        }
    }
}
