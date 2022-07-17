package com.sutinidevlab.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sutinidevlab.R;
import com.sutinidevlab.enskripsi.konten.DekripsBg;
import com.sutinidevlab.statik.Tetap;

public class GaleriAdapter extends BaseAdapter
{
    private Context mContext;


    public GaleriAdapter(Context context)
    {
        mContext = context;
    }

    public int getCount() {
        return Tetap.PENYIMPAN_ALAMAT_BG.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    public View getView(int index, View view, ViewGroup viewGroup) {
        ImageView i = new ImageView(mContext);
        i.setLayoutParams(new Gallery.LayoutParams(200, 200));
        i.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(mContext)
                .load(DekripsBg.dapatkanBitmapBg(Tetap.PENYIMPAN_ALAMAT_BG[index], mContext))
                .asBitmap()
                .into(i);
        return i;
    }


}
