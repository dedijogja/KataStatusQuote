package com.sutinidevlab.aktifitas;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.sutinidevlab.R;
import com.sutinidevlab.enskripsi.drawable.DekripDrawable;
import com.sutinidevlab.enskripsi.konten.DekripKonten;
import com.sutinidevlab.help.MuahNative;
import com.sutinidevlab.kore.App;
import com.sutinidevlab.statik.Tetap;

import java.io.IOException;

public class AktifitasMemuat extends AppCompatActivity {

    private boolean statusIklan = true;
    int hitung = 0;
    int loadIklanBerapaKali = 5;
    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aktifitas_memuat);

        try {
            Tetap.PENYIMPAN_ALAMAT_BG = Tetap.LIST_SEMUA_BG(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Glide.with(this)
                .load(DekripDrawable.dapatkanByteDrawable("logo.sdl", this))
                .asBitmap()
                .into(((ImageView) findViewById(R.id.imgSplahSlogo)));

        app = (App) getApplication();
        app.initInterstitial();
        app.loadIntersTitial();
        app.getInterstitial().setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                hitung++;
                Log.d("iklan", "gagal "+ String.valueOf(hitung));
                if(hitung<loadIklanBerapaKali){
                    if(statusIklan) {
                        app.loadIntersTitial();
                    }
                }
                if(hitung == loadIklanBerapaKali){
                    if(statusIklan) {
                        statusIklan = false;
                        app.setStatusIklan(Tetap.GAGAL_LOAD_IKLAN);
                        bukaActivity();
                    }
                }
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLoaded() {
                if(statusIklan) {
                    Log.d("iklan", "berhasil");
                    statusIklan = false;
                    app.setStatusIklan(Tetap.BERHASIL_LOAD_IKLAN);
                    bukaActivity();
                }
                super.onAdLoaded();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(statusIklan) {
                    statusIklan = false;
                    app.setStatusIklan(Tetap.GAGAL_LOAD_IKLAN);
                    bukaActivity();
                }
            }
        }, 15000);
    }



    private void bukaActivity(){
        DekripKonten dekripKonten = new DekripKonten(this, new MuahNative(this).getKeyAssets(this));
        dekripKonten.setListenerDecrypt(new DekripKonten.ListenerDecrypt() {
            @Override
            public void onSelesaiDecrypt(String[] hasil) {
                Tetap.PENYIMPAN_SEMUA_KATA = hasil;
                Intent intent = new Intent(AktifitasMemuat.this, UtamikanMenu.class);
                startActivity(intent);
                finish();
            }
        });
        try {
            String a = Tetap.LIST_SEMUA_KONTEN(this)[0];
            Tetap.PENYIMPAN_ALAMAT_KONTEN = a;
            dekripKonten.execute(a);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
