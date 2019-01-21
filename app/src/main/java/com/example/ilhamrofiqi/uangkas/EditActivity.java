package com.example.ilhamrofiqi.uangkas;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.example.ilhamrofiqi.uangkas.Helper.CurrentDate;
import com.example.ilhamrofiqi.uangkas.Helper.SqliteHelper;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class EditActivity extends AppCompatActivity {

    RadioGroup radio_status;
    RadioButton radio_masuk, radio_keluar;
    EditText edit_jumlah, edit_keterangan, edit_tanggal;
    Button btn_simpan;
    RippleView rip_simpan;
    String status, tanggal;
    SqliteHelper sqliteHelper;
    Cursor cursor;

    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        sqliteHelper = new SqliteHelper(this);

        radio_status        = findViewById(R.id.radio_status);
        radio_masuk         = findViewById(R.id.radio_masuk);
        radio_keluar        = findViewById(R.id.radio_keluar);
        edit_jumlah         = findViewById(R.id.edit_jumlah);
        edit_keterangan     = findViewById(R.id.edit_keterangan);
        edit_tanggal     = findViewById(R.id.edit_tanggal);
        btn_simpan          = findViewById(R.id.btn_simpan);
        rip_simpan          = findViewById(R.id.rip_simpan);

        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        cursor = db.rawQuery(
                "SELECT *, strftime('%d/%m/%Y', tanggal) AS tgl FROM transaksi WHERE transaksi_id='"+ MainActivity.transaksi_id +"'"
                , null);
        cursor.moveToFirst();

        status = cursor.getString(1);
        switch (status){
            case "MASUK": radio_masuk.setChecked(true);
                break;
            case "KELUAR": radio_keluar.setChecked(true);
                break;
        }

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

        edit_jumlah.setText(cursor.getString(2));
        edit_keterangan.setText(cursor.getString(3));
        tanggal = cursor.getString(4);
        edit_tanggal.setText(cursor.getString(5));
        edit_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        NumberFormat numberFormat = new DecimalFormat("00");

                        tanggal = year + "-" + numberFormat.format(month +1) + "-" + numberFormat.format(dayOfMonth);
                        Log.e(" tanggal", tanggal);

                        edit_tanggal.setText(numberFormat.format(dayOfMonth) + "/" + numberFormat.format(month+1) +
                                "/"  + year);

                    }
                }, CurrentDate.year, CurrentDate.month, CurrentDate.day);
                datePickerDialog.show();
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
                    simpanEdit();
                }
            }
        });

        getSupportActionBar().setTitle("Edit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void simpanEdit(){

        SQLiteDatabase database = sqliteHelper.getWritableDatabase();
        database.execSQL("UPDATE transaksi SET status='" + status + "', jumlah='" +edit_jumlah.getText().toString() + "', " +
                "keterangan='"+ edit_keterangan.getText().toString() +"', tanggal='" + tanggal + "' " +
                "WHERE transaksi_id='" + MainActivity.transaksi_id+"' ");

        Toast.makeText(getApplicationContext(), "Perubahan Berehasil Disimpan",
                Toast.LENGTH_LONG).show();
        finish();
    }
}
