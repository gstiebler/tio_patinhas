/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testejavadesktop;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

/**
 *
 * @author Administrator
 */
public class AjustesParametros {

    private static double AvaliaAcertosMedia(double[] AcertosNota) {
        double soma = 0;
        for (int n = 0; n < 7; n++) {
            soma += AcertosNota[n];
        }
        return soma / 7;
    }

    public static void AjustaParametros() {
        double[] Acertos = new double[7];
        double Avaliacao, MelhorAvaliacao;
        int PASSO = 10;
        double intervalo, MelhorAvaliacaoGeral, MelhorAvaliacaoGeralAnterior;
        int Valor, MelhorValor;
        DecimalFormat df = new DecimalFormat("#,###.00");
        WriteText log = new WriteText("P:\\TioPatinhas\\log.txt");
        String PastaBase = "P:\\TioPatinhas\\dinheiro\\testes\\";
        CArquivosTeste ArquivosTeste = new CArquivosTeste(PastaBase, false);
        TParamsIni ParamsIni = new TParamsIni("p:\\TioPatinhas\\ParamsTP.ini");
        TParamsIni ParamsSup = new TParamsIni("p:\\TioPatinhas\\ParamsTPSup.ini");
        TParamsIni ParamsInf = new TParamsIni("p:\\TioPatinhas\\ParamsTPInf.ini");
        //TParamsIni ParamsPasso = new TParamsIni("p:\\TioPatinhas\\ParamsTPPasso.ini");
        MelhorAvaliacaoGeral = 1;
        MelhorAvaliacaoGeralAnterior = 0;

        while ((MelhorAvaliacaoGeral - MelhorAvaliacaoGeralAnterior) > 0.001) {
            MelhorAvaliacaoGeralAnterior = MelhorAvaliacaoGeral;
            for (int k = 0; k < ParamsIni.ListaParametros.length; k++) {
                String parametro=ParamsIni.ListaParametros[k];
                MelhorAvaliacao = 0;
                MelhorValor = -1;
                intervalo = ParamsSup.LeParametro(parametro) - ParamsInf.LeParametro(parametro);
                log.EscreveLinha(parametro);
                log.Atualiza();
                for (int n = 0; n < PASSO; n++) {
                    Valor = ParamsInf.LeParametro(parametro) + MathUtils.round(n * (intervalo * 1.0 / PASSO));
                    ParamsIni.EscreveParametro(parametro, Valor);
                    CTestes.CalculaAcertos(ParamsIni, ArquivosTeste, Acertos);
                    Avaliacao = AvaliaAcertosMedia(Acertos);
                    if (Avaliacao > MelhorAvaliacao) {
                        MelhorAvaliacao = Avaliacao;
                        MelhorValor = Valor;
                    }
                    log.EscreveLinha(Valor + ": " + df.format(Avaliacao * 100.0) + StrAcertos(Acertos));
                    log.Atualiza();
                }
                ParamsIni.EscreveParametro(parametro, MelhorValor);
                log.Escreve(ParamsIni.dump());
                if (MelhorAvaliacao > MelhorAvaliacaoGeral) {
                    MelhorAvaliacaoGeral = MelhorAvaliacao;
                }
            }

        }
        log.Fecha();
    }

    private static String StrAcertos(double[] AcertosNota) {
        String retorno = "";
        DecimalFormat df = new DecimalFormat("#,###.00");
        for (int n = 0; n < 7; n++) {
            retorno += ", " + CNota.NotaPorIndice(n) + ": " + df.format(AcertosNota[n] * 100.0);
        }
        return retorno;
    }
}

class WriteText {

    private PrintWriter out;

    public WriteText(String NomeArq) {
        try {
            FileWriter outFile = new FileWriter(NomeArq);
            out = new PrintWriter(outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Escreve(String str) {
        out.print(str);
    }

    public void EscreveLinha(String str) {
        out.println(str);
    }

    public void Fecha() {
        out.close();
    }

    public void Atualiza() {
        out.flush();
    }
}

