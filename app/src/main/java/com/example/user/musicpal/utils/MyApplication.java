package com.example.user.musicpal.utils;

import android.app.Application;

public class MyApplication extends Application {
    //esta clase sirve para obtener el contexto de la app desde una clase singleton
    // (como mediaplayerglobal)
    // para que funcione tiene que estar esto en el manifest:
    /*<application
    android:name=".MyApplication" <---- (en este caso  android:name=".utils.MyApplication")
    android:icon="@drawable/icon"
    android:label="@string/app_name" >*/

    private static MyApplication mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static MyApplication getContext() {
        return mContext;
    }
}