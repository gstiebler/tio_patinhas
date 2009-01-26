/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testejavadesktop;

/**
 *
 * @author Administrator
 */
public class CTestes {

    public static String ExecutaTestes() {
        long tempo1 = System.currentTimeMillis();
        String retorno = "";
        long tempo2 = System.currentTimeMillis();
        TParamsDir ParamsDir = new TParamsDir();
        TParamsIni ParamsIni = new TParamsIni(ParamsDir.getDirBase() + "ParamsTPOTM.ini");
        double[] Acertos = new double[7];
        String PastaBase = ParamsDir.getDir("DiretorioSelecionadas");
        CArquivosTeste ArquivosTeste = new CArquivosTeste(PastaBase, true/*carrega arquivos antes*/);
        StringMatrizConfusao MatrizConfusao = new StringMatrizConfusao();
        CalculaAcertos(ParamsIni, ArquivosTeste, Acertos, MatrizConfusao, true/*salva erradas*/);
        for (int n = 0; n < 7; n++) {
            retorno += n + ": " + Acertos[n] * 100.0 + "\n";
        }
        for (int n = 0; n < 7; n++) {
            retorno += MatrizConfusao.RetornaString(n) + "\n";
        }
        for (int n = 0; n < 7; n++) {
            retorno += MatrizConfusao.RetornaStringPorcentagem(n) + "\n";
        }
        retorno += "Carga arquivos: " + new Double((tempo2 - tempo1) / 1000.0).toString() + "\n";
        retorno += "Reconhecimentos: " + new Double((System.currentTimeMillis() - tempo2) / 1000.0).toString() + "\n";
        return retorno;
    }

    public static void CalculaAcertos(TParamsIni ParamsIni,
            CArquivosTeste ArquivosTeste, double[] AcertosNota, StringMatrizConfusao MatrizConfusao,
            boolean SalvaErradas) {

        TParamsDir ParamsDir = new TParamsDir();
        int TotalNotas, Acertos;
        CNota NotaTemp;
        for (int n = 0; n < ArquivosTeste.NumNotas(); n++) {
            CImagensNota ImagensNota = ArquivosTeste.ImagensNota(n);
            TotalNotas = 0;
            Acertos = 0;
            System.out.println("Nota: " + n);
            for (int i = 0; i < ImagensNota.NumElementos(); i++) {
                NotaTemp = ImagensNota.Imagens(i);
                TParamsRC ParamsRC;
                if (SalvaErradas) {
                    //inicializa uma imagem de debug que vai ser salva no diretório especificado
                    ParamsRC = new TParamsRC(ParamsIni,
                            new CTonsCinza(NotaTemp.imagem), new CBitmap(NotaTemp.imagem));
                    COutputDebug.InicializaArquivo();
                } else {
                    ParamsRC = new TParamsRC(ParamsIni,
                            new CTonsCinza(NotaTemp.imagem), null);
                }
                UTioPatinhas.ReconheceCedula(ParamsRC);
                TotalNotas++;
                if ((i % 10) == 0) {
                    System.out.println(i);
                }
                if (ImagensNota.nota == ParamsRC.ParamsAI.ValorCedula) {
                    Acertos++;
                } else {
                    if (SalvaErradas) {
                        BMPFile bmpFile = new BMPFile();
                        String nome_arq_destino=
                                ParamsDir.getDir("DiretorioErradas")+
                                ImagensNota.nota + "_" +
                                ParamsRC.ParamsAI.ValorCedula + "_" + System.currentTimeMillis() +
                                "_" + NotaTemp.arquivo;
                        bmpFile.saveBitmap(nome_arq_destino,
                                ParamsRC.ParamsMLT.BImgDest.SaveImage(), 320, 240);
                        nome_arq_destino=nome_arq_destino.substring(0, nome_arq_destino.length()-4);
                        COutputDebug.FechaArquivo(nome_arq_destino+".txt");
                    }
                }

                MatrizConfusao.AdicionaReconhecimento(ImagensNota.nota,
                        ParamsRC.ParamsAI.ValorCedula);
            }
            AcertosNota[n] = Acertos * 1.0 / TotalNotas;
        }
    }
}

class StringMatrizConfusao {

    private int[][] matriz;
    private int[] total;

    public StringMatrizConfusao() {
        matriz = new int[7][8];
        total = new int[7];
    }

    private int ValorParaIndice(int valor) {
        switch (valor) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 5:
                return 2;
            case 10:
                return 3;
            case 20:
                return 4;
            case 50:
                return 5;
            case 100:
                return 6;
        }
        return 7;
    }

    public void AdicionaReconhecimento(int valor_esperado, int valor_reconhecido) {
        total[ValorParaIndice(valor_esperado)]++;
        matriz[ValorParaIndice(valor_esperado)][ValorParaIndice(valor_reconhecido)]++;
    }

    public String RetornaString(int indice) {
        /*return "1: " + matriz[indice][0] + "; 2: " + matriz[indice][1] + "; 5: " +
        matriz[indice][2] + "; 10: " + matriz[indice][3] + "; 20: " +
        matriz[indice][4] + "; 50: " + matriz[indice][5] + "; 100: " +
        matriz[indice][6]+"; Não reconhecido: "+matriz[indice][7];*/
        return matriz[indice][0] + "\t" + matriz[indice][1] + "\t" +
                matriz[indice][2] + "\t" + matriz[indice][3] + "\t" +
                matriz[indice][4] + "\t" + matriz[indice][5] + "\t" +
                matriz[indice][6] + "\t" + matriz[indice][7];
    }

    public String RetornaStringPorcentagem(int indice) {
        String retorno = "";
        for (int i = 0; i < 8; i++) {
            retorno += CDesktopFuncs.FormataPorcentagem(matriz[indice][i] * 1.0 / total[indice]) + "\t";
        }
        return retorno;
    }
}