/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package testejavadesktop;

import java.util.*;

/**
 *
 * @author Administrator
 */
public class UTioPatinhas {

    static int BORDA_CLARO_ESCURO = 0,  BORDA_ESCURO_CLARO = 1;
    static int TAM_VETOR_LIMITES_VERTICAIS_GRUPOS = 300;
    static int PIXEL_ACEITO = 1;
    static int PIXEL_NAO_ACEITO = 2;

    UTioPatinhas() {
    }

    static void ReconheceCedula(TParamsRC ParamsRC) {
        ParamsRC.LumMedianaImagem = Histograma(ParamsRC.ParamsMLT.TCImgSrc);
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
            AnalizaIdentificador(ParamsRC.ParamsAI);
        }
    //EscreveParametros(ParamsRC);
    }

    static int Histograma(CTonsCinza TCImgSrc) {
        int[] vetor = new int[256];
        int[] vetorCumulativa = new int[256];
        int n, x, y;
        int MetadeTotal;
        int Mediana;
        int[][] ImgSrc = TCImgSrc.TonsCinza;
        for (y = 0; y < TCImgSrc.Alt; y++) {
            for (x = 0; x < TCImgSrc.Larg; x++) {
                vetor[ImgSrc[y][x]]++;
            }
        }
        vetorCumulativa[0] = vetor[0];
        for (n = 1; n < 256; n++) {
            vetorCumulativa[n] = vetorCumulativa[n - 1] + vetor[n];
        }
        Mediana = 0;
        MetadeTotal = vetorCumulativa[255] / 2;
        for (n = 1; n < 256; n++) {
            if (vetorCumulativa[n] >= MetadeTotal) {
                Mediana = n;
                break;
            }
        }
        return Mediana;
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

    static void AnalizaBordasTarja(TParamsABT ParamsABT) {


        int x,
                n;
        int NumColunas = ParamsABT.BordasColunas.NumColunas;
        ParamsABT.ConjuntoMeioBordas = new TConjuntoMeioBordas(NumColunas);
        int dif;
        TVectorBorda BordasTemp;
        TBorda BordaEnc, BordaEmb;
        TMeioBordas MeioBordasTemp;//= new TMeioBordas();
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
                    MeioBordasTemp = new TMeioBordas(BordaEnc.Y, BordaEmb.Y);
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

        int n, nMenorX, m, LargTarjaCandidata;
        TTarja TarjaCandidata;
        int MenorX, AlturaTarjaMColunaN;
        ;
        double soma, desvio, media, MediaTarjaSelecionada = 0;
        PreparaSelecionaTarja(ParamsABT);
        MenorX = 0xFFFFFF;
        nMenorX = -1;
        ParamsABT.AchouTarja = false;
        //percorre vector com todas as tarjas
        for (n = 0; n < ParamsABT.VectorTarja.size(); n++) {
            TarjaCandidata = ParamsABT.VectorTarja.retornaTTarja(n);
            LargTarjaCandidata = TarjaCandidata.VetorAlturas.size();
            //ifdef DEBUG
            System.out.println("Tarja candidata " + String.valueOf(n) + ".\tLargura:\t" + String.valueOf(LargTarjaCandidata) +
                    "\tX:\t" + String.valueOf(TarjaCandidata.X) +
                    "\tY:\t" + String.valueOf(TarjaCandidata.PriYEnc));
            //endif

            if ((LargTarjaCandidata <= ParamsABT.LargMaxTarja) &&
                    (LargTarjaCandidata >= ParamsABT.LargMinTarja)) {
                soma = 0;
                //calcula a mÃ©dia da altura de cada coluna da possÃ­vel tarja
                for (m = 0; m < ParamsABT.VectorTarja.retornaTTarja(n).VetorAlturas.size(); m++) {
                    soma += ParamsABT.VectorTarja.retornaTTarja(n).VetorAlturas.retornaInteiro(m).intValue();
                }
                media = soma / ParamsABT.VectorTarja.retornaTTarja(n).VetorAlturas.size();
                //calcula o desvio padrÃ£o
                soma = 0;
                for (m = 0; m < ParamsABT.VectorTarja.retornaTTarja(n).VetorAlturas.size(); m++) {
                    AlturaTarjaMColunaN = ParamsABT.VectorTarja.retornaTTarja(n).VetorAlturas.retornaInteiro(m).intValue();
                    soma = Math.abs(media - AlturaTarjaMColunaN);
                }
                desvio = soma / ParamsABT.VectorTarja.retornaTTarja(n).VetorAlturas.size();
                // ifdef DEBUG
                System.out.println("desvio padrÃ£o alturas: " + String.valueOf(desvio));
                //endif

                if (desvio < ParamsABT.DesvioMax) {
                    //ifdef DEBUG
                    System.out.println("menor do que o limite de : " + String.valueOf(ParamsABT.DesvioMax));
                    //endif
                    //pode ser substituÃ­do por um cÃ³digo que pega o primeiro. mantido para debug
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
                //percorre todas as tarjas que estÃ£o ativas na coluna corrente
                for (n = 0; n < ParamsABT.VectorTarja.size(); n++) {
                    if (ParamsABT.VectorTarja.retornaTTarja(n).Ativa(x)) {
                        if (Math.abs(ParamsABT.VectorTarja.retornaTTarja(n).UltYMeio - MeioBordasTemp.yMeio) <= ParamsABT.DistMaxTarjas) {
                            ParamsABT.VectorTarja.retornaTTarja(n).VetorAlturas.adicionaInteiro(new Integer(MeioBordasTemp.Altura));
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
                    TarjaTemp.VetorAlturas.adicionaInteiro(new Integer(MeioBordasTemp.Altura));
                    ParamsABT.VectorTarja.adicionaTTarja(TarjaTemp);
                }
            }
        }
    }
//Mostra a linha cyan (azul claro) que representa o ponto de referÃªncia da tarja para a localizaÃ§Ã£o
//do identificador, e retorna a luminosidade mÃ©dia dos pixels da faixa
    static int MediaFaixa(TParamsAI ParamsAI) {
        int x, y, xIni, xFim, soma;
        int[][] ImgSrc = ParamsAI.TCImgSrc.TonsCinza;
        Cor[][] ImgDest = ParamsAI.BImgDest.PMCor;
        xIni = ParamsAI.RefTarja.x;
        xFim = xIni + ParamsAI.LargFaixaRef;
        y = ParamsAI.RefTarja.y - ParamsAI.DistFaixaRef;
        soma = 0;
        for (x = xIni; x < xFim; x++) {
            soma += ImgSrc[y][x];
            ImgDest[y][x].SetCyan();
        }
        double retorno1 = (soma * 1.0 / ParamsAI.LargFaixaRef);
        int retorno = (int) MathUtils.round(retorno1);
        return retorno;
    }
//---------------------------------------------------------------------------

//MatrizGrupos Ã© um quadrado com dimensÃµes definidas por ARect. Ã‰ onde sÃ£o retornados
// os grupos conexos do identificador
    static void MatrizGruposConexos(CTonsCinza tcImgSrc, TRect ARect, int[][] MatrizGrupos, int limiar,
            TLimitesVerticaisGrupo[] VetorLimitesVerticaisGrupo, int[] PonteiroGrupos) {
        int x, y, i, j, n;
        boolean AnteriorDentroGrupo;//informa se o pixel anteriormente processado na linha possuÃ­a um grupo
        boolean AchouGrupoEncima;
        boolean EmGrupoNovo = false;
        int ContadorGrupos, GrupoAtual, GrupoEncima;
        int[][] ImgSrc = tcImgSrc.TonsCinza;
        //inicialmente cada grupo aponta para ele mesmo
        for (n = 0; n < TAM_VETOR_LIMITES_VERTICAIS_GRUPOS; n++) {
            PonteiroGrupos[n] = n;
        }
        ContadorGrupos = 1;
        GrupoAtual = 0;

        for (y = ARect.top + 1; y < ARect.bottom; y++) {
            AnteriorDentroGrupo = false;
            for (x = ARect.left + 1; x < ARect.right - 1; x++) {
                if (ImgSrc[y][x] < limiar) {
                    if (!AnteriorDentroGrupo)//se pixel anterior (da esquerda) nÃ£o possuia grupo, prossegue
                    {
                        EmGrupoNovo = true;
                        AchouGrupoEncima = false;
                        j = y - ARect.top;
                        //procura encima por pixels com grupo
                        for (n = -1; n <= 1; n++) {
                            i = x - ARect.left;
                            GrupoAtual = PonteiroGrupos[MatrizGrupos[j - 1][i + n]];
                            if (GrupoAtual > 0) {
                                AchouGrupoEncima = true;
                                EmGrupoNovo = false;
                                break;
                            }
                        }
                        //se nÃ£o achou pixels encima entÃ£o cria um novo grupo
                        if (!AchouGrupoEncima) {
                            GrupoAtual = ContadorGrupos;
                            VetorLimitesVerticaisGrupo[GrupoAtual].yEmb = 0;
                            VetorLimitesVerticaisGrupo[GrupoAtual].yEnc = 0xFFFF;
                            ContadorGrupos++;
                        }
                    } else //jÃ¡ estamos dentro de um grupo
                    {
                        //devemos sempre ver se encima existe algum grupo antigo
                        j = y - ARect.top - 1;
                        i = x - ARect.left + 1;
                        GrupoEncima = PonteiroGrupos[MatrizGrupos[j][i]];
                        if ((MatrizGrupos[j][i] > 0) && (GrupoEncima != GrupoAtual))//tem grupo antigo encima
                        {
                            if (EmGrupoNovo) {
                                PonteiroGrupos[GrupoAtual] = GrupoEncima;
                                GrupoAtual = GrupoEncima;//o grupo atual agora Ã© o de cima
                                EmGrupoNovo = false;
                            } else {
                                PonteiroGrupos[GrupoEncima] = GrupoAtual;
                            }
                        }
                    }
                    j = y - ARect.top;
                    i = x - ARect.left;
                    MatrizGrupos[j][i] = GrupoAtual;
                    AnteriorDentroGrupo = true;
                    //atualiza limites verticais do grupo caso necessÃ¡rio
                    if (j > VetorLimitesVerticaisGrupo[GrupoAtual].yEmb) {
                        VetorLimitesVerticaisGrupo[GrupoAtual].yEmb = j;
                    }
                    if (j < VetorLimitesVerticaisGrupo[GrupoAtual].yEnc) {
                        VetorLimitesVerticaisGrupo[GrupoAtual].yEnc = j;
                    }
                } else //se pixel Ã© mais claro que o limiar
                {
                    AnteriorDentroGrupo = false;
                }
            }
        }
    }
//---------------------------------------------------------------------------
    static void SelecionaGruposIdentificador(TLimitesVerticaisGrupo[] VetorLimitesVerticaisGrupo,
            char[] VetGruposValidos, int AltMin, int[] PonteiroGrupos, int yFim, int DifMinEmb) {
        int altura, DifEmb;
        int GrupoReal;
        for (int n = 1; n < TAM_VETOR_LIMITES_VERTICAIS_GRUPOS; n++) {
            GrupoReal = PonteiroGrupos[n];
            altura = VetorLimitesVerticaisGrupo[GrupoReal].yEmb - VetorLimitesVerticaisGrupo[GrupoReal].yEnc;
            DifEmb = yFim - VetorLimitesVerticaisGrupo[GrupoReal].yEmb;

            //ifdef DEBUG
            //  if (altura)
            System.out.println("RegiÃ£o candidata altura: " + String.valueOf(altura) + " DifEmb: " + String.valueOf(DifEmb));
            //endif
            if (altura >= AltMin && DifEmb < DifMinEmb) {
                VetGruposValidos[n] = (char) PIXEL_ACEITO;
            } else {
                VetGruposValidos[n] = (char) PIXEL_NAO_ACEITO;
            }
        }
    }
//---------------------------------------------------------------------------
//MatrizGrupos Ã© variÃ¡vel de entrada e de saÃ­da
    static void CopiaGruposValidos(int[][] MatrizGrupos, TRect ARect, char[] VetGruposValidos) {
        int x, y;
        int larg, alt;
        larg = ARect.Width();
        alt = ARect.Height();
        for (y = 0; y < alt; y++) {
            for (x = 0; x < larg; x++) {
                MatrizGrupos[y][x] = VetGruposValidos[MatrizGrupos[y][x]];
            }
        }
    }
    //---------------------------------------------------------------------------
    static void PintaIdentificador(CBitmap BImgDest, TRect ARect, int[][] MatrizGrupos) {
        int x, y;
        int larg, alt;
        Cor[][] ImgDest = BImgDest.PMCor;
        larg = ARect.Width();
        alt = ARect.Height();
        for (y = 0; y < alt; y++) {
            for (x = 0; x < larg; x++) {
                if (MatrizGrupos[y][x] == PIXEL_ACEITO) {
                    ImgDest[y + ARect.top][x + ARect.left].SetCyan();
                } else if (MatrizGrupos[y][x] == PIXEL_NAO_ACEITO) {
                    ImgDest[y + ARect.top][x + ARect.left].SetAzul();
                }
            }
        }
    }
//---------------------------------------------------------------------------
    static float RetornaRelacaoMedianasLargurasEncEmb(int[] VetorLarguras, int comeco, int fim,
            int[] MediaLarguras) {
        int alt = fim - comeco;
        int[] vetor = new int[alt + 1];
        int MetadeAltura = alt / 2;
        int QuartoAltura = (int) MathUtils.round(alt * 1.0 / 4);
        int[] Larguras = new int[2];
        for (int n = 0; n < 2; n++) {
            System.arraycopy(VetorLarguras, comeco + n * MetadeAltura, vetor, 0, MetadeAltura);
            Vector a=new Vector();
            for (int u=0; u<MetadeAltura; u++)
                a.addElement(new Integer(vetor[u]));
            java.util. Collections.sort(a);
            //memcpy(vetor, VetorLarguras + comeco + n * MetadeAltura, MetadeAltura * sizeof(int));
           // qsort(vetor, MetadeAltura, sizeof(int), ComparaInteiro);
            Larguras[n] = ((Integer)a.elementAt(QuartoAltura)).intValue();
        }
        //delete [] vetor;
        //ifdef DEBUG
        System.out.println("Largura encima: " + String.valueOf(Larguras[0]));
        System.out.println("Largura embaixo: " + String.valueOf(Larguras[1]));
        //endif
        MediaLarguras[0] = (Larguras[0] + Larguras[1]) / 2;
        if (Larguras[1] > 0) {
            return (float) (Larguras[0] * 1.0 / Larguras[1]);
        } else {
            return -100;
        }
    }
    //---------------------------------------------------------------------------
    
 static void Identifica(TParamsAI ParamsAI)
{
  //ifdef DEBUG
     System.out.println("InclinaÃ§Ã£o identificador: "+String.valueOf(ParamsAI.Inclinacao));
     System.out.println("Altura identificador: "+String.valueOf(ParamsAI.Alt));
     System.out.println("RelaÃ§Ã£o larguras identificador: "+String.valueOf(ParamsAI.RelacaoMedianasLargurasEncEmb));
    if (ParamsAI.RelacaoMedianasLargurasEncEmb>0)
      System.out.println("RelaÃ§Ã£o inversa larguras identificador: "+String.valueOf(1.0/ParamsAI.RelacaoMedianasLargurasEncEmb));
    System.out.println("RelaÃ§Ã£o Altura/Largura: "+String.valueOf(ParamsAI.RelacaoLargAlt));
    System.out.println("NÃºmero mÃ©dio de colunas: "+String.valueOf(ParamsAI.NumMedColunas));
  //endif
  if (ParamsAI.Inclinacao>ParamsAI.LimiarInclinacaoidentificador)
  {                       
    //ifdef DEBUG
      System.out.println("InclinaÃ§Ã£o maior que o limite, pode ser \tR$2\tR$20");
    ///endif
    if (ParamsAI.RelacaoLargAlt>ParamsAI.LimiarRelacaoLargAlt)
    {
      //ifdef DEBUG
      System.out.println("RelaÃ§Ã£o Altura/Largura maior que o limite, Ã© R$2");
      //endif
      ParamsAI.ValorCedula=2;
    }
    else
    {
      //ifdef DEBUG
        System.out.println("RelaÃ§Ã£o Altura/Largura menor que o limite, Ã© R$20");
      //endif
      ParamsAI.ValorCedula=20;
    }
  }
  else
  {
    //ifdef DEBUG
      System.out.println("InclinaÃ§Ã£o menor que o limite, pode ser \tR$1\tR$5\tR$10\tR$50\tR$100");
    //endif
    if (ParamsAI.Alt>ParamsAI.LimiarAlturaIdentificador)
    {
      //ifdef DEBUG
        System.out.println("Altura maior que o limite, pode ser \tR$1\tR$5\tR$50\tR$100");
      //endif
      if (ParamsAI.RelacaoMedianasLargurasEncEmb>ParamsAI.LimiarLargLinhasIdentificador)
      {
        //ifdef DEBUG
        System.out.println("RelaÃ§Ã£o medianas larguras maior que o limite, Ã© R$50");
        //endif
        ParamsAI.ValorCedula=50;
      }
      else if (1.0/ParamsAI.RelacaoMedianasLargurasEncEmb>ParamsAI.LimiarLargLinhasIdentificador)
      {
        //ifdef DEBUG
        System.out.println("RelaÃ§Ã£o inversa medianas larguras maior que o limite, Ã© R$100");
        //endif
        ParamsAI.ValorCedula=100;
      }
      else
      {
        //ifdef DEBUG
        System.out.println("RelaÃ§Ã£o medianas larguras menor que o limite, pode ser \tR$1\tR$5");
        //endif
        if (ParamsAI.NumMedColunas<ParamsAI.LimiarNumMedColunas)
        {
          //ifdef DEBUG
            System.out.println("NÃºmero mÃ©dio de colunas menor que o limite, Ã© R$1");
          //endif
          ParamsAI.ValorCedula=1;
        }
        else
        {
          //ifdef DEBUG
            System.out.println("NÃºmero mÃ©dio de colunas maior que o limite, Ã© R$5");
          //endif
          ParamsAI.ValorCedula=5;
        }
      }
    }
    else
    {
      //ifdef DEBUG
            System.out.println("Altura menor que o limite, Ã© R$10");
      //endif
      ParamsAI.ValorCedula=10;
    }
  }
}
//---------------------------------------------------------------------------
    
    static void AnalizaIdentificador(TParamsAI ParamsAI) {
        int NADA = 0, IDENTIFICADOR = 1, FUNDO = 2;
        int TAM_HIST = 200;
        int EstadoUltPixel;
        int x, y;
        int xIni, xFim, yIni, yFim;
        int YEnc, YEmb, LargLinha, MaiorLargLinha, NumBordasPixelLinha;
        int XEsq, XDir, UltXEnc = 0, UltXEmb = 0, MaiorUltXEnc;
        int NumLinha, UltYComLinha, NumPixelsIdentificador;
        int[] VetorLarguras;
        char[] VetGruposValidos = new char[TAM_VETOR_LIMITES_VERTICAIS_GRUPOS];
        //usado para indicar grupos conexos que fazem parte de outros grupos
        int[] PonteiroGrupos = new int[TAM_VETOR_LIMITES_VERTICAIS_GRUPOS];
        boolean AchouLinha;
        int[] HistogramaNumBordasPixelLinha = new int[TAM_HIST];
        TRect ARect;
        xIni = ParamsAI.RefTarja.x - ParamsAI.XIniParaRefTarja;
        xFim = xIni + ParamsAI.LargIdentificador;
        yFim = ParamsAI.RefTarja.y - ParamsAI.YIniParaRefTarja;
        yIni = yFim - ParamsAI.AltIdentificador;
        int TamVetLarguras = yFim - yIni + 1;
        VetorLarguras = new int[TamVetLarguras];
        //memset(VetorLarguras, 0, TamVetLarguras*sizeof(int));
        int Media = MediaFaixa(ParamsAI);
        //ifdef DEBUG
        System.out.println("Luminosidade mÃ©dia faixa: " + String.valueOf(Media));
        //endif         
        int Limiar = Media - ParamsAI.DifMinMediaFaixaRef;

        CMatrizInteiro MatrizGrupos = new CMatrizInteiro(xFim - xIni + 1, yFim - yIni + 1);
        TLimitesVerticaisGrupo[] VetorLimitesVerticaisGrupo =
                new TLimitesVerticaisGrupo[TAM_VETOR_LIMITES_VERTICAIS_GRUPOS];
        for (int s = 0; s < TAM_VETOR_LIMITES_VERTICAIS_GRUPOS; s++) {
            VetorLimitesVerticaisGrupo[s] = new TLimitesVerticaisGrupo();
        }
        ARect = new TRect(xIni, yIni, xFim, yFim);
        MatrizGruposConexos(ParamsAI.TCImgSrc, ARect,
                MatrizGrupos.Matriz, Limiar, VetorLimitesVerticaisGrupo, PonteiroGrupos);
        SelecionaGruposIdentificador(VetorLimitesVerticaisGrupo, VetGruposValidos,
                ParamsAI.AltMinGrupoConexoIdentificador, PonteiroGrupos, ARect.Height(),
                ParamsAI.DifMinEmbGrupoEmbRegiaoIdentificador);
        CopiaGruposValidos(MatrizGrupos.Matriz, ARect, VetGruposValidos);
        PintaIdentificador(ParamsAI.BImgDest, ARect, MatrizGrupos.Matriz);

        YEnc = 0;
        YEmb = -1;
        MaiorLargLinha = 0;
        NumLinha = 0;
        MaiorUltXEnc = 0;
        NumPixelsIdentificador = 0;
        UltYComLinha = -1;
        //ifdef DEBUG

        System.out.println("Ãrea do identificador: X: (" + String.valueOf(xIni) + ", " +
                String.valueOf(xFim) + " Y: (" + String.valueOf(yIni) + ", " + String.valueOf(yFim) + ")");
        //endif
        for (y = ARect.Height(); y > 0; y--) {
            XEsq = -1;
            XDir = 0;
            AchouLinha = false;
            NumBordasPixelLinha = 0;
            EstadoUltPixel = NADA;
            for (x = 0; x < ARect.Width(); x++) {
                if (MatrizGrupos.Matriz[y][x] == PIXEL_ACEITO) {
                    if (YEmb == -1) {
                        YEmb = y;
                    }
                    YEnc = y;

                    if (XEsq == -1) {
                        XEsq = x;
                    }
                    XDir = x;
                    //se for a primeira linha
                    if (NumLinha == 0) {
                        UltXEmb = x;
                    }
                    UltXEnc = x;
                    AchouLinha = true;
                    NumPixelsIdentificador++;
                    if (EstadoUltPixel != IDENTIFICADOR) {
                        NumBordasPixelLinha++;
                    }
                    EstadoUltPixel = IDENTIFICADOR;
                } else {
                    EstadoUltPixel = FUNDO;
                }
            }
            LargLinha = XDir - XEsq;
            VetorLarguras[y] = LargLinha;
            if (LargLinha > MaiorLargLinha) {
                MaiorLargLinha = LargLinha;
            }
            if (AchouLinha) {
                if (UltXEnc > MaiorUltXEnc) {
                    MaiorUltXEnc = UltXEnc;
                }
                NumLinha++;
                UltYComLinha = y;
                HistogramaNumBordasPixelLinha[NumBordasPixelLinha]++;
            } else {
                if ((UltYComLinha != -1) &&
                        ((UltYComLinha - y) >= ParamsAI.MaiorDistSemPixelsIdentificador) &&
                        (NumPixelsIdentificador > ParamsAI.NumMinPixelsIdentificador)) {
                    break;
                }
            }
        }
        int soma = 0;
        for (int w = 0; w < TAM_HIST; w++) {
            soma += HistogramaNumBordasPixelLinha[w] * w;
        }
        if (NumLinha > 0) {
            ParamsAI.NumMedColunas = (float) (soma * 1.0 / NumLinha);
        } else {
            ParamsAI.NumMedColunas = -100;
        }
        int[] MediaLarguras = new int[1];
        ParamsAI.RelacaoMedianasLargurasEncEmb =
                RetornaRelacaoMedianasLargurasEncEmb(VetorLarguras, YEnc, YEmb, MediaLarguras);
        ParamsAI.Alt = YEmb - YEnc;
        if (MediaLarguras[0] > 0) {
            ParamsAI.RelacaoLargAlt = (float) (ParamsAI.Alt * 1.0 / MediaLarguras[0]);
        } else {
            ParamsAI.RelacaoLargAlt = 0;
            //ifdef DEBUG
            System.out.println("UltXEnc: " + String.valueOf(MaiorUltXEnc) + "\tUltXEmb: " + String.valueOf(UltXEmb));
            System.out.println("YEmb: " + String.valueOf(YEmb) + "\t\tYEnc: " + String.valueOf(YEnc));
        //endif
        }
        if (ParamsAI.Alt > 0) {
            ParamsAI.Inclinacao = (float) ((MaiorUltXEnc - UltXEmb) * 1.0 / ParamsAI.Alt);
        } else {
            ParamsAI.Inclinacao = -100;
        }
        ParamsAI.MaiorLargLinha = MaiorLargLinha;
    Identifica(ParamsAI);
    //delete [] VetorLarguras;
    //delete ARect;
    //delete MatrizGrupos;
    }
//--------------------------------------------------------------------------- 
}
    