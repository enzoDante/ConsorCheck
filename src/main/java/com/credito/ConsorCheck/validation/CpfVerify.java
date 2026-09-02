package com.credito.ConsorCheck.validation;

public class CpfVerify {
    public static boolean isValid(String cpf) {
        if (cpf == null) return true; // Deixe o @NotNull tratar a obrigatoriedade

        // Remove caracteres não numéricos
        String cleanCpf = cpf.replaceAll("\\D", "");

        if (cleanCpf.length() != 11 || cleanCpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int d1 = calcularDigito(cleanCpf.substring(0, 9), 10);
            int d2 = calcularDigito(cleanCpf.substring(0, 9) + d1, 11);
            return cleanCpf.equals(cleanCpf.substring(0, 9) + d1 + d2);
        } catch (Exception e) {
            return false;
        }
    }

    private static int calcularDigito(String str, int peso) {
        int soma = 0;
        for (int i = 0; i < str.length(); i++) {
            soma += Character.getNumericValue(str.charAt(i)) * peso--;
        }
        int resto = 11 - (soma % 11);
        return (resto > 9) ? 0 : resto;
    }
}
