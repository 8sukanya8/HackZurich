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

        final Button history = (Button) findViewById(R.id.Statistics);
        final Button test = (Button) findViewById(R.id.checkbutton);
        final Button pic = (Button) findViewById(R.id.picButton);
        pic.setOnClickListener(new onpicyclickiness());

        history.setOnClickListener(new onclickiness());
        test.setOnClickListener(new onCheckyClickiness());
    }
    class onclickiness implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent i = new Intent( getApplicationContext(), adviceActivity.class);
            startActivity(i);

        }
    }
    class onpicyclickiness implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent i = new Intent( getApplicationContext(), PictureActivity.class);
            startActivity(i);

        }
    }
    class  onCheckyClickiness implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent i = new Intent( getApplicationContext(), CheckPictureActivity.class);
            startActivity(i);
        }
    }
}
