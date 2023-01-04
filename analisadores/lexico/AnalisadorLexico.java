package analisadores.lexico;

import java.util.ArrayList;
import java.util.List;

public class AnalisadorLexico {
    /*
    PR -> Palavra Reservada
    DE -> Delimitador
    OPA -> Operador Aritmetico
    OPL -> Operador Logico
    OA -> Operador Atribuicão
    OR -> Operador Relacional

    cli -> constante literal integer
    clf -> constante literal float 
    cls -> constante literal string
    clc -> constante literal char 
    clb -> constante literal boolean
    cle -> constante literal enum 
     */

    private static final List<Token> tokens = new ArrayList<>();
    private static final List<Erros> erros = new ArrayList<>();

    public enum Simbolos{
        PalavraReservada, Delimitador, OperadorAritmetico, OperadorAtribuicao, OperadorRelacional, CLI, CLF, CLS, CLC, CLB, CLD, CLE, ID, $
    }

    public static void analisar(String imagem, int nrLinhas, int coluna) 
    {
        if(PalavrasEstaticas.isPalavraReservada(imagem)) {
          if(imagem.equals("di_vera") || imagem.equals("bagaça")) tokens.add(new Token(imagem, Simbolos.CLB.toString(), -1, nrLinhas, coluna));
          else tokens.add(new Token(imagem, Simbolos.PalavraReservada.toString(), -1, nrLinhas, coluna));
        }
        else if(PalavrasEstaticas.isDelimitador(imagem))
            tokens.add(new Token(imagem, Simbolos.Delimitador.toString(), -1, nrLinhas, coluna));
        else if(PalavrasEstaticas.isOperadorAritmetico(imagem))
            tokens.add(new Token(imagem, Simbolos.OperadorAritmetico.toString(), -1, nrLinhas, coluna));
        else if(PalavrasEstaticas.isOperadorRelacional(imagem))
            tokens.add(new Token(imagem, Simbolos.OperadorRelacional.toString(), -1, nrLinhas, coluna));
        else if(PalavrasEstaticas.isOperadorAtribuicao(imagem))
            tokens.add(new Token(imagem, Simbolos.OperadorAtribuicao.toString(), -1, nrLinhas, coluna));
        else if(imagem.matches("\\d\\d*"))
            tokens.add(new Token(imagem, Simbolos.CLI.toString(), -1, nrLinhas, coluna));
        else if(imagem.matches("\\d\\d*.\\d\\d*"))
            tokens.add(new Token(imagem, Simbolos.CLF.toString(), -1, nrLinhas, coluna));
        else if(imagem.matches("\\d\\d*.\\d\\d\\d*"))
            tokens.add(new Token(imagem, Simbolos.CLD.toString(), -1, nrLinhas, coluna));
        else if(imagem.matches("\\p{Alpha}\\p{Alnum}*"))
            tokens.add(new Token(imagem, Simbolos.ID.toString(), -1, nrLinhas, coluna));
        else erros.add(new Erros(imagem, nrLinhas, coluna));
    }

    public static void adicionaConstanteLiteralTexto(String imagem, int linha, int coluna) 
    {
        tokens.add(new Token(imagem, Simbolos.CLS.toString(), -1, linha, coluna));
    }

    public static void finalCadeia() 
    {
        tokens.add(new Token("$", Simbolos.$.toString(), -1, -1, -1));
    }

    public static void printTokens()
    {
        System.out.println("Tokens: ");
        for (Token token :
                tokens) 
        {
            System.out.println(token +" - "+ token.getClasse() +" | Linha: "+token.getLinha() +" | Coluna: "+token.getColuna());
        }
    }

    public static void printErros()
    {
        System.out.println("ERROS: ");
        for (Erros erro : erros) {
            System.out.println(erro);
        }
    }

    public static ArrayList<Erros> getErros() 
    {
        return (ArrayList<Erros>) erros;
    }

    public static ArrayList<Token> getTokens() 
    {
        return (ArrayList<Token>) tokens;
    }
}