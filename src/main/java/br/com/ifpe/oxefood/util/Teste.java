package br.com.ifpe.oxefood.util;

import java.time.LocalDateTime;

public class Teste {

    public static void main(String[] args) {

        String dataHora = LocalDateTime.now().getYear() + "-" 
            + LocalDateTime.now().getMonthValue() + "-"
            + LocalDateTime.now().getDayOfMonth() + "-" 
            + LocalDateTime.now().getHour() + "-"
            + LocalDateTime.now().getMinute() + "-"
            + LocalDateTime.now().getSecond() + " - ";

        String nomeOriginalArquivo = "pizza.png";
        String nomeArquivoComDataHora = dataHora + nomeOriginalArquivo;

        System.out.println(nomeArquivoComDataHora);
    }
}
