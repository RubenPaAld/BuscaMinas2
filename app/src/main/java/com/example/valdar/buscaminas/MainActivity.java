package com.example.valdar.buscaminas;

import android.content.DialogInterface;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * Representa el aspecto visual del tablero
     */
    GridLayout gridLayout;

    /**
     * Cronometro que cuenta el tiempo que tardas en acabar una partida
     */
    Chronometer cronometro;

    /**
     * Minas que quedan por marcar por el usuario. Se actualiza cada vez que marcas una casilla
     */
    TextView txtMinas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.gridLayout = findViewById(R.id.gbTablero);
        this.cronometro = findViewById(R.id.chCronometro);
        this.txtMinas = findViewById(R.id.txtMinas);
        this.gridLayout.post(new Runnable() { //En tiempo de construccion de la actividad (onCreate, onStart, onResume) el layout no tiene las medidas
            @Override                           //establecias en el XML por lo que la opcion propuesta es generar un nuevo hilo que pueda actuar sobre
            public void run() {                 //la interfaz de usuario que se empiece a ejecutar tras acabar los tres primeros estados del ciclo de vida
                cargarEscenario();              //de la actividad.
            }
        });
    }

    /**
     * Genera una nueva partida reiniciando todos los componentes a su valor inicial: Actualiza los estados logicos de la clase Tablero. Vacia el layout
     * genera los nuevos botones y le añade los listeners.
     */
    public void cargarEscenario() {

        Tablero.setFinal(false);

        gridLayout.removeAllViews(); //removemos todos los botones del gridLayout ya que se va generar un nuevo tablero de juego
        Tablero.reiniciarVariables(); //inicializamos la logica del tableo

        actualizarMinas(0);  //

        gridLayout.setRowCount(Tablero.getRows()); //establecemos el numero de filas
        gridLayout.setColumnCount(Tablero.getCols()); //establecemos el numero de columnas

        for (int i = 0; i < Tablero.getCasillas(); i++) { //generamos todas las casillas

            ImageButton b = new ImageButton(MainActivity.this); //cada casilla es un ImageButton

            b.setLayoutParams(new LinearLayout.LayoutParams((gridLayout.getWidth()/Tablero.getCols()),  (gridLayout.getHeight()/Tablero.getRows()))); //establecemos el tamaño del boton en funcion del ancho y alto del dispositivo
            b.setBackground(getDrawable(R.drawable.button_border)); //inicializamos la casilla a fondo blanco con borde negro

            setBackgroundColor(b,R.color.gray); //El fondo lo ponemos gris

            b.setId(i); //establecemos el id
            b.setScaleType(ImageView.ScaleType.FIT_XY); //la imagen estara centrada al boton

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { //evento de pulsacion corta
                    clickCorto(v);
                }
            });

            b.setOnLongClickListener(new View.OnLongClickListener() { //evento de pulsacion larga
                @Override
                public boolean onLongClick(View v) { //evento de pulsacion larga
                    clickLargoCasilla(v);
                    return true;
                }
            });
            gridLayout.addView(b,i); //añadimos la casilla al tablero
        }
        cronometro.setBase(SystemClock.elapsedRealtime()); //reiniciamos el cronometro
        cronometro.start(); //Iniciamos el cronometro
    }

    /**
     * Cambia el color de fondo un ImagenView
     * @param i el ImagenView
     * @param color el id del color
     */
    public void setBackgroundColor(ImageView i, int color) {

        Drawable background = i.getBackground();

        if (background instanceof ShapeDrawable)
            ((ShapeDrawable) background).getPaint().setColor(ContextCompat.getColor(this, color));
        else if (background instanceof GradientDrawable)
            ((GradientDrawable) background).setColor(ContextCompat.getColor(this,color));
        else if (background instanceof ColorDrawable)
            ((ColorDrawable) background).setColor(ContextCompat.getColor(this, color));
    }

    /**
     * Realiza los cambios necesarios cuando se pulsa de forma prolongada una casilla del tablero
     * @param v El view de la casilla correspondiente
     */
    public void clickLargoCasilla(View v) {

        if (!Tablero.isFirstClick() && !Tablero.getFinal()) { //si no es el primer click  y no acabó la partida podemos usar el click largo

            int i =  v.getId() / Tablero.getCols(); //obtenemos la fila
            int j =  v.getId() % Tablero.getCols(); //obtenemos la columna

            switch (Tablero.getStatusCasilla(i,j)) {

                case minaMarcada:case marcada:      Tablero.setStatusCasilla(i,j,Tablero.getNextState(Tablero.getStatusCasilla(i,j))); //Si esta marcada pasa a estar en incognita
                                                    ((ImageButton)v).setImageResource(R.drawable.mark_question);
                                                    actualizarMinas(+1);
                                                    break;

                case incognta:case minaIncognita:   Tablero.setStatusCasilla(i,j,Tablero.getNextState(Tablero.getStatusCasilla(i,j))); //Si esta en incognita pasa a casilla vacia
                                                    ((ImageButton)v).setImageDrawable(null);
                                                    break;

                case vacio: case mina:              Tablero.setStatusCasilla(i,j,Tablero.getNextState(Tablero.getStatusCasilla(i,j))); //si esta vacia pasa a estar marcada
                                                    ((ImageButton)v).setImageResource(Tablero.getImagenMarcado());
                                                    actualizarMinas(-1);
                                                    break;
            }
        } else { //Si aun no se inicio el tablero o acabo la partida mostramos el mensaje correspondiente

            String mensaje;

            if (Tablero.isFirstClick())
                mensaje = getResources().getString(R.string.noInicio);
            else
                mensaje = getResources().getString(R.string.finalizado);

            Toast.makeText(MainActivity.this,mensaje,Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Actualiza el contandor de minas
     * @param n el nuebo numero de minas restantes
     */
    public void actualizarMinas(int n) {

        Tablero.minasRestante = Tablero.minasRestante + n;
        txtMinas.setText(String.format(getResources().getString(R.string.count_minas),Tablero.minasRestante));
    }

    /**
     * Realiza los cambios necesaios cuando se pulsa una casilla
     * @param v El View correspondiente a la casilla
     */
    public void clickCorto(View v) {

        int i =  v.getId() / Tablero.getCols(); //obtenemos la fila
        int j =  v.getId() % Tablero.getCols(); //obtenemos la columna

        if (Tablero.isFirstClick()) { //so es el primer click generamos la logica del tablero
            Tablero.iniciarTablero(i,j);
            /*for (int id = 0; id < gridLayout.getChildCount(); id++) { //Muestra la minas con fines de prueba

                int row = id / Tablero.getCols();
                int col = id % Tablero.getCols();

                if (Tablero.getStatusCasilla(row,col).equals(Casilla.mina)) {
                    ((ImageButton)gridLayout.getChildAt(id)).setImageResource(android.R.drawable.ic_dialog_map);
                }
            }*/
        }
        if (!Tablero.getFinal()) { //Si no acabó la partida
            switch (Tablero.getStatusCasilla(i,j)) {
                case mina:                  //Si habia una mina en la casilla finaliza el juego
                case minaIncognita:         destaparMinasYFinalizar(); //destapamos todas las minas y marcamos con una X las que se marcaron de forma incorrecta
                                            setBackgroundColor((ImageView) v,R.color.rojo); //La casilla destapada que pulsamos ahora se pone en rojo
                                            break;

                case vacio: case incognta:  actualizarCasilla(i,j); //si era una casilla que nunca se pulso o que tiene una ? ponemos el numero correspondiente
                                            break;

                case numero:                int minas = Tablero.getNumMinasCasillas(i,j); //si la casilla tiene un numero podemos destapar las casillas de alrededor si el numero de minas marcadas alrededor coincide con el numero de la casilla
                                            int marcas = Tablero.getMarksAround(i,j);
                                            if (minas == marcas)
                                                destaparCasilla(i,j);
            }
        }
    }

    /**
     * Destapa las casillas alrededor, en caso de haber una mina sin marcar finaliza la patida
     * @param i la posicion de la casilla en el eje x
     * @param j la posicion de la casilla en el eje y
     */
    public void destaparCasilla(int i, int j) {

        int minas = Tablero.getMinasSinMarcar(i,j); //obtenemos el numero de minas sin marcar

        if (minas > 0 ) //si hay alguna mina sin marcar se piede
            destaparMinasYFinalizar();
        else
            iniciarActualizarCasilla(i,j);
    }

    /**
     * Muestra en el tablero todas las minas del juego
     */
    public void destaparMinasYFinalizar() {
        for (int i = 0; i < gridLayout.getChildCount(); i++) { //mostramos el resto de minas

            int row = i / Tablero.getCols();
            int col = i % Tablero.getCols();

            ImageButton b = ((ImageButton) gridLayout.getChildAt(i));

            switch (Tablero.getStatusCasilla(row, col)) {
                case mina:case minaIncognita:   b.setImageResource(Tablero.getImagen()); //destapamos la imagen
                                                setBackgroundColor(b,R.color.gainsboro);
                                                break;
                case marcada:   b.setImageResource(Tablero.getImagenCross());
                                setBackgroundColor(b,R.color.gainsboro);
                                break;
            }
        }
        Tablero.setFinal(true);
        crearMensajeFinal(getResources().getString(R.string.mensajePerder)).show(); //mostramos el mensaje de final por ganar
        cronometro.stop(); //paramos el cronometro
    }

    /**
     * Destapa las casillas alrededor de la indicada por los parametros: Se usa cuando se destapan la casillas alrededer de una casilla con numero
     * @param i coordenada en el eje x de la casilla
     * @param j coordenada en el eje y de la casilla
     */
    public void iniciarActualizarCasilla (int i , int j) {

            try {   //Explora la casilla de la esquina superio izquierda
                switch (Tablero.getStatusCasilla(i-1,j-1)) {
                    case vacio: case incognta: actualizarCasilla(i-1,j-1);
                }
            }catch (IndexOutOfBoundsException ignored) {}

            try {   //Explora la casilla superior
                switch (Tablero.getStatusCasilla(i-1,j)) {
                    case vacio: case incognta: actualizarCasilla(i-1,j);
                }
            }catch (IndexOutOfBoundsException ignored) {}

            try {   //Exploa la casilla superior derecha
                switch (Tablero.getStatusCasilla(i-1,j+1)) {
                    case vacio: case incognta: actualizarCasilla(i-1,j+1);
                }
            }catch (IndexOutOfBoundsException ignored) {}

            try {   //Exploa la casilla de la izquierda
                switch (Tablero.getStatusCasilla(i,j-1)) {
                    case vacio: case incognta: actualizarCasilla(i,j-1);
                }
            }catch (IndexOutOfBoundsException ignored) {}

            try {   //Explora la casilla de la derecha
                switch (Tablero.getStatusCasilla(i,j+1)) {
                    case vacio: case incognta: actualizarCasilla(i,j+1);
                }
            }catch (IndexOutOfBoundsException ignored) {}

            try {   //Explora la casilla inferior izquierda
                switch (Tablero.getStatusCasilla(i+1,j-1)) {
                    case vacio: case incognta: actualizarCasilla(i+1,j-1);
                }
            }catch (IndexOutOfBoundsException ignored) {}

            try {   //Explora la casilla inferior
                switch (Tablero.getStatusCasilla(i+1,j)) {
                    case vacio: case incognta: actualizarCasilla(i+1,j);
                }
            }catch (IndexOutOfBoundsException ignored) {}

            try {   //Explora la casilla inferior derecha
                switch (Tablero.getStatusCasilla(i+1,j+1)) {
                    case vacio: case incognta: actualizarCasilla(i+1,j+1);
                }
            }catch (IndexOutOfBoundsException ignored) {}

    }

    /**
     * Actualiza el numero que tiene una casilla y explora a su alrededor destapando las que puede
     * @param i coordenada de la casilla en el eje x
     * @param j coordenada de la casilla en el eje y
     */
    public void actualizarCasilla(int i , int j) {

        int minas = Tablero.getMinasAround(i,j); //obtenemos el numero de minas alrededor de la casilla

        ImageView v = (ImageView) gridLayout.getChildAt((Tablero.getRows() * i) + j); //obtenemos el view de la casilla

        if (minas == 0) { //si el numero de minas es 0
            v.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.dimgray, null)); //Ponemos el fondo gris oscuro
            v.setImageDrawable(null); //quitamos la imagen
        } else { //si tiene alguna mina alrededor
            v.setImageResource(Tablero.getImagenNumero(minas)); //Ponemos la imagen del numero correspondiente
            setBackgroundColor(v,R.color.gainsboro); //ponemos de fondo un gris claro
        }

        Tablero.setStatusCasilla(i,j,Casilla.numero); //actualizamos el estado de la logica del tablero
        Tablero.setNumMinasCasillas(i,j,minas); //actualizamos la matriz que almacena el numero de minas qie hay en una casilla

        if (Tablero.isFinal()) { //Si al actualiza acaba hemos marcado todas las casillas con numero finalizamos la partida
            Tablero.setFinal(true);
            crearMensajeFinal(getResources().getString(R.string.mensajeGanar)).show();
            cronometro.stop();
        }else if (minas == 0) { //si en la casilla habia 0 minas vamos recorriendo recursivamente el tablero hasta que marquemos todas las casillas posibles

            try {   //Explora la casilla de la esquina superio izquierda
                switch (Tablero.getStatusCasilla(i-1,j-1)) {
                    case vacio: case incognta: actualizarCasilla(i-1,j-1);
                }
            }catch (IndexOutOfBoundsException ignored) {}

            try {   //Explora la casilla superior
                switch (Tablero.getStatusCasilla(i-1,j)) {
                    case vacio: case incognta: actualizarCasilla(i-1,j);
                }
            }catch (IndexOutOfBoundsException ignored) {}

            try {    //Exploa la casilla superior derecha
                switch (Tablero.getStatusCasilla(i-1,j+1)) {
                    case vacio: case incognta: actualizarCasilla(i-1,j+1);
                }
            }catch (IndexOutOfBoundsException ignored) {}

            try {   //Exploa la casilla de la izquierda
                switch (Tablero.getStatusCasilla(i,j-1)) {
                    case vacio: case incognta: actualizarCasilla(i,j-1);
                }
            }catch (IndexOutOfBoundsException ignored) {}

            try {    //Explora la casilla de la derecha
                switch (Tablero.getStatusCasilla(i,j+1)) {
                    case vacio: case incognta: actualizarCasilla(i,j+1);
                }
            }catch (IndexOutOfBoundsException ignored) {}

            try {   //Explora la casilla inferior izquierda
                switch (Tablero.getStatusCasilla(i+1,j-1)) {
                    case vacio: case incognta: actualizarCasilla(i+1,j-1);
                }
            }catch (IndexOutOfBoundsException ignored) {}

            try {   //Explora la casilla inferior
                switch (Tablero.getStatusCasilla(i+1,j)) {
                    case vacio: case incognta: actualizarCasilla(i+1,j);
                }
            }catch (IndexOutOfBoundsException ignored) {}

            try {   //Explora la casilla inferior derecha
                switch (Tablero.getStatusCasilla(i+1,j+1)) {
                    case vacio: case incognta: actualizarCasilla(i+1,j+1);
                }
            }catch (IndexOutOfBoundsException ignored) {}
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Selector de opciones del menu
     * @param item la posicion del menu empezando por 0
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.action_settings:  mostrarInstrucciones().show(); //Opcion que muestra las instruciones
                                        break;
            case R.id.action_settings2: cambiarDificultad().show(); //Opcion que muestra la dificultad de juego
                                        break;
                                        //opcion que muesta la seleccion de un nuevo personaje
            case R.id.action_settings3: List<Personaje> list = new ArrayList<>(Arrays.asList( //Lista de los posibles personajes
                                                                                    new Personaje(getString(R.string.pj1),R.drawable.mina),
                                                                                    new Personaje(getString(R.string.pj2), R.drawable.moon),
                                                                                    new Personaje(getString(R.string.pj3), R.drawable.fire)
                                                                               )
                                                                    );
                                        SelectPj sp = new SelectPj();
                                        sp.personajes = list;

                                        sp.show(getSupportFragmentManager(),"");
                                        break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Muestras las instrucciones del juego
     * @return un AlertDialog con las intrucciones del juego
     */
    public AlertDialog mostrarInstrucciones() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle(getResources().getString(R.string.tituloInstrucciones))
                .setMessage(getResources().getString(R.string.instrucciones))
                .setPositiveButton(getResources().getString(R.string.positiveButton),null);
        return builder.create();
    }

    /**
     * Muestra un mensaje que se usara cuando finaliza la partida
     * @param mensaje el texto del mensaje
     * @return el dialogo que se mostrara
     */
    public AlertDialog crearMensajeFinal(String mensaje) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage(mensaje)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.positiveButton),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cargarEscenario();
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.negativeButton),null);
        return builder.create();
    }
    /**
     * Muestra un dialogo en el que puedess cambiar la dificultad del juego
     * @return un AlertDialog con las dificultades disponibles
     */
    public AlertDialog cambiarDificultad() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle(getResources().getString(R.string.tituloSeleccionDificultad))
                .setItems(new String[]{getResources().getString(R.string.dificultad1),getResources().getString(R.string.dificultad2),getResources().getString(R.string.dificultad3)}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int dificultad) {

                        switch (dificultad) {

                            case Tablero.FACIl:     Tablero.dificultad = Tablero.FACIl;
                                                    break;
                            case Tablero.MEDIO:     Tablero.dificultad = Tablero.MEDIO;
                                                    break;
                            case Tablero.DIFICIL:   Tablero.dificultad = Tablero.DIFICIL;
                                                    break;
                        }
                        cargarEscenario();
                    }
                })
                .setPositiveButton(getResources().getString(R.string.negativeButton),null);
        return builder.create();
    }
}
