package ar.utn.sistema.utils;

import java.security.SecureRandom;

public class CodigoGenerador {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LONGITUD_CODIGO = 11;
    private static SecureRandom random = new SecureRandom();

    private static String generarCodigo() {
        StringBuilder codigo = new StringBuilder(LONGITUD_CODIGO);
        for (int i = 0; i < LONGITUD_CODIGO; i++) {
            int indice = random.nextInt(CHARACTERS.length());
            codigo.append(CHARACTERS.charAt(indice));
        }
        return codigo.toString();
    }

    public static String generarCodigoUnico() {
        String codigo;
        do {
            codigo = generarCodigo();
        } while (codigoYaExisteEnBaseDeDatos(codigo));
        return codigo;
    }

    private static boolean codigoYaExisteEnBaseDeDatos(String codigo) {
        return false; // TODO!!!
    }
}