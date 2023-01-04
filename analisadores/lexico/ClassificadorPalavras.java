package analisadores.lexico;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ClassificadorPalavras {

  private static int variavelAnterior;

  public ArrayList<Token> classifica(BufferedReader texto) {
    String linha = null;

    try {
      int nrLinhas = 1;
      while ((linha = texto.readLine()) != null) {

        linha = linha.replaceAll(
            "(" + "^\\p{Alpha}encasqueta^\\p{Alpha}|^\\p{Alpha}inteirim^\\p{Alpha}|^\\p{Alpha}to_ti_falano^\\p{Alpha}|"
                + "^\\p{Alpha}ontoco^\\p{Alpha}|^\\p{Alpha}enjoado^\\p{Alpha}|^\\p{Alpha}enrabichado^\\p{Alpha}|"
                + "^\\p{Alpha}logali^\\p{Alpha}|^\\p{Alpha}encatoa^\\p{Alpha}|^\\p{Alpha}trenheira^\\p{Alpha}|"
                + "^\\p{Alpha}meia_boca^\\p{Alpha}|^\\p{Alpha}uai^\\p{Alpha}|"
                + "^\\p{Alpha}pelejanu^\\p{Alpha}|^\\p{Alpha}paradeza^\\p{Alpha}|"
                + "^\\p{Alpha}em_tempo^\\p{Alpha}|^\\p{Alpha}nu_jeito^\\p{Alpha}|"
                + "^\\p{Alpha}causo^\\p{Alpha}|^\\p{Alpha}ques_sera^\\p{Alpha}|"
                + "^\\p{Alpha}arreda^\\p{Alpha}|^\\p{Alpha}panha^\\p{Alpha}|"
                + "^\\p{Alpha}dediprosa^\\p{Alpha}|^\\p{Alpha}logo_ai^\\p{Alpha}|"
                + "^\\p{Alpha}e_mio^\\p{Alpha}|^\\p{Alpha}quale^\\p{Alpha}|" + "\\(|\\)|\\{|\\}|\\,|\\;|\\:|"
                + "\\+|\\-|\\*|\\/|\\%|" + "\\<=|\\>=|\\==|\\!=|\\<|\\>|" + "\\+=|\\-=|\\*=|\\/=|\\\"|\\=|\\++|\\--" + ")",
            " $1 ");
        linha = linha.replace("  ", " ");

        String aux = linha;
        variavelAnterior = 0;
        StringTokenizer st = new StringTokenizer(linha);
        int coluna = 0;
        while (st.hasMoreTokens()) {
          String imagem = st.nextToken();
          if (imagem.equals("#")) { // simbolo de comentario
            break;
          }
          if (!imagem.startsWith("\"")) { // analisa uma nao string
            coluna = posicaoColuna(linha, imagem) + 1 - imagem.length();
            linha = linha.substring(linha.indexOf(imagem) + imagem.length());
            AnalisadorLexico.analisar(imagem, nrLinhas, coluna);
          } else { // analisa a string completa
            coluna = posicaoColuna(linha, imagem) + 1 - imagem.length();
            linha = linha.substring(linha.indexOf(imagem) + imagem.length());
            if (!imagem.endsWith("\"") || imagem.length() == 1) {
              //int init = variavelAnterior - imagem.length() - 1;
              imagem = st.nextToken();
              coluna = posicaoColuna(linha, imagem) + 1 - imagem.length();
              linha = linha.substring(linha.indexOf(imagem) + imagem.length());

              while (!imagem.endsWith("\"")) {
                imagem = st.nextToken();
                coluna = posicaoColuna(linha, imagem) + 1 - imagem.length();
                linha = linha.substring(linha.indexOf(imagem) + imagem.length());
              }
              AnalisadorLexico.adicionaConstanteLiteralTexto(aux.substring(aux.indexOf("\"") + 1, variavelAnterior - 1),
                  nrLinhas, coluna);
            } else {
              AnalisadorLexico.adicionaConstanteLiteralTexto(aux.substring(aux.indexOf("\"") + 1, variavelAnterior - 1),
                  nrLinhas, coluna);
            }

          }
        }
        nrLinhas++;
      }
      AnalisadorLexico.finalCadeia();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static int posicaoColuna(String linha, String imagem) {
    variavelAnterior += linha.indexOf(imagem) + imagem.length();
    return variavelAnterior;
  }
}