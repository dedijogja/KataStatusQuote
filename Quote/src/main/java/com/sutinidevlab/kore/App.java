package com.sutinidevlab.kore;


import android.app.Application;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.sutinidevlab.help.MuahNative;
import com.sutinidevlab.statik.Tetap;

public class App extends Application {

    public App(){

    }

    private InterstitialAd interstitial;

    public void setHitungFailed() {
        this.hitungFailed++;
    }

    public void setHitungFailed(int hitungFailed) {
        this.hitungFailed = hitungFailed;
    }

    int hitungFailed = 0;

    public int getHitungFailed() {
        return hitungFailed;
    }


    public String getStatusIklan() {
        return statusIklan;
    }

    private String statusIklan = Tetap.GAGAL_LOAD_IKLAN;

    public int getPenghitungStartApp() {
        return penghitungStartApp;
    }

    public void setPenghitungStartApp(int penghitungStartApp) {
        this.penghitungStartApp = penghitungStartApp;
    }

    private int penghitungStartApp = 0;

    public void setStatusIklan(String status){
        statusIklan = status;
    }

    public void initInterstitial(){
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(new MuahNative(this).getAdInterstitial(this));
    }


    public void loadIntersTitial(){
        Log.d("iklan", "load interstitial");
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitial.loadAd(adRequest);
    }

    public InterstitialAd getInterstitial(){
        return interstitial;
    }

    public boolean isBolehMenampilkanIklanWaktu() {
        return bolehMenampilkanIklanWaktu;
    }

    boolean bolehMenampilkanIklanWaktu = true;

    public boolean isBolehMenampilkanIklanHitung() {
        return bolehMenampilkanIklanHitung;
    }

    boolean bolehMenampilkanIklanHitung = true;
    public void tampilkanInterstitial(){
        if(bolehMenampilkanIklanWaktu && bolehMenampilkanIklanHitung) {
            if(interstitial.isLoaded()) {
                interstitial.show();
                setHitungFalse();
                manajemenWaktu();
            }else {
                loadIntersTitial();
            }
        }else{
            if(!bolehMenampilkanIklanHitung) {
                setHitungTrue();
            }
        }
    }
    private void manajemenWaktu(){
        bolehMenampilkanIklanWaktu =  false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("iklan", "waktu boleh");
                bolehMenampilkanIklanWaktu = true;
            }
        }, 90000);
    }

    public void setHitungFalse(){
        bolehMenampilkanIklanHitung = false;
    }

    public void setHitungTrue(){
        Log.d("iklan", "hitung boleh");
        bolehMenampilkanIklanHitung = true;
    }
}