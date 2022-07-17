package com.sutinidevlab.aktifitas;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.startapp.android.publish.ads.banner.Banner;
import com.sutinidevlab.R;
import com.sutinidevlab.enskripsi.drawable.DekripDrawable;
import com.sutinidevlab.enskripsi.konten.DekripKonten;
import com.sutinidevlab.help.BookmarkHelper;
import com.sutinidevlab.help.MuahNative;
import com.sutinidevlab.kore.App;
import com.sutinidevlab.statik.Tetap;

import java.io.IOException;


public class AktifitasPembukaQuote extends AppCompatActivity {

    private int posisi = 0;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aktifitas_pembuka_quote);

        if(getIntent().getStringExtra(Tetap.KODE_LOAD) == null) {
            semuaUrusan();
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
                    semuaUrusan();
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

    private void semuaUrusan(){
        if(getIntent().getStringExtra(Tetap.KODE_INDEX_KONTEN) != null){
            posisi = Integer.parseInt(getIntent().getStringExtra(Tetap.KODE_INDEX_KONTEN));
        }

        if(getIntent().getStringExtra(Tetap.KODE_PERINTAH_OPEN_MENU) != null ){
            ((com.github.clans.fab.FloatingActionMenu) findViewById(R.id.menu_red)).open(true);
        }

        Glide.with(this)
                .load(DekripDrawable.dapatkanByteDrawable("back_icon.sdl", this))
                .asBitmap()
                .into(((ImageView) findViewById(R.id.btnBackPembukaQuote)));

        App app = (App) getApplication();
        String status = app.getStatusIklan();

        if(status.equals(Tetap.BERHASIL_LOAD_IKLAN)){
            AdView adView = new AdView(this);
            adView.setAdSize(AdSize.SMART_BANNER);
            adView.setAdUnitId(new MuahNative(this).getAdBanner(this));
            adView.loadAd(new AdRequest.Builder().build());
            ((LinearLayout) findViewById(R.id.adsPembuka)).addView(adView);
        }else{
            RelativeLayout.LayoutParams parameter = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            parameter.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            findViewById(R.id.adsPembuka).setLayoutParams(parameter);
            LinearLayout linearLayout = findViewById(R.id.adsPembuka);
            Banner startAppBanner = new Banner(this);
            linearLayout.addView(startAppBanner);
        }

        ((TextView) findViewById(R.id.textPembuka)).setText(Tetap.PENYIMPAN_SEMUA_KATA[posisi]);
        ((TextView) findViewById(R.id.textOpenQuotes)).setText("Quote Ke " + (posisi+1));

        ((SeekBar) findViewById(R.id.seekBarPembuka)).setProgress(25);
        ((SeekBar) findViewById(R.id.seekBarPembuka)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ((TextView) findViewById(R.id.textPembuka)).setTextSize(TypedValue.COMPLEX_UNIT_SP, (progress+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        findViewById(R.id.btnGenerateImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AktifitasGenerateQuote.class);
                intent.putExtra(Tetap.KODE_INDEX_KONTEN, String.valueOf(posisi));
                startActivity(intent);
            }
        });

        findViewById(R.id.btnCopyToClipBoard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("tercopy", Tetap.PENYIMPAN_SEMUA_KATA[posisi]);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(AktifitasPembukaQuote.this, "Teks dicopy ke Clip Board", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnShareText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody = Tetap.PENYIMPAN_SEMUA_KATA[posisi]
                        + "\n\n" +
                        "Dapatkan aplikasi " + getString(R.string.app_name)
                        + " secara gratis melalui link"
                        + " https://play.google.com/store/apps/details?id="
                        + getPackageName();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Kirim melalui..."));
            }
        });

        if(BookmarkHelper.apakahSudahterbookmark(posisi, context)){
            ((FloatingActionButton)findViewById(R.id.btnTambahkanKeBookmark)).setLabelText("Hapus dari Bookmark");
        }else{
            ((FloatingActionButton)findViewById(R.id.btnTambahkanKeBookmark)).setLabelText("Tambahkan Ke Bookmark");
        }

        findViewById(R.id.btnTambahkanKeBookmark).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BookmarkHelper.simpanKeFavorit(posisi, context)){
                    ((FloatingActionButton)findViewById(R.id.btnTambahkanKeBookmark)).setImageResource(R.drawable.hapus_bookmark);
                    ((FloatingActionButton)findViewById(R.id.btnTambahkanKeBookmark)).setLabelText("Hapus dari Bookmark");
                    Toast.makeText(context, "Berhasil menulis bookmark!", Toast.LENGTH_SHORT).show();
                }else if(BookmarkHelper.hapusDariFavorit(posisi, context)){
                    ((FloatingActionButton)findViewById(R.id.btnTambahkanKeBookmark)).setImageResource(R.drawable.tambah_bookmark);
                    ((FloatingActionButton)findViewById(R.id.btnTambahkanKeBookmark)).setLabelText("Tambahkan Ke Bookmark");
                    Toast.makeText(context, "Berhasil menghapus bookmark!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Terjadi kegagalan saat menulis/menghapus data bookmark!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.btnNex).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(posisi<(Tetap.PENYIMPAN_SEMUA_KATA.length-1)){
                    posisi++;
                    ((TextView) findViewById(R.id.textPembuka)).setText(Tetap.PENYIMPAN_SEMUA_KATA[posisi]);
                    ((TextView) findViewById(R.id.textOpenQuotes)).setText("Quote Ke " + (posisi+1));
                    if(BookmarkHelper.apakahSudahterbookmark(posisi, context)){
                        ((FloatingActionButton)findViewById(R.id.btnTambahkanKeBookmark)).setLabelText("Hapus dari Bookmark");
                    }else{
                        ((FloatingActionButton)findViewById(R.id.btnTambahkanKeBookmark)).setLabelText("Tambahkan Ke Bookmark");
                    }
                }else{
                    Toast.makeText(context, "Sudah berada diujung!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.btnPrev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(posisi>0){
                    posisi--;
                    ((TextView) findViewById(R.id.textPembuka)).setText(Tetap.PENYIMPAN_SEMUA_KATA[posisi]);
                    ((TextView) findViewById(R.id.textOpenQuotes)).setText("Quote Ke " + (posisi+1));
                    if(BookmarkHelper.apakahSudahterbookmark(posisi, context)){
                        ((FloatingActionButton)findViewById(R.id.btnTambahkanKeBookmark)).setLabelText("Hapus dari Bookmark");
                    }else{
                        ((FloatingActionButton)findViewById(R.id.btnTambahkanKeBookmark)).setLabelText("Tambahkan Ke Bookmark");
                    }
                }else{
                    Toast.makeText(context, "Sudah berada diujung!", Toast.LENGTH_SHORT).show();
                }
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
        Intent data = new Intent();
        Bundle bundle = new Bundle();
        bundle.putBoolean("kodeResult", true);
        data.putExtras(bundle);
        if (getParent() == null) {
            setResult(Activity.RESULT_OK, data);
        } else {
            getParent().setResult(Activity.RESULT_OK, data);
        }
        finish();
    }

}
