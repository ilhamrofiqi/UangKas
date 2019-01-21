package com.example.ilhamrofiqi.uangkas;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.example.ilhamrofiqi.uangkas.Helper.SqliteHelper;

public class AddActivity extends AppCompatActivity {

    RadioGroup radio_status;
    RadioButton radio_masuk, radio_keluar;
    EditText edit_jumlah, edit_keterangan;
    Button btn_simpan;
    RippleView rip_simpan;
    String status;
    SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        status = "";
        sqliteHelper = new SqliteHelper(this);

        radio_status        = findViewById(R.id.radio_status);
        radio_masuk         = findViewById(R.id.radio_masuk);
        radio_keluar        = findViewById(R.id.radio_keluar);
        edit_jumlah         = findViewById(R.id.edit_jumlah);
        edit_keterangan     = findViewById(R.id.edit_keterangan);
        btn_simpan          = findViewById(R.id.btn_simpan);
        rip_simpan          = findViewById(R.id.rip_simpan);

        radio_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_masuk: status = "MASUK";
                    break;
                    case  R.id.radio_keluar: status = "KELUAR";
                    break;
                }
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        rip_simpan.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                if (status.equals("") || edit_jumlah.getText().toString().equals("") || edit_keterangan.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Isi Data Dengan Benar",
                            Toast.LENGTH_LONG).show();
                }
                else if (status.equals("")){
                    Toast.makeText(getApplicationContext(), "Status Harus Dilengkapi",
                            Toast.LENGTH_LONG).show();
                    radio_status.requestFocus();
                } else if (edit_jumlah.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Jumlah Harus Diisi",
                            Toast.LENGTH_LONG).show();
                    edit_jumlah.requestFocus();
                } else if (edit_keterangan.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Keterangan Harus Diisi",
                            Toast.LENGTH_LONG).show();
                    edit_keterangan.requestFocus();
                } else {
                    simpanData();
                }

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

    private void simpanData(){

        SQLiteDatabase database = sqliteHelper.getWritableDatabase();
        database.execSQL("INSERT INTO transaksi (status, jumlah, keterangan) VALUES ('" + status +
                "', '" +edit_jumlah.getText().toString() +"', '"+ edit_keterangan.getText().toString() +"')");

        Toast.makeText(getApplicationContext(), "Transaksi Berehasil Disimpan",
                Toast.LENGTH_LONG).show();
        finish();
    }
}
