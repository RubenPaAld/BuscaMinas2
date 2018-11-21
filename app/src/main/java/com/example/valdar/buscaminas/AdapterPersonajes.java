package com.example.valdar.buscaminas;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Adaptador que utilizara el spinner de seleccion de personaje
 */
public class AdapterPersonajes extends ArrayAdapter<String> {

    private Context context; //El contexto en el que se estara el spinner
    private List<Personaje> personajes; //Los personajes seleccionables

    public AdapterPersonajes(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.context = context;
        this.personajes = (List<Personaje>) objects;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return CrearFilaPersonalizada(position,convertView,parent);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        return CrearFilaPersonalizada(position,convertView,parent);
    }

    /**
     * Genera una fila del spinner en funcion de los personajes
     * @param position posicion de la fila en el desplegable del spinner
     * @param convertView
     * @param parent vista que contiene la fila
     * @return
     */
    public View CrearFilaPersonalizada(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View row = layoutInflater.inflate(R.layout.opcion_personaje,parent,false);

        TextView name = row.findViewById(R.id.nombreP);
        name.setText(personajes.get(position).getNombre());

        ImageView img = row.findViewById(R.id.src);

        img.setImageResource(personajes.get(position).getImagen());

        img.getLayoutParams().height = 100;
        img.getLayoutParams().width = img.getLayoutParams().height;

        return row;
    }
}
