package analisadores.gerador;

import analisadores.lexico.TabelaSimbolos;
import analisadores.lexico.Token;
import analisadores.sintatico.No;
import analisadores.gerador.PalavrasReservadsC;

import java.io.PrintWriter;
import java.util.ArrayList;

public class GeradorCodigo {

    private static No raiz;
    private static PrintWriter out;
    private static int identa = 0;

    private GeradorCodigo(No raiz, PrintWriter out) {
      this.raiz = raiz;
      this.out = out;
    }

    public void gerar() {
      gerarCabecalho();
      analisar(raiz);
    }

    private void gerarCabecalho() {
      out.println("#include<stdio.h>");
      out.println("#include<stdlib.h>");

    }
    
    public Object analisar(No no) {
    switch (no.getTipo()) {
    case No_ListaFuncao:
      return ListaFuncao(no);
    case No_DefinirFuncao:
      return Funcao(no);
    case No_ListaParametro:
      return ListaParametro(no);
    case No_ListaParametro2:
      return ListaParametro2(no);
    case No_Paramentro:
      return Parametro(no);
    case No_Tipo_De_Dados:
      return TipoDados(no);
    case No_ListaComando:
      return ListaComando(no);
    case No_Comando:
      return Comando(no);
    case No_Comando_quale: 
      return ComandoQuale(no);
    case No_Declaracao:
      return Declaracao(no);
    case No_ListaId:
      return ListaId(no);
    case No_ListaId2:
      return ListaId2(no);
    case No_Atributo:
      return Atributo(no);
    case No_ExpressaoAritimetica:
      return ExpressaoAritimetica(no);
    case No_ExpressaoAritimetica2:
      return ExpressaoAritimetica2(no);
    case No_Termo:
      return Termo(no);
    case No_Termo2:
      return Termo2(no);
    case No_Fator:
      return Fator(no);
    case No_Operando:
      return Operando(no);
    case No_Operador_aritimetico:
      return OperadorAritmetico(no);
    case No_Operador_relacional:
      return OperadorRelacional(no);
    case No_Operador_atribuicao:
      return OperadorAtribuicao(no);
    case No_ExpressaoRelacional:
      return ExpressaoRelacional(no);
    case No_Pre_Incrementacao: 
      return PreIncrementacao(no);
    case No_Pre_Decrementacao: 
      return PreDecrementacao(no);
    case No_Pos_Incrementacao: 
      return PosIncrementacao(no);
    case No_Pos_Decrementacao: 
      return PosDecrementacao(no);
    case No_Expressao_Declaracao: 
      return ExpressaoDeclaracao(no);
    case No_nu_jeito:
      return NuJeito(no);
    case No_ques_sera:
      return QuesSera(no);
    case No_causo:
      return Causo(no);
    case No_arreda:
      return Arreda(no);
    case No_panha:
      return Panha(no);
    case No_dediprosa:
      return Dediprosa(no);
    case No_po_para:
      return PoPara(no);
    case No_perai: 
      return Perai(no);
    case No_Chamada:
      return Chamada(no);
    case No_ListaArgumento:
      return ListaArgumento(no);
    case No_ListaArgumento2:
      return ListaArgumento2(no);
    case No_meia_boca: 
      return MeiaBoca(no);
    case No_quale:
      return Quale(no);
    case No_bloco_quale: 
      return BlocoQuale(no);
    case No_quale_nomes: 
      return QualeNomes(no);
    case No_quale_nome: 
      return QualeNome(no);
    case No_em_tempo:
      return EmTempo(no);
    case No_em_tempo_linha: 
      return EmTempoLinha(no);
    case No_em_tempo_incio:
      return EmTempoInicio(no);
    case No_em_tempo_atualizar:
      return EmTempoAtualizar(no);
    case No_logo_ai: 
      return LogoAi(no);
    case No_logo_ai_linha:
      return LogoAiLinha(no);
    case No_e_mio: 
      return EMio(no);
    case No_cascar_fora: 
      return CascarFora(no);
    }

    return null;
  }

  // <ListaFuncao> ::= <DefinirFuncao><ListaFuncao> |
  private Object ListaFuncao(No no) {
    if (!no.getFilhos().isEmpty()) {
      analisar(no.getFilho(0));
      analisar(no.getFilho(1));
    }

    return null;
  }

  // <DefinirFuncao> ::= <TipoDeDados> 'encasqueta' id '(' <ListaParametro> ')' '{' <ListaComando> '}'
  private Object Funcao(No no) {
    Token id = no.getFilho(2).getToken();
    String tipo = (String) analisar(no.getFilho(0));
    out.print(tipo + " " + no.getFilho(2) + " " + no.getFilho(3));
    analisar(no.getFilho(3));
    ArrayList<Token> params = TabelaSimbolos.getParamDef(id);

    if (!params.isEmpty()){
      out.println(PalavrasReservadsC.tipo(TabelaSimbolos.getTipo(params.get(0))) + " " + params.get(0).getImagem());
    }
    for (int i = 1; i < params.size(); i++) {
      String tipo1 = PalavrasReservadsC.tipo(TabelaSimbolos.getTipo(params.get(i)));
      out.println(", " + tipo1 + " " + params.get(i).getClasse());
    }

    out.print(no.getFilho(5) + " " );
    out.print(no.getFilho(6) + "\n\n");

    TabelaSimbolos.setTipoDef(id, tipo);
    TabelaSimbolos.setTipo(id, tipo);

    analisar(no.getFilho(7));

    out.print("\n" + no.getFilho(8) + "\n\n") ;
    
    return null;
  }

  //<ListaParametro> ::= <Parametro><ListaParametro2>
  private Object ListaParametro(No no) {
    if(!no.getFilhos().isEmpty()) {
      analisar(no.getFilho(0));
      analisar(no.getFilho(1));
    }

    return null;
  }

  //<ListaParametro2> ::= ',' <Parametro><ListaParametro2>
  private Object ListaParametro2(No no) {
    if(!no.getFilhos().isEmpty()) {
      analisar(no.getFilho(1));
      analisar(no.getFilho(2));
    }

    return null;
    }

  //<Paramentro> ::= <TipoDeDados> ' ' id
  private Object Parametro(No no) {
    Token token = no.getFilho(1).getToken();
    String tipo = (String) analisar(no.getFilho(0));

    return null;
  }

  /*
  <Tipo_De_Dados> ::= 'inteirim' int
        		      | 'cadin' float
        		      | to_ti_falano string
        		      | 'ontoco' void
                  | ‘enjoado’ char
                  | ‘enrabichado’ double
                  | ‘logali ’ long
                  | ‘encantoa’ array
                  | ‘trenheira’ list
                  | ‘meia_boca’ boolean
                  | ‘uai’ enum
                  | ‘paradeza’
  */
  private Object TipoDados(No no) {
    return no.getFilho(0).getToken().getImagem();
  }

  private void formata() {
    for (int i = 0; i< identa; i++) {
      out.print("\t");
    }
  }
  
  //<ListaComando> ::=  | <Comando><ListaComando>
  private Object ListaComando(No no) {
    if(!no.getFilhos().isEmpty()) {
      formata();
      analisar(no.getFilho(0));
      analisar(no.getFilho(1));
    }

    return null;
  }

  /*
  <Comando> ::= <Declaracao>
            | <Variavel>
            | <em_tempo> for
            | <nu_jeito> if
            | <causo> else
	          | <ques_sera> else if
            | <arreda> return
            | <panha> input
            | <dediprosa> output
            | <logo_ai> while
            | <e_mio> do
            | <quale> switch
            | <Chamada>';' 
            | '{'<ListaComando>'}'
  */
  private Object Comando(No no) {
    if(no.getFilhos().size() > 1) {
      out.print(no.getFilho(0).getToken().getImagem() + "\n");
      analisar(no.getFilho(1));
      formata();
      out.print(no.getFilho(2).getToken().getImagem() + "\n");
    } else {
      analisar(no.getFilho(0));
    }

    return null;
  }

  /*<Comando_quale> ::= | <po_para> break | <perai> continue
  */
  private Object ComandoQuale(No no) {
    if(!no.getFilhos().isEmpty())
      analisar(no.getFilho(0));
    
    return null;
  }

  //<Declaracao> ::= <TipoDeDados> <ListaId> ';'
  private Object Declaracao(No no) {
    ArrayList<Token> listId = (ArrayList<Token>) analisar(no.getFilho(1));
    String tipo = (String) analisar(no.getFilho(0));

    out.print("\n\t" + tipo + " " + listId.get(0));
    for (int i = 1; i < listId.size(); i++) {
      out.print(", " + listId.get(i));
    }

    out.print(no.getFilho(2).getToken().getImagem()+ "\n");

    return null;
  }

  //<ListaId> ::= id <ListaId2>
  private Object ListaId(No no) {
    Token id = no.getFilho(0).getToken();
    ArrayList<Token> listId2 = (ArrayList<Token>) analisar(no.getFilho(1));
    listId2.add(0, id);

    return listId2;
  }

  //<ListaId2> ::= ',' id <ListaId2>
  private Object ListaId2(No no) {
    if(no.getFilhos().isEmpty()) {
        return new ArrayList<Token>();
    } else {
      Token id = no.getFilho(1).getToken();
      ArrayList<Token>listId2 = (ArrayList<Token>) analisar(no.getFilho(2));
      listId2.add(0, id);
      return listId2;
    }
  }

  //<Atributo> ::= id '=' <ExpressaoAritimetica> ';'
  private Object Atributo(No no) {
    out.print("\n");
    formata();
    Token id = no.getFilho(0).getToken();
    out.print("  "+ id + " " + no.getFilho(1));
    String tipo = TabelaSimbolos.getTipo(id);
    analisar(no.getFilho(2));
    out.print(no.getFilho(3).getToken().getImagem() + "\n");

    return null;
  }

  //<ExpressaoAritimetica> ::= <Termo><ExpressaoAritimetica2>
  private Object ExpressaoAritimetica(No no) {
    ArrayList<Token> termo = (ArrayList<Token>) analisar(no.getFilho(0));
    ArrayList<Token> expressaoAritmetica2 = (ArrayList<Token>) analisar(no.getFilho(1));
    termo.addAll(expressaoAritmetica2);
    return termo;
  }

  //<ExpressaoAritimetica2> ::= <Operador_aritimetico><ExpressaoAritimetica> |
  private Object ExpressaoAritimetica2(No no) {
    if(no.getFilhos().isEmpty()) {
      return new ArrayList<Token>();
    } else {
      out.print(no.getFilho(0).getFilho(0).getToken().getImagem());
      ArrayList<Token> expressaoAritmetica = (ArrayList<Token>) analisar(no.getFilho(1));
      return expressaoAritmetica;
    }
  }

  //<Termo> ::= <Fator><Termo2>
  private Object Termo(No no) {
    ArrayList<Token> fator = (ArrayList<Token>) analisar(no.getFilho(0));
    ArrayList<Token> termo2 = (ArrayList<Token>) analisar(no.getFilho(1));
    fator.addAll(termo2);

    return fator;
  }

  //<Termo2> ::= <Operador_aritimetico<Termo> | 
  private Object Termo2(No no) {
    if(no.getFilhos().isEmpty()){
      return new ArrayList<Token>();
    } else {
      out.print(no.getFilho(0).getFilho(0).getToken().getImagem()); 
      ArrayList<Token> expressaoAritmetica = (ArrayList<Token>) analisar(no.getFilho(1));
      return expressaoAritmetica;
    }

  }

  /*
  <Fator> ::= <Operando> 
          | <Chamada> 
          | '('<ExpressaoAritimetica>')'
  */
  private Object Fator(No no) {
    if(no.getFilhos().size() == 1) {
      Token token = (Token) analisar(no.getFilho(0));
      ArrayList<Token> fator = new ArrayList<>();
      fator.add(token);
      return fator;
    } else {
      out.print(" " + no.getFilho(0).getToken().getImagem());
      ArrayList<Token> tokens = (ArrayList<Token>) analisar(no.getFilho(1));
      out.print(no.getFilho(2).getToken().getImagem());
      return tokens;
    }
  }

  /*
  <Operando> ::= id
             | cli constante literal integer
             | clf constante literal float
             | cls constante literal string
             | clc constante literal char
             | cld  constante literal double
             | clb constante literal boolean
             | cle constante literal enum
  */
  private Object Operando(No no){
    return no.getFilho(0).getToken().getImagem();
  }

   //<Operador_aritimetico> ::= '+' | '-' | ‘*’ | '/'
  private Object OperadorAritmetico(No no) {
    return no.getFilho(0).getToken().getImagem();
  }

  //<Operador_relacional> ::= '<' | '>' | '>=' | '<=' | '==' | '!=’
  private Object OperadorRelacional(No no) {
    return no.getFilho(0).getToken().getImagem();
  }

  //<Operador_atribuicao> ::= ‘+=’ | ‘-=’ | ‘*=’ | ‘/=’
  private Object OperadorAtribuicao(No no) {
    return no.getFilho(0).getToken().getImagem();
  }

  //<ExpressaoRelacional> ::= <ExpressaoAritimetica><Operador_relacional><ExpressaoAritimetica>
  private Object ExpressaoRelacional(No no) {
    ArrayList<Token> expressaoAritmeticaE = (ArrayList<Token>) analisar(no.getFilho(0));
    ArrayList<Token> expressaoAritmeticaD = (ArrayList<Token>) analisar(no.getFilho(2));

    for (Token token : expressaoAritmeticaE) {
      out.print(" " + token.getImagem());
    }

      out.print(analisar(no.getFilho(1)) + " ");

      for (Token token : expressaoAritmeticaD) {
        out.print(" " + token.getImagem());
      }

    return null;
  }

  //<Pre_Incrementacao> ::= ‘++’ id
  private Object PreIncrementacao(No no)
  {
    String id = no.getFilho(1).getToken().getImagem();
    String operador = no.getFilho(0).getToken().getImagem();
    out.print(operador + id);

    return null;
  }

  //<Pre_Decrementacao> ::= ‘--’ id
  private Object PreDecrementacao(No no)
  {
    String id = no.getFilho(1).getToken().getImagem();
    String operador = no.getFilho(0).getToken().getImagem();
    out.print(operador + id);
    
    return null;
  }

  //<Pos_Incrementacao> ::= id ‘++’
  private Object PosIncrementacao(No no)
  {
    String id = no.getFilho(0).getToken().getImagem();
    String operador = no.getFilho(1).getToken().getImagem();
    out.print(id + operador);
    
    return null;
  }

  //<Pos_Decrementacao> ::= id ‘--’
  private Object PosDecrementacao(No no)
  {
    String id = no.getFilho(0).getToken().getImagem();
    String operador = no.getFilho(1).getToken().getImagem();
    out.print(id + operador);
    
    return null;
  }

  /*
  <Expressao_Declaracao> ::= <ExpressaoRelacional>
                         | <Pre_Incrementacao>
                         | <Pre_Decrementacao>
                         | <Pos_Incrementacao>
                         | <Pos_Decrementacao>
  */
  private Object ExpressaoDeclaracao(No no)
  {
    analisar(no.getFilho(0));

    return null;
  }

  // <nu_jeito> ::= 'nu_jeito' '('<ExpressaoRelacional>')' <Comando> <causo> if
  private Object NuJeito(No no) {
    out.print(PalavrasReservadsC.tipo(no.getFilho(0).getToken().getImagem()));
    out.print(no.getFilho(1));
    analisar(no.getFilho(2));
    out.print(no.getFilho(3));
    analisar(no.getFilho(4));
    analisar(no.getFilho(5));

    return null;
  }

  //<ques_sera> ::= ‘ques_sera’ '(' <ExpressaoRelacional> ')' <Comando> <causo> else if
  private Object QuesSera(No no) {
    out.print(PalavrasReservadsC.tipo(no.getFilho(0).getToken().getImagem()));
    out.print(no.getFilho(1));
    analisar(no.getFilho(2));
    out.print(no.getFilho(3));
    analisar(no.getFilho(4));
    analisar(no.getFilho(5));

    return null;
  }

  //<causo> ::= 'causo' <Comando> | else
  private Object Causo(No no) {
    if(!no.getFilhos().isEmpty()) {
      out.print(PalavrasReservadsC.tipo(no.getFilho(0).getToken().getImagem()));
      out.print(no.getFilho(1));
    }

    return null;
  }

  //<arreda> ::= 'arreda' <Fator> ';' return
  private Object Arreda(No no) {
    out.print(PalavrasReservadsC.tipo(no.getFilho(0).getToken().getImagem()));
    analisar(no.getFilho(1));
    out.print(no.getFilho(2));

    return null;
  }

  //<panha> ::= 'panha' '(' id ')'';' input
  private Object Panha(No no) {
    out.print(PalavrasReservadsC.tipo(no.getFilho(0).getToken().getImagem()) + no.getFilho(1));
    Token id = no.getFilho(2).getToken();
    out.print(id);
    out.print(no.getFilho(3));
    out.print(no.getFilho(4) + "\n");

    return null;
  }

  //<dediprosa> ::= 'dediprosa' '('<Operando>')' ';' output
  private Object Dediprosa(No no) {
    out.print(PalavrasReservadsC.tipo(no.getFilho(0).getToken().getImagem()) + no.getFilho(1));
    analisar(no.getFilho(2));
    out.print(no.getFilho(3));
    out.print( no.getFilho(4) + "\n");

    return null;
  }

  //<po_para> ::= ‘po_para’ ';' break
  private Object PoPara(No no) {
    out.print(PalavrasReservadsC.tipo(no.getFilho(0).getToken().getImagem()) + no.getFilho(1));

    return null;
  }

  //<perai> ::= ‘perai’ ';' /* continue */
  private Object Perai(No no) {
    out.print(PalavrasReservadsC.tipo(no.getFilho(0).getToken().getImagem()) + no.getFilho(1));

    return null;
  }

  //<Chamada> ::= 'id' '('<ListaArgumento>')' ';'
  private Object Chamada(No no) {
    Token id = no.getFilho(0).getToken();
    ArrayList<Token> listaArgumentos = (ArrayList<Token>) analisar(no.getFilho(2));

    out.print(id);
    out.print(no.getFilho(1));

    for (int i = 0; i < listaArgumentos.size(); i++) {
      out.print(listaArgumentos.get(i));
    }

    out.print(no.getFilho(3));
    out.print( no.getFilho(4) + "\n");

    return null;
  }

  //<ListaArgumento> ::= <Operando><ListaArgumento2> |
  private Object ListaArgumento(No no) {
    Token operando = no.getFilho(0).getToken();
    ArrayList<Token> listaArgumento2 = (ArrayList<Token>) analisar(no.getFilho(1));
    listaArgumento2.add(0, operando);

    return listaArgumento2;
  }

  //<ListaArgumento2> ::= ','<Operando><ListaArgumento2> |
  private Object ListaArgumento2(No no) {
    if(no.getFilhos().isEmpty()) {
        return new ArrayList<Token>();
    } else {
      Token operando = no.getFilho(1).getToken();
      ArrayList<Token> listaArgumento2 = (ArrayList<Token>) analisar(no.getFilho(2));
      listaArgumento2.add(0, no.getFilho(0).getToken());
      listaArgumento2.add(1, operando);
      return listaArgumento2;
    }
  }

  /*
  <meia_boca> ::= di_vera true
              | bagaca false
  */
  private Object MeiaBoca(No no) {
    out.print(PalavrasReservadsC.tipo(no.getFilho(0).getToken().getImagem()));

    return null;
  }

  //<quale> ::= ‘quale’ ‘(‘ id ‘)’ <bloco_quale>
  private Object Quale(No no) {
    out.print(PalavrasReservadsC.tipo(no.getFilho(0).getToken().getImagem()) + no.getFilho(1));
    Token id = no.getFilho(2).getToken();
    out.print(id);
    out.print(no.getFilho(3));
    analisar(no.getFilho(4));

    return null;
  }

  //<bloco_quale> ::= ‘{‘ <quale_nomes> ‘}’
  private Object BlocoQuale(No no) {
    out.print(no.getFilho(0));
    analisar(no.getFilho(1));
    out.print(no.getFilho(4));

    return null;
  }

  /*
  <quale_nomes> ::= <quale_nome> 
                 | <quale_nomes> <quale_nome>
  */
  private Object QualeNomes(No no) {
    if(no.getFilhos().size() == 1) {
      analisar(no.getFilho(0));
    } else {
      analisar(no.getFilho(0));
      analisar(no.getFilho(1));
    }

    return null;
  }

  /*
  <quale_nome> ::= ‘qui_nem ’ <operando> ‘:’ <ListaComando> <comando_quale>  
               | ‘murrinha’ ':' <ListaComando>
  */
  private Object QualeNome(No no) {

    if (no.getFilhos().size() == 3) {
      out.print(PalavrasReservadsC.tipo(no.getFilho(0).getToken().getImagem()));
      analisar(no.getFilho(1));
      analisar(no.getFilho(2));
    }
    else if(no.getFilhos().size() == 5)
    { 
      out.print(PalavrasReservadsC.tipo(no.getFilho(0).getToken().getImagem()));
      analisar(no.getFilho(1));
      out.print(no.getFilho(2));
      analisar(no.getFilho(3));
      analisar(no.getFilho(4));
    }

    return null;
  }

  //<em_tempo> ::= em_tempo ‘(‘ <em_tempo_incio> ‘;’ <ExpressaoRelacional> ‘;’ <em_tempo_atualizar> ‘)’ ‘{’ <ListaComando> ‘}’
  private Object EmTempo(No no)
  {
    out.print(PalavrasReservadsC.tipo(no.getFilho(0).getToken().getImagem()) + no.getFilho(1));
    analisar(no.getFilho(2));
    out.print(" " + no.getFilho(3));
    analisar(no.getFilho(4));
    out.print(" " + no.getFilho(5));
    analisar(no.getFilho(6));
    out.print(no.getFilho(7) + " " + no.getFilho(8));
    analisar(no.getFilho(9));
    out.print("\n\t" + no.getFilho(10));

    return null;
  }

  //<em_tempo_linha> ::= em_tempo ‘(‘<em_tempo_incio> ‘;’ <ExpressaoRelacional> ‘;’ <em_tempo_atualizar> ‘)’ <Comando>
  private Object EmTempoLinha(No no)
  {
    out.print(PalavrasReservadsC.tipo(no.getFilho(0).getToken().getImagem()) + no.getFilho(1));
    analisar(no.getFilho(2));
    out.print(" " + no.getFilho(3));
    analisar(no.getFilho(4));
    out.print(" " + no.getFilho(5));
    analisar(no.getFilho(6));
    out.print(no.getFilho(7) + " ");
    analisar(no.getFilho(8));

    return null;
  }

  //<em_tempo_incio> ::= <Expressao_Declaracao> | <Declaracao>
  private Object EmTempoInicio(No no)
  {
    analisar(no.getFilho(0));

    return null;
  }

  //<em_tempo_atualizar> ::= <Expressao_Declaracao>
  private Object EmTempoAtualizar(No no)
  {
    analisar(no.getFilho(0));

    return null;
  }

  //<logo_ai> ::= ‘logo_ai’ ‘(‘ <ExpressaoRelacional> ')’  '{' <ListaComando> '}'
  private Object LogoAi(No no) {
    out.print(PalavrasReservadsC.tipo(no.getFilho(0).getToken().getImagem()) + no.getFilho(1));
    analisar(no.getFilho(2));
    out.print(no.getFilho(3) + " " + no.getFilho(4));
    analisar(no.getFilho(5));
    out.print("\n\t" + no.getFilho(6));

    return null;
  }

  //<logo_ai_linha> ::= ‘logo_ai’ ‘(‘ <ExpressaoRelacional> ‘)’ <Comando>
  private Object LogoAiLinha(No no) {
    out.print(PalavrasReservadsC.tipo(no.getFilho(0).getToken().getImagem()) + no.getFilho(1));
    analisar(no.getFilho(2));
    out.print(no.getFilho(3));
    analisar(no.getFilho(4));

    return null;
  }

  //<e_mio> ::= 'e_mio' ‘{‘ <ListaComando> ‘}’ ‘logo_ai’ ‘(‘ <operando> <Operador_relacional> <operando> ‘)’ ';
  private Object EMio(No no) {
    out.print(PalavrasReservadsC.tipo(no.getFilho(0).getToken().getImagem()) + no.getFilho(1));
    analisar(no.getFilho(2));
    out.print("\n\t" + no.getFilho(3) + PalavrasReservadsC.tipo(no.getFilho(4).getToken().getImagem()) + no.getFilho(5));
    out.print(analisar(no.getFilho(4)));
    analisar(no.getFilho(5));
    out.print(analisar(no.getFilho(6)));
    out.print(no.getFilho(7));

    return null;
  }

  //<cascar_fora> ::= ‘cascar_fora’ /* end */
  private Object CascarFora(No no) {
    out.print(PalavrasReservadsC.tipo(no.getFilho(0).getToken().getImagem()) + "(" + ")" + 0 + ";");

    return null;
  }
}