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
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 *
 * @author humbe
 */
public class Escalonador {

    PriorityQueue<String> filaDeTransacao;
    ArrayList<String> listaDeTransacao;
    ArrayList<String> dados;
    HashMap<String, StatusDoDado> estadoDoDadoCorrente;

    public static final String statusDoDadoDesbloqueado = "U";
    public static final String statusDoDadoBloqueadoExclusivo = "X";
    public static final String statusDoDadoBloqueadoCompartilhado = "S";

    public Escalonador() {
        filaDeTransacao = new PriorityQueue<>();
        listaDeTransacao = new ArrayList<>();
        dados = new ArrayList<>();
        estadoDoDadoCorrente = new HashMap<>();
    }

    void executar(String arquivotxt) throws FileNotFoundException, IOException {
        FileReader file = new FileReader(new File(arquivotxt));
        BufferedReader buffer = new BufferedReader(file);
        String linha;
        int numeroDeLinhas = 0;
        int numeroDeItensDeDados = 0;
        int numeroDeTransacoes = 0;
        int numeroDeAcessos = 0;
        String[] s;

        String l;
        String[] schedule = null;
        while ((linha = buffer.readLine()) != null) {
            numeroDeLinhas++;
            System.out.println(linha);
            if (numeroDeLinhas == 1) {
                s = linha.split(" ");
                numeroDeItensDeDados = s.length;;
                numeroDeTransacoes = Integer.parseInt(s[1]);
                numeroDeAcessos = Integer.parseInt(s[2]);
            } else if (numeroDeLinhas == 2) {
                linha = linha.replace("Dados: [", "");
                linha = linha.replace("]", "");
                s = linha.split(", ");
                for (int i = 0; i < s.length; i++) {
                    dados.add(s[i]);
                }
            } else if (numeroDeLinhas == 3 && numeroDeLinhas < 3 + numeroDeTransacoes) {
                //transacoes
            } else {
                //escalonador;
                l = linha.replace("schedule: [", "");
                l = l.replace("]", "");
                schedule = linha.split(", ");

            }
        }
        escalonar(schedule);
    }

    public void solicitacaoDeBloqueio() {
    }

    public void solicitacaoDeDesbloqueio() {
    }

    public void solicitacaoDeBloqueioCompartilhado() {
    }

    public void solicitacaoDeBloqueioExclusivo() {
    }

    private void escalonar(String[] schedule) {
        for (String dado : dados) {
            estadoDoDadoCorrente.put(dado, StatusDoDado.statusDesbloqueado);
        }
        for (String i : schedule) {
            //verifica se comeca uma transacao;
            if (i.substring(0, 1).equals("S")) {
                
            }
        }
    }

}
