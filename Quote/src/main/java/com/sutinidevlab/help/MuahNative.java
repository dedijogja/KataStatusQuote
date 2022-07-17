package com.sutinidevlab.help;


import android.content.Context;
import android.util.Log;

import com.sutinidevlab.enskripsi.text.deskriptor.DeskripsiText;

public class MuahNative {
    static {
        System.loadLibrary("native-lib");
    }

    public MuahNative(Context context){
        if(!context.getPackageName().equals(packageName(context))){
            throw new RuntimeException("Halah gagal meneh!");
        }
    }

    public String getKeyAssets(Context context) {
        return keyDesAssets(context);
    }

    public String getAdBanner(Context context) {
        Log.d("enskripsi getAdBanner", new DeskripsiText(keyDesText(context), adBanner(context)).dapatkanTextAsli());
        return new DeskripsiText(keyDesText(context), adBanner(context)).dapatkanTextAsli();
    }

    public String getAdInterstitial(Context context) {
        Log.d("enskripsi getAdInter", new DeskripsiText(keyDesText(context), adInterstitial(context)).dapatkanTextAsli());
        return new DeskripsiText(keyDesText(context), adInterstitial(context)).dapatkanTextAsli();
    }


    public String getStartAppId(Context context) {
        Log.d("enskripsi startAppId", new DeskripsiText(keyDesText(context), startAppId(context)).dapatkanTextAsli());
        return new DeskripsiText(keyDesText(context), startAppId(context)).dapatkanTextAsli();
    }

    public native String packageName(Context context);
    public native String keyDesText(Context context);
    public native String keyDesAssets(Context context);
    public native String adInterstitial(Context context);
    public native String startAppId(Context context);
    public native String adBanner(Context context);
}