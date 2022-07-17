package com.sutinidevlab.aktifitas;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.startapp.android.publish.ads.banner.Banner;
import com.sutinidevlab.R;
import com.sutinidevlab.adapter.AllQuotesAdapter;
import com.sutinidevlab.enskripsi.drawable.DekripDrawable;
import com.sutinidevlab.enskripsi.konten.DekripKonten;
import com.sutinidevlab.help.DekorRecyclerView;
import com.sutinidevlab.help.MuahNative;
import com.sutinidevlab.kore.App;
import com.sutinidevlab.statik.Tetap;

import java.io.IOException;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class AktifitasAllQuotes extends AppCompatActivity {

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aktifitas_all_quotes);

        Glide.with(this)
                .load(DekripDrawable.dapatkanByteDrawable("back_icon.sdl", this))
                .asBitmap()
                .into(((ImageView) findViewById(R.id.btnBackIconAllQuote)));


        App app = (App) getApplication();
        String status = app.getStatusIklan();

        if(status.equals(Tetap.BERHASIL_LOAD_IKLAN)){
            LinearLayout adContainer = findViewById(R.id.adsBanner);
            AdView adView = new AdView(this);
            adView.setAdSize(AdSize.SMART_BANNER);
            adView.setAdUnitId(new MuahNative(this).getAdBanner(this));
            adView.loadAd(new AdRequest.Builder().build());
            adContainer.addView(adView);
        }else{
            LinearLayout linearLayout = findViewById(R.id.adsBanner);
            Banner startAppBanner = new Banner(this);
            linearLayout.addView(startAppBanner);
        }

        findViewById(R.id.tombolKembali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(getIntent().getStringExtra(Tetap.KODE_LOAD) == null) {
            RecyclerView recyclerView = findViewById(R.id.quotesRecycle);
            AllQuotesAdapter allQuotesAdapter = new AllQuotesAdapter(context, Tetap.PENYIMPAN_SEMUA_KATA);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new SlideInUpAnimator());
            recyclerView.addItemDecoration(new DekorRecyclerView(5, context));
            recyclerView.setAdapter(new ScaleInAnimationAdapter(allQuotesAdapter));
            allQuotesAdapter.notifyDataSetChanged();
        }else{
            try {
                Tetap.PENYIMPAN_ALAMAT_BG = Tetap.LIST_SEMUA_BG(this);
            } catch (IOException e) {
                e.printStackTrace();
            }

            DekripKonten dekripKonten = new DekripKonten(this, new MuahNative(this).getKeyAssets(this));
            dekripKonten.setListenerDecrypt(new DekripKonten.ListenerDecrypt() {
                @Override
                public void onSelesaiDecrypt(String[] hasil) {
                    Tetap.PENYIMPAN_SEMUA_KATA = hasil;
                    RecyclerView recyclerView = findViewById(R.id.quotesRecycle);
                    AllQuotesAdapter allQuotesAdapter = new AllQuotesAdapter(context, Tetap.PENYIMPAN_SEMUA_KATA);

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new SlideInUpAnimator());
                    recyclerView.addItemDecoration(new DekorRecyclerView(5, context));
                    recyclerView.setAdapter(new ScaleInAnimationAdapter(allQuotesAdapter));
                    allQuotesAdapter.notifyDataSetChanged();
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



    @Override
    public void onBackPressed() {
        finish();
    }
}
