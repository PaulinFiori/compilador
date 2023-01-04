import analisadores.lexico.AnalisadorLexico;
import analisadores.lexico.ClassificadorPalavras;
import analisadores.lexico.TabelaSimbolos;
import analisadores.sintatico.AnalisadorSintaticoGeradorArvore;
import analisadores.semantico.AnalisadorSemantico;
//import analisadores.gerador.GeradorCodigo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {

  public static void lexico() {
    try {
      File file = new File("analisadores//lexico//programa_teste.l7p");
      BufferedReader br = new BufferedReader(new FileReader(file));
      ClassificadorPalavras classificadorPalavras = new ClassificadorPalavras();

      classificadorPalavras.classifica(br);

      System.out.println("---Analisador Léxico---");
      if (!AnalisadorLexico.getErros().isEmpty()) {
        System.out.println("->Erros léxicos apresentados: ");
        AnalisadorLexico.printErros();
      } else {
        System.out.println("->Analise Léxica Finalizada com Sucesso!");
        AnalisadorLexico.printTokens();
      }
      System.out.println("-----------------------");
    } 
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static void sintatico() {
    System.out.println("-----Analisador Sintático-----");
    AnalisadorSintaticoGeradorArvore sintatico = new AnalisadorSintaticoGeradorArvore(AnalisadorLexico.getTokens());
    sintatico.analisar();

    if(!AnalisadorSintaticoGeradorArvore.getErros().isEmpty()) { 
      System.out.println("->Erros sintáticos apresentados: ");
      AnalisadorSintaticoGeradorArvore.printErros();
    } else {
      System.out.println("->Análise Sintática Finalizada com Sucesso!");
      AnalisadorSintaticoGeradorArvore.mostraArvore();
    }
    System.out.println("------------------------------");
  }

  private static void semantico() {
    System.out.println("-----Analisador Semántico-----");
    AnalisadorSemantico semantico = new AnalisadorSemantico(AnalisadorSintaticoGeradorArvore.getRaiz());
    semantico.analisar();

    if(!AnalisadorSemantico.getErros().isEmpty()) {
      System.out.println("->Erros Semánticos apresentados: ");
      AnalisadorSemantico.mostraErros();
    } else {
      System.out.println("->Análise Semántica Finalizada com Sucesso!");
      TabelaSimbolos.mostrarSimbolos();
    }

    System.out.println("------------------------------");
  }

  //ainda não acabado
  private static void gerador() {
    System.out.println("-----Gerador de Código-----");
    System.out.println("---------------------------");
  }

  public static void main(String[] args) {
    lexico();
    sintatico();
    semantico();
    gerador();
  }
}