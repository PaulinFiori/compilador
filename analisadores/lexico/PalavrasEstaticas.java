package analisadores.lexico;

import java.util.Arrays;
import java.util.List;

public class PalavrasEstaticas {

  private static final List<String> palavrareservada = initPalavraReservada();

  private static List<String> initPalavraReservada() {
    return Arrays.asList("encasqueta", "inteirim", "to_ti_falano", "ontoco", "enjoado", "enrabichado", "logali", "meia_boca", "uai", "pelejanu", "paradeza", "em_tempo", "nu_jeito", "causo", "ques_sera", "arreda", "panha", "dediprosa", "logo_ai", "e_mio", "quale", "di_vera", "bagaca", "quale", "qui_nem", "murrinha", "po_para", "perai", "cascar_fora");
  }

  private static final List<String> delimitador = initDelimitador();

  private static List<String> initDelimitador() {
    return Arrays.asList("(", ")", "{", "}", ":", ";", ",", "[", "]");
  }

  private static final List<String> operadoraritimetico = initOperadorAritmetico();

  private static List<String> initOperadorAritmetico() {
    return Arrays.asList("+", "-", "/", "*");
  }

  private static final List<String> operadorrelacional = initOperadorRelacional();

  private static List<String> initOperadorRelacional() {
    return Arrays.asList("<", "<=", ">", ">=", "!=", "==");
  }

  private static final List<String> operadoratribuicao = initOperadorAtribuicao();

  private static List<String> initOperadorAtribuicao() {
    return Arrays.asList("=", "+=", "-=", "*=", "/=", "++", "--");
  }

  public static boolean isPalavraReservada(String imagem) {
    return palavrareservada.contains(imagem);
  }

  public static boolean isDelimitador(String imagem) {
    return delimitador.contains(imagem);
  }

  public static boolean isOperadorAritmetico(String imagem) {
    return operadoraritimetico.contains(imagem);
  }

  public static boolean isOperadorRelacional(String imagem) {
    return operadorrelacional.contains(imagem);
  }

  public static boolean isOperadorAtribuicao(String imagem) {
    return operadoratribuicao.contains(imagem);
  }
}