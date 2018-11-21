package com.example.valdar.buscaminas;

import java.util.Random;

public class Tablero {

    public final static int NUM_MINAS_PRINCIPIANTE = 10; //Numero de minas que habrá en la dificultad principiante
    public final static int NUM_MINAS_INTERMEDIO = 30;  //Numero de minas que habrá en la dificultad intermedia
    public final static int NUM_MINAS_AVANZADO = 60;    //Numero de minas que habra en la dificultad avanzada

    private static boolean firstClick = true; //Si se ha hecho el primer click en el juego
    private static boolean fin = false; //si acabo el juego

    public static final int FACIl = 0;  //Representa la dificultad facil
    public static final int MEDIO = 1;  //Representa la dificultad media
    public static final int DIFICIL = 2;    //Representa la dificultad dificil

    public static int dificultad = FACIl; //Alamacena la dificultad actual del juego, inicialmente sera finla
    public static int minasRestante = getNumMinas(); //Alacena las minas que quedan por marcar, inicialmente son todas las minas que hay en el tablero

    private static int imagen = R.drawable.mina; //Imagen de la "mina"

    public static final int ROWS_D_FACIL = 8;   //Numero de filas de la dificultad principiante
    public static final int COLS_D_FACIL = 8;   //Numero de columnas de la dificultad principiante

    public static final int ROWS_D_MEDIO = 12;  //Numero de filas de la dificultad intermedia
    public static final int COLS_D_MEDIO = 12;  //Numero de columnas de la dificultad intermedia

    public static final int ROWS_D_DIFICIL = 16;    //Numero de filas de la dificultad avanzada
    public static final int COLS_D_DIFICIL = 16;    //Numero de columnas de la dificultad avanzada

    private static Casilla[][] tablero; //Matiz que almacena el estado de las casillas
    private static int[][] nMinasTablero;   //Matriz que almacena el numero de minas que tiene alrededor cada casilla

    /**
     * Devuelve si es el primer click o no
     * @return si es el primer click o no
     */
    public static boolean isFirstClick() {
        return firstClick;
    }

    /**
     * Inicia el tablero de juego en funciond de la dificultad, se le pasa una casilla inicial la cual no puede contener mina
     * @param row la fila de la casilla de inicio
     * @param col la columna de la casilla de inicio
     */
    public static void iniciarTablero(int row, int col) {

        tablero = new Casilla[getRows()][getCols()];
        nMinasTablero = new int[getRows()][getCols()];

        for (int i = 0; i < tablero.length; i++) {  //recorremos el tablero iniciando las casillas en vacio
            for (int j = 0; j < tablero[i].length; j++) {
                tablero[i][j] = Casilla.vacio;
            }
        }
        Random r = new Random(); //Añadimos minas de forma aleatoria

        int minas = 0;
        int numMinas = getNumMinas();

        while (minas < numMinas) { //mientras no se hayan puesto todas las minas

            int i = r.nextInt(getRows());
            int j = r.nextInt(getRows());

            if ( !(i == row && j == col) && tablero[i][j].equals(Casilla.vacio)) { //una mina no puede estar en una casilla que sea la de inicio o que ya tenga una mina
                tablero[i][j] = Casilla.mina;
                minas++;
            }
        }
        firstClick = false; //El prmer click ya se hizo
    }

    /**
     * Devuelve el numero de minas en funcion de la dificultad del juego
     * @return numero de minas
     */
    public static int getNumMinas() {

        switch(dificultad) {

            case FACIl: return NUM_MINAS_PRINCIPIANTE;
            case MEDIO: return NUM_MINAS_INTERMEDIO;
            case DIFICIL: return NUM_MINAS_AVANZADO;
            default: return -1;
        }
    }

    /**
     * Devuelve el numero de filas del tablero en funcion de la dificultad
     * @return el numero de filas
     */
    public static int getRows () {

        switch (dificultad) {

            case FACIl: return ROWS_D_FACIL;
            case MEDIO: return ROWS_D_MEDIO;
            case DIFICIL: return ROWS_D_DIFICIL;
            default: return -1;
        }
    }

    /**
     * Devuelve el numero de columnas del tablero en funcion de la dificultad
     * @return el numero de columnas
     */
    public static int getCols () {

        switch (dificultad) {

            case FACIl: return COLS_D_FACIL;
            case MEDIO: return COLS_D_MEDIO;
            case DIFICIL: return COLS_D_DIFICIL;
            default: return -1;
        }
    }

    /**
     * Devuelve el numero de casillas del tablero en funcion de la dificultad
     * @return el numero de casillas
     */
    public static int getCasillas() {

        switch (dificultad) {
            case FACIl: return ROWS_D_FACIL * COLS_D_FACIL;
            case MEDIO: return ROWS_D_MEDIO * COLS_D_MEDIO;
            case DIFICIL: return ROWS_D_DIFICIL * COLS_D_DIFICIL;
            default: return -1;
        }
    }

    /**
     * Siguiente estado que la casilla tendria que tener despues de hacer un click
     * @param c La casilla
     * @return El estado siguiente
     */
    public static Casilla getNextState(Casilla c) {

        switch (c) {

            case minaMarcada: return Casilla.minaIncognita;
            case minaIncognita: return Casilla.mina;
            case marcada: return Casilla.incognta;
            case incognta: return Casilla.vacio;
            case mina: return Casilla.minaMarcada;
            case vacio: return Casilla.marcada;
            default: return Casilla.vacio;
        }
    }

    /**
     * Devuelve el estado de una casilla
     * @param row la fila de la casilla
     * @param col la columna de la casilla
     * @return el estado de la casilla
     */
    public static Casilla getStatusCasilla(int row, int col) {
        return tablero[row][col];
    }

    /**
     * Cambia el estado de una casilla
     * @param row la fila de la casilla
     * @param col la columna de la casilla
     * @param casilla el nuevo estado de la casilla
     */
    public static void setStatusCasilla(int row, int col, Casilla casilla) {
        tablero[row][col] = casilla;
    }

    /**
     * Obtiene la imagen de marcado
     * @return la imagen de marcado
     */
    public static int getImagen() {
        return imagen;
    }

    /**
     * Obtiene la imagen de marcado
     * @return la imagen de marcado
     */
    public static int getImagenCross() {

        switch (imagen) {

            case R.drawable.mina: return R.drawable.mina_cross;
            case R.drawable.moon: return R.drawable.moon_cross;
            case R.drawable.fire: return R.drawable.fire_cross;
            default: return -1;
        }
    }

    public static int getImagenMarcado() {
        return R.drawable.mark;
    }

    /**
     * Cambia la imagen de marcado
     * @param i el valor de la nueva imagen
     */
    public static void setImagen(int i) {
        imagen = i;
    }

    public static int getImagenNumero(int n) {

        switch (n) {

            case 1: return R.drawable.uno;
            case 2: return R.drawable.dos;
            case 3: return R.drawable.tres;
            case 4: return R.drawable.cuatro;
            case 5: return R.drawable. cinco;
            case 6: return R.drawable.seis;
            default: return -1;
        }
    }

    /**
     * Devuelve las minas que hay alrededor de una casilla
     * @param row la fila de la casilla
     * @param col la columna de la casilla
     * @return
     */
    public static int getMinasAround(int row, int col) {

        int minas = 0;

        try {
            switch (tablero[row-1][col-1]) {
                case mina:case minaIncognita:case minaMarcada: minas++;
            }
        } catch (IndexOutOfBoundsException ignored) {}

        try {
            switch (tablero[row-1][col]) {
                case mina:case minaIncognita:case minaMarcada: minas++;
            }
        } catch (IndexOutOfBoundsException ignored) {}

        try {
            switch (tablero[row-1][col+1]) {
                case mina:case minaIncognita:case minaMarcada: minas++;
            }
        } catch (IndexOutOfBoundsException ignored) {}

        try {
            switch (tablero[row][col-1]) {
                case mina:case minaIncognita:case minaMarcada: minas++;
            }
        } catch (IndexOutOfBoundsException ignored) {}

        try {
            switch (tablero[row][col+1]) {
                case mina:case minaIncognita:case minaMarcada: minas++;
            }
        } catch (IndexOutOfBoundsException ignored) {}

        try {
            switch (tablero[row+1][col-1]) {
                case mina:case minaIncognita:case minaMarcada: minas++;
            }
        } catch (IndexOutOfBoundsException ignored) {}

        try {
            switch (tablero[row+1][col]) {
                case mina:case minaIncognita:case minaMarcada: minas++;
            }
        } catch (IndexOutOfBoundsException ignored) {}

        try {
            switch (tablero[row+1][col+1]) {
                case mina:case minaIncognita:case minaMarcada: minas++;
            }
        } catch (IndexOutOfBoundsException ignored) {}

        return minas;
    }

    /**
     * Devuelve las minas que hay alrededor de una casilla que no esten marcadas
     * @param row
     * @param col
     * @return
     */
    public static int getMinasSinMarcar(int row,int col) {

        int minas = 0;

        try {
            switch (tablero[row-1][col-1]) {
                case mina:case minaIncognita: minas++;
            }
        } catch (IndexOutOfBoundsException ignored) {}

        try {
            switch (tablero[row-1][col]) {
                case mina:case minaIncognita: minas++;
            }
        } catch (IndexOutOfBoundsException ignored) {}

        try {
            switch (tablero[row-1][col+1]) {
                case mina:case minaIncognita: minas++;
            }
        } catch (IndexOutOfBoundsException ignored) {}

        try {
            switch (tablero[row][col-1]) {
                case mina:case minaIncognita: minas++;
            }
        } catch (IndexOutOfBoundsException ignored) {}

        try {
            switch (tablero[row][col+1]) {
                case mina:case minaIncognita: minas++;
            }
        } catch (IndexOutOfBoundsException ignored) {}

        try {
            switch (tablero[row+1][col-1]) {
                case mina:case minaIncognita: minas++;
            }
        } catch (IndexOutOfBoundsException ignored) {}

        try {
            switch (tablero[row+1][col]) {
                case mina:case minaIncognita: minas++;
            }
        } catch (IndexOutOfBoundsException ignored) {}

        try {
            switch (tablero[row+1][col+1]) {
                case mina:case minaIncognita: minas++;
            }
        } catch (IndexOutOfBoundsException ignored) {}

        return minas;
    }

    public static int getMarksAround(int row, int col) {

        int marcas = 0;

        try {
            switch (tablero[row-1][col-1]) {
                case marcada:case minaMarcada: marcas++;
            }
        } catch (IndexOutOfBoundsException ignored) {}

        try {
            switch (tablero[row-1][col]) {
                case marcada:case minaMarcada: marcas++;
            }
        } catch (IndexOutOfBoundsException ignored) {}

        try {
            switch (tablero[row-1][col+1]) {
                case marcada:case minaMarcada: marcas++;
            }
        } catch (IndexOutOfBoundsException ignored) {}

        try {
            switch (tablero[row][col-1]) {
                case marcada:case minaMarcada: marcas++;
            }
        } catch (IndexOutOfBoundsException ignored) {}

        try {
            switch (tablero[row][col+1]) {
                case marcada:case minaMarcada: marcas++;
            }
        } catch (IndexOutOfBoundsException ignored) {}

        try {
            switch (tablero[row+1][col-1]) {
                case marcada:case minaMarcada: marcas++;
            }
        } catch (IndexOutOfBoundsException ignored) {}

        try {
            switch (tablero[row+1][col]) {
                case marcada:case minaMarcada: marcas++;
            }
        } catch (IndexOutOfBoundsException ignored) {}

        try {
            switch (tablero[row+1][col+1]) {
                case marcada:case minaMarcada: marcas++;
            }
        } catch (IndexOutOfBoundsException ignored) {}

        return marcas;
    }

    /**
     * Devuelve el numero de minas que tiene alrededor un casilla
     * @param i Coordenada en el eje x
     * @param j coordenada en el eje y
     * @return
     */
    public static int getNumMinasCasillas(int i, int j) {
        return nMinasTablero[i][j];
    }

    /**
     * Actualiza el numero de minas que tiene alrededor una casilla
     * @param i
     * @param j
     * @param minas
     */
    public static void  setNumMinasCasillas(int i, int j, int minas) {
        nMinasTablero[i][j] = minas;
    }

    /**
     * Comprueba si el juego termina, Termina cuando todas las casillas sin mina son pues con un valor numerico
     * @return si se acabo el juego
     */
    public static boolean isFinal() {

        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[i].length; j++) {
                switch (tablero[i][j]) {
                    case vacio:case marcada:case incognta:return false;
                }
            }
        }
        return true;
    }

    /**
     * Devuelve si el juego termino
     * @return si el juego termino
     */
    public static boolean getFinal() {
        return fin;
    }

    /**
     * Actualiza el final del juego
     * @param fin
     */
    public static void setFinal(boolean fin) {
        Tablero.fin = fin;
    }

    /**
     * Reinicia las variables de inicio
     */
    public static void reiniciarVariables() {
        firstClick = true;
        minasRestante = getNumMinas();
    }
}
