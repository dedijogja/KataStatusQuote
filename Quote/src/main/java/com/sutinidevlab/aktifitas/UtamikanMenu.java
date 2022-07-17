package com.sutinidevlab.aktifitas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.NativeExpressAdView;
import com.startapp.android.publish.ads.splash.SplashConfig;
import com.startapp.android.publish.adsCommon.SDKAdPreferences;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;
import com.sutinidevlab.R;
import com.sutinidevlab.enskripsi.drawable.DekripDrawable;
import com.sutinidevlab.enskripsi.konten.DekripKonten;
import com.sutinidevlab.help.MuahNative;
import com.sutinidevlab.kore.App;
import com.sutinidevlab.statik.Tetap;

import java.io.IOException;

public class UtamikanMenu extends AppCompatActivity {

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Glide.with(this)
                .load(DekripDrawable.dapatkanByteDrawable("all_quotes.sdl", this))
                .asBitmap()
                .into(((ImageView) findViewById(R.id.imgAllQuote)));

        Glide.with(this)
                .load(DekripDrawable.dapatkanByteDrawable("bookmark.sdl", this))
                .asBitmap()
                .into(((ImageView) findViewById(R.id.imgBookmarkedQuote)));

        Glide.with(this)
                .load(DekripDrawable.dapatkanByteDrawable("generate.sdl", this))
                .asBitmap()
                .into(((ImageView) findViewById(R.id.imgGenerateQuote)));

        Glide.with(this)
                .load(DekripDrawable.dapatkanByteDrawable("rate.sdl", this))
                .asBitmap()
                .into(((ImageView) findViewById(R.id.imgRateAplication)));

        App app = (App) getApplication();
        String status = app.getStatusIklan();
        if(status.equals(Tetap.BERHASIL_LOAD_IKLAN)){
            AdView adView = new AdView(this);
            adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
            adView.setAdUnitId(new MuahNative(this).getAdBanner(this));
            adView.loadAd(new AdRequest.Builder().build());
            ((LinearLayout) findViewById(R.id.iklanMenuNative)).addView(adView);

        }else if(status.equals(Tetap.GAGAL_LOAD_IKLAN)){
            StartAppSDK.init(this, new MuahNative(this).getStartAppId(this),
                    new SDKAdPreferences()
                            .setAge(35)
                            .setGender(SDKAdPreferences.Gender.FEMALE), false);
            if (!isNetworkConnected()) {
                StartAppAd.disableSplash();
            }else{
                StartAppAd.showSplash(this, savedInstanceState,
                        new SplashConfig()
                                .setTheme(SplashConfig.Theme.USER_DEFINED)
                                .setCustomScreen(R.layout.activity_aktifitas_memuat)
                );
            }
        }

        if(status.equals(Tetap.GAGAL_LOAD_IKLAN)){
            ImageView imageView = new ImageView(this);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(500, 500);
            imageView.setLayoutParams(layoutParams);

            ((LinearLayout)findViewById(R.id.iklanMenuNative)).addView(imageView);
             Glide .with(context)
                .load(DekripDrawable.dapatkanByteDrawable("logo.sdl", context))
                .asBitmap()
                .into(imageView);
        }

        findViewById(R.id.btnAllQuotes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AktifitasAllQuotes.class));
            }
        });

        findViewById(R.id.btnGenerateQuotes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AktifitasGenerateQuote.class));
            }
        });

        findViewById(R.id.btnBookmarkedQuotes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AktifitasBookmarkedQuotes.class));
            }
        });

        findViewById(R.id.btnRateAplication).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=" + getApplicationContext().getPackageName()));
                    startActivity(intent);
                }catch (Exception e){
                    Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://play.google.com/store/apps/details?id="+ getApplicationContext().getPackageName()));
                    startActivity(i);
                }
            }
        });
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Konfirmasi");
        alertDialog.setMessage("Yakin ingin menutup aplikasi?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YA",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "BATAL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

}
