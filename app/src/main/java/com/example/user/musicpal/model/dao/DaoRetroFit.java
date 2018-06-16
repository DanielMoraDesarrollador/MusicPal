package com.example.user.musicpal.model.dao;

import com.example.user.musicpal.model.pojo.Album;
import com.example.user.musicpal.model.pojo.ContenedorAlbum;
import com.example.user.musicpal.utils.ResultListener;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DaoRetroFit {
    private Retrofit retrofit;
    private Service service;

    public DaoRetroFit() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder retroBuilder = new Retrofit.Builder()
                .baseUrl("https://api.deezer.com/")
                .addConverterFactory(GsonConverterFactory.create());
        retrofit = retroBuilder.client(httpClient.build()).build();
        service = retrofit.create(Service.class);
    }


    public void obtenerAlbunes(final ResultListener<List<Album>> resultListenerDelController) {
        service.obtenerAlbunes().enqueue(new Callback<ContenedorAlbum>() {
            @Override
            public void onResponse(Call<ContenedorAlbum> call, Response<ContenedorAlbum> response) {
                ContenedorAlbum contenedorAlbumObtenido = response.body();
                if (contenedorAlbumObtenido != null && contenedorAlbumObtenido.getAlbumList() != null) {
                    List<Album> albumLista = contenedorAlbumObtenido.getAlbumList();
                    resultListenerDelController.finish(albumLista);
                } else {
                    resultListenerDelController.finish(new ArrayList<Album>());
                }
            }

            @Override
            public void onFailure(Call<ContenedorAlbum> call, Throwable t) {
                resultListenerDelController.finish(new ArrayList<Album>());
            }
        });
    }
}
