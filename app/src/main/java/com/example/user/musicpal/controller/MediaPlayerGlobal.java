package com.example.user.musicpal.controller;

import android.media.AudioManager;
import android.media.MediaPlayer;

import com.example.user.musicpal.model.pojo.Album;
import com.example.user.musicpal.model.pojo.Artista;
import com.example.user.musicpal.model.pojo.Cancion;
import com.example.user.musicpal.view.FragmentReproductor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MediaPlayerGlobal implements MediaPlayer.OnPreparedListener {
    private static MediaPlayerGlobal mediaPlayerGlobal;
    private MediaPlayer mediaPlayer;
    private Cancion cancion;
    private List<Cancion> playList;
    private Integer posicionPlaylist;
    private NotificadorQueTermino notificadorQueTermino;
    private Boolean quieroQueInicie;

    private MediaPlayerGlobal() {
        posicionPlaylist = 0;
        Artista artista = new Artista("Linkin Park");
        cancion = new Cancion(
                "In my remains",
                "http://cdn-preview-3.deezer.com/stream/c-361a62705689675bcd00bcf1e2126684-22.mp3",
                artista, new Album("Living things",
                artista, "http://e-cdn-images.deezer.com/images/cover/0ce480e7723712dee352c68fdfef2599/250x250-000000-80-0-0.jpg"),
                "37734401");
        playList = new ArrayList<>();
        playList.add(cancion);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            setearPlaylist(playList, false, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
      /*  notificadorQueTermino = new NotificadorQueTermino() {
            @Override
            public void cambioCancion() {
                //esto esta solo para que no de null
            }
        };*/
    }

    public static MediaPlayerGlobal getInstance() {
        if (mediaPlayerGlobal == null) {
            mediaPlayerGlobal = new MediaPlayerGlobal();
        }
        return mediaPlayerGlobal;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setearPlaylist(List<Cancion> playListAReproducir, Boolean quieroQueInicie,
                               final Integer posicion) throws IOException {
        this.quieroQueInicie = quieroQueInicie;
        final Integer[] posicionNueva = {posicion};
        this.cancion = playListAReproducir.get(posicion);
        this.playList = playListAReproducir;
        posicionPlaylist = posicion;
        mediaPlayer.reset();
        mediaPlayer.setDataSource(cancion.getUrlPreview());
        mediaPlayer.prepareAsync();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (!(posicionPlaylist + 1 > playList.size() - 1)) {
                    posicionNueva[0] += 1;
                    posicionPlaylist = posicionNueva[0];
                    Cancion cancionSiguiente = playList.get(posicionNueva[0]);
                    cancion = cancionSiguiente;
                    try {
                        mediaPlayer.reset();
                        //notificadorQueTermino.cambioCancion();
                        mediaPlayer.setDataSource(cancionSiguiente.getUrlPreview());
                        mediaPlayer.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public List<Cancion> getPlayList() {
        return playList;
    }

    public Integer getPosicionPlaylist() {
        return posicionPlaylist;
    }

    public Cancion getCancion() {
        return cancion;
    }

    public void setCancion(Cancion cancion) {
        this.cancion = cancion;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        if (quieroQueInicie) {
            mediaPlayer.start();
        }

    }

    public interface NotificadorQueTermino {
        public void cambioCancion();
    }

}
