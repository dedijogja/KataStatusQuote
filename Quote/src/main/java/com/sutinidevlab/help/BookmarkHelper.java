package com.sutinidevlab.help;


import android.content.Context;
import android.preference.PreferenceManager;

import com.sutinidevlab.statik.Tetap;

import java.util.ArrayList;
import java.util.List;

public class BookmarkHelper {

    private static int kodeUnik = 9999;  //maksimum item yang bisa di bookmark

    public static boolean simpanKeFavorit(int index, Context context){
        if(dataBelumAda(index, context)) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("index_" + index, index).apply();
            return true;
        }else{
            return false;
        }
    }

    public static boolean hapusDariFavorit(int index, Context context){
        if(!dataBelumAda(index, context)) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().remove("index_" + index).apply();
            return true;
        }else{
            return false;
        }
    }

    public static boolean apakahSudahterbookmark(int index, Context context){
        return !dataBelumAda(index, context);
    }

    private static boolean dataBelumAda(int index, Context context){
        int hasil = PreferenceManager.getDefaultSharedPreferences(context).getInt("index_"+index, kodeUnik);
        return hasil == kodeUnik;
    }

    public static List<Integer> getDataFavorit(Context context){
        List<Integer> semuaDataFavorit = new ArrayList<>();
        for(int i = 0; i< Tetap.PENYIMPAN_SEMUA_KATA.length; i++) {
            int data = PreferenceManager.getDefaultSharedPreferences(context).getInt("index_"+i, kodeUnik);
            if(data != kodeUnik){
                semuaDataFavorit.add(data);
            }
        }
        return semuaDataFavorit;
    }
}
