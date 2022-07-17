package com.sutinidevlab.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.sutinidevlab.R;
import com.sutinidevlab.aktifitas.AktifitasPembukaQuote;
import com.sutinidevlab.kore.App;
import com.sutinidevlab.statik.Tetap;

public class AllQuotesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private String[] allQuotes;

    public AllQuotesAdapter(Context context, String[] allQuotes) {
        this.context = context;
        this.allQuotes = allQuotes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewWrapper<>(new RecyPerItem(context));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holders, final int position) {
        final RecyPerItem holder = (RecyPerItem) holders.itemView;
        holder.tampunganList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                final App app = (App) activity.getApplication();
                String status = app.getStatusIklan();
                if(status.equals(Tetap.BERHASIL_LOAD_IKLAN)) {
                    if (!app.isBolehMenampilkanIklanHitung() || !app.isBolehMenampilkanIklanWaktu()
                            || !app.getInterstitial().isLoaded()) {
                        Intent intent = new Intent(context, AktifitasPembukaQuote.class);
                        intent.putExtra(Tetap.KODE_INDEX_KONTEN, String.valueOf(position));
                        context.startActivity(intent);
                    }
                    app.getInterstitial().setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            Intent intent = new Intent(context, AktifitasPembukaQuote.class);
                            intent.putExtra(Tetap.KODE_INDEX_KONTEN, String.valueOf(position));
                            context.startActivity(intent);

                            app.loadIntersTitial();
                            super.onAdClosed();
                        }
                        @Override
                        public void onAdFailedToLoad(int i) {
                            if(app.getHitungFailed() < 2 ) {
                                app.loadIntersTitial();
                                app.setHitungFailed();
                            }
                            super.onAdFailedToLoad(i);
                        }

                        @Override
                        public void onAdLoaded() {
                            app.setHitungFailed(0);
                            super.onAdLoaded();
                        }
                    });
                    app.tampilkanInterstitial();
                }else{
                    if(app.getPenghitungStartApp() == 0) {
                        Log.d("iklan", "startApp inters tampil " + app.getPenghitungStartApp());
                        app.setPenghitungStartApp(1);
                        Intent intent = new Intent(context, AktifitasPembukaQuote.class);
                        intent.putExtra(Tetap.KODE_INDEX_KONTEN, String.valueOf(position));
                        context.startActivity(intent);
                        StartAppAd.showAd(context);
                    }else{
                        Log.d("iklan", "startApp tidak tampil " + app.getPenghitungStartApp());
                        app.setPenghitungStartApp(0);
                        Intent intent = new Intent(context, AktifitasPembukaQuote.class);
                        intent.putExtra(Tetap.KODE_INDEX_KONTEN, String.valueOf(position));
                        context.startActivity(intent);
                    }
                }
            }
        });

        holder.textPerItem.setText(allQuotes[position]);
        holder.tampunganList.setBackgroundColor(Tetap.GET_COLOR(position));
    }

    @Override
    public int getItemCount() {
        return allQuotes.length;
    }


    class RecyPerItem extends FrameLayout {
        private RelativeLayout tampunganList;
        private TextView textPerItem;
        public RecyPerItem(Context context) {
            super(context);
            inflate(context, R.layout.item_quote, this);
            tampunganList = (RelativeLayout) findViewById(R.id.tampungan_list);
            textPerItem = (TextView) findViewById(R.id.textPerItem);
        }
    }
}
