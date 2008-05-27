//---------------------------------------------------------------------------
#pragma hdrstop
#include "UTioPatinhas.h"
//---------------------------------------------------------------------------
#pragma package(smart_init)

#ifdef DEBUG
  extern TStrings *Log;
#endif

void ReconheceCedula(TParamsRC &ParamsRC)
{
  MostraLimiteTarja(ParamsRC.ParamsMLT);
  ParamsRC.ParamsABT.BImgDest=ParamsRC.ParamsMLT.BImgDest;
  ParamsRC.ParamsABT.BordasColunas=ParamsRC.ParamsMLT.BordasColunas;
  AnalizaBordasTarja(ParamsRC.ParamsABT);
  if (ParamsRC.ParamsABT.AchouTarja)
  {
    ParamsRC.ParamsAI.RefTarja=ParamsRC.ParamsABT.RefTarja;
    ParamsRC.ParamsAI.BImgDest=ParamsRC.ParamsABT.BImgDest;
    ParamsRC.ParamsAI.TCImgSrc=ParamsRC.ParamsMLT.TCImgSrc;
    AnalizaIdentificador(ParamsRC.ParamsAI);
  }
}
//---------------------------------------------------------------------------

void MostraLimiteTarja(TParamsMLT &ParamsMLT)
{
  struct TMaiorBorda
  {
    int y;
    int DifLum;
  };

  const int ALT_COLUNA=3, DY=2, DIF_MIN_LUM=15;
  int TamVetorExtremos=ALT_COLUNA+DY;
  int x, y, n, j;
  int yBorda;
  int DifLum;
  int UltYBordaCE, UltYBordaEC;
  int MaisEscuroAtual, MaisClaroAtual, MaisClaroAnterior, MaisEscuroAnterior;
  byte lum;
  byte **ImgSrc=ParamsMLT.TCImgSrc->TonsCinza;
  Cor **ImgDest=ParamsMLT.BImgDest->PMCor;
  int YIni=ParamsMLT.TCImgSrc->Alt*ParamsMLT.PropYIni;
  int XFim=ParamsMLT.TCImgSrc->Larg*ParamsMLT.PropXFim;
  TBorda BordaTemp;
  TMaiorBorda MaiorBorda;
  ParamsMLT.BordasColunas=new TBordasColunas(XFim);
  for (x=0; x<XFim; x++)
  {
    UltYBordaCE=UltYBordaEC=-1;
    MaiorBorda.DifLum=0;
    for (y=YIni+ALT_COLUNA; y<(ParamsMLT.TCImgSrc->Alt-ALT_COLUNA); y++)
    {
      MaisEscuroAtual=255;
      MaisEscuroAnterior=255;
      MaisClaroAtual=0;
      MaisClaroAnterior=0;

      //define os mais claros e mais escuros atuais
      for (j=y; j<(y+ALT_COLUNA); j++)
      {
        lum=ImgSrc[j][x];
        if (lum>MaisClaroAtual)
          MaisClaroAtual=lum;
        if (lum<MaisEscuroAtual)
          MaisEscuroAtual=lum;
      }

      //define os mais claros e os mais escuros anteriores
      for (j=y-(ALT_COLUNA+DY); j<(y-DY); j++)
      {
        lum=ImgSrc[j][x];
        if (lum>MaisClaroAnterior)
          MaisClaroAnterior=lum;
        if (lum<MaisEscuroAnterior)
          MaisEscuroAnterior=lum;
      }

      if (x==25 && y==170)
        int w=5;

      //borda claro encima, escuro embaixo
      DifLum=MaisEscuroAnterior-MaisClaroAtual;
      if (DifLum>DIF_MIN_LUM)
      {
        if (DifLum>MaiorBorda.DifLum)
        {
          MaiorBorda.DifLum=DifLum;
          MaiorBorda.y=y;
        }           
        UltYBordaCE=y;
      }
      else
      {
        if (y==(UltYBordaCE+1))
        {
          yBorda=MaiorBorda.y-ALT_COLUNA;
          ImgDest[yBorda][x].SetVermelho();

          BordaTemp.Y=yBorda;
          BordaTemp.TipoBorda=BORDA_CLARO_ESCURO;
          ParamsMLT.BordasColunas->Bordas[x]->push_back(BordaTemp);

          MaiorBorda.DifLum=0;
        }
      }

      //borda escuro encima, claro embaixo
      DifLum=MaisEscuroAtual-MaisClaroAnterior;
      if (DifLum>DIF_MIN_LUM)
      {    
        if (DifLum>MaiorBorda.DifLum)
        {
          MaiorBorda.DifLum=DifLum;
          MaiorBorda.y=y;
        }                   
        UltYBordaEC=y;
      }  
      else
      {
        if (y==(UltYBordaEC+1))
        {
          yBorda=MaiorBorda.y-ALT_COLUNA;
          ImgDest[yBorda][x].SetVerde();

          BordaTemp.Y=yBorda;
          BordaTemp.TipoBorda=BORDA_ESCURO_CLARO;
          ParamsMLT.BordasColunas->Bordas[x]->push_back(BordaTemp);

          MaiorBorda.DifLum=0;
        }
      }
    }
  }
}
//---------------------------------------------------------------------------

void AnalizaBordasTarja(TParamsABT &ParamsABT)
{
  uint x, n;
  uint YMedio;
  uint NumColunas=ParamsABT.BordasColunas->NumColunas;
  ParamsABT.ConjuntoMeioBordas=new TConjuntoMeioBordas(NumColunas);
  int dif;
  TVectorBorda *BordasTemp;
  TBorda *BordaEnc, *BordaEmb;
  TMeioBordas MeioBordasTemp;                        
  Cor **ImgDest=ParamsABT.BImgDest->PMCor;
  for (x=0; x<NumColunas; x++)
  {
    BordasTemp=ParamsABT.BordasColunas->Bordas[x];
    //percorre todas as bordas da coluna
    for (n=1; n<BordasTemp->size(); n++)
    {
      BordaEnc=&(*BordasTemp)[n-1];
      BordaEmb=&(*BordasTemp)[n];
      dif=BordaEmb->Y-BordaEnc->Y;
      if (    (BordaEmb->TipoBorda==BORDA_ESCURO_CLARO) &&
              (BordaEnc->TipoBorda==BORDA_CLARO_ESCURO) &&
              (dif>=ParamsABT.AltMinTarja) && (dif<=ParamsABT.AltMaxTarja) )
      {
        MeioBordasTemp.Inicializa(BordaEnc->Y, BordaEmb->Y);
        ParamsABT.ConjuntoMeioBordas->VectorMeioBordas[x]->push_back(MeioBordasTemp);
        ImgDest[MeioBordasTemp.yMeio][x].SetAmarelo();
      }
    }
  }
  SelecionaTarja(ParamsABT);
  if (ParamsABT.AchouTarja)
  {
    ImgDest[ParamsABT.RefTarja.y][ParamsABT.RefTarja.x].SetMagenta();
    ImgDest[ParamsABT.RefTarja.y+1][ParamsABT.RefTarja.x].SetMagenta();
    ImgDest[ParamsABT.RefTarja.y][ParamsABT.RefTarja.x+1].SetMagenta();
    ImgDest[ParamsABT.RefTarja.y+1][ParamsABT.RefTarja.x+1].SetMagenta();
  }
}
//---------------------------------------------------------------------------

void PreparaSelecionaTarja(TParamsABT &ParamsABT)
{
  int x;
  uint n, m;
  bool Adicionou;
  TMeioBordas *MeioBordasTemp;
  for (x=0; x<ParamsABT.ConjuntoMeioBordas->NumColunas; x++)
  {
    //percorre todos os meio de bordas da coluna (pixels amarelos)
    for (m=0; m<ParamsABT.ConjuntoMeioBordas->VectorMeioBordas[x]->size(); m++)
    {
      Adicionou=false;
      MeioBordasTemp=&(ParamsABT.ConjuntoMeioBordas->VectorMeioBordas[x]->at(m));
      //percorre todas as tarjas que estão ativas na coluna corrente
      for (n=0; n<ParamsABT.VectorTarja.size(); n++)
      {
        if (ParamsABT.VectorTarja[n].Ativa(x))
        {
          if (abs(ParamsABT.VectorTarja[n].UltYMeio-MeioBordasTemp->yMeio)<=ParamsABT.DistMaxTarjas)
          {
            ParamsABT.VectorTarja[n].VetorAlturas.push_back(MeioBordasTemp->Altura);
            ParamsABT.VectorTarja[n].UltYMeio=MeioBordasTemp->yMeio;
            Adicionou=true;
          }
        }
      }
      if (!Adicionou)
      {
        ParamsABT.VectorTarja.push_back();
        ParamsABT.VectorTarja.back().X=x;
        ParamsABT.VectorTarja.back().UltYMeio=MeioBordasTemp->yMeio;
        ParamsABT.VectorTarja.back().PriYEnc=MeioBordasTemp->Y1;
        ParamsABT.VectorTarja.back().VetorAlturas.push_back(MeioBordasTemp->Altura);
      }
    }
  }
}
//---------------------------------------------------------------------------

void SelecionaTarja(TParamsABT &ParamsABT)
{
  uint n, nMenorX, m, LargTarjaCandidata;
  TTarja *TarjaCandidata;
  int MenorX;
  double soma, desvio, media;
  PreparaSelecionaTarja(ParamsABT);
  MenorX=0xFFFFFF;
  nMenorX=-1;
  ParamsABT.AchouTarja=false;
  //percorre vector com todas as tarjas
  for (n=0; n<ParamsABT.VectorTarja.size(); n++)
  {
    TarjaCandidata=&(ParamsABT.VectorTarja[n]);
    LargTarjaCandidata=TarjaCandidata->VetorAlturas.size();
    #ifdef DEBUG
      Log->Add("Tarja candidata "+IntToStr(n)+".\tLargura:\t"+IntToStr(LargTarjaCandidata)+
      "\tX:\t"+IntToStr(TarjaCandidata->X));
    #endif
    if ((LargTarjaCandidata<=ParamsABT.LargMaxTarja) &&
            (LargTarjaCandidata>=ParamsABT.LargMinTarja))
    {
      soma=0;
      //calcula a média da altura de cada coluna da possível tarja
      for (m=0; m<ParamsABT.VectorTarja[n].VetorAlturas.size(); m++)
        soma+=ParamsABT.VectorTarja[n].VetorAlturas[m];
      media=soma/ParamsABT.VectorTarja[n].VetorAlturas.size();
      //calcula o desvio padrão
      soma=0;
      for (m=0; m<ParamsABT.VectorTarja[n].VetorAlturas.size(); m++)
        soma=fabs(media-ParamsABT.VectorTarja[n].VetorAlturas[m]);
        desvio=soma/ParamsABT.VectorTarja[n].VetorAlturas.size();
      #ifdef DEBUG
        Log->Add("desvio padrão alturas: "+FloatToStr(desvio));
      #endif
      if (desvio<ParamsABT.DesvioMax)
      {
        #ifdef DEBUG
          Log->Add("menor do que o limite de : "+FloatToStr(ParamsABT.DesvioMax));
        #endif
        //pode ser substituído por um código que pega o primeiro. mantido para debug
        if (ParamsABT.VectorTarja[n].X<MenorX)
        {
          MenorX=ParamsABT.VectorTarja[n].X;
          nMenorX=n;
        }
      }
    }
  }
  if (nMenorX!=-1)
  {
    ParamsABT.RefTarja.x=ParamsABT.VectorTarja[nMenorX].X;
    ParamsABT.RefTarja.y=ParamsABT.VectorTarja[nMenorX].PriYEnc;
    ParamsABT.AchouTarja=true;
  }
}
//---------------------------------------------------------------------------

//Mostra a linha cyan (azul claro) que representa o ponto de referência da tarja para a localização
//do identificador, e retorna a luminosidade média dos pixels da faixa
byte MediaFaixa(TParamsAI &ParamsAI)
{
  int x, y, xIni, xFim, soma;
  byte **ImgSrc=ParamsAI.TCImgSrc->TonsCinza;
  Cor **ImgDest=ParamsAI.BImgDest->PMCor;
  xIni=ParamsAI.RefTarja.x;
  xFim=xIni+ParamsAI.LargFaixaRef;
  y=ParamsAI.RefTarja.y-ParamsAI.DistFaixaRef;
  soma=0;
  for (x=xIni; x<xFim; x++)
  {
    soma+=ImgSrc[y][x];
    ImgDest[y][x].SetCyan();
  }
  return soma*1.0/ParamsAI.LargFaixaRef;
}
//---------------------------------------------------------------------------

void AnalizaIdentificador(TParamsAI &ParamsAI)
{
  enum TEstadoUltPixel {NADA, IDENTIFICADOR, FUNDO};
  const int TAM_HIST=100;
  TEstadoUltPixel EstadoUltPixel;
  int x, y;
  int xIni, xFim, yIni, yFim;
  int YEnc, YEmb, LargLinha, MaiorLargLinha, NumBordasPixelLinha;
  int XEsq, XDir, UltXEnc, UltXEmb, MaiorUltXEnc;
  int NumLinha, UltYComLinha, NumPixelsIdentificador;
  int *VetorLarguras;
  bool AchouLinha;
  int HistogramaNumBordasPixelLinha[TAM_HIST]={0};
  byte **ImgSrc=ParamsAI.TCImgSrc->TonsCinza;
  Cor **ImgDest=ParamsAI.BImgDest->PMCor;
  xIni=ParamsAI.RefTarja.x-ParamsAI.XIniParaRefTarja;
  xFim=xIni+ParamsAI.LargIdentificador;
  yFim=ParamsAI.RefTarja.y-ParamsAI.YIniParaRefTarja;
  yIni=yFim-ParamsAI.AltIdentificador;
  int TamVetLarguras=yFim-yIni+1;
  VetorLarguras=new int [TamVetLarguras];
  memset(VetorLarguras, 0, TamVetLarguras*sizeof(int));
  byte Media=MediaFaixa(ParamsAI);
  #ifdef DEBUG
    Log->Add("Média faixa: "+IntToStr(Media));
  #endif
  byte Limiar=Media-ParamsAI.DifMinMediaFaixaRef;
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
  for (y=yFim; y>yIni; y--)
  {
    XEsq=-1;
    XDir=0;
    AchouLinha=false;
    NumBordasPixelLinha=0;
    EstadoUltPixel=NADA;
    for (x=xIni; x<xFim; x++)
    {
      if (ImgSrc[y][x]<Limiar)
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

        ImgDest[y][x].SetCyan();
        NumPixelsIdentificador++;
        if (EstadoUltPixel!=IDENTIFICADOR)
          NumBordasPixelLinha++;
        EstadoUltPixel=IDENTIFICADOR;
      }
      else
        EstadoUltPixel=FUNDO;
    }
    LargLinha=XDir-XEsq;
    VetorLarguras[y-yIni]=LargLinha;
    if (LargLinha>MaiorLargLinha)
      MaiorLargLinha=LargLinha;
    if (UltXEnc>MaiorUltXEnc)
      MaiorUltXEnc=UltXEnc;
    if (AchouLinha)
    {
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
        RetornaRelacaoMedianasLargurasEncEmb(VetorLarguras, YEnc-yIni, YEmb-yIni, MediaLarguras);
  ParamsAI.Alt=YEmb-YEnc;
  if (MediaLarguras>0)
    ParamsAI.RelacaoLargAlt=ParamsAI.Alt*1.0/MediaLarguras;
  else
    ParamsAI.RelacaoLargAlt=0;
  #ifdef DEBUG
    Log->Add("UltXEnc: "+IntToStr(MaiorUltXEnc)+"\tUltXEmb: "+IntToStr(UltXEmb));
  #endif
  if (ParamsAI.Alt>0)
    ParamsAI.Inclinacao=(MaiorUltXEnc-UltXEmb)*1.0/ParamsAI.Alt;
  else
    ParamsAI.Inclinacao=-100;
  ParamsAI.MaiorLargLinha=MaiorLargLinha;
  Identifica(ParamsAI);
  delete [] VetorLarguras;
}
//--------------------------------------------------------------------------- 

float RetornaRelacaoMedianasLargurasEncEmb(int *VetorLarguras, int comeco, int fim,
                                                                        int  &MediaLarguras)
{
  int alt=fim-comeco;
  int *vetor=new int [alt+1];
  int MetadeAltura=alt/2;
  int QuartoAltura=Round(alt*1.0/4);
  int Larguras[2];
  for (int n=0; n<2; n++)
  {
    memcpy(vetor, VetorLarguras+comeco + n*MetadeAltura, MetadeAltura*sizeof(int));
    qsort(vetor, MetadeAltura, sizeof(int), ComparaInteiro);
    Larguras[n]=vetor[QuartoAltura];
  }
  delete [] vetor;
  #ifdef DEBUG
    Log->Add("Largura encima: "+IntToStr(Larguras[0]));
    Log->Add("Largura embaixo: "+IntToStr(Larguras[1]));
  #endif
  MediaLarguras=(Larguras[0]+Larguras[1])/2;
  if (Larguras[1]>0)
    return Larguras[0]*1.0/Larguras[1];
  else
    return -100;
}     
//---------------------------------------------------------------------------

void Identifica(TParamsAI &ParamsAI)
{
  #ifdef DEBUG
    Log->Add("Inclinação identificador: "+FloatToStr(ParamsAI.Inclinacao));
    Log->Add("Altura identificador: "+IntToStr(ParamsAI.Alt));           
    Log->Add("Relação larguras identificador: "+FloatToStr(ParamsAI.RelacaoMedianasLargurasEncEmb));
    if (ParamsAI.RelacaoMedianasLargurasEncEmb>0)
      Log->Add("Relação inversa larguras identificador: "+FloatToStr(1.0/ParamsAI.RelacaoMedianasLargurasEncEmb));
    Log->Add("Relação Altura/Largura: "+FloatToStr(ParamsAI.RelacaoLargAlt));                                     
    Log->Add("Número médio de colunas: "+FloatToStr(ParamsAI.NumMedColunas));
  #endif
  if (ParamsAI.Inclinacao>ParamsAI.LimiarInclinacaoidentificador)
  {                       
    #ifdef DEBUG
      Log->Add("Inclinação maior que o limite, pode ser \tR$2\tR$20");
    #endif
    if (ParamsAI.RelacaoLargAlt>ParamsAI.LimiarRelacaoLargAlt)
    {
      #ifdef DEBUG
        Log->Add("Relação Altura/Largura maior que o limite, é R$2");
      #endif
      ParamsAI.ValorCedula=2;
    }
    else
    {
      #ifdef DEBUG
        Log->Add("Relação Altura/Largura menor que o limite, é R$20");
      #endif
      ParamsAI.ValorCedula=20;
    }
  }
  else
  {
    #ifdef DEBUG
      Log->Add("Inclinação menor que o limite, pode ser \tR$1\tR$5\tR$10\tR$50\tR$100");
    #endif
    if (ParamsAI.Alt>ParamsAI.LimiarAlturaIdentificador)
    {
      #ifdef DEBUG
        Log->Add("Altura maior que o limite, pode ser \tR$1\tR$5\tR$50\tR$100");
      #endif
      if (ParamsAI.RelacaoMedianasLargurasEncEmb>ParamsAI.LimiarLargLinhasIdentificador)
      {
        #ifdef DEBUG
          Log->Add("Relação medianas larguras maior que o limite, é R$50");
        #endif
        ParamsAI.ValorCedula=50;
      }
      else if (1.0/ParamsAI.RelacaoMedianasLargurasEncEmb>ParamsAI.LimiarLargLinhasIdentificador)
      {
        #ifdef DEBUG
          Log->Add("Relação inversa medianas larguras maior que o limite, é R$100");
        #endif
        ParamsAI.ValorCedula=100;
      }
      else
      {
        #ifdef DEBUG
          Log->Add("Relação medianas larguras menor que o limite, pode ser \tR$1\tR$5");
        #endif
        if (ParamsAI.NumMedColunas<ParamsAI.LimiarNumMedColunas)
        {
          #ifdef DEBUG
            Log->Add("Número médio de colunas menor que o limite, é R$1");
          #endif
          ParamsAI.ValorCedula=1;
        }
        else
        {
          #ifdef DEBUG
            Log->Add("Número médio de colunas maior que o limite, é R$5");
          #endif
          ParamsAI.ValorCedula=5;
        }
      }
    }
    else
    {
      #ifdef DEBUG
        Log->Add("Altura menor que o limite, é R$10");
      #endif
      ParamsAI.ValorCedula=10;
    }
  }
}
//---------------------------------------------------------------------------
