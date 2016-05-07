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

/**
 *
 * @author humbe
 */
public class Escalonador {

    ArrayList<String> listaDeTransacao;
    ArrayList<String> dados;
    PriorityQueue<String> filaDeTransacao;
    HashMap<String, Integer> componFnteDaLista;
//    PriorityQueue<HashMap<Integer, ArrayList<String>>> filaDeTransacao;
    //
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
                s = linha.split(", ");
                numeroDeItensDeDados = s.length;
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

    
    public void despertarFila(String dado)
    {
        //procurar por toda a fila quem esta primeiro e atender o chamado, executa;
    }
    
    public void solicitacaoDeDesbloqueio(String transacao, String dado) {
        if (estadoDoDadoCorrente.get(dado).equals(statusDoDadoBloqueadoExclusivo)) {
            estadoDoDadoCorrente.get(dado).setStatus(0);//desbloqueia
            //desperta a fila-wait(dado);
//            estadoDoDadoCorrente.get(dado).setStatus(1);
        } else if (estadoDoDadoCorrente.get(dado).equals(statusDoDadoBloqueadoCompartilhado)) {
//            listaDeTransacao.add(transacao);
            listaDeTransacao.remove(dado);
            if (listaDeTransacao.isEmpty()) {
                estadoDoDadoCorrente.get(dado).setStatus(0);
                //desperta a fila-wait(dado);
            }

        }

    }

    public void solicitacaoDeBloqueioCompartilhado(String transacao, String dado) {
        if (estadoDoDadoCorrente.get(dado).equals(statusDoDadoDesbloqueado)) {
            listaDeTransacao.add(transacao);
            estadoDoDadoCorrente.get(dado).setStatus(1);
        } else if (estadoDoDadoCorrente.get(dado).equals(statusDoDadoBloqueadoCompartilhado)) {
            listaDeTransacao.add(transacao);
        } else {
            filaDeTransacao.add(transacao);
        }

    }

    public void solicitacaoDeBloqueioExclusivo(String transacao, String dado) {
        if (estadoDoDadoCorrente.get(dado).equals(statusDoDadoDesbloqueado)) {
            listaDeTransacao.add(transacao);
            estadoDoDadoCorrente.get(dado).setStatus(2);
        } else {
            filaDeTransacao.add(transacao);
        }
    }

    public void solicitacaoDeBloqueio(String status, String transacao, String dado) {
        if (status.equals(statusDoDadoBloqueadoCompartilhado)) {
            solicitacaoDeBloqueioCompartilhado(transacao, dado);
        } else {
            solicitacaoDeBloqueioExclusivo(transacao, dado);
        }
    }

    private void escalonar(String[] schedule) {
        for (String dado : dados) {
            estadoDoDadoCorrente.put(dado, StatusDoDado.statusDesbloqueado);
        }
        for (String i : schedule) {
            //verifica se comeca uma transacao;
            if (i.substring(0, 1).equals("S")) {
                //comeca transacao
            }
            if (i.substring(0, 1).equals("S")) {
                //termina transacao
            }
            if (i.substring(0, 1).equals("R")) {
                //solicita bloqueio compartilhado
                System.out.println(i.substring(1, 2));
                System.out.println(i.substring(3, 4));
                solicitacaoDeBloqueio(statusDoDadoBloqueadoCompartilhado, i.substring(1, 2), i.substring(4, 5));
            }
            if (i.substring(0, 1).equals("W")) {
                //solicita bloqueio exclusivo
                System.out.println(i.substring(1, 2));
                System.out.println(i.substring(3, 4));
                solicitacaoDeBloqueio(statusDoDadoBloqueadoExclusivo, i.substring(1, 2), i.substring(4, 5));

            }

        }
    }

}
