/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testejavadesktop;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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
        double Avaliacao, MelhorAvaliacao, ValorD, MelhorValorD;
        int PASSO = 12;
        double intervalo;
        int ValorI, MelhorValorI;
        WriteText log=new WriteText("P:\\TioPatinhas\\log.txt");
        String PastaBase = "P:\\TioPatinhas\\dinheiro\\testes\\";
        CArquivosTeste ArquivosTeste = new CArquivosTeste(PastaBase, false);
        TParamsIni ParamsIni = new TParamsIni("p:\\TioPatinhas\\ParamsTP.ini");
        TParamsIni ParamsSup = new TParamsIni("p:\\TioPatinhas\\ParamsTPSup.ini");
        TParamsIni ParamsInf = new TParamsIni("p:\\TioPatinhas\\ParamsTPInf.ini");
        //TParamsIni ParamsPasso = new TParamsIni("p:\\TioPatinhas\\ParamsTPPasso.ini");

        MelhorAvaliacao = 0;
        MelhorValorI = -1;
        intervalo = ParamsSup.AltMinTarja - ParamsInf.AltMinTarja;
        log.EscreveLinha("AltMinTarja");
        for (int n = 0; n < PASSO; n++) {
            ValorI = ParamsInf.AltMinTarja + MathUtils.round(n * (intervalo * 1.0 / PASSO));
            ParamsIni.AltMinTarja = ValorI;
            CTestes.CalculaAcertos(ParamsIni, ArquivosTeste, Acertos);
            Avaliacao = AvaliaAcertosMedia(Acertos);
            if (Avaliacao > MelhorAvaliacao) {
                MelhorAvaliacao = Avaliacao;
                MelhorValorI = ValorI;
            }
            log.EscreveLinha(ValorI+": "+Avaliacao);
            log.Atualiza();
        }
        ParamsIni.AltMinTarja = MelhorValorI;
        log.Escreve(ParamsIni.dump());
        
        log.Fecha();
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
    
    public void Escreve(String str)
    {
        out.print(str);
    }
    
    public void EscreveLinha(String str)
    {
        out.println(str);
    }
    
    public void Fecha()
    {
        out.close();
    }
    
    public void Atualiza()
    {
        out.flush();
    }
}

