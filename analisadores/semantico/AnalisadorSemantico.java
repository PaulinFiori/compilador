package analisadores.semantico;

import java.io.Console;
import java.util.ArrayList;

import analisadores.lexico.Token;
import analisadores.lexico.TabelaSimbolos;
import analisadores.sintatico.No;

public class AnalisadorSemantico {

  private static No raiz;
  private static ArrayList<String> erros = new ArrayList<>();
  private static Token funcaoAtual;

  public AnalisadorSemantico(No raiz) {
    this.raiz = raiz;
  }

  public static No getRaiz() {
    return raiz;
  }

  public static void setRaiz(No raiz) {
    AnalisadorSemantico.raiz = raiz;
  }

  public static ArrayList<String> getErros() {
    return erros;
  }

  public void setErros(ArrayList<String> erros) {
    this.erros = erros;
  }

  public static Token getFuncaoAtual() {
    return funcaoAtual;
  }

  public static void setFuncaoAtual(Token funcaoAtual) {
    AnalisadorSemantico.funcaoAtual = funcaoAtual;
  }

  public static void mostraErros() {
    for (String erro : erros) {
      System.out.println(erro);
    }
  }

  public void analisar() {
    analisar(raiz);
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
    case No_declaracao_em_tempo:
      return DeclaracaoEmTempo(no);
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
    funcaoAtual = id;

    if (TabelaSimbolos.getTipo(id) != null) {
      erros.add("Essa função já foi declarada: " + id + ", na linha: " + id.getLinha() + "!");
    } else {
      TabelaSimbolos.setTipo(id, tipo);
      analisar(no.getFilho(4));
      analisar(no.getFilho(7));
    }

    return null;
  }

  // <ListaParametro> ::= <Parametro><ListaParametro2>
  private Object ListaParametro(No no) {
    if (!no.getFilhos().isEmpty()) {
      analisar(no.getFilho(0));
      analisar(no.getFilho(1));
    }

    return null;
  }

  // <ListaParametro2> ::= ',' <Parametro><ListaParametro2>
  private Object ListaParametro2(No no) {
    if (!no.getFilhos().isEmpty()) {
      analisar(no.getFilho(1));
      analisar(no.getFilho(2));
    }

    return null;
  }

  // <Paramentro> ::= <TipoDeDados> ' ' id
  private Object Parametro(No no) {
    Token token = no.getFilho(1).getToken();
    String tipo = (String) analisar(no.getFilho(0));
    String tipoId = TabelaSimbolos.getTipo(token);

    if (tipoId == null) {
      TabelaSimbolos.setTipo(token, tipo);
      TabelaSimbolos.setParam(token);
    } else {
      erros.add("A variável de parametro já foi declarada! Identificador: " + token + ", na linha: " + token.getLinha() + "!");
    }

    return null;
  }

  /*
   * <Tipo_De_Dados> ::= 'inteirim' int | 'cadin' float | to_ti_falano string |
   * 'ontoco' void | ‘enjoado’ char | ‘enrabichado’ double | ‘meia_boca’ boolean
   */
  private Object TipoDados(No no) {
    return no.getFilho(0).getToken().getImagem();
  }

  // <ListaComando> ::= | <Comando><ListaComando>
  private Object ListaComando(No no) {
    if (!no.getFilhos().isEmpty()) {
      analisar(no.getFilho(0));
      analisar(no.getFilho(1));
    }

    return null;
  }

  /*
   * <Comando> ::= <Declaracao> | <Variavel> | <em_tempo> for | <nu_jeito> if |
   * <causo> else | <ques_sera> else if | <arreda> return | <panha> input |
   * <dediprosa> output | <logo_ai> while | <e_mio> do | <quale> switch |
   * <Chamada> ';' | '{'<ListaComando>'}'
   */
  private Object Comando(No no) {
    if (no.getFilhos().size() > 1) {
      analisar(no.getFilho(1));
    } else {
      analisar(no.getFilho(0));
    }

    return null;
  }

  //<Comando_quale> ::= | <po_para> break | <perai> continue
  private Object ComandoQuale(No no) {
    if (!no.getFilhos().isEmpty()) {
      analisar(no.getFilho(0));
    }

    return null;
  }

  // <Declaracao> ::= <TipoDeDados> <ListaId> ';'
  private Object Declaracao(No no) {
    String tipo = (String) analisar(no.getFilho(0));
    ArrayList<Token> listId = (ArrayList<Token>) analisar(no.getFilho(1));

    for (Token id : listId) {
      if (TabelaSimbolos.getTipo(id) != null) {
        erros.add("A variável " + id.getImagem() + " já foi declarada na linha = " + id.getLinha() + "!");
      } else {
        TabelaSimbolos.setTipo(id, tipo);
      }
    }

    return null;
  }

  // <ListaId> ::= id <ListaId2>
  private Object ListaId(No no) {
    Token id = no.getFilho(0).getToken();
    ArrayList<Token> listId2 = (ArrayList<Token>) analisar(no.getFilho(1));
    listId2.add(0, id);

    return listId2;
  }

  // <ListaId2> ::= ',' id <ListaId2> |
  private Object ListaId2(No no) {
    if (no.getFilhos().isEmpty()) {
      return new ArrayList<Token>();
    } else {
      Token id = no.getFilho(1).getToken();
      ArrayList<Token> listId2 = (ArrayList<Token>) analisar(no.getFilho(2));
      listId2.add(0, id);
      return listId2;
    }
  }

  /* 
  <Atributo> ::= id '=' <ExpressaoAritimetica> ';'
              | id '=' <Comando>
              | <TipoDeDados> id '=' <ExpressaoAritimetica> ';'
              | <TipoDeDados> id '=' <Comando>
  */
  private Object Atributo(No no) {
    if(no.getFilho(0).toString().equals("No_Tipo_De_Dados")) {
      Token id = no.getFilho(1).getToken();
      String tipo = (String) analisar(no.getFilho(0));

      if (TabelaSimbolos.getTipo(id) != null) {
        erros.add("A variável " + id.getImagem() + " já foi declarada na linha = " + id.getLinha() + "!");
      } else {
        TabelaSimbolos.setTipo(id, tipo);
      }

      if (tipo != null) {
        if(no.getFilho(3).toString().equals("No_ExpressaoAritimetica")) {
          ArrayList<Token> expressaoAritmetica = (ArrayList<Token>) analisar(no.getFilho(3));

          for (Token operando : expressaoAritmetica) {
            String tipoOperando = TabelaSimbolos.getTipo(operando);
            if (tipoOperando == null) {
              erros.add("Variável do lado direito não declarado na linha = " + id.getLinha() + "!");
            } else if (!tipoOperando.equals(tipo)) {
              erros.add("Variável do lado direito incompatível com do tipo declarado " + operando + " na linha = " + id.getLinha() + "!");
            }
          }
        } else {
          if(no.getFilho(3).toString().equals("No_panha")) analisar(no.getFilho(3));
          else if (no.getFilho(3).toString().equals("No_meia_boca")) analisar(no.getFilho(3));
          else if (no.getFilho(3).getFilho(0).toString().equals("No_Chamada")) analisar(no.getFilho(3));
        }
      } else {
        erros.add("Variável = " + id + ", não declarada na linha = " + id.getLinha() + "!");
      }
    } else {
      Token id = no.getFilho(0).getToken();
      String tipo = TabelaSimbolos.getTipo(id);
      if (tipo != null) {
        if(no.getFilho(3).toString().equals("No_ExpressaoAritimetica")) {
          ArrayList<Token> expressaoAritmetica = (ArrayList<Token>) analisar(no.getFilho(3));

          for (Token operando : expressaoAritmetica) {
            String tipoOperando = TabelaSimbolos.getTipo(operando);
            if (tipoOperando == null) {
              erros.add("Variável do lado direito não declarado na linha = " + id.getLinha() + "!");
            } else if (!tipoOperando.equals(tipo)) {
              erros.add("Variável do lado direito incompatível com do tipo declarado " + operando + " na linha = " + id.getLinha() + "!");
            }
          }
        } else {
          if(no.getFilho(2).toString().equals("No_panha")) analisar(no.getFilho(2));
          else if (no.getFilho(2).toString().equals("No_meia_boca")) analisar(no.getFilho(2));
          else if (no.getFilho(2).getFilho(0).toString().equals("No_Chamada")) analisar(no.getFilho(2));
        }
      } else {
        erros.add("Variável = " + id + ", não declarada na linha = " + id.getLinha() + "!");
      }
    }
    return null;
  }

  // <ExpressaoAritimetica> ::= <Termo><ExpressaoAritimetica2>
  private Object ExpressaoAritimetica(No no) {
    ArrayList<Token> termo = (ArrayList<Token>) analisar(no.getFilho(0));
    ArrayList<Token> expressaoAritmetica2 = (ArrayList<Token>) analisar(no.getFilho(1));
    termo.addAll(expressaoAritmetica2);

    return termo;
  }

  // <ExpressaoAritimetica2> ::= <Operador_aritimetico><ExpressaoAritimetica> |
  private Object ExpressaoAritimetica2(No no) {
    if (no.getFilhos().isEmpty()) {
      return new ArrayList<Token>();
    } else {
      ArrayList<Token> expressaoAritmetica = (ArrayList<Token>) analisar(no.getFilho(1));
      return expressaoAritmetica;
    }
  }

  // <Termo> ::= <Fator><Termo2>
  private Object Termo(No no) {
    ArrayList<Token> fator = (ArrayList<Token>) analisar(no.getFilho(0));
    ArrayList<Token> termo2 = (ArrayList<Token>) analisar(no.getFilho(1));
    fator.addAll(termo2);

    return fator;
  }

  // <Termo2> ::= <Operador_aritimetico><Termo> |
  private Object Termo2(No no) {
    if (no.getFilhos().isEmpty()) {
      return new ArrayList<Token>();
    } else {
      return analisar(no.getFilho(1));
    }
  }

  /*
   * <Fator> ::= <Operando> | <Chamada> | '('<ExpressaoAritimetica>')'
   */
  private Object Fator(No no) {
    if (no.getFilhos().size() == 1) {
      Token token = (Token) analisar(no.getFilho(0));
      ArrayList<Token> fator = new ArrayList<>();
      fator.add(token);
      return fator;
    } else {
      return analisar(no.getFilho(1));
    }
  }

  /*
   * <Operando> ::= id | cli constante literal integer | clf constante literal
   * float | cls constante literal string | clc constante literal char | cld
   * constante literal double | cla constante literal array | cll constante
   * literal list | clb constante literal boolean | cle constante literal enum
   */
  private Object Operando(No no) {
    return no.getFilho(0).getToken();
  }

  //<Operador_aritimetico> ::= '+' | '-' | ‘*’ | '/'
  private Object OperadorAritmetico(No no) {
    return null;
  }

  //<Operador_relacional> ::= '<' | '>' | '>=' | '<=' | '==' | '!=’
  private Object OperadorRelacional(No no) {
    return null;
  }

  //<Operador_atribuicao> ::= ‘+=’ | ‘-=’ | ‘*=’ | ‘/=’
  private Object OperadorAtribuicao(No no) {
    return null;
  }

  //<logo_ai> ::= ‘logo_ai’ ‘(‘ <operando> <Operador_relacional> <operando> ')’ '{' <ListaComando> '}'
  public Object LogoAi(No no) {
    analisar(no.getFilho(6));
    return null;
  }

  //<logo_ai_linha> ::= ‘logo_ai’ ‘(‘ <operando> <Operador_relacional> <operando> ‘)’ <Comando>
  public Object LogoAiLinha(No no) {
    analisar(no.getFilho(6));

    return null;
  }

  // <ExpressaoRelacional> ::=
  // <ExpressaoAritimetica><Operador_relacional><ExpressaoAritimetica>
  public Object ExpressaoRelacional(No no) {
    ArrayList<Token> expressaoAritmeticaE = (ArrayList<Token>) analisar(no.getFilho(0));
    ArrayList<Token> expressaoAritmeticaD = (ArrayList<Token>) analisar(no.getFilho(2));

    String tipoE = TabelaSimbolos.getTipo(expressaoAritmeticaE.get(0));
    String tipoD = TabelaSimbolos.getTipo(expressaoAritmeticaD.get(0));

    if (tipoE == null) {
      erros.add("Variável do operando do lado esquerdo da expressão relacional não declardo!");
    } else {
      for (Token token : expressaoAritmeticaE) {
        if (TabelaSimbolos.getTipo(token) == null) {
          erros.add("A variável não foi declarada: " + token.getImagem() + ", na linha: " + token.getLinha() + "!");
        } else if (!TabelaSimbolos.getTipo(token).equals(tipoE)) {
          erros.add("Operando com tipo incompatível do lado esquerdo da expressão relacional");
        }
      }
    }

    if (tipoD == null) {
      erros.add("Variável do operando do lado direto da expressão relacional não declardo!");
    } else {
      for (Token token : expressaoAritmeticaD) {
        if (TabelaSimbolos.getTipo(token) == null) {
          erros.add("A variável não foi declarada: " + token.getImagem() + ", na linha: " + token.getLinha() + "!");
        } else if (!TabelaSimbolos.getTipo(token).equals(tipoD)) {
          erros.add("Operando com tipo incompatível do lado direito da expressão relacional!");
        }
      }
    }

    if (tipoE != null && tipoD != null && !tipoE.equals(tipoD)) {
      erros.add("Tipo de variáveis incompatíveis na expressão relacional!");
    }

    return null;
  }

  //<Pre_Incrementacao> ::= ‘++’ id
  private Object PreIncrementacao(No no) {
    Token id = no.getFilho(1).getToken();
    String tipo = TabelaSimbolos.getTipo(id);

    if(tipo == null) erros.add("Variável = " + id + " não declarada ma linha = " + id.getLinha() + "!");

    return null;
  }

  //<Pre_Decrementacao> ::= ‘--’ id
  private Object PreDecrementacao(No no) {
    Token id = no.getFilho(1).getToken();
    String tipo = TabelaSimbolos.getTipo(id);

    if(tipo == null) erros.add("Variável = " + id + " não declarada ma linha = " + id.getLinha() + "!");

    return null;
  }

  //<Pos_Incrementacao> ::= id ‘++’
  private Object PosIncrementacao(No no) {
    Token id = no.getFilho(0).getToken();
    String tipo = TabelaSimbolos.getTipo(id);

    if(tipo == null) erros.add("Variável = " + id + " não declarada ma linha = " + id.getLinha() + "!");

    return null;
  }

  //<Pos_Decrementacao> ::= id ‘--’
  private Object PosDecrementacao(No no) {
    Token id = no.getFilho(0).getToken();
    String tipo = TabelaSimbolos.getTipo(id);

    if(tipo == null) erros.add("Vavriável = " + id + "nao declarada ma linha = " + id.getLinha() + "!");

    return null;
  }

  /*
  <Expressao_Declaracao> ::= <ExpressaoRelacional>
                         | <Pre_Incrementacao>
                         | <Pre_Decrementacao>
                         | <Pos_Incrementacao>
                         | <Pos_Decrementacao>
  */
  private Object ExpressaoDeclaracao(No no) {
    return analisar(no.getFilho(0));
  }

  // <nu_jeito> ::= 'nu_jeito' '('<ExpressaoRelacional>')' <Comando> <causo> if
  private Object NuJeito(No no) {
    analisar(no.getFilho(2));
    analisar(no.getFilho(4));
    analisar(no.getFilho(5));
    return null;
  }

  // <ques_sera> ::= ‘ques_sera’ '(' <ExpressaoRelacional> ')' <Comando> <causo>
  // else if
  private Object QuesSera(No no) {
    analisar(no.getFilho(2));
    analisar(no.getFilho(4));
    analisar(no.getFilho(5));
    return null;
  }

  // <causo> ::= 'causo' <Comando> | else
  private Object Causo(No no) {
    if (!no.getFilhos().isEmpty()) {
      analisar(no.getFilho(1));
    }
    return null;
  }

  // <arreda> ::= 'arreda' <Fator> ';' return
  private Object Arreda(No no) {
    String tipoFuncao = TabelaSimbolos.getTipo(funcaoAtual);
    ArrayList<Token> fator = (ArrayList<Token>) analisar(no.getFilho(1));

    for (Token token : fator) {
      if (TabelaSimbolos.getTipo(token) == null) {
        erros.add("Variável = " + token + " retornada não declarada na linha = " + token.getLinha() + "!");
      } else if (!TabelaSimbolos.getTipo(token).equals(tipoFuncao)) {
        erros.add("O retorno da função possui um tipo incompatível com o declarado!");
      }
    }

    return null;
  }

  // <panha> ::= 'panha' '(' id ')'';' /* input */
  private Object Panha(No no) {
    return null;
  }

  // <dediprosa> ::= 'dediprosa' '('<Operando>')' ';' output
  private Object Dediprosa(No no) {
    return null;
  }

  //<po_para> ::= ‘po_para’ ';' break
  private Object PoPara(No no) {
    return null;
  }

  //<perai> ::= ‘perai’ ';' continue
  private Object Perai(No no) {
    return null;
  }

  // <Chamada> ::= 'id' '('<ListaArgumento>')' ';'
  private Object Chamada(No no) {
    Token id = no.getFilho(0).getToken();
    ArrayList<Token> params = TabelaSimbolos.getParamDef(id);
    ArrayList<Token> listaArgumentos = (ArrayList<Token>) analisar(no.getFilho(2));

    if (params.size() == listaArgumentos.size()) {
      for (int i = 0; i < listaArgumentos.size(); i++) {
        if (!TabelaSimbolos.getTipo(params.get(i)).equals(listaArgumentos.get(i))) {
          erros.add("Os tipos dos argumentos da chamada da função são incompatíveis com os declarados!");
        }
      }
    } else {
      erros.add("Quantidade de argumentos incompatível com a declaração da função!");
    }

    return null;
  }

  // <ListaArgumento> ::= <Operando><ListaArgumento2> |
  private Object ListaArgumento(No no) {
    return null;
  }

  //<ListaArgumento2> ::= ','<Operando><ListaArgumento2>
  private Object ListaArgumento2(No no) {
    return null;
  }

  //<meia_boca> ::= di_vera /* true */ | bagaca /* false */
  private Object MeiaBoca(No no) {
    return null;
  }
  
  //<quale> ::= ‘quale’ ‘(‘ id ‘)’ <bloco_quale>
  private Object Quale(No no) {
    Token id = no.getFilho(2).getToken();
    String tipo = TabelaSimbolos.getTipo(id);

    if(tipo != null)
      analisar(no.getFilho(4));
    else 
      erros.add("Variável = " + id + " não declarada na linha = " + id.getLinha() + "!");

    return null;
  }
  
  //<bloco_quale> ::= ‘{‘ <quale_nomes> ‘}’
  private Object BlocoQuale(No no) {
    analisar(no.getFilho(1));

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
      analisar(no.getFilho(2));
    }
    else if(no.getFilhos().size() == 5)
    { 
      analisar(no.getFilho(3));
      analisar(no.getFilho(4));
    }
    else erros.add("Tem algumas coisas erradas no qui_nem ou murrinha");

    return null;
  }

  //<e_mio> ::= 'e_mio' ‘{‘ <ListaComando> ‘}’ ‘logo_ai’ ‘(‘ <operando> <Operador_relacional> <operando> ‘)’ ';' 
  private Object EMio(No no) {
    analisar(no.getFilho(2));
    analisar(no.getFilho(7));
    
    return null;
  }


  //<em_tempo> ::= em_tempo ‘(‘ <em_tempo_incio> ‘;’ <ExpressaoRelacional> ‘;’ <em_tempo_atualizar> ‘)’ ‘{’ <ListaComando> ‘}’ 
  private Object EmTempo(No no) {
    analisar(no.getFilho(2));
    analisar(no.getFilho(4));
    analisar(no.getFilho(6));
    analisar(no.getFilho(9));

   return null;
 }
  
  //<em_tempo_linha> ::= em_tempo ‘(‘<em_tempo_incio> ‘;’ <ExpressaoRelacional> ‘;’ <em_tempo_atualizar> ‘)’ <Comando> ‘;’ 
  private Object EmTempoLinha(No no) {
      analisar(no.getFilho(2));
      analisar(no.getFilho(4));
      analisar(no.getFilho(6));
      analisar(no.getFilho(8));

      return null;
  }

  //<em_tempo_incio> ::= <declaracao_em_tempo>
  private Object EmTempoInicio(No no) {
    if (!no.getFilhos().isEmpty())
      analisar(no.getFilho(0));
    else
      erros.add("Parametro do em_tempo não pode ser vazio");

    return null;
  }

  //<em_tempo_atualizar> ::= <Expressao_Declaracao> 
  private Object EmTempoAtualizar(No no) {
    if (!no.getFilhos().isEmpty())
      analisar(no.getFilho(0));
    else
      erros.add("Parametro do em_tempo não pode ser vazio");
    
    return null;
  }

  //<declaracao_em_tempo> ::= <TipoDeDados> id '=' <operando>
  private Object DeclaracaoEmTempo(No no) {
    Token id = no.getFilho(1).getToken();
    String tipo = (String) analisar(no.getFilho(0));

    if(TabelaSimbolos.getTipo(id) != null) erros.add("Variável = " + id.getImagem() + " já declarada, na linha = " + id.getLinha() + "!");
    else TabelaSimbolos.setTipo(id, tipo);

    return null;
  }

  //<cascar_fora> ::= ‘Cascar_fora’ ';'' /* end */
  private Object CascarFora(No no) {
    return null;
  }
}