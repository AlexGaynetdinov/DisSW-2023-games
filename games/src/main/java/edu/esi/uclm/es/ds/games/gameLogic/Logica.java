package edu.esi.uclm.es.ds.games.gameLogic;

import java.security.SecureRandom;

class DiseñoSW_LogicaCeldasValidas {
    public static void main(String[] args) {
        // celdas [fila,columna]
        byte[] celda1 = {0, 1};
        byte[] celda2 = {3, 0};

        SecureRandom dice = new SecureRandom();
        byte[][] digits = new byte[9][9];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                digits[i][j] = (byte) (dice.nextInt(9) + 1);
            }
        }

        System.out.println("Celda 1 pre-validacion: " + digits[celda1[0]][celda1[1]]);
        System.out.println("Celda 2 pre-validacion: " + digits[celda2[0]][celda2[1]]);

        System.out.println(validMove_Matriz(celda1, celda2, digits));

        System.out.println("Celda 1 post-validacion: " + digits[celda1[0]][celda1[1]]);
        System.out.println("Celda 2 post-validacion: " + digits[celda2[0]][celda2[1]]);
    }

    public static boolean validMove_Matriz(byte[] celda1, byte[] celda2, byte[][] tablero) {
        // En el frontend se deberá comprobar que las 2 celdas esten dentro del tablero
        boolean result = false;

        // Ordenación de las celdas para que los algoritmos funcionen
        byte[] primeraCelda;
        byte[] segundaCelda;
        if (celda1[0] < celda2[0]) {
            primeraCelda = celda1;
            segundaCelda = celda2;
        } else { // Como comprobamos previamente si están en la misma fila, no tenemos por qué
                 // comprobar más aquí
            primeraCelda = celda2;
            segundaCelda = celda1;
        }

        // Primero nos aseguramos que los números puedan ser elegidos juntos
        byte num1 = tablero[celda1[0]][celda1[1]];
        byte num2 = tablero[celda2[0]][celda2[1]];
        if (num1 != num2 && (num1 + num2) != 10) {
            System.out.println("Los numeros no suman 10 ni son iguales.");
            return result;
        }

        // Comprobamos las 4 direcciones basicas
        // Filas
        if (celda1[0] == celda2[0]) {
            result = recorrerFila(tablero, primeraCelda, segundaCelda);
            return result;
        }
        // Columnas
        if (celda1[1] == celda2[1]) {
            result = recorrerColumna(tablero, primeraCelda, segundaCelda);
            return result;
        }
        // Diagonales
        byte diffFilas = (byte) Math.abs(celda1[0] - celda2[0]);
        byte diffColumns = (byte) Math.abs(celda1[1] - celda2[1]);
        if (diffFilas == diffColumns) {
            result = recorrerDiagonal(tablero, primeraCelda, segundaCelda);
            return result;
        }

        // Comprobamos ahora para cuando no están alineados
        // Recorremos la matriz como un vector lineal comprobando si hay valores
        // (distinto de 0) entre las celdas
        int vinicio = (primeraCelda[0] * tablero[0].length) + primeraCelda[1]; // Transformamos la primera celda a un
                                                                               // valor para el vector lineal
        int vfinal = (segundaCelda[0] * tablero[0].length) + segundaCelda[1]; // Transformamos la segunda celda a un
                                                                              // valor para el vector lineal
        for (int i = (vinicio + 1); i < vfinal; i++) {
            int fila = i / tablero[0].length; // Convertimos la posición actual a índice de fila
            int columna = i % tablero[0].length; // Convertimos la posición actual a índice de columna
            int valor = tablero[fila][columna]; // Obtenemos el valor de la celda actual
            if (valor != 0) {
                System.out.println("Hay elementos entre los números, movimiento no válido.");
                return result;
            }
        }
        vaciarCeldas(tablero, celda1, celda2);
        result = true;
        return result;
    }

    public static boolean recorrerFila(byte[][] tablero, byte[] celda1, byte[] celda2) {
        boolean result = true;

        for (int i = celda1[1] + 1; i < celda2[1]; i++) {
            if (tablero[celda1[0]][i] != 0) {
                result = false;
                System.out.println("Hay elementos en la fila, movimiento no válido.");
                return result;
            }
        }
        vaciarCeldas(tablero, celda1, celda2);

        return result;
    }

    public static boolean recorrerColumna(byte[][] tablero, byte[] celda1, byte[] celda2) {
        boolean result = true;

        for (int i = celda1[0] + 1; i < celda2[0]; i++) {
            if (tablero[i][celda1[1]] != 0) {
                result = false;
                System.out.println("Hay elementos en la columna, movimiento no válido.");
                return result;
            }
        }
        vaciarCeldas(tablero, celda1, celda2);

        return result;
    }

    public static boolean recorrerDiagonal(byte[][] tablero, byte[] celda1, byte[] celda2) {
        boolean result = true;

        int diffColumnas = celda2[1] - celda1[1]; // Diferencia entre las columnas de las celdas
        int fila = celda1[0] + 1;
        int inc_columna;
        if (diffColumnas > 0) {
            inc_columna = 1;
        } else {
            inc_columna = -1;
        }
        for (int diff_columna = inc_columna; Math.abs(diff_columna) < Math.abs(diffColumnas); diff_columna = diff_columna + inc_columna, fila++) {
            if (tablero[fila][celda1[1] + diff_columna] != 0) {
                result = false;
                System.out.println("Hay elementos en la diagonal, movimiento no válido.");
                return result;
            }
        }
        vaciarCeldas(tablero, celda1, celda2);

        return result;
    }

    public static void vaciarCeldas(byte[][] tablero, byte[] celda1, byte[] celda2) {
        tablero[celda1[0]][celda1[1]] = 0;
        tablero[celda2[0]][celda2[1]] = 0;
    }
}
