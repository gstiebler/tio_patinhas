/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testejavadesktop;

import java.awt.GridLayout;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

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
        return soma*100.0 / 7;
    }

    public static void AjustaParametros() {
        double[] Acertos = new double[7];
        double Avaliacao, MelhorAvaliacao;
        int PASSO = 5;
        double intervalo, MelhorAvaliacaoGeral, MelhorAvaliacaoGeralAnterior;
        int Valor, MelhorValor;
        DecimalFormat df = new DecimalFormat("#,###.00");
        TParamsDir ParamsDir=new TParamsDir();
        WriteText log = new WriteText(ParamsDir.getDirBase()+DateUtils.nowCurto()+" log.txt");
        CArquivosTeste ArquivosTeste = new CArquivosTeste(ParamsDir.getDirImagens(), true);
        TParamsIni ParamsIni = new TParamsIni(ParamsDir.getDirBase()+"ParamsTP.ini");
        TParamsIni ParamsSup = new TParamsIni(ParamsDir.getDirBase()+"ParamsTPSup.ini");
        TParamsIni ParamsInf = new TParamsIni(ParamsDir.getDirBase()+"ParamsTPInf.ini");
        //TParamsIni ParamsPasso = new TParamsIni("p:\\TioPatinhas\\ParamsTPPasso.ini");
        MelhorAvaliacaoGeral = 1;
        MelhorAvaliacaoGeralAnterior = 0;
        int NumCiclos=1;
        while ((MelhorAvaliacaoGeral - MelhorAvaliacaoGeralAnterior) > 0.001) {
            MelhorAvaliacaoGeralAnterior = MelhorAvaliacaoGeral;
            log.EscreveLinha("****************************************************************");
            log.EscreveLinha(DateUtils.now());
            log.EscreveLinha("Iniciando ciclo "+NumCiclos);
            //PERCORRE TODOS OS PARÂMETROS
            for (int k = 0; k < ParamsIni.ListaParametros.length; k++) {
                String parametro=ParamsIni.ListaParametros[k];
                MelhorAvaliacao = 0;
                MelhorValor = -1;
                intervalo = ParamsSup.LeParametro(parametro) - ParamsInf.LeParametro(parametro);
                log.EscreveLinha(DateUtils.now());
                log.EscreveLinha(parametro);
                log.Atualiza();
                //PERCORRE OS VALORES DO PARÂMETRO ATUAL
                for (int n = 0; n < PASSO; n++) {
                    Valor = ParamsInf.LeParametro(parametro) + MathUtils.round(n * (intervalo * 1.0 / PASSO));
                    ParamsIni.EscreveParametro(parametro, Valor);
                    StringMatrizConfusao MatrizConfusao=new StringMatrizConfusao();
                    CTestes.CalculaAcertos(ParamsIni, ArquivosTeste, Acertos, MatrizConfusao, false);
                    Avaliacao = AvaliaAcertosMedia(Acertos);
                    if (Avaliacao > MelhorAvaliacao) {
                        MelhorAvaliacao = Avaliacao;
                        MelhorValor = Valor;
                    }
                    log.EscreveLinha(Valor + ": " + df.format(Avaliacao) + StrAcertos(Acertos));
                    log.Atualiza();
                }
                ParamsIni.EscreveParametro(parametro, MelhorValor);
                log.Escreve(ParamsIni.dump());
                if (MelhorAvaliacao > MelhorAvaliacaoGeral) {
                    log.EscreveLinha("Melhora de "+df.format(MelhorAvaliacao-MelhorAvaliacaoGeral)+
                                                "% na otimização da variável");
                    MelhorAvaliacaoGeral = MelhorAvaliacao;
                }
                log.EscreveLinha("Melhor avaliação geral atual: "+df.format(MelhorAvaliacaoGeral)+"%");
                log.EscreveLinha("");
            }
            log.EscreveLinha("Melhor no ciclo: "+df.format(MelhorAvaliacaoGeral-MelhorAvaliacaoGeralAnterior)+"%");
            log.EscreveLinha("FIM DO CICLO ************************************************");
            log.EscreveLinha("");
            NumCiclos++;
        }
        log.Fecha();
    }

    private static String StrAcertos(double[] AcertosNota) {
        String retorno = "";
        DecimalFormat df = new DecimalFormat("#,###.00");
        for (int n = 0; n < 7; n++) {
            retorno += ", " + CNota.NotaPorIndice(n) + ": " + df.format(AcertosNota[n] * 100.0)+"%";
        }
        return retorno;
    }
}

class WriteText {

    private PrintWriter out;
    private JPanel cp;
    private JTextArea ta;
    public WriteText(String NomeArq) {
        try {
            cp = new JPanel(new GridLayout(0, 1));
            ta = new JTextArea();
            cp.add(ta);
            JFrame fr = new JFrame("Tio Patinhas Debug");
            fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            fr.setContentPane(cp);
            fr.add(ta);
            fr.pack();
            fr.setLocationRelativeTo(null);
            fr.setVisible(true);
            
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
        out.print(str+"\n");
        ta.append(str);
    }

    public void Fecha() {
        out.close();
    }

    public void Atualiza() {
        out.flush();
    }
}

