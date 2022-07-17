package com.sutinidevlab.aktifitas;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sutinidevlab.R;
import com.sutinidevlab.adapter.BookmarkedQuotesAdapter;
import com.sutinidevlab.enskripsi.drawable.DekripDrawable;
import com.sutinidevlab.help.BookmarkHelper;
import com.sutinidevlab.help.DekorRecyclerView;
import com.sutinidevlab.statik.Tetap;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;

public class AktifitasBookmarkedQuotes extends AppCompatActivity {

    private Context context = this;
    private BookmarkedQuotesAdapter bookmarkedQuotesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aktifitas_bookmarked_quotes);

        Glide.with(this)
                .load(DekripDrawable.dapatkanByteDrawable("back_icon.sdl", this))
                .asBitmap()
                .into(((ImageView) findViewById(R.id.btnBackBookmarkedQuote)));

        Glide.with(this)
                .load(DekripDrawable.dapatkanByteDrawable("refresh.sdl", this))
                .asBitmap()
                .into(((ImageView) findViewById(R.id.btnRefreshImage)));

        String[] semuaFavorit = new String[BookmarkHelper.getDataFavorit(context).size()];
        for(int i = 0; i<BookmarkHelper.getDataFavorit(context).size(); i++){
            semuaFavorit[i] = Tetap.PENYIMPAN_SEMUA_KATA[BookmarkHelper.getDataFavorit(context).get(i)];
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.quotesRecycle);
        bookmarkedQuotesAdapter = new BookmarkedQuotesAdapter(context, semuaFavorit);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new ScaleInAnimator());
        recyclerView.addItemDecoration(new DekorRecyclerView(5, context));
        recyclerView.setAdapter(new ScaleInAnimationAdapter(bookmarkedQuotesAdapter));

        findViewById(R.id.btnRefresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmarkedQuotesAdapter.aksiRefresh();
            }
        });

        findViewById(R.id.tombolKembali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bookmarkedQuotesAdapter.aksiRefresh();
    }

}
