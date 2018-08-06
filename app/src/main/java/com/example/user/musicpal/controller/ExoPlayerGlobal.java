package com.example.user.musicpal.controller;

import android.net.Uri;

import com.example.user.musicpal.model.pojo.Cancion;
import com.example.user.musicpal.utils.MyApplication;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;


public class ExoPlayerGlobal {
    private static ExoPlayerGlobal exoPlayerGlobal;
    private SimpleExoPlayer exoPlayer;
    private Boolean quieroQueInicie;
    private Cancion cancion;
    private List<Cancion> playList;
    private Integer posicionPlaylist;
    private DefaultBandwidthMeter bandwidthMeter;
    private DataSource.Factory dataSourceFactory;
    private MediaSource audioSource;
    private Uri uriAudio;
    private NotificadorQueTermino notificadorQueTermino;


    private ExoPlayerGlobal() {
        nuevoExoplayer();
        // Measures bandwidth during playback. Can be null if not required.
        bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        dataSourceFactory = new DefaultDataSourceFactory(MyApplication.getContext(),
                Util.getUserAgent(MyApplication.getContext(), "yourApplicationName"), bandwidthMeter);
        //url a uri
        uriAudio = Uri.parse("http://cdn-preview-3.deezer.com/stream/c-361a62705689675bcd00bcf1e2126684-22.mp3");
        // This is the MediaSource representing the media to be played.
        audioSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uriAudio);
        // Prepare the player with the source.
        exoPlayer.prepare(audioSource);
        exoPlayer.setPlayWhenReady(true);
        notificadorQueTermino = new NotificadorQueTermino() {
            @Override
            public void cambioCancion() {
                //para que no crashee
            }
        };
    }

    public static ExoPlayerGlobal getInstance() {
        if (exoPlayerGlobal == null) {
            exoPlayerGlobal = new ExoPlayerGlobal();
        }
        return exoPlayerGlobal;
    }

    private void nuevoExoplayer() {
        //exoplayer
        exoPlayer = ExoPlayerFactory.newSimpleInstance(MyApplication.getContext(), new DefaultTrackSelector());
    }

    private void playerListener() {

        exoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                onCompletionListener(playbackState);
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
    }

    private void onCompletionListener(int playbackState) {
        //este es el nuevo oncompletion
        int[] posicionNueva = {posicionPlaylist};
        if (playbackState == Player.STATE_ENDED) {
            if (!(posicionPlaylist + 1 > playList.size() - 1)) {

                posicionNueva[0] += 1;
                posicionPlaylist = posicionNueva[0];
                Cancion cancionSiguiente = playList.get(posicionNueva[0]);
                cancion = cancionSiguiente;
                notificadorQueTermino.cambioCancion();
                uriAudio = Uri.parse(cancion.getUrlPreview());
                // This is the MediaSource representing the media to be played.
                audioSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(uriAudio);
                // Prepare the player with the source.
                exoPlayer.prepare(audioSource);
                exoPlayer.setPlayWhenReady(true);

            }
        }
    }

    public void setearPlaylist(List<Cancion> playListAReproducir, Boolean quieroQueInicie,
                               final Integer posicion) {
        this.quieroQueInicie = quieroQueInicie;
        final Integer[] posicionNueva = {posicion};
        this.cancion = playListAReproducir.get(posicion);
        this.playList = playListAReproducir;
        posicionPlaylist = posicion;
        uriAudio = Uri.parse(cancion.getUrlPreview());
        // This is the MediaSource representing the media to be played.
        audioSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uriAudio);
        // Prepare the player with the source.
        exoPlayer.prepare(audioSource);
        exoPlayer.setPlayWhenReady(quieroQueInicie);
    }


    public interface NotificadorQueTermino {
        public void cambioCancion();
    }
}
