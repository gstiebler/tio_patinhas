/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testejavadesktop;

import java.util.Vector;

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
        ParamsRC.ParamsABT.BImgDest = ParamsRC.ParamsMLT.BImgDest;
        ParamsRC.ParamsABT.BordasColunas = ParamsRC.ParamsMLT.BordasColunas;
        AnalizaBordasTarja(ParamsRC.ParamsABT);
        ParamsRC.ConverteParametrosDependentesAlturaFaixa();
        if (ParamsRC.ParamsABT.AchouTarja) {
            ParamsRC.ParamsAI.RefTarja = ParamsRC.ParamsABT.RefTarja;
            ParamsRC.ParamsAI.BImgDest = ParamsRC.ParamsABT.BImgDest;
            ParamsRC.ParamsAI.TCImgSrc = ParamsRC.ParamsMLT.TCImgSrc;
            //AnalizaIdentificador(ParamsRC.ParamsAI);
        }
        //EscreveParametros(ParamsRC);
    }

    static void MostraLimiteTarja(TParamsMLT ParamsMLT) {

           int ALT_COLUNA = 3, DY = 2, DIF_MIN_LUM = 15;
         
         
         
        int x,  y,  j;
        int yBorda;
        int DifLum;
        int UltYBordaCE,  UltYBordaEC;
         
        int MaisEscuroAtual,  MaisClaroAtual  ,MaisClaroAnterior   ,  MaisEscuroAnterior     ;
        int lum     ;
         
            
           
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

    static void AnalizaBordasTarja(TParamsABT ParamsABT) {
        int x, n;
        int NumColunas = ParamsABT.BordasColunas.NumColunas;
        ParamsABT.ConjuntoMeioBordas = new TConjuntoMeioBordas(NumColunas);
        int dif;
        TVectorBorda BordasTemp;
        TBorda BordaEnc, BordaEmb;
        TMeioBordas MeioBordasTemp = new TMeioBordas();
        Cor[][] ImgDest = ParamsABT.BImgDest.PMCor;
        for (x = 0; x < NumColunas; x++) {
            BordasTemp = ParamsABT.BordasColunas.Bordas[x];
            //percorre todas as bordas da coluna
            for (n = 1; n < BordasTemp.size(); n++) {
                BordaEnc = BordasTemp.retornaTBorda(n - 1);
                BordaEmb = BordasTemp.retornaTBorda(n);
                dif = BordaEmb.Y - BordaEnc.Y;
                if ((BordaEmb.TipoBorda == BORDA_ESCURO_CLARO) &&
                        (BordaEnc.TipoBorda == BORDA_CLARO_ESCURO) &&
                        (dif >= ParamsABT.AltMinTarja) && (dif <= ParamsABT.AltMaxTarja)) {
                    MeioBordasTemp.Inicializa(BordaEnc.Y, BordaEmb.Y);
                    ParamsABT.ConjuntoMeioBordas.VectorMeioBordas[x].addElement(MeioBordasTemp);
                    ImgDest[MeioBordasTemp.yMeio][x].SetAmarelo();
                }
            }
        }
        SelecionaTarja(ParamsABT);
        if (ParamsABT.AchouTarja) {
            ImgDest[ParamsABT.RefTarja.y][ParamsABT.RefTarja.x].SetMagenta();
            ImgDest[ParamsABT.RefTarja.y + 1][ParamsABT.RefTarja.x].SetMagenta();
            ImgDest[ParamsABT.RefTarja.y][ParamsABT.RefTarja.x + 1].SetMagenta();
            ImgDest[ParamsABT.RefTarja.y + 1][ParamsABT.RefTarja.x + 1].SetMagenta();
        }
    }

    static void SelecionaTarja(TParamsABT ParamsABT) {
         
         
         
         
         
        int n,  nMenorX,  m,  LargTarjaCandidata;
         TTarja  TarjaCandidata;
        int MenorX;
        double soma, desvio, media, MediaTarjaSelecionada = 0;
        PreparaSelecionaTarja(ParamsABT);
        MenorX = 0xFFFFFF;
        nMenorX = -1;
        ParamsABT.AchouTarja = false;
        //percorre vector com todas as tarjas
        for (n = 0; n < ParamsABT.VectorTarja.size(); n++) {
            TarjaCandidata = ParamsABT.VectorTarja.retornaTTarja(n);
            LargTarjaCandidata = TarjaCandidata.VetorAlturas.size();
            /*#ifdef DEBUG
            Log->Add("Tarja candidata "+IntToStr(n)+".\tLargura:\t"+IntToStr(LargTarjaCandidata)+
            "\tX:\t"+IntToStr(TarjaCandidata->X));
            #
            endif*/

            if ((LargTarjaCandidata <= ParamsABT.LargMaxTarja) &&
                    (LargTarjaCandidata >= ParamsABT.LargMinTarja)) {
                soma = 0;
                //calcula a média da altura de cada coluna da possível tarja
                for (m = 0; m < ParamsABT.VectorTarja.retornaTTarja(n).VetorAlturas.size(); m++) {
                    soma += ParamsABT.VectorTarja.retornaTTarja(n).VetorAlturas.retornaInteiro(m);
                }
                media = soma / ParamsABT.VectorTarja.retornaTTarja(n).VetorAlturas.size();
                //calcula o desvio padrão
                soma = 0;
                for (m = 0; m < ParamsABT.VectorTarja.retornaTTarja(n).VetorAlturas.size(); m++) {
                    soma = Math.abs(media - ParamsABT.VectorTarja.retornaTTarja(n).VetorAlturas.retornaInteiro(m));
                }
                desvio = soma / ParamsABT.VectorTarja.retornaTTarja(n).VetorAlturas.size();
                /*#
                ifdef DEBUG
                Log->Add("desvio padrão alturas: "+FloatToStr(desvio));
                #
                endif*/

                if (desvio < ParamsABT.DesvioMax) {
                    /*#
                    ifdef DEBUG
                    Log->Add("menor do que o limite de : "+FloatToStr(ParamsABT.DesvioMax));
                    #
                    endif*/
                    //pode ser substituído por um código que pega o primeiro. mantido para debug
                    if (ParamsABT.VectorTarja.retornaTTarja(n).X < MenorX) {
                        MenorX = ParamsABT.VectorTarja.retornaTTarja(n).X;
                        nMenorX = n;
                        MediaTarjaSelecionada = media;
                    }
                }
            }
        }
        if (nMenorX != -1) {
            ParamsABT.RefTarja.x = ParamsABT.VectorTarja.retornaTTarja(nMenorX).X;
            ParamsABT.RefTarja.y = ParamsABT.VectorTarja.retornaTTarja(nMenorX).PriYEnc;
            ParamsABT.MediaAlturaTarja = MediaTarjaSelecionada;
            ParamsABT.AchouTarja = true;
        }
    }

    static void PreparaSelecionaTarja(TParamsABT ParamsABT) {
        int x;
        int n, m;
        boolean Adicionou;
        TMeioBordas MeioBordasTemp;
        for (x = 0; x < ParamsABT.ConjuntoMeioBordas.NumColunas; x++) {
            //percorre todos os meio de bordas da coluna (pixels amarelos)
            for (m = 0; m < ParamsABT.ConjuntoMeioBordas.VectorMeioBordas[x].size(); m++) {
                Adicionou = false;
                MeioBordasTemp = ParamsABT.ConjuntoMeioBordas.VectorMeioBordas[x].retornaTMeioBordas(m);
                //percorre todas as tarjas que estão ativas na coluna corrente
                for (n = 0; n < ParamsABT.VectorTarja.size(); n++) {
                    if (ParamsABT.VectorTarja.retornaTTarja(n).Ativa(x)) {
                        if (Math.abs(ParamsABT.VectorTarja.retornaTTarja(n).UltYMeio - MeioBordasTemp.yMeio) <= ParamsABT.DistMaxTarjas) {
                            ParamsABT.VectorTarja.retornaTTarja(n).VetorAlturas.adicionaInteiro(MeioBordasTemp.Altura);
                            ParamsABT.VectorTarja.retornaTTarja(n).UltYMeio = MeioBordasTemp.yMeio;
                            Adicionou = true;
                        }
                    }
                }
                if (!Adicionou) {
                    TTarja TarjaTemp = new TTarja();
                    TarjaTemp.X = x;
                    TarjaTemp.UltYMeio = MeioBordasTemp.yMeio;
                    TarjaTemp.PriYEnc = MeioBordasTemp.Y1;
                    TarjaTemp.VetorAlturas.adicionaInteiro(MeioBordasTemp.Altura);
                    ParamsABT.VectorTarja.adicionaTTarja(TarjaTemp);
                }
            }
        }
    }
}

class TMaiorBorda {

    int y;
    int DifLum;
}