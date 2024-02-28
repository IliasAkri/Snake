package com.example.snake;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tv_inicio;
    private Button bt_jugar;
    private Button bt_contacto;
    private Button bt_config;
    private Button bt_salir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_inicio = findViewById(R.id.tv_inicio);
        bt_jugar = findViewById(R.id.bt_jugar);
        bt_contacto = findViewById(R.id.bt_contacto);
        bt_config = findViewById(R.id.bt_config);
        bt_salir = findViewById(R.id.bt_salir);

        bt_jugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SnakeActivity.class);
                startActivity(i);
            }
        });
        bt_contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                composeEmail(view);
            }
        });

        bt_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    /*
    * ENVIO DE EMAIL
    * */
    public void composeEmail(View v) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"a33p16m@gmail.com",
                "andresparra5@educa.madrid.org"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "¿?");


        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


}