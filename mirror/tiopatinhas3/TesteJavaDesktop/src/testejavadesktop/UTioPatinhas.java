/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testejavadesktop;

/**
 *
 * @author Administrator
 */
public class UTioPatinhas {

    static int BORDA_CLARO_ESCURO = 0,  BORDA_ESCURO_CLARO = 1;

    UTioPatinhas() {
    }

    static void ReconheceCedula(TParamsRC ParamsRC) {
        ParamsRC.LumMedianaImagem =
                ImageProcessor.Histograma(ParamsRC.ParamsMLT.TCImgSrc);
        ParamsRC.ConverteParametrosDependentesLumMediana();
        MostraLimiteTarja(ParamsRC.ParamsMLT);
    /*ParamsRC.ParamsABT.BImgDest=ParamsRC.ParamsMLT.BImgDest;
    ParamsRC.ParamsABT.BordasColunas=ParamsRC.ParamsMLT.BordasColunas;
    AnalizaBordasTarja(ParamsRC.ParamsABT);
    ParamsRC.ConverteParametrosDependentesAlturaFaixa();
    if (ParamsRC.ParamsABT.AchouTarja)
    {
    ParamsRC.ParamsAI.RefTarja=ParamsRC.ParamsABT.RefTarja;
    ParamsRC.ParamsAI.BImgDest=ParamsRC.ParamsABT.BImgDest;
    ParamsRC.ParamsAI.TCImgSrc=ParamsRC.ParamsMLT.TCImgSrc;
    AnalizaIdentificador(ParamsRC.ParamsAI);
    }
    EscreveParametros(ParamsRC);*/
    }

    static void MostraLimiteTarja(TParamsMLT ParamsMLT) {

        int ALT_COLUNA = 3, DY = 2, DIF_MIN_LUM = 15;
        int x, y, j;
        int yBorda;
        int DifLum;
        int UltYBordaCE, UltYBordaEC;
        int MaisEscuroAtual, MaisClaroAtual, MaisClaroAnterior, MaisEscuroAnterior;
        int lum;
        int[][] ImgSrc = ParamsMLT.TCImgSrc.TonsCinza;
        Cor[][] ImgDest = ParamsMLT.BImgDest.PMCor;
        int YIni = (int) (ParamsMLT.TCImgSrc.Alt * ParamsMLT.PropYIni);
        int XFim = (int) (ParamsMLT.TCImgSrc.Larg * ParamsMLT.PropXFim);
        TBorda BordaTemp;
        TMaiorBorda MaiorBorda = new TMaiorBorda();
        ParamsMLT.BordasColunas = new TBordasColunas(XFim);
        for (x = 0; x < XFim; x++) {
            UltYBordaCE = UltYBordaEC = -1;
            MaiorBorda.DifLum = 0;
            for (y = YIni + ALT_COLUNA; y < (ParamsMLT.TCImgSrc.Alt - ALT_COLUNA); y++) {
                MaisEscuroAtual = 255;
                MaisEscuroAnterior = 255;
                MaisClaroAtual = 0;
                MaisClaroAnterior = 0;

                //define os mais claros e mais escuros atuais
                for (j = y; j < (y + ALT_COLUNA); j++) {
                    lum = ImgSrc[j][x];
                    if (lum > MaisClaroAtual) {
                        MaisClaroAtual = lum;
                    }
                    if (lum < MaisEscuroAtual) {
                        MaisEscuroAtual = lum;
                    }
                }

                //define os mais claros e os mais escuros anteriores
                for (j = y - (ALT_COLUNA + DY); j < (y - DY); j++) {
                    lum = ImgSrc[j][x];
                    if (lum > MaisClaroAnterior) {
                        MaisClaroAnterior = lum;
                    }
                    if (lum < MaisEscuroAnterior) {
                        MaisEscuroAnterior = lum;
                    }
                }

                //      if (x==25 && y==170)
                //        int w=5;

                //borda claro encima, escuro embaixo
                DifLum = MaisEscuroAnterior - MaisClaroAtual;
                if (DifLum > DIF_MIN_LUM) {
                    if (DifLum > MaiorBorda.DifLum) {
                        MaiorBorda.DifLum = DifLum;
                        MaiorBorda.y = y;
                    }
                    UltYBordaCE = y;
                } else {
                    if (y == (UltYBordaCE + 1)) {
                        yBorda = MaiorBorda.y - ALT_COLUNA;
                        ImgDest[yBorda][x].SetVermelho();
                        BordaTemp = new TBorda();
                        BordaTemp.Y = yBorda;
                        BordaTemp.TipoBorda = BORDA_CLARO_ESCURO;
                        ParamsMLT.BordasColunas.Bordas[x].addElement(BordaTemp);

                        MaiorBorda.DifLum = 0;
                    }
                }

                //borda escuro encima, claro embaixo
                DifLum = MaisEscuroAtual - MaisClaroAnterior;
                if (DifLum > DIF_MIN_LUM) {
                    if (DifLum > MaiorBorda.DifLum) {
                        MaiorBorda.DifLum = DifLum;
                        MaiorBorda.y = y;
                    }
                    UltYBordaEC = y;
                } else {
                    if (y == (UltYBordaEC + 1)) {
                        yBorda = MaiorBorda.y - ALT_COLUNA;
                        ImgDest[yBorda][x].SetVerde();
                        BordaTemp = new TBorda();
                        BordaTemp.Y = yBorda;
                        BordaTemp.TipoBorda = BORDA_ESCURO_CLARO;
                        ParamsMLT.BordasColunas.Bordas[x].addElement(BordaTemp);

                        MaiorBorda.DifLum = 0;
                    }
                }
            }
        }
    }
}

class TMaiorBorda {

    int y;
    int DifLum;
}