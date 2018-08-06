package com.example.user.musicpal.model.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.musicpal.R;
import com.example.user.musicpal.controller.ControllerGlobal;
import com.example.user.musicpal.controller.ExoPlayerGlobal;
import com.example.user.musicpal.controller.MediaPlayerGlobal;
import com.example.user.musicpal.model.pojo.Cancion;
import com.example.user.musicpal.utils.ResultListener;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdapterCanciones extends RecyclerView.Adapter {

    private List<Cancion> listaDeCanciones;
    private FragmentManager fragmentManager;
    private Cancion cancion;
    private NotificadorCancionCelda notificadorCancionCelda;
    private Context context;
    private boolean esDeFavoritos;

    public AdapterCanciones(List<Cancion> listaDeCanciones,
                            FragmentManager fragmentManager,
                            NotificadorCancionCelda notificadorCancionCelda,
                            Context context, boolean esDeFavoritos) {

        this.context = context;
        this.listaDeCanciones = listaDeCanciones;
        this.fragmentManager = fragmentManager;
        this.notificadorCancionCelda = notificadorCancionCelda;
        this.esDeFavoritos = esDeFavoritos;

    }

    public AdapterCanciones(FragmentManager fragmentManager,
                            NotificadorCancionCelda notificadorCancionCelda,
                            Context context, boolean esDeFavoritos) {

        listaDeCanciones = new ArrayList<>();
        this.fragmentManager = fragmentManager;
        this.notificadorCancionCelda = notificadorCancionCelda;
        this.context = context;
        this.esDeFavoritos = esDeFavoritos;
    }

    public List<Cancion> getListaDeCanciones() {
        return listaDeCanciones;
    }

    public void setListaDeCanciones(List<Cancion> listaDeCanciones) {
        this.listaDeCanciones = listaDeCanciones;
        notifyDataSetChanged();
    }

    public void agregarPlaylist(Cancion cancionNueva) {
        if (listaDeCanciones.contains(cancionNueva)) {
            return;
        }
        listaDeCanciones.add(cancionNueva);
        notifyItemChanged(listaDeCanciones.size() - 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.celda_recycler_cancion, parent, false);
        if (esDeFavoritos) {
            ViewHolderFavoritos viewHolderFavoritos = new ViewHolderFavoritos(view);
            return viewHolderFavoritos;
        } else {
            ViewHolderCancion viewHolderCancion = new ViewHolderCancion(view);
            return viewHolderCancion;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        cancion = listaDeCanciones.get(position);
        if (esDeFavoritos) {
            ViewHolderFavoritos viewHolderFavoritos = (ViewHolderFavoritos) holder;
            viewHolderFavoritos.cargarCancion(cancion);
        } else {
            ViewHolderCancion viewHolderCancion = (ViewHolderCancion) holder;
            viewHolderCancion.cargarCancion(cancion);
        }
    }

    @Override
    public int getItemCount() {
        if (listaDeCanciones != null) {
            return listaDeCanciones.size();
        } else {
            return 0;
        }
    }

    public void eliminarPorCambioEnlaFDB(Cancion resultado) {
        if (resultado == null) {
            return;
        }
        listaDeCanciones.remove(resultado);
        notifyDataSetChanged();
    }

    private class ViewHolderFavoritos extends RecyclerView.ViewHolder {
        private TextView textViewNombre;
        private ImageView imagenPlay;
        private ImageView imagenFav;

        public ViewHolderFavoritos(View itemView) {
            super(itemView);
            this.textViewNombre = itemView.findViewById(R.id.nombre_canciones_id);
            this.imagenPlay = itemView.findViewById(R.id.play_chico);
            imagenFav = itemView.findViewById(R.id.fav_icon);
            imagenFav.setBackgroundResource(R.drawable.ic_star);

            textViewNombre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Cancion cancionAreproducir = listaDeCanciones.get(getAdapterPosition());
                    ExoPlayerGlobal exoPlayerGlobal = ExoPlayerGlobal.getInstance();
                    exoPlayerGlobal.setearPlaylist(listaDeCanciones, true, getAdapterPosition());
                    /*try {
                        MediaPlayerGlobal mediaPlayerGlobal = MediaPlayerGlobal.getInstance();
                        mediaPlayerGlobal.setearPlaylist(listaDeCanciones, true, getAdapterPosition());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    notificadorCancionCelda.notificarCeldaClikeada(cancionAreproducir);
                }
            });
            imagenPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Cancion cancionAreproducir = listaDeCanciones.get(getAdapterPosition());
                    ExoPlayerGlobal exoPlayerGlobal = ExoPlayerGlobal.getInstance();
                    exoPlayerGlobal.setearPlaylist(listaDeCanciones, true, getAdapterPosition());
                    /*try {
                        MediaPlayerGlobal mediaPlayerGlobal = MediaPlayerGlobal.getInstance();
                        mediaPlayerGlobal.setearPlaylist(listaDeCanciones, true, getAdapterPosition());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    notificadorCancionCelda.notificarCeldaClikeada(cancionAreproducir);
                }
            });
            imagenFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Cancion cancion = listaDeCanciones.get(getAdapterPosition());
                    notificadorCancionCelda.notificarFavorito(cancion);
                    listaDeCanciones.remove(cancion);
                    notifyDataSetChanged();
                }
            });
        }

        public void cargarCancion(Cancion cancion) {
            textViewNombre.setText(cancion.getTitle());
        }
    }

    private class ViewHolderCancion extends RecyclerView.ViewHolder {
        private ControllerGlobal controllerGlobal;
        private TextView textViewNombre;
        private ImageView imagenPlay;
        private ImageView imagenFav;
        private List<String> listaRecibida;
        private Boolean favoriteada;

        public ViewHolderCancion(final View itemView) {
            super(itemView);
            listaRecibida = new ArrayList<>();
            this.textViewNombre = itemView.findViewById(R.id.nombre_canciones_id);
            this.imagenPlay = itemView.findViewById(R.id.play_chico);
            this.imagenFav = itemView.findViewById(R.id.fav_icon);
            favoriteada = false;
            controllerGlobal = new ControllerGlobal(context);

            textViewNombre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Cancion cancionAreproducir = listaDeCanciones.get(getAdapterPosition());
                    try {
                        MediaPlayerGlobal mediaPlayerGlobal = MediaPlayerGlobal.getInstance();
                        mediaPlayerGlobal.setearPlaylist(listaDeCanciones, true, getAdapterPosition());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    notificadorCancionCelda.notificarCeldaClikeada(cancionAreproducir);
                }
            });
            imagenPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Cancion cancionAreproducir = listaDeCanciones.get(getAdapterPosition());
                    try {
                        MediaPlayerGlobal mediaPlayerGlobal = MediaPlayerGlobal.getInstance();
                        mediaPlayerGlobal.setearPlaylist(listaDeCanciones, true, getAdapterPosition());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    notificadorCancionCelda.notificarCeldaClikeada(cancionAreproducir);
                }
            });

            imagenFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        if (favoriteada) {
                            imagenFav.setBackgroundResource(R.drawable.ic_star_border);
                            favoriteada = false;
                        } else {
                            imagenFav.setBackgroundResource(R.drawable.ic_star);
                            favoriteada = true;
                        }
                        Cancion cancion = listaDeCanciones.get(getAdapterPosition());
                        notificadorCancionCelda.notificarFavorito(cancion);
                    }else {
                        Toast.makeText(context, "logueate y disfruta de tus favoritos!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        public void cargarCancion(Cancion cancion) {
            textViewNombre.setText(cancion.getTitle());
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                controllerGlobal.obtenerFavPorID(cancion, new ResultListener<Cancion>() {
                    @Override
                    public void finish(Cancion resultado) {
                        if (resultado == null) {
                            favoriteada = false;
                            imagenFav.setBackgroundResource(R.drawable.ic_star_border);
                        } else {
                            favoriteada = true;
                            imagenFav.setBackgroundResource(R.drawable.ic_star);
                        }
                    }
                });
            }
        }


        private void agregarAFB() {
            Cancion cancion = listaDeCanciones.get(getAdapterPosition());
            controllerGlobal.pushearOeliminarCancion(cancion);
        }

    }

    public interface NotificadorCancionCelda {
        public void notificarCeldaClikeada(Cancion cancion);

        public void notificarFavorito(Cancion cancion);
    }
}
