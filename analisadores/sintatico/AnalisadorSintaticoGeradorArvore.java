package analisadores.sintatico;

import java.util.ArrayList;

import analisadores.lexico.TabelaSimbolos;
import analisadores.lexico.Token;

public class AnalisadorSintaticoGeradorArvore {
  private Token token;
  private int pToken;
  private static ArrayList<Token> tokens = new ArrayList<Token>();
  private static ArrayList<String> erros = new ArrayList<String>();
  private String escopo = "";
  private static No raiz;

  @SuppressWarnings("static-access")
  public AnalisadorSintaticoGeradorArvore(ArrayList<Token> tokens) {
    this.tokens = tokens;
  }

  public void leToken() {
    if (token != null && token.getClasse().equals("ID")) {
      TabelaSimbolos.addSimbolo(token, escopo);
    }

    if (pToken < tokens.size()) {
      token = tokens.get(pToken);
      pToken++;
    }
  }

  public void geraErro(String imagem) {
    String erro;
    Token lastToken = lastToken();

    erro = "ERRO SINTÁTICO: era esperado um(a) " + imagem + ". Erro na Linha: " + lastToken.getLinha() + "\n";

    erros.add(erro);
  }

  public Token lookAhead() {
    return tokens.get(pToken);
  }

  public void analisar() {
    pToken = 0;
    leToken();
    raiz = ListaFuncao();

    if (!token.getClasse().equals("$")) {
      erros.add("Erro: esperado um final de cadeia.");
    }
  }

  public Token lastToken() {
    return tokens.get(pToken - 2);
  }

  // <ListaFuncao> ::= <Funcao><ListaFuncao> |
  private No ListaFuncao() {
    No no = new No(TipoNo.No_ListaFuncao);

    if (token.getImagem().equals("inteirim") || token.getImagem().equals("cadin")
        || token.getImagem().equals("to_ti_falano") || token.getImagem().equals("ontoco")
        || token.getImagem().equals("enjoado") || token.getImagem().equals("enrabichado") || token.getImagem().equals("meia_boca")) {
      Token lookAhead = lookAhead();
      if (lookAhead.getImagem().equals("encasqueta")) {
        no.addFilho(Funcao());
        no.addFilho(ListaFuncao());
      }
    }

    return no;
  }

  // <DefinirFuncao> ::= <TipoDeDados> 'encasqueta' id '(' <ListaParametro> ')' '{' <ListaComando> '}'
  private No Funcao() {
    No no = new No(TipoNo.No_DefinirFuncao);

    no.addFilho(TipoDeDados());
    if (token.getImagem().equals("encasqueta")) {
      no.addFilho(new No(token));
      leToken();
      if (token.getClasse().equals("ID")) {
        escopo = token.getImagem();
        System.out.println("-> ESCOPO " + escopo);
        no.addFilho(new No(token));
        leToken();
        if (token.getImagem().equals("(")) {
          no.addFilho(new No(token));
          leToken();
          no.addFilho(ListaParametro());
          if (token.getImagem().equals(")")) {
            no.addFilho(new No(token));
            leToken();
            if (token.getImagem().equals("{")) {
              no.addFilho(new No(token));
              leToken();
              no.addFilho(ListaComando());
              if (token.getImagem().equals("}")) {
                no.addFilho(new No(token));
                leToken();
              } else
                geraErro("}");
            } else
              geraErro("{");
          } else
            geraErro(")");
        } else
          geraErro("(");
      } else
        geraErro("identificador");
    } else
      geraErro("encasqueta");

    return no;
  }

  /*
   * <Tipo_De_Dados> ::= 'inteirim' int | 'cadin' float | to_ti_falano string |
   * 'ontoco' void | ‘enjoado’ char | ‘enrabichado’ double | ‘meia_boca’ boolean
   */
  private No TipoDeDados() {
    No no = new No(TipoNo.No_Tipo_De_Dados);

    if (token.getClasse().equals("PalavraReservada")) {
      if (token.getImagem().equals("inteirim") || token.getImagem().equals("cadin")
          || token.getImagem().equals("to_ti_falano") || token.getImagem().equals("ontoco")
          || token.getImagem().equals("enjoado") || token.getImagem().equals("enrabichado") || token.getImagem().equals("meia_boca")) {
        no.addFilho(new No(token));
        leToken();
      } else
        geraErro(
            "'inteirim', 'cadin', 'to_ti_falano', 'ontoco', 'enjoado' , 'enrabichado' ou 'meia_boca'");
    } else
      geraErro("palavra reservada");

    return no;
  }

  // <ListaParametro> ::= <Parametro><ListaParametro2>
  private No ListaParametro() {
    No no = new No(TipoNo.No_ListaParametro);

    no.addFilho(Parametro());
    no.addFilho(ListaParametro2());

    return no;
  }

  // | <Paramentro> ::= <TipoDeDados> ' ' id
  private No Parametro() {
    No no = new No(TipoNo.No_Paramentro);

    if (token.getImagem().equals("inteirim") || token.getImagem().equals("cadin")
        || token.getImagem().equals("to_ti_falano") || token.getImagem().equals("ontoco")
        || token.getImagem().equals("enjoado") || token.getImagem().equals("enrabichado") || token.getImagem().equals("meia_boca")) {
      no.addFilho(TipoDeDados());
      if (token.getClasse().equals("ID")) {
        no.addFilho(new No(token));
        leToken();
      }
    } else
      geraErro("identificador");

    return no;
  }

  // <ListaParametro2> ::= ',' <Parametro><ListaParametro2>
  private No ListaParametro2() {
    No no = new No(TipoNo.No_ListaParametro2);

    if (token.getImagem().equals(",")) {
      no.addFilho(new No(token));
      leToken();
      no.addFilho(Parametro());
      no.addFilho(ListaParametro2());
    }

    return no;
  }

  /* <ListaComando> ::= | <Comando><ListaComando> */
  private No ListaComando() {
    No no = new No(TipoNo.No_ListaComando);

    if (token.getClasse().equals("PalavraReservada") || token.getClasse().equals("ID")
        || token.getImagem().equals("em_tempo")
        || token.getImagem().equals("nu_jeito") || token.getImagem().equals("causo")
        || token.getImagem().equals("ques_sera") || token.getImagem().equals("arreda")
        || token.getImagem().equals("panha") || token.getImagem().equals("logo_ai") 
        || token.getImagem().equals("e_mio")
        || token.getImagem().equals("quale") || token.getImagem().equals("cascar_fora")
        || token.getImagem().equals("{")) {
      no.addFilho(Comando());
      no.addFilho(ListaComando());
    }

    return no;
  }

  /*
   * <Comando> ::= <Declaracao> | <Atributo> | <em_tempo> for | <nu_jeito> if |
   * <causo> else | <ques_sera> else if | <arreda> return | <panha> input |
   * <dediprosa> output | <logo_ai> while | <e_mio> do | <quale> switch |
   * <Chamada>';' | '{'<ListaComando>'}'
   */
  private No Comando() {
    No no = new No(TipoNo.No_Comando);

    if (token.getImagem().equals("inteirim") || token.getImagem().equals("cadin")
        || token.getImagem().equals("to_ti_falano") || token.getImagem().equals("ontoco")
        || token.getImagem().equals("enjoado") || token.getImagem().equals("enrabichado") || token.getImagem().equals("meia_boca")) {
      Token lookAhead = lookAhead();
      if (lookAhead.getClasse().equals("ID")) {
        Token token2 = tokens.get(pToken+1);
        if (token2.getImagem().equals("=")) {
          no.addFilho(Atributo());
        } else {
          no.addFilho(Declaracao());
        }
      } else
        geraErro("identificador");
    } else if (token.getClasse().equals("ID")) {
      Token lookAhead = lookAhead();
      if (lookAhead.getImagem().equals("=")) {
        no.addFilho(Atributo());
      } else {
        if (lookAhead.getImagem().equals("(")) {
          no.addFilho(Chamada());
          if (token.getImagem().equals(";")) {
            no.addFilho(new No(token));
            leToken();
          } else
            geraErro(";");
        }
      }
    } else if (token.getImagem().equals("em_tempo"))
      no.addFilho(em_tempo());
    else if (token.getImagem().equals("nu_jeito"))
      no.addFilho(nu_jeito());
    else if (token.getImagem().equals("causo"))
      no.addFilho(causo());
    else if (token.getImagem().equals("ques_sera"))
      no.addFilho(ques_sera());
    else if (token.getImagem().equals("arreda"))
      no.addFilho(arreda());
    else if (token.getImagem().equals("panha"))
      no.addFilho(panha());
    else if (token.getImagem().equals("dediprosa"))
      no.addFilho(dediprosa());
    else if (token.getImagem().equals("logo_ai"))
      no.addFilho(logo_ai());
    else if (token.getImagem().equals("e_mio"))
      no.addFilho(e_mio());
    else if (token.getImagem().equals("quale"))
      no.addFilho(quale());
    else if (token.getImagem().equals("cascar_fora"))
      no.addFilho(cascar_fora());
    else if (token.getImagem().equals("{")) {
      no.addFilho(new No(token));
      leToken();
      no.addFilho(ListaComando());
      if (token.getImagem().equals("}")) {
        no.addFilho(new No(token));
        leToken();
      } else
        geraErro("}");
    }
    else
      geraErro(
          "'identificador' ou 'em_tempo' ou 'nu_jeito' ou 'causo' ou 'ques_sera' ou 'arreda' ou 'panha' ou 'dediprosa' ou 'logo_ai' ou 'e_mio' ou 'quale' ou cascar_fora");

    return no;
  }

  /*
  <Atributo> ::= id '=' <ExpressaoAritimetica> ';'
              | id '=' <Comando>
              | <TipoDeDados> id '=' <ExpressaoAritimetica> ';'
              | <TipoDeDados> id '=' <Comando>
  */
  private No Atributo() {
    No no = new No(TipoNo.No_Atributo);

    if (token.getImagem().equals("inteirim") || token.getImagem().equals("cadin")
        || token.getImagem().equals("to_ti_falano") || token.getImagem().equals("ontoco")
        || token.getImagem().equals("enjoado") || token.getImagem().equals("enrabichado") || token.getImagem().equals("meia_boca")) {
      no.addFilho(TipoDeDados());
      if(token.getClasse().equals("ID")) {
        no.addFilho(new No(token));
        leToken();
        if (token.getImagem().equals("=")) {
          no.addFilho(new No(token));
          leToken();
          Token lookAhead = lookAhead();
          if (token.getImagem().equals("dediprosa")) no.addFilho(dediprosa());
          else if(token.getImagem().equals("panha")) no.addFilho(panha());
          else if(lookAhead.getImagem().equals("(")) no.addFilho(Comando());
          else if(token.getClasse().equals("CLB")) no.addFilho(meia_boca());
          else {
            no.addFilho(ExpressaoAritimetica());
            if (token.getImagem().equals(";")) {
              no.addFilho(new No(token));
              leToken();
            } else
              geraErro(";");
          }
        } else
          geraErro("=");
      }
    } else {
      if(token.getClasse().equals("ID"))
      {
        no.addFilho(new No(token));
        leToken();
        if (token.getImagem().equals("=")) {
          no.addFilho(new No(token));
          leToken();
          Token lookAhead = lookAhead();
          if (token.getImagem().equals("dediprosa")) no.addFilho(dediprosa());
          else if(token.getImagem().equals("panha")) no.addFilho(panha());
          else if(lookAhead.getImagem().equals("(")) no.addFilho(Chamada());
          else if(token.getClasse().equals("CLB")) no.addFilho(meia_boca());
          else {
            no.addFilho(ExpressaoAritimetica());
            if (token.getImagem().equals(";")) {
              no.addFilho(new No(token));
              leToken();
            } else
              geraErro(";");
          }
        } else
          geraErro("=");
      }
    }

    return no;
  }

  // <Chamada> ::= 'id' '('<ListaArgumento>')' ';'
  private No Chamada() {
    No no = new No(TipoNo.No_Chamada);

    no.addFilho(new No(token));
    leToken();

    if (token.getImagem().equals("(")) {
      no.addFilho(new No(token));
      leToken();
      no.addFilho(ListaArgumento());
      if (token.getImagem().equals(")")) {
        no.addFilho(new No(token));
        leToken();
      } else
        geraErro(")");
    } else
      geraErro("(");

    return no;
  }

  // <Declaracao> ::= <TipoDeDados> <ListaId> ';'
  private No Declaracao() {
    No no = new No(TipoNo.No_Declaracao);

    no.addFilho(TipoDeDados());
    if(token.getClasse().equals("ID")) {
      no.addFilho(ListaId());
      raiz = no; 
      if(token.getImagem().equals(";")) {
        no.addFilho(new No(token));
        leToken();
      } else
        geraErro(";");
    }

    return no;
  }

  /*
   * <em_tempo> ::= em_tempo ‘(‘ <em_tempo_incio> ‘;’ <ExpressaoRelacional> ‘;’ <em_tempo_atualizar> ‘)’ ‘{’ <ListaComando> ‘}’ 
   <em_tempo_linha> ::= em_tempo‘(‘<em_tempo_incio> ‘;’ <ExpressaoRelacional> ‘;’ <em_tempo_atualizar> ‘)’ <Comando>
   */
  private No em_tempo() {
    Token verificar = tokens.get(pToken+14);
    No no;
    if(verificar.getImagem().equals("{"))
      no = new No(TipoNo.No_em_tempo);
    else
      no = new No(TipoNo.No_em_tempo_linha);


    if (token.getImagem().equals("em_tempo")) {
      no.addFilho(new No(token));
      leToken();
      if (token.getImagem().equals("(")) {
        no.addFilho(new No(token));
        leToken();
        no.addFilho(em_tempo_inicio());
        if (token.getImagem().equals(";")) {
          no.addFilho(new No(token));
          leToken();
          no.addFilho(ExpressaoRelacional());
          if (token.getImagem().equals(";")) {
            no.addFilho(new No(token));
            leToken();
            no.addFilho(em_tempo_atualizar());
            if (token.getImagem().equals(")")) {
              no.addFilho(new No(token));
              leToken();
              if (token.getImagem().equals("{")) {
                no.addFilho(ListaComando());
                if (token.getImagem().equals("}")) {
                  no.addFilho(new No(token));
                  leToken();
                } else
                  geraErro("}");
              } else {
                no.addFilho(Comando());
              }
            } else
              geraErro(")");
          } else
            geraErro(";");
        } else
          geraErro(";");
      } else
        geraErro("(");
    } else
      geraErro("em_tempo");

    return no;
  }

  //<em_tempo_incio> ::= <declaracao_em_tempo>
  private No em_tempo_inicio() {
    No no = new No(TipoNo.No_em_tempo_incio);

    if (token.getImagem().equals("inteirim") || token.getImagem().equals("cadin")
        || token.getImagem().equals("to_ti_falano") || token.getImagem().equals("ontoco")
        || token.getImagem().equals("enjoado") || token.getImagem().equals("enrabichado") || token.getImagem().equals("meia_boca")) 
      no.addFilho(Declaracao_Em_Tempo());
    else 
      no.addFilho(Expressao_Declaracao());

    return no;
  }

  //<declaracao_em_tempo> ::= <TipoDeDados> id '=' <operando>
  private No Declaracao_Em_Tempo()
  {
    No no = new No(TipoNo.No_declaracao_em_tempo);

    if (token.getImagem().equals("inteirim") || token.getImagem().equals("cadin")
        || token.getImagem().equals("to_ti_falano") || token.getImagem().equals("ontoco")
        || token.getImagem().equals("enjoado") || token.getImagem().equals("enrabichado") || token.getImagem().equals("meia_boca")) {
      no.addFilho(TipoDeDados());
      if(token.getClasse().equals("ID")) {
        no.addFilho(new No(token));
        leToken();
        if(token.getImagem().equals("=")) {
          no.addFilho(new No(token));
          leToken();
          no.addFilho(Operando());
        }
      }
    }
    return no;
  }

  // <em_tempo_atualizar> ::= <Expressao_Declaracao> 
  private No em_tempo_atualizar() {
    No no = new No(TipoNo.No_em_tempo_atualizar);

    no.addFilho(Expressao_Declaracao());

    return no;
  }

  //<Pre_Incrementacao> ::= ‘++’ id
  private No Pre_Incrementacao() {
    No no = new No(TipoNo.No_Pre_Incrementacao);

    if(token.getImagem().equals("+")) {
      no.addFilho(new No(token));
      leToken();
      if(token.getImagem().equals("+")) {
          no.addFilho(new No(token));
          leToken();
          if(token.getClasse().equals("ID")) {
            no.addFilho(new No(token));
            leToken();
          } else
            geraErro("Identificador");
        } else 
          geraErro("+");
    } else
      geraErro("+");

    return no;
  }

  //<Pre_Decrementacao> ::= ‘--’ id
  private No Pre_Decrementacao() {
    No no = new No(TipoNo.No_Pre_Decrementacao);

    if(token.getImagem().equals("-")) {
      no.addFilho(new No(token));
      leToken();
      if(token.getImagem().equals("-")) {
          no.addFilho(new No(token));
          leToken();
          if(token.getClasse().equals("ID")) {
            no.addFilho(new No(token));
            leToken();
          } else
            geraErro("Identificador");
        } else 
          geraErro("-");
    } else
      geraErro("-");

    return no;
  }

  //<Pos_Incrementacao> ::= id ‘++’
  private No Pos_Incrementacao() {
    No no = new No(TipoNo.No_Pos_Incrementacao);

    if(token.getClasse().equals("ID")) {
      no.addFilho(new No(token));
      leToken();
      if(token.getImagem().equals("+")) {
        no.addFilho(new No(token));
        leToken();
        if(token.getImagem().equals("+"))
        {
          no.addFilho(new No(token));
          leToken();
        } else 
          geraErro("+");
      } else
        geraErro("+");
    } else
      geraErro("Identificador");

    return no;
  }

  //<Pos_Decrementacao> ::= id ‘--’
  private No Pos_Decrementacao() {
    No no = new No(TipoNo.No_Pos_Decrementacao);

    if(token.getClasse().equals("ID")) {
      no.addFilho(new No(token));
      leToken();
      if(token.getImagem().equals("-")) {
        no.addFilho(new No(token));
        leToken();
        if(token.getImagem().equals("-"))
        {
          no.addFilho(new No(token));
          leToken();
        } else 
          geraErro("-");
      } else
        geraErro("-");
    } else
      geraErro("Identificador");

    return no;
  }

  // <Expressao_Declaracao> ::= <ExpressaoRelacional> | <Pre_Incrementacao> | <Pre_Decrementacao> | <Pos_Incrementacao> | <Pos_Decrementacao>
  private No Expressao_Declaracao() {
    No no = new No(TipoNo.No_Expressao_Declaracao);

    if(token.getImagem().equals("-"))
      no.addFilho(Pre_Decrementacao());
    else if(token.getImagem().equals("+"))
      no.addFilho(Pre_Incrementacao());
    else if(token.getClasse().equals("ID"))
    {
      Token lookAhead = lookAhead();
      if(lookAhead.getImagem().equals("-"))
        no.addFilho(Pos_Decrementacao());
      else if(lookAhead.getImagem().equals("+"))
        no.addFilho(Pos_Incrementacao());
    } else
      geraErro("'--' ou '++' ou 'Identificador'");

    return no;
  }

  //<ExpressaoRelacional> ::= <ExpressaoAritimetica><Operador_relacional><ExpressaoAritimetica>
  private No ExpressaoRelacional() {
    No no = new No(TipoNo.No_ExpressaoRelacional);

    no.addFilho(ExpressaoAritimetica());
    no.addFilho(Operador_relacional());
    no.addFilho(ExpressaoAritimetica());

    return no;
  }

  //<Operador_aritimetico> ::= '+' | '-' | ‘*’ | '/'
  private No Operador_aritimetico() {
    No no = new No(TipoNo.No_ExpressaoRelacional);

    if (token.getImagem().equals("+") || token.getImagem().equals("-") || token.getImagem().equals("*")
        || token.getImagem().equals("/")) {
      no.addFilho(new No(token));
      leToken();
    } else
      geraErro("'+' ou '-' ou '*' ou '/'");

    return no;
  }

  //<Operador_relacional> ::= '<' | '>' | '>=' | '<=' | '==' | '!=’
  private No Operador_relacional() {
    No no = new No(TipoNo.No_Operador_relacional);

    if (token.getImagem().equals("<") || token.getImagem().equals("<=") || token.getImagem().equals(">")
        || token.getImagem().equals(">=") || token.getImagem().equals("==") || token.getImagem().equals("!=")) {
      no.addFilho(new No(token));
      leToken();
    } else
      geraErro("'<' ou '<=' ou '>' ou '>=' ou '==' ou '!='");

    return no;
  }

  //<Operador_atribuicao> ::= ‘+=’ | ‘-=’ | ‘*=’ | ‘/=’
  private No Operador_atribuicao() {
    No no = new No(TipoNo.No_ExpressaoRelacional);

    if (token.getImagem().equals("+=") || token.getImagem().equals("-=") || token.getImagem().equals("*=")
        || token.getImagem().equals("/=")) {
      no.addFilho(new No(token));
      leToken();
    } else
      geraErro("'+=' ou '-=' ou '*=' ou '/='");

    return no;
  }

  // <nu_jeito> ::= 'nu_jeito' '('<ExpressaoRelacional>')' <Comando> <causo> if
  private No nu_jeito() {
    No no = new No(TipoNo.No_nu_jeito);

    if (token.getImagem().equals("nu_jeito")) {
      no.addFilho(new No(token));
      leToken();
      if (token.getImagem().equals("(")) {
        no.addFilho(new No(token));
        leToken();
        no.addFilho(ExpressaoRelacional());
        if (token.getImagem().equals(")")) {
          no.addFilho(new No(token));
          leToken();
          no.addFilho(Comando());
          no.addFilho(causo());
        } else
          geraErro(")");
      } else
        geraErro("(");
    } else
      geraErro("nu_jeito");

    return no;
  }

  // <causo> ::= 'causo' <Comando> | else
  private No causo() {
    No no = new No(TipoNo.No_causo);

    if (token.getImagem().equals("causo")) {
      no.addFilho(new No(token));
      leToken();
      no.addFilho(Comando());
    }

    return no;
  }

  // <ques_sera> ::= ‘ques_sera’ '(' <ExpressaoRelacional> ')' <Comando> <causo> else if
  private No ques_sera() {
    No no = new No(TipoNo.No_ques_sera);

    if (token.getImagem().equals("ques_sera")) {
      no.addFilho(new No(token));
      leToken();
      if (token.getImagem().equals("(")) {
        no.addFilho(new No(token));
        leToken();
        no.addFilho(ExpressaoRelacional());
        if (token.getImagem().equals(")")) {
          no.addFilho(new No(token));
          leToken();
          no.addFilho(Comando());
          no.addFilho(causo());
        } else
          geraErro(")");
      } else
        geraErro("(");
    } else
      geraErro("ques_sera");

    return no;
  }

  // <arreda> ::= 'arreda' <Fator> ';' /* return */
  private No arreda() {
    No no = new No(TipoNo.No_arreda);

    if (token.getImagem().equals("arreda")) {
      no.addFilho(new No(token));
      leToken();
      no.addFilho(Fator());
      if (token.getImagem().equals(";")) {
        no.addFilho(new No(token));
        leToken();
      } else
        geraErro("arreda");
    } else
      geraErro(";");

    return no;
  }

  // <panha> ::= 'panha' '(' id ')'';' input
  private No panha() {
    No no = new No(TipoNo.No_panha);

    if (token.getImagem().equals("panha")) {
      no.addFilho(new No(token));
      leToken();
      if (token.getImagem().equals("(")) {
        no.addFilho(new No(token));
        leToken();
        if (token.getClasse().equals("CLS")) {
          no.addFilho(new No(token));
          leToken();
          if (token.getImagem().equals(")")) {
            no.addFilho(new No(token));
            leToken();
            if (token.getImagem().equals(";")) {
              no.addFilho(new No(token));
              leToken();
            } else
              geraErro(";");
          } else
            geraErro(")");
        } else
          geraErro("Texto");
      } else
        geraErro("(");
    } else
      geraErro("panha");

    return no;
  }

  // <dediprosa> ::= 'dediprosa' '('<Operando>')' ';' output
  private No dediprosa() {
    No no = new No(TipoNo.No_dediprosa);

    if (token.getImagem().equals("dediprosa")) {
      no.addFilho(new No(token));
      leToken();
      if (token.getImagem().equals("(")) {
        no.addFilho(new No(token));
        leToken();
        no.addFilho(Operando());
        if (token.getImagem().equals(")")) {
          no.addFilho(new No(token));
          leToken();
          if (token.getImagem().equals(";")) {
            no.addFilho(new No(token));
            leToken();
          } else
            geraErro(";");
        } else
          geraErro(")");
      } else
        geraErro("(");
    } else
      geraErro("dediprosa");

    return no;
  }

  // <po_para> ::= ‘po_para’ ';' break
  private No po_para() {
    No no = new No(TipoNo.No_po_para);

    if (token.getImagem().equals("po_para")) {
      no.addFilho(new No(token));
      leToken();
      if (token.getImagem().equals(";")) {
        no.addFilho(new No(token));
        leToken();
      } else
        geraErro(";");
    } else
      geraErro("po_para");

    return no;
  }

  // <perai> ::= ‘perai’ '; continue
  private No perai() {
    No no = new No(TipoNo.No_perai);

    if (token.getImagem().equals("perai")) {
      no.addFilho(new No(token));
      leToken();
      if (token.getImagem().equals(";")) {
        no.addFilho(new No(token));
        leToken();
      } else
        geraErro(";");
    } else
      geraErro("perai");

    return no;
  }

  /*
   * <logo_ai> ::= ‘logo_ai’ ‘(‘ <operando> <Operador_relacional> <operando> ')’ '{' <ListaComando> '}'
   * <logo_ai_linha> ::= ‘logo_ai’ ‘(‘ <operando> <Operador_relacional> <operando> ‘)’ <Comando>
   */
  private No logo_ai() {
    Token verificar = tokens.get(pToken+5);
    No no;
    if(verificar.getImagem().equals("{"))
      no = new No(TipoNo.No_logo_ai);
    else
      no = new No(TipoNo.No_logo_ai_linha);

    if (token.getImagem().equals("logo_ai")) {
      no.addFilho(new No(token));
      leToken();
      if (token.getImagem().equals("(")) {
        no.addFilho(new No(token));
        leToken();
        no.addFilho(Operando());
        no.addFilho(Operador_relacional());
        no.addFilho(Operando());
        if (token.getImagem().equals(")")) {
          no.addFilho(new No(token));
          leToken();
          if (token.getImagem().equals("{")) {
            no.addFilho(new No(token));
            leToken();
            no.addFilho(ListaComando());
            if (token.getImagem().equals("}")) {
              no.addFilho(new No(token));
              leToken();
            } else
              geraErro("}");
          } else {
            no.addFilho(Comando());
            leToken();
          }
        } else
          geraErro(")");
      } else
        geraErro("(");
    } else
      geraErro("logo_ai");

    return no;
  }

  // <e_mio> ::= 'e_mio' ‘{‘ <ListaComando> ‘}’ ‘logo_ai’ ‘(‘ <operando>
  // <Operador_relacional> <operando> ‘)’ ';'
  private No e_mio() {
    No no = new No(TipoNo.No_e_mio);

    if (token.getImagem().equals("e_mio")) {
      no.addFilho(new No(token));
      leToken();
      if (token.getImagem().equals("{")) {
        no.addFilho(new No(token));
        leToken();
        no.addFilho(ListaComando());
        if (token.getImagem().equals("}")) {
          no.addFilho(new No(token));
          leToken();
          if (token.getImagem().equals("logo_ai")) {
            no.addFilho(new No(token));
            leToken();
            if (token.getImagem().equals("(")) {
              no.addFilho(new No(token));
              leToken();
              no.addFilho(Operando());
              no.addFilho(Operador_relacional());
              no.addFilho(Operando());
              if (token.getImagem().equals(")")) {
                no.addFilho(new No(token));
                leToken();
                if (token.getImagem().equals(";")) {
                  no.addFilho(new No(token));
                  leToken();
                } else
                  geraErro(";");
              } else
                geraErro(")");
            } else
              geraErro("(");
          } else
            geraErro("logo_ai");
        } else
          geraErro("}");
      } else
        geraErro("{");
    } else
      geraErro("e_mio");

    return no;
  }

  /*
   * <quale> ::= ‘quale’ ‘(‘ id ‘)’ <bloco_quale> <bloco_quale> ::= ‘{‘ <quale_nomes> ‘}’
   */
  private No quale() {
    No no = new No(TipoNo.No_quale);

    if (token.getImagem().equals("quale")) {
      no.addFilho(new No(token));
      leToken();
      if (token.getImagem().equals("(")) {
        no.addFilho(new No(token));
        leToken();
        if (token.getClasse().equals("ID")) {
          no.addFilho(new No(token));
          leToken();
          if (token.getImagem().equals(")")) {
            no.addFilho(new No(token));
            leToken();
            if (token.getImagem().equals("{")) {
              no.addFilho(new No(token));
              leToken();
              no.addFilho(quale_nomes());
              if (token.getImagem().equals("}")) {
                no.addFilho(new No(token));
                leToken();
              } else
                geraErro("}");
            } else
              geraErro("{");
          } else
            geraErro(")");
        } else
          geraErro("identificador");
      } else
        geraErro("(");
    } else
      geraErro("quale");

    return no;
  }

  //<quale_nomes> ::= <quale_nome> | <quale_nomes> <quale_nome>
  private No quale_nomes() {
    No no = new No(TipoNo.No_quale_nomes);

    if(token.getImagem().equals("qui_nem")
    || token.getImagem().equals("murrinha")) {
      no.addFilho(quale_nome());
      no.addFilho(quale_nomes());
    }

    return no;
  }

  //<quale_nome> ::= ‘qui_nem ’ <operando> ‘:’ <Comando> <comando_quale>  | ‘murrinha’ ':' <Comando>
  private No quale_nome() {
    No no = new No(TipoNo.No_quale_nome);

    if(token.getImagem().equals("qui_nem")) {
      no.addFilho(new No(token));
      leToken();
      no.addFilho(Operando());
      if (token.getImagem().equals(":")) {
        no.addFilho(new No(token));
        leToken();
        no.addFilho(Comando());
        no.addFilho(Comando_quale());
      } else
        geraErro(":");
    } else if(token.getImagem().equals("murrinha")) {
      no.addFilho(new No(token));
      leToken();
      if (token.getImagem().equals(":")) {
        no.addFilho(new No(token));
        leToken();
        no.addFilho(ListaComando());
      } else
        geraErro(":");
    }

    return no;
  }

  /*
   * <Comando_quale> ::= | <po_para> break | <perai> continue
   */
  private No Comando_quale() {
    No no = new No(TipoNo.No_Comando_quale);

    if (token.getImagem().equals("po_para")) {
      no.addFilho(po_para());
    } else if (token.getImagem().equals("perai")) {
      no.addFilho(perai());
    }

    return no;
  }

  /*
   * <meia_boca> ::= di_vera true | bagaca false
   */
  private No meia_boca() {
    No no = new No(TipoNo.No_meia_boca);

    if (token.getImagem().equals("di_vera") || token.getImagem().equals("bagaca")) {
      no.addFilho(new No(token));
      leToken();
      if(token.getImagem().equals(";")) {
        no.addFilho(new No(token));
        leToken();
      } else 
        geraErro(";");
    } else
      geraErro("'di_vera' ou 'bagaca'");

    return no;
  }

  // <ListaId> ::= id <ListaId2>
  private No ListaId() {
    No no = new No(TipoNo.No_ListaId);

    no.addFilho(new No(token));
    leToken();
    no.addFilho(ListaId2());

    return no;
  }

  // <ListaId2> ::= ',' id <ListaId2>
  private No ListaId2() {
    No no = new No(TipoNo.No_ListaId2);

    if (token.getImagem().equals(",")) {
      no.addFilho(new No(token));
      leToken();
      if (token.getClasse().equals("ID")) {
        no.addFilho(new No(token));
        leToken();
        no.addFilho(ListaId2());
      } else
        geraErro("identificador");
    }

    return no;
  }

  // <ExpressaoAritimetica> ::= <Termo><ExpressaoAritimetica2>
  private No ExpressaoAritimetica() {
    No no = new No(TipoNo.No_ExpressaoAritimetica);

    no.addFilho(Termo());
    no.addFilho(ExpressaoAritimetica2());

    return no;
  }

  // <ExpressaoAritimetica2> ::= <Operador_aritimetico><ExpressaoAritimetica> |
  private No ExpressaoAritimetica2() {
    No no = new No(TipoNo.No_ExpressaoAritimetica2);

    if (token.getImagem().equals("+") || token.getImagem().equals("-")) {
      no.addFilho(Operador_aritimetico());
      no.addFilho(ExpressaoAritimetica());
    }

    return no;
  }

  // <Termo> ::= <Fator><Termo2>
  private No Termo() {
    No no = new No(TipoNo.No_Termo);

    no.addFilho(Fator());
    no.addFilho(Termo2());

    return no;
  }

  // <Termo2> ::= <Operador_aritimetico<Termo> |
  private No Termo2() {
    No no = new No(TipoNo.No_Termo2);

    if (token.getImagem().equals("*") || token.getImagem().equals("/")) {
      no.addFilho(Operador_aritimetico());
      no.addFilho(Termo());
    }

    return no;
  }

  /*
   * <Fator> ::= <Operando> | <Chamada> | '('<ExpressaoAritimetica>')'
   */
  private No Fator() {
    No no = new No(TipoNo.No_Fator);

    if (token.getClasse().equals("ID")) {
      Token lookahead = lookAhead();
      if (lookahead.getImagem().equals("("))
        no.addFilho(Chamada());
      else
        no.addFilho(Operando());

    } else if (token.getClasse().equals("CLI") || token.getClasse().equals("CLF") || token.getClasse().equals("CLS")
        || token.getClasse().equals("CLC") || token.getClasse().equals("CLD") || token.getClasse().equals("CLA")
        || token.getClasse().equals("CLL") || token.getClasse().equals("CLB") || token.getClasse().equals("CLE"))
      no.addFilho(Operando());
    else if (token.getImagem().equals("(")) {
      no.addFilho(new No(token));
      leToken();
      no.addFilho(ExpressaoAritimetica());
      if (token.getImagem().equals(")")) {
        no.addFilho(new No(token));
        leToken();
      } else
        geraErro(")");

    } else
      geraErro("(");

    return no;
  }

  /*
   * <Operando> ::= id | cli constante literal integer | clf constante literal
   * float | cls constante literal string | clc constante literal char | cld
   * constante literal double | cll constante | clb constante literal boolean | cle constante literal enum
   */
  private No Operando() {
    No no = new No(TipoNo.No_Operando);

    if (token.getClasse().equals("ID") || token.getClasse().equals("CLI") || token.getClasse().equals("CLF")
        || token.getClasse().equals("CLS") || token.getClasse().equals("CLC") || token.getClasse().equals("CLD") || token.getClasse().equals("CLB")
        || token.getClasse().equals("CLE")) {
      no.addFilho(new No(token));
      leToken();
    }

    return no;
  }

  // <ListaArgumento> ::= <Operando><ListArgumento2> |
  private No ListaArgumento() {
    No no = new No(TipoNo.No_ListaArgumento);

    if (token.getClasse().equals("ID")) {
      no.addFilho(Operando());
      no.addFilho(ListaArgumento2());
    }

    return no;
  }

  // <ListaArgumento2> ::= ','<Operando><ListArgumento2>
  private No ListaArgumento2() {
    No no = new No(TipoNo.No_ListaArgumento2);

    if (token.getImagem().equals(",")) {
      no.addFilho(new No(token));
      leToken();
      no.addFilho(Operando());
      no.addFilho(ListaArgumento2());
    }

    return no;
  }

  // <cascar_fora> ::= ‘cascar_fora’ ';' /* end */
  private No cascar_fora() {
    No no = new No(TipoNo.No_cascar_fora);

    if (token.getImagem().equals("cascar_fora")) {
      no.addFilho(new No(token));
      leToken();
      if(token.getImagem().equals(";")) {
        no.addFilho(new No(token));
        leToken();
      } else
        geraErro(";");
    }

    return no;
  }

  public static ArrayList<Token> getTokens() {
    return tokens;
  }

  public static ArrayList<String> getErros() {
    return erros;
  }

  public static No getRaiz() {
    return raiz;
  }

  public static void printTokens() {
    System.out.println("Tokens:");
    for (Token token : tokens) {
      System.out.println(token);
    }
  }

  public static void printErros() {
    System.out.println("ERROS:");
    for (String erro : erros) {
      System.out.println(erro);
    }
  }

  public static void mostraNo(No no, String esp) {
    System.out.println(esp + no);
    for (No noFilho : no.getFilhos()) {
      mostraNo(noFilho, esp + "  ");
    }
  }

  public static void mostraArvore() {
    mostraNo(raiz, "  ");
  }
}