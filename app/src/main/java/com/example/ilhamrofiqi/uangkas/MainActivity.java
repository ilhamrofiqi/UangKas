package com.example.ilhamrofiqi.uangkas;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.ilhamrofiqi.uangkas.Helper.SqliteHelper;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    SwipeRefreshLayout swipe_refresh;
    ListView list_kas;
    ArrayList<HashMap<String, String>> aruskas;

    TextView text_masuk, text_keluar, text_total;

    SqliteHelper sqliteHelper;
    Cursor cursor;

    String transaksi_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddActivity.class));
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });

        text_masuk      = findViewById(R.id.text_masuk);
        text_keluar     = findViewById(R.id.text_keluar);
        text_total      = findViewById(R.id.text_total);
        list_kas        = findViewById(R.id.list_kas);
        swipe_refresh   = findViewById(R.id.swipe_refresh);
        aruskas         = new ArrayList<>();

        sqliteHelper    = new SqliteHelper(this);

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                kasAdapter();
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        kasAdapter();
    }

    private void kasAdapter(){

        aruskas.clear(); list_kas.setAdapter(null);

        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        cursor = db.rawQuery(
                "SELECT *, strftime('%d/%m/%Y', tanggal) AS tgl FROM transaksi ORDER BY transaksi_id DESC "
                , null);
        cursor.moveToFirst();

        for (int i=0; i< cursor.getCount(); i++){
            cursor.moveToPosition(i);

            HashMap<String, String> map = new HashMap<>();
            map.put("transaksi_id", cursor.getString(0));
            map.put("status", cursor.getString(1));
            map.put("jumlah", cursor.getString(2));
            map.put("keterangan", cursor.getString(3));
            map.put("tanggal", cursor.getString(4));
            map.put("tanggal", cursor.getString(5));

            aruskas.add(map);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, aruskas, R.layout.list_kas,
                new String[]{"transaksi_id", "status", "jumlah","keterangan", "tanggal"},
                new int[]{R.id.text_transaksi_id, R.id.text_status, R.id.text_jumlah, R.id.text_keterangan, R.id.text_tanggal}
                );
        list_kas.setAdapter(simpleAdapter);
        list_kas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                transaksi_id = ((TextView)view.findViewById(R.id.text_transaksi_id)).getText().toString();
                Log.e("_transaksi_id", transaksi_id);

                ListMenu();
            }
        });

        KasTotal();

    }

    private void KasTotal(){
        NumberFormat rupiah = NumberFormat.getInstance(Locale.GERMAN);

        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        cursor = db.rawQuery(
                "SELECT SUM(jumlah) AS total," +
                        "(SELECT SUM(jumlah) FROM transaksi WHERE status='MASUK') AS masuk," +
                        "(SELECT SUM(jumlah) FROM transaksi WHERE status='KELUAR') AS keluar FROM transaksi"
                ,null);
        cursor.moveToFirst();

        text_masuk.setText(rupiah.format(cursor.getDouble(1)));
        text_keluar.setText(rupiah.format(cursor.getDouble(2)));
        text_total.setText(
                rupiah.format(cursor.getDouble(1) - cursor.getDouble(2))
        );

        swipe_refresh.setRefreshing(false);

    }

    private void ListMenu(){
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.list_menu);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView text_hapus = dialog.findViewById(R.id.text_hapus);
        TextView text_edit = dialog.findViewById(R.id.text_edit);

        text_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Hapus();
            }
        });

        text_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void Hapus(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
