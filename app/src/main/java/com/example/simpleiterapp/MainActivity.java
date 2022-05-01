package com.example.simpleiterapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void start_button(View view){
        EditText mtr = findViewById(R.id.matrix_enter);
        EditText fr_mem = findViewById(R.id.free_members);
        EditText st_acc = findViewById(R.id.start_accuracy);
        EditText eps = findViewById(R.id.epsilon);
        if(mtr.getText().toString().length() != 0 &&
                eps.getText().toString().length() != 0 &&
                fr_mem.getText().toString().length() != 0 &&
                st_acc.getText().toString().length() != 0) {
            Intent intent = new Intent(this, SecondActivity.class);

            intent.putExtra("epsilon", eps.getText().toString());
            intent.putExtra("vector_b", fr_mem.getText().toString());
            intent.putExtra("x_0", st_acc.getText().toString());
            intent.putExtra("matrix", mtr.getText().toString());
            startActivity(intent);
        }
    }
}