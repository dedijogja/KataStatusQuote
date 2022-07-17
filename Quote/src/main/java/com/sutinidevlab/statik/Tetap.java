package com.sutinidevlab.statik;


import android.content.Context;
import android.graphics.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tetap {
    public static final String DEV_NAME = "Sutini Dev Lab";

    public static final String NAMA_FOLDER_KONTEN = "kata";
    public static final String NAMA_FOLDER_BG = "bg";
    public static final String NAMA_FOLDER_DRAWABLE = "drawable";

    public static final String GAGAL_LOAD_IKLAN = "iklanGagalDiload";
    public static final String BERHASIL_LOAD_IKLAN = "iklanBerhasilDIload";
    public static final String KODE_INDEX_KONTEN = "posisi";
    public static final String KODE_PERINTAH_OPEN_MENU = "openmenu";
    public static final String KODE_LOAD = "loadsemua";

    public static String[] LIST_SEMUA_KONTEN(Context context) throws IOException {
        List<String> modelist = new ArrayList<>();
        String[] fileNames = context.getAssets().list(NAMA_FOLDER_KONTEN);
        Collections.addAll(modelist, fileNames);
        String[] dap = new String[modelist.size()];
        for(int i = 0; i<modelist.size(); i++){
            dap[i] = modelist.get(i);
        }
        return dap;
    }

    public static String[] LIST_SEMUA_BG(Context context) throws IOException {
        List<String> modelist = new ArrayList<>();
        String[] fileNames = context.getAssets().list(NAMA_FOLDER_BG);
        Collections.addAll(modelist, fileNames);
        String[] dap = new String[modelist.size()];
        for(int i = 0; i<modelist.size(); i++){
            dap[i] = modelist.get(i);
        }
        return dap;
    }

    public static String[] PENYIMPAN_SEMUA_KATA = null;

    public static String PENYIMPAN_ALAMAT_KONTEN = null;
    public static String[] PENYIMPAN_ALAMAT_BG = null;


    private static final int[] KOLEKSI_WARNA = {
            Color.parseColor("#EC7063"),
            Color.parseColor("#45B39D"),
            Color.parseColor("#A569BD"),
            Color.parseColor("#DC7633"),
            Color.parseColor("#CD6155"),
            Color.parseColor("#5D6D7E"),
            Color.parseColor("#AF7AC5"),
            Color.parseColor("#48C9B0"),
            Color.parseColor("#F5B041"),
            Color.parseColor("#5499C7"),
    };

    public static int GET_COLOR(int posisi){
        int[] newArray = rotateArray(posisi, KOLEKSI_WARNA);
        return newArray[0];
    }

    private static int[] rotateArray(int n, int[] data) {
        if(n < 0) {
            n = -n % data.length;
            n = data.length - n;
        }
        int[] result = new int[data.length];
        for(int i = 0; i < data.length; i++){
            result[(i+n) % data.length ] = data[i];
        }
        return result;
    }

}
