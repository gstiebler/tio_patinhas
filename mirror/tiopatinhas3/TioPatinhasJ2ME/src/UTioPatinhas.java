/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author Administrator
 */
public class UTioPatinhas {

    static int BORDA_CLARO_ESCURO = 0,  BORDA_ESCURO_CLARO = 1;

    UTioPatinhas() {
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
        //AnalizaIdentificador(ParamsRC.ParamsAI);
        }
    //EscreveParametros(ParamsRC);
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

        int n, nMenorX, m, LargTarjaCandidata;
        TTarja TarjaCandidata;
        int MenorX, AlturaTarjaMColunaN;;
        double soma, desvio, media, MediaTarjaSelecionada = 0;
        PreparaSelecionaTarja(ParamsABT);
        MenorX = 0xFFFFFF;
        nMenorX = -1;
        ParamsABT.AchouTarja = false;
        //percorre vector com todas as tarjas
        for (n = 0; n < ParamsABT.VectorTarja.size(); n++) {
            TarjaCandidata = ParamsABT.VectorTarja.retornaTTarja(n);
            LargTarjaCandidata = TarjaCandidata.VetorAlturas.size();

            if ((LargTarjaCandidata <= ParamsABT.LargMaxTarja) &&
                    (LargTarjaCandidata >= ParamsABT.LargMinTarja)) {
                soma = 0;
                //calcula a média da altura de cada coluna da possível tarja
                for (m = 0; m < ParamsABT.VectorTarja.retornaTTarja(n).VetorAlturas.size(); m++) {
                    soma = soma + ParamsABT.VectorTarja.retornaTTarja(n).VetorAlturas.retornaInteiro(m).intValue();
                }
                media = soma / ParamsABT.VectorTarja.retornaTTarja(n).VetorAlturas.size();
                //calcula o desvio padrão
                soma = 0;
                for (m = 0; m < ParamsABT.VectorTarja.retornaTTarja(n).VetorAlturas.size(); m++) {
                    AlturaTarjaMColunaN=ParamsABT.VectorTarja.retornaTTarja(n).VetorAlturas.retornaInteiro(m).intValue();
                    soma = Math.abs(media - AlturaTarjaMColunaN);
                }
                desvio = soma / ParamsABT.VectorTarja.retornaTTarja(n).VetorAlturas.size();
              
                if (desvio < ParamsABT.DesvioMax) {
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
                    TarjaTemp.VetorAlturas.adicionaInteiro(new Integer (MeioBordasTemp.Altura));
                    ParamsABT.VectorTarja.adicionaTTarja(TarjaTemp);
                }
            }
        }
    }
    
    /* -----------------------------------------------------------
    void AnalizaIdentificador(TParamsAI &ParamsAI)
  {
  enum TEstadoUltPixel {NADA, IDENTIFICADOR, FUNDO};
  const int TAM_HIST=200;
  TEstadoUltPixel EstadoUltPixel;
  int x, y;
  int xIni, xFim, yIni, yFim;
  int YEnc, YEmb, LargLinha, MaiorLargLinha, NumBordasPixelLinha;
  int XEsq, XDir, UltXEnc, UltXEmb, MaiorUltXEnc;
  int NumLinha, UltYComLinha, NumPixelsIdentificador;
  int *VetorLarguras;
  char VetGruposValidos[TAM_VETOR_LIMITES_VERTICAIS_GRUPOS];
  //usado para indicar grupos conexos que fazem parte de outros grupos
  int PonteiroGrupos[TAM_VETOR_LIMITES_VERTICAIS_GRUPOS];
  bool AchouLinha;
  int HistogramaNumBordasPixelLinha[TAM_HIST]={0};
  TRect *ARect;
  xIni=ParamsAI.RefTarja.x-ParamsAI.XIniParaRefTarja;
  xFim=xIni+ParamsAI.LargIdentificador;
  yFim=ParamsAI.RefTarja.y-ParamsAI.YIniParaRefTarja;
  yIni=yFim-ParamsAI.AltIdentificador;
  int TamVetLarguras=yFim-yIni+1;
  VetorLarguras=new int [TamVetLarguras];
  memset(VetorLarguras, 0, TamVetLarguras*sizeof(int));
  byte Media=MediaFaixa(ParamsAI);
  #ifdef DEBUG
    Log->Add("Luminosidade média faixa: "+IntToStr(Media));
  #endif         
  byte Limiar=Media-ParamsAI.DifMinMediaFaixaRef;

  CMatrizInteiro *MatrizGrupos=new CMatrizInteiro(xFim-xIni+1, yFim-yIni+1);
  TLimitesVerticaisGrupo VetorLimitesVerticaisGrupo[TAM_VETOR_LIMITES_VERTICAIS_GRUPOS];
  for (int s=0; s<TAM_VETOR_LIMITES_VERTICAIS_GRUPOS; s++)
  {
    VetorLimitesVerticaisGrupo[s].yEmb=0; 
    VetorLimitesVerticaisGrupo[s].yEnc=0;
  }
  ARect=new TRect(xIni, yIni, xFim, yFim);
  MatrizGruposConexos(ParamsAI.TCImgSrc, *ARect,
                        MatrizGrupos->Matriz, Limiar, VetorLimitesVerticaisGrupo, PonteiroGrupos);
  SelecionaGruposIdentificador(VetorLimitesVerticaisGrupo, VetGruposValidos,
                           ParamsAI.AltMinGrupoConexoIdentificador, PonteiroGrupos, ARect->Height(),
                                ParamsAI.DifMinEmbGrupoEmbRegiaoIdentificador);
  CopiaGruposValidos(MatrizGrupos->Matriz, *ARect, VetGruposValidos);
  PintaIdentificador(ParamsAI.BImgDest, *ARect, MatrizGrupos->Matriz);

  YEnc=0;
  YEmb=-1;
  MaiorLargLinha=0;
  NumLinha=0;
  MaiorUltXEnc=0;
  NumPixelsIdentificador=0;
  UltYComLinha=-1;
  #ifdef DEBUG
    Log->Add("Área do identificador: X: ("+IntToStr(xIni)+", "+
        IntToStr(xFim)+" Y: ("+IntToStr(yIni)+", "+IntToStr(yFim)+")");
  #endif
  for (y=ARect->Height(); y>0; y--)
  {
    XEsq=-1;
    XDir=0;
    AchouLinha=false;
    NumBordasPixelLinha=0;
    EstadoUltPixel=NADA;
    for (x=0; x<ARect->Width(); x++)
    {
      if (MatrizGrupos->Matriz[y][x]==PIXEL_ACEITO)
      {
        if (YEmb==-1)
          YEmb=y;
        YEnc=y;

        if (XEsq==-1)
          XEsq=x;
        XDir=x;
        //se for a primeira linha
        if (NumLinha==0)
          UltXEmb=x;
        UltXEnc=x;
        AchouLinha=true;
        NumPixelsIdentificador++;
        if (EstadoUltPixel!=IDENTIFICADOR)
          NumBordasPixelLinha++;
        EstadoUltPixel=IDENTIFICADOR;
      }
      else
        EstadoUltPixel=FUNDO;
    }
    LargLinha=XDir-XEsq;
    VetorLarguras[y]=LargLinha;
    if (LargLinha>MaiorLargLinha)
      MaiorLargLinha=LargLinha;
    if (AchouLinha)
    {             
      if (UltXEnc>MaiorUltXEnc)
        MaiorUltXEnc=UltXEnc;
      NumLinha++;
      UltYComLinha=y;
      HistogramaNumBordasPixelLinha[NumBordasPixelLinha]++;
    }
    else
    {
      if ((UltYComLinha!=-1) &&
                ((UltYComLinha-y)>=ParamsAI.MaiorDistSemPixelsIdentificador)  &&
              (  NumPixelsIdentificador>ParamsAI.NumMinPixelsIdentificador  )  )
        break;
    }
  }
  int soma=0;
  for (int w=0; w<TAM_HIST; w++)
    soma+=HistogramaNumBordasPixelLinha[w]*w;
  if (NumLinha>0)
    ParamsAI.NumMedColunas=soma*1.0/NumLinha;
  else
    ParamsAI.NumMedColunas=-100;
  int MediaLarguras;
  ParamsAI.RelacaoMedianasLargurasEncEmb=
        RetornaRelacaoMedianasLargurasEncEmb(VetorLarguras, YEnc, YEmb, MediaLarguras);
  ParamsAI.Alt=YEmb-YEnc;
  if (MediaLarguras>0)
    ParamsAI.RelacaoLargAlt=ParamsAI.Alt*1.0/MediaLarguras;
  else
    ParamsAI.RelacaoLargAlt=0;
  #ifdef DEBUG
    Log->Add("UltXEnc: "+IntToStr(MaiorUltXEnc)+"\tUltXEmb: "+IntToStr(UltXEmb));
    Log->Add("YEmb: "+IntToStr(YEmb)+"\t\tYEnc: "+IntToStr(YEnc));
  #endif
  if (ParamsAI.Alt>0)
    ParamsAI.Inclinacao=(MaiorUltXEnc-UltXEmb)*1.0/ParamsAI.Alt;
  else
    ParamsAI.Inclinacao=-100;
  ParamsAI.MaiorLargLinha=MaiorLargLinha;
  Identifica(ParamsAI);
  delete [] VetorLarguras;
  delete ARect;
  delete MatrizGrupos;
}
     * 

     ---------------------- */

}
class TMaiorBorda {

    int y;
    int DifLum;
}