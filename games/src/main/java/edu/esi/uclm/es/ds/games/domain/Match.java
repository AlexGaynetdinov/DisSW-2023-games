package edu.esi.uclm.es.ds.games.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Match {

	private String id;
	private boolean ready;
	private ArrayList<String> players;
	private int[][] board;

	public Match() {
		this.id = UUID.randomUUID().toString();
		this.ready = false;
		this.players = new ArrayList<>();
		this.board = new int[6][7];
	}
	
	public String getId() {
		return this.id;
	}
	public List<String> getPlayers() {
		return this.players;
	}
	public boolean isReady() {
		return this.ready;
	}
	public void setReady(boolean value) {
		this.ready = value;
	}
	public int[][] getBoard(){
		return this.board;
	}
	public void addPlayer(String player) {
		// Si el jugador ya existe no lo añadimos porque sería un duplicado
		if (this.players.contains(player)) {
			return;
		}
		this.players.add(player);
		if (this.players.size() == 2) {
			this.setReady(true);
		}
	}
	
	public void move(int columna, String player) throws Exception {
		int playerIndex;
		if (players.get(0).equals(player)) {
			playerIndex = 0;
		} else {
			playerIndex = 1;
		}
		Logica_4EnRaya.jugada(this.board, columna, playerIndex + 1);
	}
}

class Logica_4EnRaya {
    // Realizacion de una jugada
    public static boolean jugada(int[][] tablero, int columna, int jugador)
            throws IllegalMoveException, UnknownPlayerException {
        // Comprobamos la legalidad de la jugada y el jugador
        if (!isLegal(tablero, columna))
            throw new IllegalMoveException();
        
        if (jugador < 1 || jugador > 2)
            throw new UnknownPlayerException();

        if (isWinner(tablero, columna, jugador)) {
            return true; // Devuelve true si ha ganado alguien en la jugada
        }

        return false; // Devuelve false si no ha ganado nadie en esta jugada
    }

    // Comprobacion de casillas restantes
    public static boolean hayCasillas(int[][] tablero) {
        for (int i = 0; i < tablero[0].length; i++) {
            if (tablero[0][i] == 0)
                return true; // Si queda alguna columna no llena devuelve true
        }
        return false; // Si no queda ninguna columna devuelve false
    }

    // Comprobacion de que la columna elegida es valida
    public static boolean isLegal(int[][] tablero, int columna) {
        // Comprobamos que la columna este dentro del tablero
        if (columna < 0 || columna >= tablero[0].length) {
            return false;
        }
        // Comprobamos que la columna no este llena
        if (tablero[0][columna] != 0) {
            return false;
        }

        return true;
    }

    // Comprobacion de victoria
    public static boolean isWinner(int[][] tablero, int columna, int jugador) {
        // Para saber si una jugada es ganadora tenemos que comprobar si la casilla
        // donde se ha jugado forma parte de una linea del mismo tipo en cualquiera de
        // las 8 direcciones validas
        int fila = -1;
        try {
            fila = filaJugada(tablero, columna);
            tablero[fila][columna] = jugador;
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error: " + e.getMessage());
        }

        // Comprobar si hay una secuencia ganadora en la fila, columna o diagonal
        if (comprobarDireccion(tablero, fila, columna, jugador, 0, 1) ||
                comprobarDireccion(tablero, fila, columna, jugador, 1, 0) ||
                comprobarDireccion(tablero, fila, columna, jugador, 1, 1) ||
                comprobarDireccion(tablero, fila, columna, jugador, 1, -1)) {
            return true; // La jugada es ganadora
        }

        return false; // La jugada no es ganadora
    }

    // Metodo auxiliar para obtener la fila vacia en una columna especifica
    private static int filaJugada(int[][] tablero, int columna) throws IllegalMoveException {
        for (int fila = tablero.length - 1; fila >= 0; fila--) {
            if (tablero[fila][columna] == 0) {
                return fila;
            }
        }
        throw new IllegalMoveException();
    }

    // Metodo auxiliar para comprobar una secuencia de fichas en una direccion especifica
    private static boolean comprobarDireccion(int[][] tablero, int fila, int columna, int jugador, int deltaFila, int deltaColumna) {
        int count = 0; // Contador de fichas consecutivas

        // Comprobar hacia adelante
        int i = fila + deltaFila;
        int j = columna + deltaColumna;
        while (i >= 0 && i < tablero.length && j >= 0 && j < tablero[0].length && tablero[i][j] == jugador) {
            count++;
            i += deltaFila;
            j += deltaColumna;
        }

        // Comprobar hacia atras
        i = fila - deltaFila;
        j = columna - deltaColumna;
        while (i >= 0 && i < tablero.length && j >= 0 && j < tablero[0].length && tablero[i][j] == jugador) {
            count++;
            i -= deltaFila;
            j -= deltaColumna;
        }

        return count >= 3; // Se requieren al menos 4 fichas consecutivas para ganar en 4 en raya
    }
}

class IllegalMoveException extends Exception {
	private static final long serialVersionUID = 1L;
	public IllegalMoveException() {
        super("La jugada no es valida.");
    }
}

class UnknownPlayerException extends Exception {
	private static final long serialVersionUID = 1L;
	public UnknownPlayerException() {
        super("El jugador no existe o no se reconoce");
    }
}
