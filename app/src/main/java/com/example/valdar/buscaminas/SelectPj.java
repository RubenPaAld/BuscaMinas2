package com.example.valdar.buscaminas;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectPj extends DialogFragment {

    /**
     * Lista con los iconos posibles
     */
    public List<Personaje> personajes;

    /**
     * Referencia al personaje seleccionado con el spinner
     */
    Personaje personaje;

    /**
     * Crea un dialogo personalizado inflando la vista de seleccion de personaje
     * @param savedInstanceState
     * @return El Dialogo inflado
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); //creamos el dialogo
        builder.setTitle(getResources().getString(R.string.tituloSeleccionPersonaje)); //cambiamos el titulo

        LayoutInflater inflater = getActivity().getLayoutInflater(); //creamos un inflador
        View view = inflater.inflate(R.layout.alert_selecion_personaje, null); //Creamos con el inflador una vista con el layout del seleccionador de personaje

        Spinner spinner = view.findViewById(R.id.spinnerP); //referenciamos el spinner del seleccionador de personaje

        AdapterPersonajes ap = new AdapterPersonajes(this.getContext(),R.layout.opcion_personaje,personajes); //creamos un adaptador con el layout de la vista de cada personaje
        spinner.setAdapter(ap); //añadimos el adaptador al spinner

        personaje  = personajes.get(0);

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                personaje = personajes.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        builder.setView(view)
                .setPositiveButton(getResources().getString(R.string.positiveButton), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Tablero.setImagen(personaje.getImagen());
                    }
                });

        return builder.create();
    }
}
