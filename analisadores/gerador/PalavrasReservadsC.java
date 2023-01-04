package analisadores.gerador;

import java.util.Hashtable;

public class PalavrasReservadsC {
  static Hashtable<String, String> hash = new Hashtable<>();

  static {
    hash.put("inteirim", "int");
    hash.put("to_ti_falando", "char[250]");
    hash.put("enjoado", "char");
    hash.put("enrabichado", "double");
    hash.put("cadin", "float");
    hash.put("meia_boca", "boolean");
    hash.put("bagaca", "true");
    hash.put("di_vera", "false");
    hash.put("perai", "continue");
    hash.put("ontoco", "void");
    hash.put("po_para", "break");
    hash.put("dediprosa", "printf");
    hash.put("panha", "scanf");
    hash.put("nu_jeito", "if");
    hash.put("causo", "else");
    hash.put("ques_sera", "else if");
    hash.put("e_mio", "do");
    hash.put("logo_ai", "while");
    hash.put("em_tempo", "for");
    hash.put("arreda", "return");
    hash.put("quale", "switch");
    hash.put("qui_nem", "case");
    hash.put("murrinha", "default");
    hash.put("paradeza", "static");
    hash.put("logo_ali", "long");
    hash.put("end", "exit");
  }

  public static String tipo(String value) {
    return hash.get(value);
  }
}