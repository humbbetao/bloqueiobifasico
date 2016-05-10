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
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author humbe
 */
public class Escalonador {

    PrintWriter writer;

    ArrayList<String> listaDeTransacao;
    ArrayList<String> dados;
//    PriorityQueue<String> filaDeTransacao;
    HashMap<String, Integer> componFnteDaLista;
    LinkedList<ItemDaFila> filaDeTransacao;
//    HashMap<String, StatusDoDado> estadoDoDadoCorrente;
    HashMap<String, EstadoDoDado> estadoDoDadoCorrente;
    public static final String statusDoDadoDesbloqueado = "U";
    public static final String statusDoDadoBloqueadoExclusivo = "X";
    public static final String statusDoDadoBloqueadoCompartilhado = "S";

    public Escalonador() {
        filaDeTransacao = new LinkedList<>();
        listaDeTransacao = new ArrayList<>();
        dados = new ArrayList<>();
        estadoDoDadoCorrente = new HashMap<>();
    }

    public void despertarFila(String dado) {
        if (dado.equals("")) {
            for (int i = 0; i < filaDeTransacao.size(); i++) {
                ItemDaFila j = filaDeTransacao.get(i);
                if (j.getEstado() == 1) {
                    filaDeTransacao.remove(i);
                    solicitacaoDeBloqueio(statusDoDadoBloqueadoCompartilhado, j.getTransacao(), j.getDado());
                }
                if (j.getEstado() == 2) {
                    filaDeTransacao.remove(i);
                    solicitacaoDeBloqueio(statusDoDadoBloqueadoExclusivo, j.getTransacao(), j.getDado());
                }
            }

        } else {
            for (ItemDaFila i : filaDeTransacao) {
                if (i.getDado().equals(dado)) {
                    System.out.println("Despertando " + dado);
                    if (i.getEstado() == 1) {
                        filaDeTransacao.remove(i);
                        solicitacaoDeBloqueio(statusDoDadoBloqueadoCompartilhado, i.getTransacao(), i.getDado());
                    }
                    if (i.getEstado() == 2) {
                        filaDeTransacao.remove(i);
                        solicitacaoDeBloqueio(statusDoDadoBloqueadoExclusivo, i.getTransacao(), i.getDado());
                    }
                }
            }
            //procurar por toda a fila quem esta primeiro e atender o chamado, executa;
        }
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
                    System.out.println(s[i] + "DADO");
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
        int verif = 0;
        while (!filaDeTransacao.isEmpty()) {
            if (verif < 500) {
                despertarFila("");
                verif++;
            } else {
                System.out.println("Deadlock Encontrado");
                break;
            }
        }
    }

    public void solicitacaoDeDesbloqueio(String transacao, String dado) {
        if (!dado.equals("infinito")) {
            System.out.println("Desbloqueando " + transacao + " / " + dado);
            if (estadoDoDadoCorrente.get(dado).getEstado() == 2) {
                estadoDoDadoCorrente.get(dado).setEstado(0);//desbloqueia
//                filade.remove(dado);
                despertarFila(dado);
                //desperta a fila-wait(dado);
                estadoDoDadoCorrente.get(dado).setEstado(1);
            } else if (estadoDoDadoCorrente.get(dado).getEstado() == 1) {
                listaDeTransacao.remove(dado);
                if (listaDeTransacao.isEmpty()) {
                    estadoDoDadoCorrente.get(dado).setEstado(0);
                    //desperta a fila-wait(dado);
                    despertarFila(dado);
                }
            }
        }
    }

    public void solicitacaoDeBloqueioCompartilhado(String transacao, String dado) {
        System.out.println("Bloquando compartilhado " + transacao + " / " + dado);
        if (estadoDoDadoCorrente.get(dado).getEstado() == 0) {
            if (estadoDoDadoCorrente.get(dado).getTransacao().equals(transacao)) {
                writer.write("R" + transacao + "(" + dado + ")");
                System.out.println("Conseguiu o bloqueio, esta desbloqueado" + "\n");
                listaDeTransacao.add(transacao);
                estadoDoDadoCorrente.get(dado).setEstado(1);
            }
            writer.write("R" + transacao + "(" + dado + ")");
            System.out.println("Conseguiu o bloqueio, esta desbloqueado" + "\n");
            listaDeTransacao.add(transacao);
            estadoDoDadoCorrente.get(dado).setEstado(1);

        } else if (estadoDoDadoCorrente.get(dado).getEstado() == 1) {
            if (estadoDoDadoCorrente.get(dado).getTransacao().equals(transacao)) {
                writer.write("R" + transacao + "(" + dado + ")");
                System.out.println("Conseguiu o bloqueio, esta compartilhado" + "\n");
                listaDeTransacao.add(transacao);
            }
            writer.write("R" + transacao + "(" + dado + ")");
            System.out.println("Conseguiu o bloqueio, esta desbloqueado" + "\n");
            listaDeTransacao.add(transacao);
            estadoDoDadoCorrente.get(dado).setEstado(1);
        } else {
            System.out.println("Nao Conseguiu, entrando pra fila");
            ItemDaFila novoItemDaFila = new ItemDaFila(1, transacao, dado);
            filaDeTransacao.add(novoItemDaFila);

        }
    }

    public void solicitacaoDeBloqueioExclusivo(String transacao, String dado) {
        System.out.println("Bloqueando Exclusivo " + transacao + " / " + dado);
        if (estadoDoDadoCorrente.get(dado).getEstado() == 0) {
            writer.write("W" + transacao + "(" + dado + ")" + "\n");
            System.out.println("Conseguiu o bloqueio");
            listaDeTransacao.add(transacao);
            estadoDoDadoCorrente.get(dado).setEstado(2);
        } else if (estadoDoDadoCorrente.get(dado).getEstado() == 2
                && estadoDoDadoCorrente.get(dado).getTransacao().equals(transacao)) {
            writer.write("W" + transacao + "(" + dado + ")" + "\n");
            System.out.println("Conseguiu o bloqueio");
            listaDeTransacao.add(transacao);
            estadoDoDadoCorrente.get(dado).setEstado(2);
        } else if (estadoDoDadoCorrente.get(dado).getEstado() == 1
                && estadoDoDadoCorrente.get(dado).getTransacao().equals(transacao)) {
            System.out.println("Nao Conseguiu, entrando pra fila");
            ItemDaFila novoItemDaFila = new ItemDaFila(2, transacao, dado);
            filaDeTransacao.add(novoItemDaFila);
//            filaDeTransacao.add(transacao);
        } else {
            System.out.println("Nao Conseguiu, entrando pra fila");
            ItemDaFila novoItemDaFila = new ItemDaFila(2, transacao, dado);
            filaDeTransacao.add(novoItemDaFila);
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
        System.out.println("Digite o nome do arquivo de Saida: ");
        Scanner leitor = new Scanner(System.in);
        String arq = leitor.next();
        try {
            writer = new PrintWriter(arq, "UTF-8");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Escalonador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Escalonador.class.getName()).log(Level.SEVERE, null, ex);
        }
        writer.write("Schedule: " + "\n");
        for (String dado : dados) {
            System.out.println(dado + "NOVO");
            EstadoDoDado item = new EstadoDoDado("", 0);
            estadoDoDadoCorrente.put(dado, item);
        }
        for (String i : schedule) {
            //verifica se comeca uma transacao;

            if (i.substring(0, 1).equals("S")) {
                //comeca transacao
                System.out.println("Comecou A transacao " + i);
                writer.write(i + "\n");
            }
            if (i.substring(0, 1).equals("E")) {

                writer.write(i + "\n");
                //tem q ver se num tem nada na fila;
                //termina transacao

                solicitacaoDeDesbloqueio(i.substring(1, 2), "infinito");
                verificarFila(i.substring(1, 2));
                System.out.println("Commitou " + i);
            }
            if (i.substring(0, 1).equals("R")) {
                //solicita bloqueio compartilhado
                System.out.println(i.substring(1, 2));
                System.out.println(i.substring(3, 4));
                solicitacaoDeBloqueio(statusDoDadoBloqueadoCompartilhado, i.substring(1, 2), i.substring(3, 4));
            }
            if (i.substring(0, 1).equals("W")) {
                //solicita bloqueio exclusivo
                System.out.println(i.substring(1, 2));
                System.out.println(i.substring(3, 4));
                solicitacaoDeBloqueio(statusDoDadoBloqueadoExclusivo, i.substring(1, 2), i.substring(3, 4));

            }

        }
//        while (!filaDeTransacao.isEmpty()) {
//            despertarFila();
//        }
        writer.close();
    }

    private void verificarFila(String substring) {
        ItemDaFila first;
        System.out.println(Arrays.toString(filaDeTransacao.toArray()));
        for (int i = 0; i < filaDeTransacao.size(); i++) {
            first = filaDeTransacao.get(i);
            if (estadoDoDadoCorrente.get(first.getDado()).getEstado() == 0) {
                switch (first.getEstado()) {
                    case 1:
                        solicitacaoDeBloqueio(statusDoDadoBloqueadoCompartilhado, first.getTransacao(), first.getDado());
                        break;
                    case 2:
                        solicitacaoDeBloqueio(statusDoDadoBloqueadoExclusivo, first.getTransacao(), first.getDado());
                        break;
                }
            }
        }
    }
}
