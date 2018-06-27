package com.example.user.musicpal.model.dao;

import com.example.user.musicpal.model.pojo.Cancion;
import com.example.user.musicpal.model.pojo.ContenedorDeCanciones;
import com.example.user.musicpal.utils.ResultListener;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DaoCancion {
    private Retrofit retrofit;
    private Service service;

    public DaoCancion() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder retroBuilder = new Retrofit.Builder()
                .baseUrl("https://api.deezer.com/")
                .addConverterFactory(GsonConverterFactory.create());
        retrofit = retroBuilder.client(httpClient.build()).build();
        service = retrofit.create(Service.class);
    }

    public void obtenerCancionesPorAlbum(final ResultListener<List<Cancion>> resultListenerDelController, int id) {
        Call<ContenedorDeCanciones> call = service.obtenerCancionesPorAlbumConId(id);
        call.enqueue(new Callback<ContenedorDeCanciones>() {
            @Override
            public void onResponse(Call<ContenedorDeCanciones> call, Response<ContenedorDeCanciones> response) {
                ContenedorDeCanciones contenedorObtenido = response.body();

                if (contenedorObtenido != null && contenedorObtenido.getCanciones() != null) {
                    List<Cancion> listaCancion = contenedorObtenido.getCanciones();
                    resultListenerDelController.finish(listaCancion);
                } else {
                    resultListenerDelController.finish(new ArrayList<Cancion>());
                }
            }

            @Override
            public void onFailure(Call<ContenedorDeCanciones> call, Throwable t) {
                resultListenerDelController.finish(new ArrayList<Cancion>());
            }
        });
    }

    public void obtenerCancionesPorArtista(final ResultListener<List<Cancion>> resultListenerDelController, int id) {
        Call<ContenedorDeCanciones> call = service.obtenerCancionesPorArtistaConId(id);
        call.enqueue(new Callback<ContenedorDeCanciones>() {
            @Override
            public void onResponse(Call<ContenedorDeCanciones> call, Response<ContenedorDeCanciones> response) {
                ContenedorDeCanciones contenedorObtenido = response.body();

                if (contenedorObtenido != null && contenedorObtenido.getCanciones() != null) {
                    List<Cancion> listaCancion = contenedorObtenido.getCanciones();
                    resultListenerDelController.finish(listaCancion);
                } else {
                    resultListenerDelController.finish(new ArrayList<Cancion>());
                }
            }

            @Override
            public void onFailure(Call<ContenedorDeCanciones> call, Throwable t) {
                resultListenerDelController.finish(new ArrayList<Cancion>());
            }
        });
    }


}
