/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bloqueiobifasico;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author * @author Humberto Felipe Guilherme Alberton
 */
public class BloqueioBifasico {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        Scanner leitor = new Scanner(System.in);
        try {
//        PrintWriter writer = new PrintWriter("Arquivo.txt", "UTF-8");

            System.out.println("Insira o nome do arquivo: ");
//        Scanner leitor = new Scanner(System.in);
            String arq = leitor.next();
            PrintWriter writer = new PrintWriter(arq, "UTF-8");

            char operações[] = new char[]{'R', 'W'};

            System.out.println("Insira a quantidade de itens de dados:");
            int ndados = leitor.nextInt();
            char[] dados = new char[ndados];
            int k = 0;

            for (int i = 0; i < ndados; i++) {
                dados[i] = (char) (65 + (k++));
            }
            System.out.println("Dados: " + Arrays.toString(dados));
//        writer.write();
//        writer.write("Dados: "+ Arrays.toString(dados)+"\n");

            System.out.println("Insira a quantidade de Trasações:");
            int ntrans = leitor.nextInt();
            List<List<String>> Transações = new ArrayList<>();

            for (int i = 0; i < ntrans; i++) {
                List<String> transação = new ArrayList<>();
                Transações.add(transação);
            }

            System.out.println("Insira a quantidade de Acessos:");
            int nacess = leitor.nextInt();

            Random gerador = new Random();

            for (int i = 0; i < ntrans; i++) {
                String start = new StringBuilder().append("S").append(i).toString();
                Transações.get(i).add(start);
                for (int j = 0; j < nacess; j++) {
                    String x = new StringBuilder()
                            .append(operações[gerador.nextInt(operações.length)])
                            .append(i)
                            .append("(")
                            .append(dados[gerador.nextInt(ndados)])
                            .append(")").toString();
                    Transações.get(i).add(x);
                }
                String end = new StringBuilder().append("E").append(i).toString();
                Transações.get(i).add(end);

                System.out.println("T" + i + ": " + Transações.get(i).toString());
            }

            int TotalTransações = (nacess + 2) * ntrans;

            String[] Schedule = new String[TotalTransações];
            int i = 0;

            while (!Transações.isEmpty()) {

                int j = gerador.nextInt(ntrans);

                if (!Transações.get(j).isEmpty()) {
                    Schedule[i] = Transações.get(j).get(0);
                    Transações.get(j).remove(0);
                    i++;
                } else {
                    Transações.remove(j);
                    ntrans = ntrans - 1;
                }
            }

            System.out.println("Schedule: " + Arrays.toString(Schedule));
            writer.write(ndados + ", " + ntrans + ", " + nacess);
            writer.write("Dados: " + Arrays.toString(dados) + "\n");
            writer.write("T" + i + ": " + Transações.get(i).toString() + "\n");
            writer.write("Schedule: " + Arrays.toString(Schedule) + "\n");
            writer.close();

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BloqueioBifasico.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Digite o nome do Arquivo Para ser Escalonado");
        Escalonador e = new Escalonador();
        String arq = leitor.next();
        
        e.executar(arq);// passa o nome do arquivo que quer escalonar

    }
}
