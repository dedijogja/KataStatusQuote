package com.sutinidevlab.aktifitas;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.startapp.android.publish.ads.banner.Banner;
import com.sutinidevlab.R;
import com.sutinidevlab.adapter.GaleriAdapter;
import com.sutinidevlab.enskripsi.drawable.DekripDrawable;
import com.sutinidevlab.enskripsi.konten.DekripKonten;
import com.sutinidevlab.enskripsi.konten.DekripsBg;
import com.sutinidevlab.help.KotakLayout;
import com.sutinidevlab.help.MuahNative;
import com.sutinidevlab.kore.App;
import com.sutinidevlab.statik.Tetap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class AktifitasGenerateQuote extends AppCompatActivity {

    private ImageView imageBG;
    private ImageView imageTTD;
    private String text;
    private Gallery gallery;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aktifitas_generate_quote);
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
        init();

        Glide.with(this)
                .load(DekripDrawable.dapatkanByteDrawable("back_icon.sdl", this))
                .asBitmap()
                .into(((ImageView) findViewById(R.id.btnBackGenerateCustomQuote)));

        App app = (App) getApplication();
        String status = app.getStatusIklan();

        if(status.equals(Tetap.BERHASIL_LOAD_IKLAN)){
            LinearLayout adContainer = findViewById(R.id.adsBannerGenerate);
            AdView adView = new AdView(this);
            adView.setAdSize(AdSize.SMART_BANNER);
            adView.setAdUnitId(new MuahNative(this).getAdBanner(this));
            adView.loadAd(new AdRequest.Builder().build());
            adContainer.addView(adView);
        }else{
            LinearLayout linearLayout = findViewById(R.id.adsBannerGenerate);
            Banner startAppBanner = new Banner(this);
            linearLayout.addView(startAppBanner);
        }

        text = Tetap.DEV_NAME;
        if(getIntent().getStringExtra(Tetap.KODE_INDEX_KONTEN) != null){
            text = Tetap.PENYIMPAN_SEMUA_KATA[Integer.parseInt(getIntent().getStringExtra(Tetap.KODE_INDEX_KONTEN))];
        }

        if(getIntent().getStringExtra(Tetap.KODE_PERINTAH_OPEN_MENU) != null ){
            ((com.github.clans.fab.FloatingActionMenu) findViewById(R.id.menu_red_generate)).open(true);
        }

        ((TextView)findViewById(R.id.textGenerateQuote)).setText(text);
        ((SeekBar) findViewById(R.id.seekBarGenerateQuote)).setProgress(25);
        ((SeekBar) findViewById(R.id.seekBarGenerateQuote)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ((TextView) findViewById(R.id.textGenerateQuote)).setTextSize(TypedValue.COMPLEX_UNIT_SP, (progress+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        gallery.setSpacing(20);
        final GaleriAdapter galleryImageAdapter= new GaleriAdapter(this);
        gallery.setAdapter(galleryImageAdapter);

        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Glide.with(context)
                        .load(DekripsBg.dapatkanBitmapBg(Tetap.PENYIMPAN_ALAMAT_BG[position], context))
                        .asBitmap()
                        .into(imageBG);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        urusanButton();
    }

    private void urusanButton(){
        findViewById(R.id.btnSimpanGambar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanGambar();
            }
        });

        findViewById(R.id.btnAturSebagai).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aturSebagai();
            }
        });

        findViewById(R.id.btnBagikanGambar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bagikanGambar();
            }
        });

        findViewById(R.id.btnUbahWarnaFont).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder
                        .with(context)
                        .setTitle("Pilih Warna")
                        .initialColor(16777215)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                ((TextView) findViewById(R.id.textGenerateQuote)).setTextColor(selectedColor);
                            }
                        })
                        .setPositiveButton("OK", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                ((TextView) findViewById(R.id.textGenerateQuote)).setTextColor(selectedColor);
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();
            }
        });

        findViewById(R.id.btnUbahText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Ubah Teks");

                final EditText input = new EditText(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setText(text);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ((TextView) findViewById(R.id.textGenerateQuote)).setText(input.getText().toString());
                                text = ((TextView) findViewById(R.id.textGenerateQuote)).getText().toString();
                            }
                        });

                alertDialog.setNegativeButton("BATAL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            }
        });

        findViewById(R.id.tombolKembali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init(){
        imageBG = findViewById(R.id.imageBG);
        imageTTD = findViewById(R.id.imageTTD);
        gallery = findViewById(R.id.galeri);
    }

    private Bitmap dapatkanBitmap(){
        KotakLayout view = findViewById(R.id.kotakLayout);
        view.setDrawingCacheEnabled(false);
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        imageTTD.setVisibility(View.GONE);
        return bitmap;
    }

    private void simpanGambar(){
        imageTTD.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/" + getString(R.string.app_name));
                myDir.mkdirs();
                Random generator = new Random();
                int n = 10000;
                n = generator.nextInt(n);
                String fname = getString(R.string.app_name) + "-" + n + ".jpg";
                File file = new File(myDir, fname);
                if (file.exists()) file.delete();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    dapatkanBitmap().compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();

                    MediaScannerConnection.scanFile(context, new String[]{
                                    file.getAbsolutePath()},
                            null, new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, Uri uri) {

                                }
                            });
                    Toast.makeText(context, "Gambar disimpan di folder " + getString(R.string.app_name), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Gambar gagal disimpan", Toast.LENGTH_SHORT).show();
                }
            }
        }, 1000);
    }

    private void aturSebagai(){
        imageTTD.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "title");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
                Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                OutputStream outstream;
                try {
                    outstream = context.getContentResolver().openOutputStream(uri);
                    dapatkanBitmap().compress(Bitmap.CompressFormat.PNG, 100, outstream);
                    outstream.close();
                } catch (Exception e) {
                    System.err.println(e.toString());
                }
                Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setDataAndType(uri, "image/*");
                intent.putExtra("mimeType", "image/*");
                context.startActivity(Intent.createChooser(intent, "Atur sebagai ..."));
            }

        }, 1000);

    }

    private void bagikanGambar(){
        imageTTD.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/" + getString(R.string.app_name));
                myDir.mkdirs();
                String fname = "Image-share" + ".jpg";
                File file = new File(myDir, fname);
                if (file.exists()) file.delete();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    dapatkanBitmap().compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Uri imageUri = Uri.parse("file://" + file);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/png");
                intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                context.startActivity(Intent.createChooser(intent, "Bagikan menggunakan..."));
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
