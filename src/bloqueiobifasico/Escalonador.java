/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bloqueiobifasico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;

/**
 *
 * @author humbe
 */
public class Escalonador {

    Queue<String> filaDeTransacao;
    ArrayList<String> listaDeTransacao;

    public Escalonador() {
    }

    void executar(String arquivotxt) throws FileNotFoundException, IOException {
        FileReader file = new FileReader(new File(arquivotxt));
        BufferedReader buffer = new BufferedReader(file);
        String linha;
        int numeroDeLinhas = 0;
        int numeroDeItensDeDados = 0;
        int numeroDeTransacoes = 0;
        int numeroDeAcessos = 0;
        while ((linha = buffer.readLine()) != null) {
            numeroDeLinhas++;
            System.out.println(linha);
            if (numeroDeLinhas == 1) {
                linha = linha.replace("Dados: [", "");
                linha = linha.replace("]", "");
                
                String[] s = linha.split(", ");
                

                numeroDeItensDeDados = s.length;
                numeroDeTransacoes = Integer.parseInt(s[1]);
                numeroDeAcessos = Integer.parseInt(s[2]);
            } else {
            }
        }
    }
}