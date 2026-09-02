package com.credito.ConsorCheck.validation;

public class CnpjVerify {
    public static boolean isCnpjValido(String cnpj) {
        if (cnpj == null) return false;

        // Remove tudo que não for dígito
        String cleanCnpj = cnpj.replaceAll("\\D", "");

        // CNPJ precisa ter 14 dígitos e não ter todos os números iguais
        if (cleanCnpj.length() != 14 || cleanCnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        try {
            // Pesos para os dígitos verificadores do CNPJ
            int[] pesoD1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int[] pesoD2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

            int d1 = calcularDigito(cleanCnpj.substring(0, 12), pesoD1);
            int d2 = calcularDigito(cleanCnpj.substring(0, 12) + d1, pesoD2);

            return cleanCnpj.equals(cleanCnpj.substring(0, 12) + d1 + d2);
        } catch (Exception e) {
            return false;
        }
    }

    private static int calcularDigito(String str, int[] peso) {
        int soma = 0;
        for (int i = str.length() - 1; i >= 0; i--) {
            int digito = Character.getNumericValue(str.charAt(i));
            soma += digito * peso[peso.length - str.length() + i];
        }
        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }
}
