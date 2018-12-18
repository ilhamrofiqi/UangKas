package com.example.ilhamrofiqi.uangkas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    RadioGroup radio_status;
    RadioButton radio_masuk, radio_keluar;
    EditText edit_jumlah, edit_keterangan;
    Button btn_simpan;

    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        status = "";

        radio_status        = findViewById(R.id.radio_status);
        radio_masuk         = findViewById(R.id.radio_masuk);
        radio_keluar        = findViewById(R.id.radio_keluar);
        edit_jumlah         = findViewById(R.id.edit_jumlah);
        edit_keterangan     = findViewById(R.id.edit_keterangan);
        btn_simpan          = findViewById(R.id.btn_simpan);

        radio_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_masuk: status = "MASUK";
                    break;
                    case  R.id.radio_keluar: status = "Keluar";
                    break;
                }
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), " Jumlah : "+ edit_jumlah.getText().toString() + " Status : " + status,
                        Toast.LENGTH_LONG).show();
            }
        });

        getSupportActionBar().setTitle("Tambah");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
