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
        CDesktopFuncs.WriteOutput("teste");
        String retorno = "";
        long tempo2 = System.currentTimeMillis();
        TParamsIni ParamsIni = new TParamsIni("p:\\TioPatinhas\\ParamsTP.ini");
        double[] Acertos = new double[7];
        String PastaBase = "P:\\TioPatinhas\\dinheiro\\testes\\";
        CArquivosTeste ArquivosTeste = new CArquivosTeste(PastaBase, true);
        CalculaAcertos(ParamsIni, ArquivosTeste, Acertos);
        for (int n = 0; n < 7; n++) {
            retorno += n + ": " + Acertos[n] * 100.0 + "\n";
        }
        retorno += "Carga arquivos: " + new Double((tempo2 - tempo1) / 1000.0).toString() + "\n";
        retorno += "Reconhecimentos: " + new Double((System.currentTimeMillis() - tempo2) / 1000.0).toString() + "\n";
        return retorno;
    }

    public static void CalculaAcertos(TParamsIni ParamsIni, 
                    CArquivosTeste ArquivosTeste, double[] AcertosNota) {

        int TotalNotas, Acertos;
        CNota NotaTemp;
        for (int n = 0; n < ArquivosTeste.NumNotas(); n++) {
            CImagensNota ImagensNota = ArquivosTeste.ImagensNota(n);
            TotalNotas = 0;
            Acertos = 0;
            for (int i = 0; i < ImagensNota.NumElementos(); i++) {
                NotaTemp = ImagensNota.Imagens(i);
                TParamsRC ParamsRC = new TParamsRC(ParamsIni,
                        new CTonsCinza(NotaTemp.imagem), null);
                UTioPatinhas.ReconheceCedula(ParamsRC);
                TotalNotas++;

                if (ImagensNota.nota == ParamsRC.ParamsAI.ValorCedula) {
                    Acertos++;
                } else {
                    /*BMPFile bmpFile=new BMPFile();
                    bmpFile.saveBitmap(
                    "P:\\TioPatinhas\\dinheiro\\testes\\erradas\\"+
                    System.currentTimeMillis()+NotaTemp.arquivo, 
                    ParamsRC.ParamsMLT.BImgDest.SaveImage(), 320, 240);*/
                }
            }
            AcertosNota[n] = Acertos * 1.0 / TotalNotas;
        }
    }
}
